package com.project.elibrary.service.ratingservice;

import com.project.elibrary.bean.ratings.Rating;

public interface RatingService {

    // Add a new rating
    boolean addRating(Rating rating);

    // Get a user's rating for a specific book
    Rating getUserRating(Long userId, Long bookId);

    // Update an existing rating
    boolean updateRating(Rating rating);

    // Get the average rating of a book
    Double getAverageRating(Long bookId);

    // Get the total number of ratings for a book
    int getRatingCount(Long bookId);
}