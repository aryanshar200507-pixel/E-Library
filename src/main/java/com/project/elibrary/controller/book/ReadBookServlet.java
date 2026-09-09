package com.project.elibrary.controller.book;

import java.io.IOException;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.bean.readingprogress.ReadingProgress;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.bookservice.BookService;
import com.project.elibrary.service.bookservice.BookServiceImpl;
import com.project.elibrary.service.readingprogressservice.ReadingProgressService;
import com.project.elibrary.service.readingprogressservice.ReadingProgressServiceImpl;
import com.project.elibrary.service.storageservice.S3StorageServiceImpl;
import com.project.elibrary.service.storageservice.StorageService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/books/read")
public class ReadBookServlet extends HttpServlet {

    private BookService bookService;
    private StorageService storageService;
    private ReadingProgressService readingProgressService;

    public ReadBookServlet() {

        bookService = new BookServiceImpl();
        storageService = new S3StorageServiceImpl();
        readingProgressService = new ReadingProgressServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Get the existing session.
        // We don't create a new session here because the user
        // should already be logged in.
        HttpSession session = request.getSession(false);

        User user = null;

        if (session != null) {
            user = (User) session.getAttribute("loggedInUser");
        }

        // User must be logged in to read a book.
        if (user == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp");

            return;
        }

        // Get book ID from URL.
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Book ID is required.");

            return;
        }

        Long bookId;

        try {

            bookId = Long.parseLong(idParam);

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid book ID.");

            return;
        }

        // Find the requested book.
        Book book = bookService.getBookById(bookId);

        if (book == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Book not found.");

            return;
        }

        // Check whether the book has a PDF.
        String pdfStorageKey = book.getPdfStorageKey();

        if (pdfStorageKey == null || pdfStorageKey.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "PDF file not found for this book.");

            return;
        }

        /*
         * Increment book views.
         *
         * This happens only when the actual reader is opened,
         * not when the user visits the book details page.
         */
        bookService.incrementVIews(bookId);

        /*
         * Get the user's saved reading progress.
         *
         * Each user has separate progress for each book.
         */
        ReadingProgress progress =
                readingProgressService.getProgress(
                        user.getUserId(),
                        bookId);

        int currentPage = 1;

        if (progress != null) {
            currentPage = progress.getCurrentPage();
        }

        // Generate the temporary S3 URL for the PDF.
        String pdfUrl =
                storageService.getFileUrl(pdfStorageKey);

        // Send data to JSP.
        request.setAttribute("book", book);
        request.setAttribute("pdfUrl", pdfUrl);
        request.setAttribute("currentPage", currentPage);

        // Open our PDF.js reader.
        request.getRequestDispatcher("/read-book.jsp")
                .forward(request, response);
    }
}