package com.example.videolibrary.servlet;

import com.example.videolibrary.model.Movie;
import com.example.videolibrary.model.Person;
import com.example.videolibrary.service.MovieService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/movies/*")
public class MovieServlet extends HttpServlet {
    private final MovieService movieService = MovieService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            String yearParam = request.getParameter("year");
            if (yearParam != null && !yearParam.isEmpty()) {
                int year = Integer.parseInt(yearParam);
                List<Movie> movies = movieService.getMoviesByYear(year);
                request.setAttribute("movies", movies);
            }
            request.getRequestDispatcher("/WEB-INF/jsp/movies.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/view/")) {
            String[] parts = pathInfo.split("/");
            if (parts.length == 3) {
                Long movieId = Long.parseLong(parts[2]);
                Movie movie = movieService.getMovieById(movieId);
                if (movie != null) {
                    request.setAttribute("movie", movie);
                    request.getRequestDispatcher("/WEB-INF/jsp/movie-details.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String directorId = request.getParameter("directorId");
        String releaseDate = request.getParameter("releaseDate");
        String country = request.getParameter("country");
        String genre = request.getParameter("genre");
        String[] actorIds = request.getParameterValues("actorIds");

        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setReleaseDate(LocalDate.parse(releaseDate));
        movie.setCountry(country);
        movie.setGenre(genre);

        Person director = new Person();
        director.setId(Long.parseLong(directorId));
        movie.setDirector(director);

        Movie savedMovie = movieService.addMovie(movie);
        response.sendRedirect(request.getContextPath() + "/movies/view/" + savedMovie.getId());
    }
}
