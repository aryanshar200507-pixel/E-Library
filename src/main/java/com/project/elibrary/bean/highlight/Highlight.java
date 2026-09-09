package com.project.elibrary.bean.highlight;

import java.time.LocalDateTime;

public class Highlight {

    private Long highlightId;

    private Long userId;

    private Long bookId;

    private int pageNumber;

    private String selectedText;

    private int startOffset;

    private int endOffset;

    /*
     * Stores the exact positions of the selected
     * text rectangles as JSON.
     *
     * One selection can contain multiple rectangles
     * because the selection can cover multiple lines.
     */
    private String rectanglesJson;

    private String color;

    private LocalDateTime createdAt;


    public Highlight() {
    }


    public Highlight(
            Long userId,
            Long bookId,
            int pageNumber,
            String selectedText,
            int startOffset,
            int endOffset,
            String rectanglesJson,
            String color) {

        this.userId = userId;
        this.bookId = bookId;
        this.pageNumber = pageNumber;
        this.selectedText = selectedText;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.rectanglesJson = rectanglesJson;
        this.color = color;
    }


    public Long getHighlightId() {
        return highlightId;
    }

    public void setHighlightId(Long highlightId) {
        this.highlightId = highlightId;
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


    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }


    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }


    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }


    public String getRectanglesJson() {
        return rectanglesJson;
    }

    public void setRectanglesJson(String rectanglesJson) {
        this.rectanglesJson = rectanglesJson;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}