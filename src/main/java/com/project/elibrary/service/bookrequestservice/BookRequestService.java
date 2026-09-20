package com.project.elibrary.service.bookrequestservice;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import com.project.elibrary.bean.bookrequest.BookRequest;
import com.project.elibrary.dao.bookrequest.BookRequestDao;

import com.project.elibrary.dao.book.BookDao;
import com.project.elibrary.dao.book.BookDaoImpl;

public class BookRequestService {

    private BookRequestDao bookRequestDao = new BookRequestDao();
    private BookDao bookDao = new BookDaoImpl();
    
    // Get Monday of current week
    public Date getWeekStart() {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        return Date.valueOf(monday);
    }

    // Check maximum 10 requests
    public boolean canAddRequest() {
        Date weekStart = getWeekStart();
        return bookRequestDao.getRequestCount(weekStart) < 10;
    }
    
 // Check duplicate request
    public boolean isDuplicateRequest(String title) {

        Date weekStart = getWeekStart();

        return bookRequestDao.isDuplicateRequest(title, weekStart);
    }
    
    
 // Check whether the request belongs to the current week
    public boolean isCurrentWeekRequest(long requestId) {

        Date weekStart = getWeekStart();

        return bookRequestDao.isCurrentWeekRequest(
                requestId,
                weekStart
        );
    }
    

    // Add book request
    public boolean addRequest(BookRequest request) {

        if (!canAddRequest()) {
            return false;
        }

        // Check if book already exists in the library
        if (bookDao.existsByTitle(request.getTitle())) {
            return false;
        }

        // Check if the same book was already requested this week
        if (isDuplicateRequest(request.getTitle())) {
            return false;
        }

        request.setWeekStart(getWeekStart());

        return bookRequestDao.addRequest(request);
    }

    // Get book requests
    public List<BookRequest> getRequests() {

        Date weekStart = getWeekStart();

        return bookRequestDao.getRequests(weekStart);
    }

    // Get request count

    public int getRequestCount() {

        Date weekStart = getWeekStart();

        return bookRequestDao.getRequestCount(weekStart);
    }

    // Delete book request
    public boolean deleteRequest(long requestId) {
        return bookRequestDao.deleteRequest(requestId);
    }
    
 // Get the total number of votes for a specific book request
    public int getVoteCount(long requestId) {

        // Ask the DAO to get the vote count from the database
        return bookRequestDao.getVoteCount(requestId);
    }
}
