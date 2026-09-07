package com.project.elibrary.controller.user;

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

@WebServlet("/user/dashboard")
public class UserDashboardServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private CategoryService categoryService;
	private BookService bookService;
	private RatingService ratingService;
	private StorageService storageService;

	@Override
	public void init() throws ServletException {

		categoryService = new CategoryServiceImpl();
		bookService = new BookServiceImpl();
		ratingService = new RatingServiceImpl();
		storageService = new S3StorageServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * Get the 3 categories having the highest average rating.
		 */
		List<Category> topCategories = categoryService.getTopRatedCategories(3);

		/*
		 * Stores the books belonging to each category.
		 *
		 * Key = category ID Value = list of 5 books
		 */
		Map<Long, List<Book>> categoryBooksMap = new HashMap<>();

		/*
		 * Stores the average rating of each book.
		 *
		 * Key = book ID Value = average rating
		 */
		Map<Long, Double> bookAverageRatingMap = new HashMap<>();

		/*
		 * Stores the number of ratings for each book.
		 *
		 * Key = book ID Value = rating count
		 */
		Map<Long, Integer> bookRatingCountMap = new HashMap<>();

		/*
		 * Stores the S3 cover URL for each book.
		 *
		 * Key = book ID Value = cover URL
		 */
		Map<Long, String> bookCoverUrlMap = new HashMap<>();

		/*
		 * Get 5 books for every top-rated category.
		 */
		for (Category category : topCategories) {

			Long categoryId = category.getCategoryId();

			List<Book> books = bookService.getBookByCategory(categoryId, 1, 5);

			categoryBooksMap.put(categoryId, books);

			/*
			 * Get additional information for every book.
			 */
			for (Book book : books) {

				Long bookId = book.getBookId();

				// Average rating
				Double averageRating = ratingService.getAverageRating(bookId);

				bookAverageRatingMap.put(bookId, averageRating);

				// Rating count
				int ratingCount = ratingService.getRatingCount(bookId);

				bookRatingCountMap.put(bookId, ratingCount);

				// Cover URL
				String storageKey = book.getCoverStorageKey();

				if (storageKey != null && !storageKey.isBlank()) {

					String coverUrl = storageService.getFileUrl(storageKey);

					bookCoverUrlMap.put(bookId, coverUrl);
				}
			}
		}

		/*
		 * Send everything to the JSP.
		 */
		request.setAttribute("topCategories", topCategories);

		request.setAttribute("categoryBooksMap", categoryBooksMap);

		request.setAttribute("bookAverageRatingMap", bookAverageRatingMap);

		request.setAttribute("bookRatingCountMap", bookRatingCountMap);

		request.setAttribute("bookCoverUrlMap", bookCoverUrlMap);

		/*
		 * Display the User Dashboard.
		 */
		request.getRequestDispatcher("/WEB-INF/user/dashboard.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}