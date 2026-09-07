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

@WebServlet("/books/category")
public class CategoryBookServlet extends HttpServlet {

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * ========================================== GET CATEGORY ID
		 * ==========================================
		 *
		 * Example:
		 *
		 * /books/category?id=1
		 */

		String idParameter = request.getParameter("id");

		if (idParameter == null || idParameter.isBlank()) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Category ID is required.");

			return;
		}

		Long categoryId;

		try {

			categoryId = Long.parseLong(idParameter);

		} catch (NumberFormatException e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid category ID.");

			return;
		}

		if (categoryId <= 0) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid category ID.");

			return;
		}

		/*
		 * ========================================== FIND CATEGORY
		 * ==========================================
		 *
		 * At the moment CategoryService provides getAllCategories(), so we find the
		 * selected category from that list.
		 */

		Category selectedCategory = null;

		List<Category> categories = categoryService.getAllCategories();

		for (Category category : categories) {

			if (category.getCategoryId() == categoryId) {

				selectedCategory = category;

				break;
			}
		}

		/*
		 * If the category does not exist, return 404.
		 */

		if (selectedCategory == null) {

			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Category not found.");

			return;
		}

		/*
		 * ========================================== PAGINATION
		 * ==========================================
		 *
		 * Example:
		 *
		 * page = 1 pageSize = 10
		 *
		 * means:
		 *
		 * books 1 - 10
		 */

		int page = 1;

		int pageSize = 10;

		String pageParameter = request.getParameter("page");

		if (pageParameter != null && !pageParameter.isBlank()) {

			try {

				page = Integer.parseInt(pageParameter);

			} catch (NumberFormatException e) {

				page = 1;
			}
		}

		/*
		 * Never allow page to be less than 1.
		 */

		if (page < 1) {

			page = 1;
		}

		/*
		 * ========================================== GET BOOKS FOR THIS CATEGORY
		 * ==========================================
		 *
		 * IMPORTANT:
		 *
		 * This does NOT get all books.
		 *
		 * It only gets books whose category_id matches categoryId.
		 */

		List<Book> books = bookService.getBookByCategory(categoryId, page, pageSize);

		/*
		 * ========================================== TOTAL BOOKS
		 * ==========================================
		 *
		 * Used to calculate total pages.
		 */

		int totalBooks = bookService.getTotalBookByCategory(categoryId);

		/*
		 * Calculate number of pages.
		 *
		 * Example:
		 *
		 * 25 books / 10 per page
		 *
		 * = 3 pages
		 */

		int totalPages = (int) Math.ceil((double) totalBooks / pageSize);

		/*
		 * ========================================== RATING DATA
		 * ==========================================
		 *
		 * We need rating information for every book displayed on this page.
		 */

		Map<Long, Double> averageRatingMap = new HashMap<>();

		Map<Long, Integer> ratingCountMap = new HashMap<>();

		/*
		 * ========================================== COVER URL DATA
		 * ==========================================
		 *
		 * Database stores the S3 storage key.
		 *
		 * We convert each key into a temporary URL.
		 */

		Map<Long, String> coverUrlMap = new HashMap<>();

		for (Book book : books) {

			Long bookId = book.getBookId();

			/*
			 * Get average rating.
			 */

			Double averageRating = ratingService.getAverageRating(bookId);

			averageRatingMap.put(bookId, averageRating);

			/*
			 * Get number of ratings.
			 */

			int ratingCount = ratingService.getRatingCount(bookId);

			ratingCountMap.put(bookId, ratingCount);

			/*
			 * Get cover URL.
			 */

			String storageKey = book.getCoverStorageKey();

			if (storageKey != null && !storageKey.isBlank()) {

				String coverUrl = storageService.getFileUrl(storageKey);

				coverUrlMap.put(bookId, coverUrl);
			}
		}

		/*
		 * ========================================== SEND DATA TO JSP
		 * ==========================================
		 */

		request.setAttribute("category", selectedCategory);

		request.setAttribute("books", books);

		request.setAttribute("coverUrlMap", coverUrlMap);

		request.setAttribute("averageRatingMap", averageRatingMap);

		request.setAttribute("ratingCountMap", ratingCountMap);

		request.setAttribute("currentPage", page);

		request.setAttribute("totalPages", totalPages);

		/*
		 * Open the category books page.
		 */

		request.getRequestDispatcher("/category-books.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}