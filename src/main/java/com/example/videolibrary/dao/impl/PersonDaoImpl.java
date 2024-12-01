package com.example.videolibrary.dao.impl;

import com.example.videolibrary.dao.PersonDao;
import com.example.videolibrary.model.Person;
import com.example.videolibrary.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDaoImpl implements PersonDao {
    private final DatabaseConnection dbConnection = DatabaseConnection.getInstance();

    @Override
    public Person save(Person person) {
        String sql = "INSERT INTO persons (full_name, birth_date, is_actor, is_director) VALUES (?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, person.getFullName());
            stmt.setDate(2, Date.valueOf(person.getBirthDate()));
            stmt.setBoolean(3, person.isActor());
            stmt.setBoolean(4, person.isDirector());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                person.setId(rs.getLong("id"));
                return person;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving person", e);
        }
        return null;
    }

    @Override
    public Person findById(Long id) {
        String sql = "SELECT * FROM persons WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractPersonFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding person by id", e);
        }
        return null;
    }

    @Override
    public List<Person> findAll() {
        String sql = "SELECT * FROM persons";
        List<Person> persons = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                persons.add(extractPersonFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all persons", e);
        }
        return persons;
    }

    @Override
    public List<Person> findActors() {
        String sql = "SELECT * FROM persons WHERE is_actor = true";
        List<Person> actors = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                actors.add(extractPersonFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding actors", e);
        }
        return actors;
    }

    @Override
    public List<Person> findDirectors() {
        String sql = "SELECT * FROM persons WHERE is_director = true";
        List<Person> directors = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                directors.add(extractPersonFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding directors", e);
        }
        return directors;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM persons WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting person", e);
        }
    }

    @Override
    public Person update(Person person) {
        String sql = "UPDATE persons SET full_name = ?, birth_date = ?, is_actor = ?, is_director = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, person.getFullName());
            stmt.setDate(2, Date.valueOf(person.getBirthDate()));
            stmt.setBoolean(3, person.isActor());
            stmt.setBoolean(4, person.isDirector());
            stmt.setLong(5, person.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return person;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating person", e);
        }
        return null;
    }

    private Person extractPersonFromResultSet(ResultSet rs) throws SQLException {
        Person person = new Person();
        person.setId(rs.getLong("id"));
        person.setFullName(rs.getString("full_name"));
        person.setBirthDate(rs.getDate("birth_date").toLocalDate());
        person.setActor(rs.getBoolean("is_actor"));
        person.setDirector(rs.getBoolean("is_director"));
        return person;
    }
}
