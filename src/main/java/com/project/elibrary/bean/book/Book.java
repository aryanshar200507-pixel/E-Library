package com.project.elibrary.bean.book;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Book {
	private Long bookId;
	private String title;
	private String author;
	private String description;
	private Long categoryId;
	private LocalDate publishedAt;
	private String coverStorageKey;
	private String pdfStorageKey;
	private Long views;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public Book() {}

	public Book(String title, String author, String description, Long categoryId, LocalDate publishedAt,
			String coverStorageKey, String pdfStorageKey, Long views, LocalDateTime createdAt,
			LocalDateTime updatedAt) {
		super();
		this.title = title;
		this.author = author;
		this.description = description;
		this.categoryId = categoryId;
		this.publishedAt = publishedAt;
		this.coverStorageKey = coverStorageKey;
		this.pdfStorageKey = pdfStorageKey;
		this.views = views;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public LocalDate getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(LocalDate publishedAt) {
		this.publishedAt = publishedAt;
	}

	public String getCoverStorageKey() {
		return coverStorageKey;
	}

	public void setCoverStorageKey(String coverStorageKey) {
		this.coverStorageKey = coverStorageKey;
	}

	public String getPdfStorageKey() {
		return pdfStorageKey;
	}

	public void setPdfStorageKey(String pdfStorageKey) {
		this.pdfStorageKey = pdfStorageKey;
	}

	public Long getViews() {
		return views;
	}

	public void setViews(Long views) {
		this.views = views;
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
