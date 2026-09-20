package com.project.elibrary.bean.bookrequest;

import java.sql.Timestamp; 
import java.sql.Date;

public class BookRequest {

	    private long requestId;
	    private long userId;
	    private String title;
	    private String author;
	    private Date weekStart;
	    private Timestamp createdAt;

	    // Default constructor
	    public BookRequest() {
	    }

	    // Getters and Setters

	    public long getRequestId() {
	        return requestId;
	    }

	    public void setRequestId(long requestId) {
	        this.requestId = requestId;
	    }

	    public long getUserId() {
	        return userId;
	    }

	    public void setUserId(long userId) {
	        this.userId = userId;
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

	    public Date getWeekStart() {
	        return weekStart;
	    }

	    public void setWeekStart(Date weekStart) {
	        this.weekStart = weekStart;
	    }

	    public Timestamp getCreatedAt() {
	        return createdAt;
	    }

	    public void setCreatedAt(Timestamp createdAt) {
	        this.createdAt = createdAt;
	    }

}
