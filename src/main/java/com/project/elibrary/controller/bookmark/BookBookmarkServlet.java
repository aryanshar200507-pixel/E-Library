package com.project.elibrary.controller.bookmark;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.bookmarkservice.BookmarkService;
import com.project.elibrary.service.bookmarkservice.BookmarkServiceImpl;
import com.project.elibrary.service.storageservice.S3StorageServiceImpl;
import com.project.elibrary.service.storageservice.StorageService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/books/bookmark-book")
public class BookBookmarkServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private BookmarkService bookmarkService;
    private StorageService storageService;

    private static final int PAGE_SIZE = 10;

    @Override
    public void init() throws ServletException {

        bookmarkService = new BookmarkServiceImpl();
        storageService = new S3StorageServiceImpl();
    }

    // =========================================================
    // GET
    // =========================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        // =====================================================
        // CHECK LOGIN
        // =====================================================

        if (session == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User is not logged in.");

            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User is not logged in.");

            return;
        }

        // =====================================================
        // ONLY USER CAN ACCESS BOOKMARKS
        // =====================================================

        if (loggedInUser.getRole() == null
                || !"USER".equalsIgnoreCase(
                        loggedInUser.getRole().name())) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only users can manage bookmarks.");

            return;
        }

        // =====================================================
        // CHECK WHETHER THIS IS:
        //
        // 1. SINGLE BOOK STATUS REQUEST
        // 2. BOOKMARKED BOOKS PAGE REQUEST
        // =====================================================

        String bookIdParam =
                request.getParameter("bookId");

        /*
         * If bookId exists:
         *
         * /books/bookmark-book?bookId=5
         *
         * Return true/false for that particular book.
         */

        if (bookIdParam != null
                && !bookIdParam.isBlank()) {

            handleBookBookmarkStatus(
                    request,
                    response,
                    loggedInUser,
                    bookIdParam);

            return;
        }

        /*
         * No bookId:
         *
         * /books/bookmark-book
         * /books/bookmark-book?page=2
         *
         * Show the bookmarked books page.
         */

        handleBookmarkedBooksPage(
                request,
                response,
                loggedInUser);
    }

    // =========================================================
    // SINGLE BOOK BOOKMARK STATUS
    // =========================================================

    private void handleBookBookmarkStatus(
            HttpServletRequest request,
            HttpServletResponse response,
            User loggedInUser,
            String bookIdParam)
            throws IOException {

        Long bookId;

        try {

            bookId =
                    Long.parseLong(bookIdParam);

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid book ID.");

            return;
        }

        if (bookId <= 0) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid book ID.");

            return;
        }

        boolean bookmarked =
                bookmarkService.isBookBookmarked(
                        loggedInUser.getUserId(),
                        bookId);

        response.setContentType(
                "text/plain");

        response.setCharacterEncoding(
                "UTF-8");

        response.getWriter().write(
                bookmarked ? "true" : "false"
        );
    }

    // =========================================================
    // BOOKMARKED BOOKS PAGE
    // =========================================================

    private void handleBookmarkedBooksPage(
            HttpServletRequest request,
            HttpServletResponse response,
            User loggedInUser)
            throws ServletException, IOException {

        // =====================================================
        // READ PAGE NUMBER
        // =====================================================

        int currentPage = 1;

        String pageParam =
                request.getParameter("page");

        if (pageParam != null
                && !pageParam.isBlank()) {

            try {

                currentPage =
                        Integer.parseInt(pageParam);

            } catch (NumberFormatException e) {

                currentPage = 1;
            }
        }

        // Never allow page below 1
        if (currentPage < 1) {
            currentPage = 1;
        }

        // =====================================================
        // GET TOTAL BOOKMARKED BOOKS
        // =====================================================

        int totalBooks =
                bookmarkService.getTotalBookmarkedBooks(
                        loggedInUser.getUserId());

        // =====================================================
        // CALCULATE TOTAL PAGES
        // =====================================================

        int totalPages =
                (int) Math.ceil(
                        (double) totalBooks / PAGE_SIZE
                );

        /*
         * If there are no bookmarks,
         * keep page as 1.
         */
        if (totalPages == 0) {
            totalPages = 1;
        }

        /*
         * If someone manually enters a page
         * greater than the available pages,
         * move them to the last page.
         */
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        // =====================================================
        // GET BOOKMARKED BOOKS
        // =====================================================

        List<Book> books =
                bookmarkService.getBookmarkedBooks(
                        loggedInUser.getUserId(),
                        currentPage,
                        PAGE_SIZE);

        // =====================================================
        // GENERATE S3 COVER URLS
        // =====================================================

        Map<Long, String> coverUrlMap =
                new HashMap<>();

        for (Book book : books) {

            String coverStorageKey =
                    book.getCoverStorageKey();

            if (coverStorageKey == null
                    || coverStorageKey.isBlank()) {

                continue;
            }

            try {

                String coverUrl =
                        storageService.getFileUrl(
                                coverStorageKey);

                if (coverUrl != null
                        && !coverUrl.isBlank()) {

                    coverUrlMap.put(
                            book.getBookId(),
                            coverUrl);
                }

            } catch (Exception e) {

                getServletContext().log(
                        "Failed to generate cover URL "
                        + "for book ID: "
                        + book.getBookId(),
                        e);
            }
        }

        // =====================================================
        // SEND DATA TO JSP
        // =====================================================

        request.setAttribute(
                "books",
                books);

        request.setAttribute(
                "coverUrlMap",
                coverUrlMap);

        request.setAttribute(
                "currentPage",
                currentPage);

        request.setAttribute(
                "totalPages",
                totalPages);

        request.setAttribute(
                "totalBooks",
                totalBooks);

        // =====================================================
        // FORWARD TO JSP
        // =====================================================

        request.getRequestDispatcher(
                "/WEB-INF/user/bookmarked-books.jsp")
                .forward(request, response);
    }

    // =========================================================
    // POST
    // =========================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        // =====================================================
        // CHECK LOGIN
        // =====================================================

        if (session == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User is not logged in.");

            return;
        }

        User loggedInUser =
                (User) session.getAttribute(
                        "loggedInUser");

        if (loggedInUser == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User is not logged in.");

            return;
        }

        // =====================================================
        // ONLY USER CAN MANAGE BOOKMARKS
        // =====================================================

        if (loggedInUser.getRole() == null
                || !"USER".equalsIgnoreCase(
                        loggedInUser.getRole().name())) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only users can manage bookmarks.");

            return;
        }

        // =====================================================
        // GET BOOK ID
        // =====================================================

        String bookIdParam =
                request.getParameter("bookId");

        if (bookIdParam == null
                || bookIdParam.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Book ID is required.");

            return;
        }

        Long bookId;

        try {

            bookId =
                    Long.parseLong(bookIdParam);

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid book ID.");

            return;
        }

        if (bookId <= 0) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid book ID.");

            return;
        }

        // =====================================================
        // TOGGLE BOOKMARK
        // =====================================================

        Long userId =
                loggedInUser.getUserId();

        boolean alreadyBookmarked =
                bookmarkService.isBookBookmarked(
                        userId,
                        bookId);

        boolean success;
        String action;

        if (alreadyBookmarked) {

            success =
                    bookmarkService.removeBookBookmark(
                            userId,
                            bookId);

            action = "removed";

        } else {

            success =
                    bookmarkService.addBookBookmark(
                            userId,
                            bookId);

            action = "added";
        }

        // =====================================================
        // CHECK RESULT
        // =====================================================

        if (!success) {

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Failed to update book bookmark.");

            return;
        }

        // =====================================================
        // RETURN RESULT
        // =====================================================

        response.setContentType(
                "text/plain");

        response.setCharacterEncoding(
                "UTF-8");

        response.getWriter().write(
                action);
    }
}