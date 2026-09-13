package com.project.elibrary.controller.readingprogress;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.bean.user.User;
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

@WebServlet("/books/history")
public class ReadingHistoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int PAGE_SIZE = 10;

    private ReadingProgressService readingProgressService;

    private StorageService storageService;

    @Override
    public void init() throws ServletException {

        readingProgressService =
                new ReadingProgressServiceImpl();

        storageService =
                new S3StorageServiceImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * Get existing session.
         */
        HttpSession session =
                request.getSession(false);

        /*
         * Get logged-in user.
         */
        User user = null;

        if (session != null) {

            user = (User) session.getAttribute(
                    "loggedInUser");
        }

        /*
         * User must be logged in.
         */
        if (user == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp");

            return;
        }

        /*
         * Get requested page.
         */
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

        /*
         * Page must be at least 1.
         */
        if (currentPage < 1) {
            currentPage = 1;
        }

        Long userId = user.getUserId();

        /*
         * Get total number of books in reading history.
         */
        int totalBooks =
                readingProgressService
                        .getTotalReadingHistory(userId);

        /*
         * Calculate total pages.
         */
        int totalPages =
                (int) Math.ceil(
                        (double) totalBooks / PAGE_SIZE);

        /*
         * Always keep at least one page.
         */
        if (totalPages == 0) {
            totalPages = 1;
        }

        /*
         * Prevent page number from going
         * beyond the last page.
         */
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        /*
         * Get books for the current page.
         */
        List<Book> books =
                readingProgressService
                        .getReadingHistory(
                                userId,
                                currentPage,
                                PAGE_SIZE);

        /*
         * Generate cover URLs.
         *
         * Database stores the S3 storage key,
         * not the actual image URL.
         */
        Map<Long, String> coverUrlMap =
                new HashMap<>();

        for (Book book : books) {

            String storageKey =
                    book.getCoverStorageKey();

            if (storageKey != null
                    && !storageKey.isBlank()) {

                String coverUrl =
                        storageService.getFileUrl(
                                storageKey);

                if (coverUrl != null
                        && !coverUrl.isBlank()) {

                    coverUrlMap.put(
                            book.getBookId(),
                            coverUrl);
                }
            }
        }

        /*
         * Send data to JSP.
         */
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

        /*
         * Open History page.
         */
        request.getRequestDispatcher(
                "/WEB-INF/user/history.jsp")
                .forward(request, response);
    }
}