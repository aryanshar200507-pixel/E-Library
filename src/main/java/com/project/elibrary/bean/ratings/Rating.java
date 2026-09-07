package com.project.elibrary.bean.ratings;

import java.time.LocalDateTime;

public class Rating {
	private Long ratingId;
	private Long userId;
	private Long bookId;
	private Integer rating;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	 public Rating() {}
	
	public Rating(Long userId, Long bookId, Integer rating) {
		super();
		this.userId = userId;
		this.bookId = bookId;
		this.rating = rating;
		
	}

	public Long getRatingId() {
		return ratingId;
	}

	public void setRatingId(Long ratingId) {
		this.ratingId = ratingId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	
}
