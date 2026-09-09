package com.project.elibrary.bean.bookmark;

import java.time.LocalDateTime;

public class Bookmark {

    private Long bookmarkId;
    private Long userId;
    private Long bookId;
    private int pageNumber;
    private LocalDateTime createdAt;

    public Bookmark() {
    }

    public Bookmark(Long userId, Long bookId, int pageNumber) {
        this.userId = userId;
        this.bookId = bookId;
        this.pageNumber = pageNumber;
    }

    public Long getBookmarkId() {
        return bookmarkId;
    }

    public void setBookmarkId(Long bookmarkId) {
        this.bookmarkId = bookmarkId;
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

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}