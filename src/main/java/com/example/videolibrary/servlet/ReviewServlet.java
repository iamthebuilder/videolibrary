package com.example.videolibrary.servlet;

import com.example.videolibrary.model.Movie;
import com.example.videolibrary.model.Review;
import com.example.videolibrary.model.User;
import com.example.videolibrary.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/reviews/*")
public class ReviewServlet extends HttpServlet {
    private final ReviewService reviewService = ReviewService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo != null && pathInfo.startsWith("/movie/")) {
            String[] parts = pathInfo.split("/");
            if (parts.length == 3) {
                Long movieId = Long.parseLong(parts[2]);
                List<Review> reviews = reviewService.getReviewsByMovie(movieId);
                request.setAttribute("reviews", reviews);
                request.getRequestDispatcher("/WEB-INF/jsp/reviews.jsp").forward(request, response);
            }
        } else if (pathInfo != null && pathInfo.startsWith("/user/")) {
            String[] parts = pathInfo.split("/");
            if (parts.length == 3) {
                Long userId = Long.parseLong(parts[2]);
                List<Review> reviews = reviewService.getReviewsByUser(userId);
                request.setAttribute("reviews", reviews);
                request.getRequestDispatcher("/WEB-INF/jsp/reviews.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/users");
            return;
        }

        String movieId = request.getParameter("movieId");
        String text = request.getParameter("text");
        String rating = request.getParameter("rating");

        Review review = new Review();
        Movie movie = new Movie();
        movie.setId(Long.parseLong(movieId));
        review.setMovie(movie);
        review.setUser(user);
        review.setText(text);
        review.setRating(Integer.parseInt(rating));

        Review savedReview = reviewService.addReview(review);
        if (savedReview != null) {
            response.sendRedirect(request.getContextPath() + "/movies/view/" + movieId);
        } else {
            request.setAttribute("error", "Failed to save review");
            response.sendRedirect(request.getContextPath() + "/movies/view/" + movieId);
        }
    }
}
