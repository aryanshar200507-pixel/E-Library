package com.project.elibrary.dao.rating;

import com.project.elibrary.bean.ratings.Rating;

public interface RatingDao {

    // Save a new rating
    boolean save(Rating rating);

    // Find the rating given by a specific user for a specific book
    Rating findByUserAndBook(Long userId, Long bookId);

    // Update an existing rating
    boolean update(Rating rating);

    // Get the average rating of a book
    Double getAverageRating(Long bookId);

    // Get the total number of ratings for a book
    int getRatingCount(Long bookId);
}