package com.example.videolibrary.dao;

import com.example.videolibrary.model.Review;
import java.util.List;

public interface ReviewDao {
    Review save(Review review);
    Review findById(Long id);
    List<Review> findByMovieId(Long movieId);
    List<Review> findByUserId(Long userId);
    void delete(Long id);
    Review update(Review review);
}
