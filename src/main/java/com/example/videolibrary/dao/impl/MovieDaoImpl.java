package com.example.videolibrary.dao.impl;

import com.example.videolibrary.dao.MovieDao;
import com.example.videolibrary.model.Movie;
import com.example.videolibrary.model.Person;
import com.example.videolibrary.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDaoImpl implements MovieDao {
    private final DatabaseConnection dbConnection = DatabaseConnection.getInstance();

    @Override
    public Movie save(Movie movie) {
        String sql = "INSERT INTO movies (title, director_id, release_date, country, genre) VALUES (?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, movie.getTitle());
            stmt.setLong(2, movie.getDirector().getId());
            stmt.setDate(3, Date.valueOf(movie.getReleaseDate()));
            stmt.setString(4, movie.getCountry());
            stmt.setString(5, movie.getGenre());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                movie.setId(rs.getLong("id"));
                saveMovieActors(movie);
                return movie;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving movie", e);
        }
        return null;
    }

    private void saveMovieActors(Movie movie) throws SQLException {
        String sql = "INSERT INTO movie_actors (movie_id, actor_id) VALUES (?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Person actor : movie.getActors()) {
                stmt.setLong(1, movie.getId());
                stmt.setLong(2, actor.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    @Override
    public Movie findById(Long id) {
        String sql = "SELECT m.*, p.id as director_id, p.full_name as director_name, p.birth_date as director_birth_date " +
                    "FROM movies m " +
                    "JOIN persons p ON m.director_id = p.id " +
                    "WHERE m.id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Movie movie = extractMovieFromResultSet(rs);
                movie.setActors(findActorsByMovie(id));
                return movie;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding movie by id", e);
        }
        return null;
    }

    @Override
    public List<Movie> findByYear(int year) {
        String sql = "SELECT m.*, p.id as director_id, p.full_name as director_name, p.birth_date as director_birth_date " +
                    "FROM movies m " +
                    "JOIN persons p ON m.director_id = p.id " +
                    "WHERE EXTRACT(YEAR FROM m.release_date) = ?";
        
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Movie movie = extractMovieFromResultSet(rs);
                movie.setActors(findActorsByMovie(movie.getId()));
                movies.add(movie);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding movies by year", e);
        }
        return movies;
    }

    @Override
    public List<Movie> findByActor(Person actor) {
        String sql = "SELECT m.*, p.id as director_id, p.full_name as director_name, p.birth_date as director_birth_date " +
                    "FROM movies m " +
                    "JOIN persons p ON m.director_id = p.id " +
                    "JOIN movie_actors ma ON m.id = ma.movie_id " +
                    "WHERE ma.actor_id = ?";
        
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, actor.getId());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Movie movie = extractMovieFromResultSet(rs);
                movie.setActors(findActorsByMovie(movie.getId()));
                movies.add(movie);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding movies by actor", e);
        }
        return movies;
    }

    @Override
    public List<Person> findActorsByMovie(Long movieId) {
        String sql = "SELECT p.* FROM persons p " +
                    "JOIN movie_actors ma ON p.id = ma.actor_id " +
                    "WHERE ma.movie_id = ?";
        
        List<Person> actors = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, movieId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Person actor = new Person();
                actor.setId(rs.getLong("id"));
                actor.setFullName(rs.getString("full_name"));
                actor.setBirthDate(rs.getDate("birth_date").toLocalDate());
                actor.setActor(rs.getBoolean("is_actor"));
                actor.setDirector(rs.getBoolean("is_director"));
                actors.add(actor);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding actors by movie", e);
        }
        return actors;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM movies WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting movie", e);
        }
    }

    @Override
    public Movie update(Movie movie) {
        String sql = "UPDATE movies SET title = ?, director_id = ?, release_date = ?, country = ?, genre = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, movie.getTitle());
            stmt.setLong(2, movie.getDirector().getId());
            stmt.setDate(3, Date.valueOf(movie.getReleaseDate()));
            stmt.setString(4, movie.getCountry());
            stmt.setString(5, movie.getGenre());
            stmt.setLong(6, movie.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Update movie actors
                String deleteSql = "DELETE FROM movie_actors WHERE movie_id = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                    deleteStmt.setLong(1, movie.getId());
                    deleteStmt.executeUpdate();
                }
                saveMovieActors(movie);
                return movie;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating movie", e);
        }
        return null;
    }

    private Movie extractMovieFromResultSet(ResultSet rs) throws SQLException {
        Movie movie = new Movie();
        movie.setId(rs.getLong("id"));
        movie.setTitle(rs.getString("title"));
        movie.setReleaseDate(rs.getDate("release_date").toLocalDate());
        movie.setCountry(rs.getString("country"));
        movie.setGenre(rs.getString("genre"));
        
        Person director = new Person();
        director.setId(rs.getLong("director_id"));
        director.setFullName(rs.getString("director_name"));
        director.setBirthDate(rs.getDate("director_birth_date").toLocalDate());
        director.setDirector(true);
        movie.setDirector(director);
        
        return movie;
    }
}
