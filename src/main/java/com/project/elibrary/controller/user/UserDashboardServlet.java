package com.project.elibrary.controller.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.bean.category.Category;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.bookservice.BookService;
import com.project.elibrary.service.bookservice.BookServiceImpl;
import com.project.elibrary.service.categoryservice.CategoryService;
import com.project.elibrary.service.categoryservice.CategoryServiceImpl;
import com.project.elibrary.service.ratingservice.RatingService;
import com.project.elibrary.service.ratingservice.RatingServiceImpl;
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

@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CategoryService categoryService;
    private BookService bookService;
    private RatingService ratingService;
    private StorageService storageService;
    private ReadingProgressService readingProgressService;

    
    public UserDashboardServlet() {

        categoryService = new CategoryServiceImpl();
        bookService = new BookServiceImpl();
        ratingService = new RatingServiceImpl();
        storageService = new S3StorageServiceImpl();
        readingProgressService = new ReadingProgressServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * ============================================
         * GET LOGGED-IN USER
         * ============================================
         */

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                request.getContextPath() + "/login.jsp"
            );
            return;
        }

        User loggedInUser =
            (User) session.getAttribute("loggedInUser");
        
       

        if (loggedInUser == null) {
            response.sendRedirect(
                request.getContextPath() + "/login.jsp"
            );
            return;
        }

        Long userId = loggedInUser.getUserId();


        /*
         * ============================================
         * RECENTLY READ BOOKS
         * ============================================
         *
         * Get the last 5 books opened by this user.
         *
         * The ReadingProgress DAO uses:
         *
         * reading_progress.updated_at
         *
         * to determine the most recently read books.
         */

        List<Book> recentlyReadBooks =
            readingProgressService.getRecentlyRead(userId, 5);


        /*
         * ============================================
         * TOP RATED CATEGORIES
         * ============================================
         */

        List<Category> topCategories =
            categoryService.getTopRatedCategories(3);


        /*
         * ============================================
         * CATEGORY BOOK MAP
         * ============================================
         *
         * Key   = category ID
         * Value = list of books
         */

        Map<Long, List<Book>> categoryBooksMap =
            new HashMap<>();


        /*
         * ============================================
         * BOOK AVERAGE RATING MAP
         * ============================================
         */

        Map<Long, Double> bookAverageRatingMap =
            new HashMap<>();


        /*
         * ============================================
         * BOOK RATING COUNT MAP
         * ============================================
         */

        Map<Long, Integer> bookRatingCountMap =
            new HashMap<>();


        /*
         * ============================================
         * BOOK COVER URL MAP
         * ============================================
         */

        Map<Long, String> bookCoverUrlMap =
            new HashMap<>();


        /*
         * ============================================
         * PROCESS RECENTLY READ BOOKS
         * ============================================
         */

        for (Book book : recentlyReadBooks) {

            Long bookId = book.getBookId();

            /*
             * Average rating
             */
            Double averageRating =
                ratingService.getAverageRating(bookId);

            bookAverageRatingMap.put(
                bookId,
                averageRating
            );


            /*
             * Rating count
             */
            int ratingCount =
                ratingService.getRatingCount(bookId);

            bookRatingCountMap.put(
                bookId,
                ratingCount
            );


            /*
             * Cover URL
             */
            String storageKey =
                book.getCoverStorageKey();

            if (storageKey != null && !storageKey.isBlank()) {

                String coverUrl =
                    storageService.getFileUrl(storageKey);

                bookCoverUrlMap.put(
                    bookId,
                    coverUrl
                );
            }
        }


        /*
         * ============================================
         * GET BOOKS FOR TOP CATEGORIES
         * ============================================
         */

        for (Category category : topCategories) {

            Long categoryId =
                category.getCategoryId();

            List<Book> books =
            	    bookService.getBookByCategory(
            	        categoryId,
            	        "",
            	        1,
            	        5
            	    );

            categoryBooksMap.put(
                categoryId,
                books
            );


            /*
             * Get additional information for
             * every category book.
             */

            for (Book book : books) {

                Long bookId =
                    book.getBookId();


                /*
                 * Average rating
                 */
                Double averageRating =
                    ratingService.getAverageRating(bookId);

                bookAverageRatingMap.put(
                    bookId,
                    averageRating
                );


                /*
                 * Rating count
                 */
                int ratingCount =
                    ratingService.getRatingCount(bookId);

                bookRatingCountMap.put(
                    bookId,
                    ratingCount
                );


                /*
                 * Cover URL
                 */
                String storageKey =
                    book.getCoverStorageKey();

                if (storageKey != null && !storageKey.isBlank()) {

                    String coverUrl =
                        storageService.getFileUrl(storageKey);

                    bookCoverUrlMap.put(
                        bookId,
                        coverUrl
                    );
                }
            }
        }


        /*
         * ============================================
         * SEND DATA TO JSP
         * ============================================
         */

        request.setAttribute(
            "recentlyReadBooks",
            recentlyReadBooks
        );

        request.setAttribute(
            "topCategories",
            topCategories
        );

        request.setAttribute(
            "categoryBooksMap",
            categoryBooksMap
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


        /*
         * ============================================
         * FORWARD TO DASHBOARD
         * ============================================
         */

        request.getRequestDispatcher(
            "/WEB-INF/user/dashboard.jsp"
        ).forward(request, response);
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}