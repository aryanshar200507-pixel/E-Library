package com.project.elibrary.bean.recommendation;

import com.project.elibrary.bean.book.Book;

public class Recommendation {


	    private Book book;
	    private double score;

	    public Recommendation() {
	    }

	    public Recommendation(Book book, double score) {
	        this.book = book;
	        this.score = score;
	    }

	    public Book getBook() {
	        return book;
	    }

	    public void setBook(Book book) {
	        this.book = book;
	    }

	    public double getScore() {
	        return score;
	    }

	    public void setScore(double score) {
	        this.score = score;
	    }
	}

