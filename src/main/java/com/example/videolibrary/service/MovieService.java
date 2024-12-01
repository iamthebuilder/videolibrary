package com.example.videolibrary.service;

import com.example.videolibrary.dao.MovieDao;
import com.example.videolibrary.dao.impl.MovieDaoImpl;
import com.example.videolibrary.model.Movie;
import com.example.videolibrary.model.Person;

import java.util.List;

public class MovieService {
    private static MovieService instance;
    private final MovieDao movieDao;

    private MovieService() {
        this.movieDao = new MovieDaoImpl();
    }

    public static MovieService getInstance() {
        if (instance == null) {
            instance = new MovieService();
        }
        return instance;
    }

    public Movie addMovie(Movie movie) {
        return movieDao.save(movie);
    }

    public Movie getMovieById(Long id) {
        return movieDao.findById(id);
    }

    public List<Movie> getMoviesByYear(int year) {
        return movieDao.findByYear(year);
    }

    public List<Movie> getMoviesByActor(Person actor) {
        return movieDao.findByActor(actor);
    }

    public List<Person> getMovieActors(Long movieId) {
        return movieDao.findActorsByMovie(movieId);
    }

    public void deleteMovie(Long id) {
        movieDao.delete(id);
    }

    public Movie updateMovie(Movie movie) {
        return movieDao.update(movie);
    }
}
