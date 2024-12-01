package com.example.videolibrary.service;

import com.example.videolibrary.dao.ReviewDao;
import com.example.videolibrary.dao.impl.ReviewDaoImpl;
import com.example.videolibrary.model.Review;

import java.util.List;

public class ReviewService {
    private static ReviewService instance;
    private final ReviewDao reviewDao;

    private ReviewService() {
        this.reviewDao = new ReviewDaoImpl();
    }

    public static ReviewService getInstance() {
        if (instance == null) {
            instance = new ReviewService();
        }
        return instance;
    }

    public Review addReview(Review review) {
        return reviewDao.save(review);
    }

    public Review getReviewById(Long id) {
        return reviewDao.findById(id);
    }

    public List<Review> getReviewsByMovie(Long movieId) {
        return reviewDao.findByMovieId(movieId);
    }

    public List<Review> getReviewsByUser(Long userId) {
        return reviewDao.findByUserId(userId);
    }

    public void deleteReview(Long id) {
        reviewDao.delete(id);
    }

    public Review updateReview(Review review) {
        return reviewDao.update(review);
    }
}
