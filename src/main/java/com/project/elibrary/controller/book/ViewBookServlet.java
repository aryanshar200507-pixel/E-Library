package com.project.elibrary.controller.book;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.bean.category.Category;
import com.project.elibrary.service.bookservice.BookService;
import com.project.elibrary.service.bookservice.BookServiceImpl;
import com.project.elibrary.service.categoryservice.CategoryService;
import com.project.elibrary.service.categoryservice.CategoryServiceImpl;
import com.project.elibrary.service.ratingservice.RatingService;
import com.project.elibrary.service.ratingservice.RatingServiceImpl;
import com.project.elibrary.service.storageservice.S3StorageServiceImpl;
import com.project.elibrary.service.storageservice.StorageService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/books")
public class ViewBookServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private BookService bookService;
    private CategoryService categoryService;
    private RatingService ratingService;
    private StorageService storageService;

    @Override
    public void init() throws ServletException {

        bookService = new BookServiceImpl();
        categoryService = new CategoryServiceImpl();
        ratingService = new RatingServiceImpl();
        storageService = new S3StorageServiceImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * Get the search keyword.
         *
         * If there is no keyword, the page behaves exactly as before
         * and displays categories.
         */
        String keyword = request.getParameter("keyword");

        if (keyword != null) {
            keyword = keyword.trim();
        }

        /*
         * Get page number.
         */
        int page = 1;
        int pageSize = 6;

        String pageParameter = request.getParameter("page");

        if (pageParameter != null && !pageParameter.isBlank()) {

            try {
                page = Integer.parseInt(pageParameter);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        if (page < 1) {
            page = 1;
        }

        /*
         * =========================================================
         * SEARCH MODE
         * =========================================================
         *
         * If the user entered a search keyword, search books.
         */
        if (keyword != null && !keyword.isBlank()) {

            List<Book> books =
                    bookService.searchBooks(
                            keyword,
                            page,
                            pageSize
                    );

            int totalBooks =
                    bookService.getTotalSearchResults(keyword);

            int totalPages =
                    (int) Math.ceil(
                            (double) totalBooks / pageSize
                    );

            /*
             * Maps used by the JSP to display rating information.
             */
            Map<Long, Double> bookAverageRatingMap =
                    new HashMap<>();

            Map<Long, Integer> bookRatingCountMap =
                    new HashMap<>();

            Map<Long, String> bookCoverUrlMap =
                    new HashMap<>();

            for (Book book : books) {

                Long bookId = book.getBookId();

                /*
                 * Get average rating.
                 */
                Double averageRating =
                        ratingService.getAverageRating(bookId);

                bookAverageRatingMap.put(
                        bookId,
                        averageRating
                );

                /*
                 * Get number of ratings.
                 */
                int ratingCount =
                        ratingService.getRatingCount(bookId);

                bookRatingCountMap.put(
                        bookId,
                        ratingCount
                );

                /*
                 * Generate temporary S3 URL for cover.
                 */
                String storageKey =
                        book.getCoverStorageKey();

                if (storageKey != null
                        && !storageKey.isBlank()) {

                    String coverUrl =
                            storageService.getFileUrl(
                                    storageKey
                            );

                    bookCoverUrlMap.put(
                            bookId,
                            coverUrl
                    );
                }
            }

            /*
             * Send search results to JSP.
             */
            request.setAttribute(
                    "searchMode",
                    true
            );

            request.setAttribute(
                    "keyword",
                    keyword
            );

            request.setAttribute(
                    "books",
                    books
            );

            request.setAttribute(
                    "bookAverageRatingMap",
                    bookAverageRatingMap
            );

            request.setAttribute(
                    "bookRatingCountMap",
                    bookRatingCountMap
            );

            request.setAttribute(
                    "bookCoverUrlMap",
                    bookCoverUrlMap
            );

            request.setAttribute(
                    "currentPage",
                    page
            );

            request.setAttribute(
                    "totalPages",
                    totalPages
            );

            request.getRequestDispatcher(
                    "/books.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }

        /*
         * =========================================================
         * CATEGORY MODE
         * =========================================================
         *
         * No search keyword means /books behaves as before.
         */
        List<Category> categories =
                categoryService.getCategories(
                        page,
                        pageSize
                );

        int totalCategories =
                categoryService.getTotalCategories();

        int totalPages =
                (int) Math.ceil(
                        (double) totalCategories / pageSize
                );

        /*
         * categoryId -> highest-rated book
         */
        Map<Long, Book> highestRatedBookMap =
                new HashMap<>();

        /*
         * categoryId -> S3 cover URL
         */
        Map<Long, String> categoryCoverUrlMap =
                new HashMap<>();

        for (Category category : categories) {

            Long categoryId =
                    category.getCategoryId();

            Book highestRatedBook =
                    bookService.getHighestRatedBookByCategory(
                            categoryId
                    );

            highestRatedBookMap.put(
                    categoryId,
                    highestRatedBook
            );

            if (highestRatedBook != null) {

                String storageKey =
                        highestRatedBook.getCoverStorageKey();

                if (storageKey != null
                        && !storageKey.isBlank()) {

                    String coverUrl =
                            storageService.getFileUrl(
                                    storageKey
                            );

                    categoryCoverUrlMap.put(
                            categoryId,
                            coverUrl
                    );
                }
            }
        }

        /*
         * Send category data to JSP.
         */
        request.setAttribute(
                "searchMode",
                false
        );

        request.setAttribute(
                "categories",
                categories
        );

        request.setAttribute(
                "highestRatedBookMap",
                highestRatedBookMap
        );

        request.setAttribute(
                "categoryCoverUrlMap",
                categoryCoverUrlMap
        );

        request.setAttribute(
                "currentPage",
                page
        );

        request.setAttribute(
                "totalPages",
                totalPages
        );

        request.getRequestDispatcher(
                "/books.jsp"
        ).forward(
                request,
                response
        );
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}