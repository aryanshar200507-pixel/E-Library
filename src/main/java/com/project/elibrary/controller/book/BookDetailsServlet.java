package com.project.elibrary.controller.book;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.bean.category.Category;
import com.project.elibrary.bean.comment.Comment;
import com.project.elibrary.bean.ratings.Rating;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.dao.userdao.UserDao;
import com.project.elibrary.dao.userdao.UserDaoImpl;
import com.project.elibrary.service.bookservice.BookService;
import com.project.elibrary.service.bookservice.BookServiceImpl;

import com.project.elibrary.service.categoryservice.CategoryService;
import com.project.elibrary.service.categoryservice.CategoryServiceImpl;

import com.project.elibrary.service.commentservice.CommentService;
import com.project.elibrary.service.commentservice.CommentServiceImpl;

import com.project.elibrary.service.ratingservice.RatingService;
import com.project.elibrary.service.ratingservice.RatingServiceImpl;

import com.project.elibrary.service.storageservice.S3StorageServiceImpl;
import com.project.elibrary.service.storageservice.StorageService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/books/details")
public class BookDetailsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private BookService bookService;
	private CategoryService categoryService;
	private RatingService ratingService;
	private CommentService commentService;
	private StorageService storageService;
	private UserDao userDao;

	public BookDetailsServlet() {

		this.bookService = new BookServiceImpl();
		this.categoryService = new CategoryServiceImpl();
		this.ratingService = new RatingServiceImpl();
		this.commentService = new CommentServiceImpl();
		this.storageService = new S3StorageServiceImpl();
		this.userDao = new UserDaoImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * Get the book ID from the URL.
		 *
		 * Example:
		 *
		 * /books/details?id=5
		 */
		String idParameter = request.getParameter("id");

		if (idParameter == null || idParameter.isBlank()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required.");
			return;
		}

		Long bookId;

		try {
			bookId = Long.parseLong(idParameter);
		} catch (NumberFormatException e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID.");
			return;
		}

		if (bookId <= 0) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID.");
			return;
		}

		/*
		 * Find the book.
		 */
		Book book = bookService.getBookById(bookId);

		if (book == null) {

			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found.");
			return;
		}

		/*
		 * ========================================== CATEGORY
		 * ==========================================
		 *
		 * Our CategoryService currently provides getAllCategories().
		 *
		 * So we find the matching category from that list.
		 */
		String categoryName = null;

		List<Category> categories = categoryService.getAllCategories();

		for (Category category : categories) {

			if (category.getCategoryId() == book.getCategoryId()) {

				categoryName = category.getCategoryName();

				break;
			}
		}

		/*
		 * ========================================== BOOK COVER
		 * ==========================================
		 *
		 * The database stores the S3 storage key.
		 *
		 * We convert that key into a temporary URL.
		 */
		String coverUrl = null;

		String storageKey = book.getCoverStorageKey();

		if (storageKey != null && !storageKey.isBlank()) {

			coverUrl = storageService.getFileUrl(storageKey);
		}

		/*
		 * ========================================== RATING
		 * ==========================================
		 */

		Double averageRating = ratingService.getAverageRating(bookId);

		int ratingCount = ratingService.getRatingCount(bookId);

		/*
		 * ========================================== CURRENT USER'S RATING
		 * ==========================================
		 *
		 * Only useful when a normal user is logged in.
		 */
		User loggedInUser = (User) request.getSession().getAttribute("loggedInUser");

		Rating userRating = null;

		if (loggedInUser != null) {

			userRating = ratingService.getUserRating(loggedInUser.getUserId(), bookId);
		}

		/*
		 * ========================================== COMMENTS
		 * ==========================================
		 */

		List<Comment> comments = commentService.getCommentsByBookId(bookId);

		/*
		 * Create a map containing:
		 *
		 * userId -> username
		 *
		 * This allows the JSP to display the actual name of the person who commented.
		 */
		Map<Long, String> commentUserNameMap = new HashMap<>();

		for (Comment comment : comments) {

			User commentUser = userDao.findById(comment.getUserId());

			if (commentUser != null) {

				commentUserNameMap.put(comment.getUserId(), commentUser.getName());
			}
		}

		/*
		 * ========================================== SEND DATA TO JSP
		 * ==========================================
		 */

		request.setAttribute("book", book);
		request.setAttribute("categoryName", categoryName);
		request.setAttribute("coverUrl", coverUrl);

		request.setAttribute("averageRating", averageRating);
		request.setAttribute("ratingCount", ratingCount);

		request.setAttribute("userRating", userRating);

		request.setAttribute("comments", comments);
		request.setAttribute("commentUserNameMap", commentUserNameMap);

		/*
		 * Finally open the individual book page.
		 */
		request.getRequestDispatcher("/book-details.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * Details page only needs GET.
		 *
		 * If POST is received, handle it the same way.
		 */
		doGet(request, response);
	}
}