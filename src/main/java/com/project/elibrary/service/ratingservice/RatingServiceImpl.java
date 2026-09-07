package com.project.elibrary.service.ratingservice;

import com.project.elibrary.bean.ratings.Rating;
import com.project.elibrary.dao.rating.RatingDao;
import com.project.elibrary.dao.rating.RatingDaoImpl;

public class RatingServiceImpl implements RatingService {

	private RatingDao ratingDao;

	public RatingServiceImpl() {
		ratingDao = new RatingDaoImpl();
	}

	@Override
	public boolean addRating(Rating rating) {

		validateRating(rating);

		// Check whether the user has already rated this book
		Rating existingRating = ratingDao.findByUserAndBook(rating.getUserId(), rating.getBookId());

		if (existingRating != null) {
			throw new IllegalStateException("User has already rated this book.");
		}

		return ratingDao.save(rating);
	}

	@Override
	public Rating getUserRating(Long userId, Long bookId) {

		if (userId == null || userId <= 0) {
			throw new IllegalArgumentException("Invalid user Id");
		}

		if (bookId == null || bookId <= 0) {
			throw new IllegalArgumentException("Invalid book Id");
		}

		return ratingDao.findByUserAndBook(userId, bookId);
	}

	@Override
	public boolean updateRating(Rating rating) {

		validateRating(rating);

		if (rating.getRatingId() == null || rating.getRatingId() <= 0) {
			throw new IllegalArgumentException("Invalid rating Id");
		}

		return ratingDao.update(rating);
	}

	@Override
	public Double getAverageRating(Long bookId) {

		if (bookId == null || bookId <= 0) {
			throw new IllegalArgumentException("Invalid book Id");
		}

		return ratingDao.getAverageRating(bookId);
	}

	@Override
	public int getRatingCount(Long bookId) {

		if (bookId == null || bookId <= 0) {
			throw new IllegalArgumentException("Invalid book Id");
		}

		return ratingDao.getRatingCount(bookId);
	}

	/**
	 * Validates the rating before sending it to the DAO.
	 */
	private void validateRating(Rating rating) {

		if (rating == null) {
			throw new IllegalArgumentException("Rating cannot be null");
		}

		if (rating.getUserId() == null || rating.getUserId() <= 0) {
			throw new IllegalArgumentException("Invalid user Id");
		}

		if (rating.getBookId() == null || rating.getBookId() <= 0) {
			throw new IllegalArgumentException("Invalid book Id");
		}

		if (rating.getRating() == null || rating.getRating() < 1 || rating.getRating() > 5) {

			throw new IllegalArgumentException("Rating must be between 1 and 5");
		}
	}
}