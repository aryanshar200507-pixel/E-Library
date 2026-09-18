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
import com.project.elibrary.service.bookmarkservice.BookmarkService;
import com.project.elibrary.service.bookmarkservice.BookmarkServiceImpl;
import com.project.elibrary.service.storageservice.S3StorageServiceImpl;
import com.project.elibrary.service.storageservice.StorageService;

import com.project.elibrary.service.recommendationservice.RecommendationService;
import com.project.elibrary.service.recommendationservice.RecommendationServiceImpl;

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
    private BookmarkService bookmarkService;
  
    private RecommendationService recommendationService;
   
    public UserDashboardServlet() {

        categoryService = new CategoryServiceImpl();
        bookService = new BookServiceImpl();
        ratingService = new RatingServiceImpl();
        storageService = new S3StorageServiceImpl();
        readingProgressService = new ReadingProgressServiceImpl();
        bookmarkService = new BookmarkServiceImpl();
        recommendationService = new RecommendationServiceImpl();
        }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * ============================================
         * GET LOGGED-IN USER
         * ============================================
         */

        HttpSession session =
                request.getSession(false);

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

        Long userId =
                loggedInUser.getUserId();


        /*
         * ============================================
         * RECENTLY READ BOOKS
         * ============================================
         */

        List<Book> recentlyReadBooks =
                readingProgressService.getRecentlyRead( userId, 5  );
         
        /*This tells the recommendation system:
		"Which user's recommendations should I calculate?"*/
    
        /*
         * ============================================
         * RECOMMENDING BOOKS
         * ============================================
         */
        
        List<Book> recommendedBooks =
                recommendationService.getRecommendations(userId, 5);

        /*This tells it:
		"Give me a maximum of 5 recommended books."*/
        
        /*
         * ============================================
         * BOOKMARKED BOOKS
         * ============================================
         *
         * Get the first 5 bookmarked books.
         *
         * The dedicated Bookmarks page has
         * pagination. The dashboard only shows
         * a small preview.
         */

        List<Book> bookmarkedBooks =
                bookmarkService.getBookmarkedBooks(
                        userId,
                        1,
                        5
                );


        /*
         * ============================================
         * BOOKMARKED BOOK COVER URL MAP
         * ============================================
         */

        Map<Long, String> bookmarkedBookCoverUrlMap =
                new HashMap<>();


        /*
         * ============================================
         * PROCESS BOOKMARKED BOOK COVERS
         * ============================================
         */

        for (Book book : bookmarkedBooks) {

            Long bookId =
                    book.getBookId();

            String storageKey =
                    book.getCoverStorageKey();

            if (storageKey != null
                    && !storageKey.isBlank()) {

                String coverUrl =
                        storageService.getFileUrl(
                                storageKey
                        );

                bookmarkedBookCoverUrlMap.put(
                        bookId,
                        coverUrl
                );
            }
        }


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
         * BOOK COVER && RECOMMENDED BOOK COVER URL MAP
         * ============================================
         */

        Map<Long, String> bookCoverUrlMap =
                new HashMap<>();

        for (Book book : recommendedBooks) {

            Long bookId = book.getBookId();

            String storageKey = book.getCoverStorageKey();

            if (storageKey != null && !storageKey.isBlank()) {

                String coverUrl =
                        storageService.getFileUrl(storageKey);

                bookCoverUrlMap.put(bookId, coverUrl);
            }
        }
       
        
        /*
         * ============================================
         * PROCESS RECENTLY READ BOOKS
         * ============================================
         */

        for (Book book : recentlyReadBooks) {

            Long bookId =
                    book.getBookId();


            /*
             * Average rating
             */

            Double averageRating =
                    ratingService.getAverageRating(
                            bookId
                    );

            bookAverageRatingMap.put(
                    bookId,
                    averageRating
            );


            /*
             * Rating count
             */

            int ratingCount =
                    ratingService.getRatingCount(
                            bookId
                    );

            bookRatingCountMap.put(
                    bookId,
                    ratingCount
            );


            /*
             * Cover URL
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
                        ratingService.getAverageRating(
                                bookId
                        );

                bookAverageRatingMap.put(
                        bookId,
                        averageRating
                );


                /*
                 * Rating count
                 */

                int ratingCount =
                        ratingService.getRatingCount(
                                bookId
                        );

                bookRatingCountMap.put(
                        bookId,
                        ratingCount
                );


                /*
                 * Cover URL
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
        }


        /*
         * ============================================
         * SEND DATA TO JSP
         * ============================================
         */

        /*
         * Recently read
         */

        request.setAttribute(
                "recentlyReadBooks",
                recentlyReadBooks
        );


        /*
         * Bookmarked books
         */

        request.setAttribute(
                "bookmarkedBooks",
                bookmarkedBooks
        );


        /*
         * Bookmarked book covers
         */

        request.setAttribute(
                "bookmarkedBookCoverUrlMap",
                bookmarkedBookCoverUrlMap
        );


        /*
         * Top categories
         */

        request.setAttribute(
                "topCategories",
                topCategories
        );


        /*
         * Category books
         */

        request.setAttribute(
                "categoryBooksMap",
                categoryBooksMap
        );


        /*
         * Average ratings
         */

        request.setAttribute(
                "bookAverageRatingMap",
                bookAverageRatingMap
        );


        /*
         * Rating counts
         */

        request.setAttribute(
                "bookRatingCountMap",
                bookRatingCountMap
        );


        /*
         * Cover URLs
         */

        request.setAttribute(
                "bookCoverUrlMap",
                bookCoverUrlMap
        );
        
        request.setAttribute(
        		"recommendedBooks",
        		recommendedBooks);

        /*
         * ============================================
         * FORWARD TO DASHBOARD
         * ============================================
         */

        request.getRequestDispatcher(
                "/WEB-INF/user/dashboard.jsp"
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