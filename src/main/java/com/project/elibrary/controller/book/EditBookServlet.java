package com.project.elibrary.controller.book;

import java.io.IOException;
import java.util.List;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.bean.category.Category;
import com.project.elibrary.service.bookservice.BookService;
import com.project.elibrary.service.bookservice.BookServiceImpl;
import com.project.elibrary.service.categoryservice.CategoryService;
import com.project.elibrary.service.categoryservice.CategoryServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/books/edit")
public class EditBookServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private BookService bookService;
	private CategoryService categoryService;

	public EditBookServlet() {
		super();
		this.bookService = new BookServiceImpl();
		this.categoryService = new CategoryServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idParam = request.getParameter("id");

		if (idParam == null || idParam.isBlank()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required.");
			return;
		}

		try {

			Long bookId = Long.parseLong(idParam);

			Book book = bookService.getBookById(bookId);

			if (book == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found.");
				return;
			}

			List<Category> categories = categoryService.getAllCategories();

			request.setAttribute("book", book);
			request.setAttribute("categories", categories);

			request.getRequestDispatcher("/WEB-INF/admin/editBook.jsp").forward(request, response);

		} catch (NumberFormatException e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID.");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		try {

			Long bookId = Long.parseLong(request.getParameter("bookId"));

			String title = request.getParameter("title");

			String author = request.getParameter("author");

			String description = request.getParameter("description");

			Long categoryId = Long.parseLong(request.getParameter("categoryId"));

			String publishedAt = request.getParameter("publishedAt");

			Book book = bookService.getBookById(bookId);

			if (book == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found.");
				return;
			}

			book.setTitle(title);
			book.setAuthor(author);
			book.setDescription(description);
			book.setCategoryId(categoryId);

			if (publishedAt != null && !publishedAt.isBlank()) {

				book.setPublishedAt(java.time.LocalDate.parse(publishedAt));

			} else {

				book.setPublishedAt(null);
			}

			boolean updated = bookService.updateBook(book);

			if (updated) {

				if (updated) {

					response.sendRedirect(request.getContextPath() + "/books/details?id=" + bookId);

				}

			} else {

				request.setAttribute("error", "Failed to update book.");

				List<Category> categories = categoryService.getAllCategories();

				request.setAttribute("book", book);
				request.setAttribute("categories", categories);

				request.getRequestDispatcher("/WEB-INF/admin/editBook.jsp").forward(request, response);
			}

		} catch (Exception e) {

			e.printStackTrace();

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book information.");
		}
	}
}