package com.project.elibrary.bean.comment;

import java.time.LocalDateTime;

public class Comment {
	private Long commentId;
	private Long userId;
	private Long bookId;

	private String comment;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public Comment() {}

	public Comment(Long userId, Long bookId, String comment) {

		this.userId = userId;
		this.bookId = bookId;
		this.comment = comment;
	}

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
