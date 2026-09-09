package com.project.elibrary.bean.readingprogress;

import java.time.LocalDateTime;

public class ReadingProgress {

    private Long progressId;
    private Long userId;
    private Long bookId;
    private int currentPage;
    private LocalDateTime updatedAt;

    public ReadingProgress() {
    }

    public ReadingProgress(Long userId, Long bookId, int currentPage) {
        this.userId = userId;
        this.bookId = bookId;
        this.currentPage = currentPage;
    }

    public Long getProgressId() {
        return progressId;
    }

    public void setProgressId(Long progressId) {
        this.progressId = progressId;
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

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}