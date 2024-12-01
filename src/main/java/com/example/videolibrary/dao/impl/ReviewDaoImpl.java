package com.example.videolibrary.dao.impl;

import com.example.videolibrary.dao.ReviewDao;
import com.example.videolibrary.model.Movie;
import com.example.videolibrary.model.Review;
import com.example.videolibrary.model.User;
import com.example.videolibrary.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDaoImpl implements ReviewDao {
    private final DatabaseConnection dbConnection = DatabaseConnection.getInstance();

    @Override
    public Review save(Review review) {
        String sql = "INSERT INTO reviews (movie_id, user_id, text, rating) VALUES (?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, review.getMovie().getId());
            stmt.setLong(2, review.getUser().getId());
            stmt.setString(3, review.getText());
            stmt.setInt(4, review.getRating());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                review.setId(rs.getLong("id"));
                return review;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving review", e);
        }
        return null;
    }

    @Override
    public Review findById(Long id) {
        String sql = "SELECT r.*, m.title as movie_title, u.full_name as user_name " +
                    "FROM reviews r " +
                    "JOIN movies m ON r.movie_id = m.id " +
                    "JOIN users u ON r.user_id = u.id " +
                    "WHERE r.id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractReviewFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding review by id", e);
        }
        return null;
    }

    @Override
    public List<Review> findByMovieId(Long movieId) {
        String sql = "SELECT r.*, m.title as movie_title, u.full_name as user_name " +
                    "FROM reviews r " +
                    "JOIN movies m ON r.movie_id = m.id " +
                    "JOIN users u ON r.user_id = u.id " +
                    "WHERE r.movie_id = ?";
        
        List<Review> reviews = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, movieId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding reviews by movie id", e);
        }
        return reviews;
    }

    @Override
    public List<Review> findByUserId(Long userId) {
        String sql = "SELECT r.*, m.title as movie_title, u.full_name as user_name " +
                    "FROM reviews r " +
                    "JOIN movies m ON r.movie_id = m.id " +
                    "JOIN users u ON r.user_id = u.id " +
                    "WHERE r.user_id = ?";
        
        List<Review> reviews = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding reviews by user id", e);
        }
        return reviews;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM reviews WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting review", e);
        }
    }

    @Override
    public Review update(Review review) {
        String sql = "UPDATE reviews SET movie_id = ?, user_id = ?, text = ?, rating = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, review.getMovie().getId());
            stmt.setLong(2, review.getUser().getId());
            stmt.setString(3, review.getText());
            stmt.setInt(4, review.getRating());
            stmt.setLong(5, review.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return review;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating review", e);
        }
        return null;
    }

    private Review extractReviewFromResultSet(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setId(rs.getLong("id"));
        review.setText(rs.getString("text"));
        review.setRating(rs.getInt("rating"));
        
        Movie movie = new Movie();
        movie.setId(rs.getLong("movie_id"));
        movie.setTitle(rs.getString("movie_title"));
        review.setMovie(movie);
        
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setFullName(rs.getString("user_name"));
        review.setUser(user);
        
        return review;
    }
}
