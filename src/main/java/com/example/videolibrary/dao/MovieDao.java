package com.example.videolibrary.dao;

import com.example.videolibrary.model.Movie;
import com.example.videolibrary.model.Person;

import java.util.List;

public interface MovieDao {
    Movie save(Movie movie);
    Movie findById(Long id);
    List<Movie> findByYear(int year);
    List<Movie> findByActor(Person actor);
    List<Person> findActorsByMovie(Long movieId);
    void delete(Long id);
    Movie update(Movie movie);
}
