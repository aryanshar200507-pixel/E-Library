package com.project.elibrary.controller.readingprogress;

import java.io.IOException;

import com.project.elibrary.bean.readingprogress.ReadingProgress;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.readingprogressservice.ReadingProgressService;
import com.project.elibrary.service.readingprogressservice.ReadingProgressServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/books/save-progress")
public class SaveReadingProgressServlet extends HttpServlet {

    private ReadingProgressService readingProgressService;

    @Override
    public void init() throws ServletException {

        readingProgressService =
                new ReadingProgressServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Get existing session.
        HttpSession session = request.getSession(false);

        User user = null;

        if (session != null) {
            user = (User) session.getAttribute("loggedInUser");
        }

        // User must be logged in.
        if (user == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User is not logged in.");

            return;
        }

        // Get book ID.
        String bookIdParam =
                request.getParameter("bookId");

        // Get current page.
        String pageParam =
                request.getParameter("currentPage");

        if (bookIdParam == null
                || pageParam == null
                || bookIdParam.isBlank()
                || pageParam.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Book ID and current page are required.");

            return;
        }

        Long bookId;
        int currentPage;

        try {

            bookId = Long.parseLong(bookIdParam);
            currentPage = Integer.parseInt(pageParam);

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid book ID or page number.");

            return;
        }

        // Page number must be valid.
        if (currentPage < 1) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid page number.");

            return;
        }

        /*
         * Check whether progress already exists.
         */
        ReadingProgress existingProgress =
                readingProgressService.getProgress(
                        user.getUserId(),
                        bookId);

        boolean success;

        if (existingProgress == null) {

            /*
             * First time this user is reading this book.
             * Create a new progress record.
             */
            ReadingProgress progress =
                    new ReadingProgress(
                            user.getUserId(),
                            bookId,
                            currentPage);

            success =
                    readingProgressService
                            .saveProgress(progress);

        } else {

            /*
             * Progress already exists.
             * Update the existing record.
             */
            existingProgress.setCurrentPage(currentPage);

            success =
                    readingProgressService
                            .updateProgress(existingProgress);
        }

        if (success) {

            response.setStatus(
                    HttpServletResponse.SC_OK);

        } else {

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Failed to save reading progress.");
        }
    }
}