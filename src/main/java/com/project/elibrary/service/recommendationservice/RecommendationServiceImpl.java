package com.project.elibrary.service.recommendationservice;

	import java.util.ArrayList;
	import java.util.Collections;
	import java.util.Comparator;
	import java.util.HashSet;
	import java.util.LinkedHashMap;
	import java.util.List;
	import java.util.Map;
	import java.util.Set;

	import com.project.elibrary.bean.book.Book;
	import com.project.elibrary.bean.recommendation.Recommendation;
	import com.project.elibrary.service.bookservice.BookService;
	import com.project.elibrary.service.bookservice.BookServiceImpl;
	import com.project.elibrary.service.ratingservice.RatingService;
	import com.project.elibrary.service.ratingservice.RatingServiceImpl;
	import com.project.elibrary.service.readingprogressservice.ReadingProgressService;
	import com.project.elibrary.service.readingprogressservice.ReadingProgressServiceImpl;

	public class RecommendationServiceImpl implements RecommendationService {

	    private BookService bookService;
	    private RatingService ratingService;
	    private ReadingProgressService readingProgressService;

	    public RecommendationServiceImpl() {

	        bookService = new BookServiceImpl();
	        ratingService = new RatingServiceImpl();
	        readingProgressService = new ReadingProgressServiceImpl();
	    }

	    @Override
	    public List<Book> getRecommendations(Long userId, int limit) {

	        List<Book> result = new ArrayList<>();

	        // Check whether user ID and limit are valid
	        if (userId == null || limit <= 0) {
	            return result;
	        }

	        // Get the user's recently read books
	        List<Book> recentlyRead =
	                readingProgressService.getRecentlyRead(userId, 5);

	        // If the user has no reading history,
	        // use fallback recommendations
	        if (recentlyRead == null || recentlyRead.isEmpty()) {
	            return getFallbackRecommendations(limit);
	        }

	        // Store IDs of recently read books
	        Set<Long> recentlyReadIds = new HashSet<>();

	        // Store categories from recently read books
	        Set<Long> interestedCategories = new HashSet<>();

	        for (Book book : recentlyRead) {

	            if (book == null) {
	                continue;
	            }

	            // Add recently read book ID
	            if (book.getBookId() != null) {
	                recentlyReadIds.add(book.getBookId());
	            }

	            // Add category ID
	            if (book.getCategoryId() != null) {
	                interestedCategories.add(book.getCategoryId());
	            }
	        }

	        // If no category was found,
	        // use fallback recommendations
	        if (interestedCategories.isEmpty()) {
	            return getFallbackRecommendations(limit);
	        }

	        /*
	         * Store candidate books.
	         *
	         * LinkedHashMap prevents duplicate books
	         * when the same book appears in candidate results.
	         */
	        Map<Long, Book> candidateBooks = new LinkedHashMap<>();

	        // Find books from categories the user recently read
	        for (Long categoryId : interestedCategories) {

	            if (categoryId == null) {
	                continue;
	            }

	            /*
	             * Your actual BookService method is:
	             *
	             * getBookByCategory(categoryId, keyword, page, pageSize)
	             */
	            List<Book> books = bookService.getBookByCategory(
	                    categoryId,
	                    "",
	                    1,
	                    5
	            );

	            if (books == null) {
	                continue;
	            }

	            for (Book book : books) {

	                if (book == null || book.getBookId() == null) {
	                    continue;
	                }

	                // Do not recommend books already read
	                if (recentlyReadIds.contains(book.getBookId())) {
	                    continue;
	                }

	                // Add candidate book
	                candidateBooks.put(book.getBookId(), book);
	            }
	        }

	        // If no candidate books were found,
	        // use fallback recommendations
	        if (candidateBooks.isEmpty()) {
	            return getFallbackRecommendations(limit);
	        }

	        // Find the highest number of views
	        long maxViews = 0;

	        for (Book book : candidateBooks.values()) {

	            Long views = book.getViews();

	            if (views != null && views > maxViews) {
	                maxViews = views;
	            }
	        }

	        // Store books with their calculated scores
	        List<Recommendation> recommendations =
	                new ArrayList<>();

	        // Calculate score for each candidate book
	        for (Book book : candidateBooks.values()) {

	            /*
	             * Category Match Score
	             *
	             * The book belongs to a category
	             * recently read by the user.
	             */
	            double categoryMatchScore = 5.0;

	            // Get average rating
	            Double averageRating =
	                    ratingService.getAverageRating(
	                            book.getBookId()
	                    );

	            // If no rating exists, use 0
	            if (averageRating == null) {
	                averageRating = 0.0;
	            }

	            // Get number of ratings
	            int ratingCount =
	                    ratingService.getRatingCount(
	                            book.getBookId()
	                    );

	            /*
	             * Rating Score
	             *
	             * Average rating is given a weight of 2.
	             */
	            double ratingScore =
	                    averageRating * 2.0;

	            /*
	             * Rating Confidence
	             *
	             * More ratings give slightly more confidence.
	             * Maximum contribution = 1.0
	             */
	            double ratingConfidence =
	                    Math.min(ratingCount, 10) * 0.10;

	            /*
	             * Popularity Bonus
	             *
	             * Books with more views get a small bonus.
	             */
	            double popularityBonus = 0.0;

	            if (maxViews > 0 && book.getViews() != null) {

	                popularityBonus =
	                        ((double) book.getViews() / maxViews) * 0.5;
	            }

	            /*
	             * Final recommendation score
	             */
	            double score =
	                    categoryMatchScore
	                    + ratingScore
	                    + ratingConfidence
	                    + popularityBonus;

	            // Create recommendation object
	            Recommendation recommendation =
	                    new Recommendation(book, score);

	            recommendations.add(recommendation);
	        }

	        /*
	         * Sort recommendations from highest score
	         * to lowest score.
	         */
	        Collections.sort(
	                recommendations,
	                Comparator.comparingDouble(
	                        Recommendation::getScore
	                ).reversed()
	        );

	        /*
	         * Return only the required number of books.
	         */
	        int resultSize =
	                Math.min(limit, recommendations.size());

	        for (int i = 0; i < resultSize; i++) {

	            result.add(
	                    recommendations.get(i).getBook()
	            );
	        }

	        return result;
	    }


	    /*
	     * Fallback recommendation method.
	     *
	     * This is used when a user has no reading history.
	     */
	    private List<Book> getFallbackRecommendations(int limit) {

	        List<Book> result = new ArrayList<>();

	        /*
	         * Get a small list of existing books.
	         *
	         * We use the existing BookService instead
	         * of creating a new DAO/database query.
	         */
	        List<Book> books =
	                bookService.getAllBooks(1, 20);

	        if (books == null || books.isEmpty()) {
	            return result;
	        }

	        List<Recommendation> recommendations =
	                new ArrayList<>();

	        // Calculate scores for fallback books
	        for (Book book : books) {

	            if (book == null || book.getBookId() == null) {
	                continue;
	            }

	            // Get average rating
	            Double averageRating =
	                    ratingService.getAverageRating(
	                            book.getBookId()
	                    );

	            if (averageRating == null) {
	                averageRating = 0.0;
	            }

	            // Get rating count
	            int ratingCount =
	                    ratingService.getRatingCount(
	                            book.getBookId()
	                    );

	            // Rating score
	            double ratingScore =
	                    averageRating * 2.0;

	            // Rating confidence
	            double ratingConfidence =
	                    Math.min(ratingCount, 10) * 0.10;

	            // Popularity bonus
	            double popularityBonus = 0.0;

	            if (book.getViews() != null) {

	                popularityBonus =
	                        Math.min(book.getViews(), 1000L)
	                        / 1000.0;
	            }

	            // Final score
	            double score =
	                    ratingScore
	                    + ratingConfidence
	                    + popularityBonus;

	            recommendations.add(
	                    new Recommendation(book, score)
	            );
	        }

	        // Sort from highest score to lowest
	        Collections.sort(
	                recommendations,
	                Comparator.comparingDouble(
	                        Recommendation::getScore
	                ).reversed()
	        );

	        // Return only required number of books
	        int resultSize =
	                Math.min(limit, recommendations.size());

	        for (int i = 0; i < resultSize; i++) {

	            result.add(
	                    recommendations.get(i).getBook()
	            );
	        }

	        return result;
	    }
	}
