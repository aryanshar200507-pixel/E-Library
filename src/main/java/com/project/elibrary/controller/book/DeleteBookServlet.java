package com.project.elibrary.controller.book;

import java.io.IOException;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.service.bookservice.BookService;
import com.project.elibrary.service.bookservice.BookServiceImpl;
import com.project.elibrary.service.storageservice.S3StorageServiceImpl;
import com.project.elibrary.service.storageservice.StorageService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/books/delete")
public class DeleteBookServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private BookService bookService;
	private StorageService storageService;

	public DeleteBookServlet() {
		super();

		this.bookService = new BookServiceImpl();
		this.storageService = new S3StorageServiceImpl();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idParam = request.getParameter("bookId");

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

			// Delete database record

			boolean deleted = bookService.deleteBook(bookId);

			if (deleted) {

				// Delete cover from S3

				if (book.getCoverStorageKey() != null && !book.getCoverStorageKey().isBlank()) {

					storageService.deleteFile(book.getCoverStorageKey());
				}

				// Delete PDF from S3

				if (book.getPdfStorageKey() != null && !book.getPdfStorageKey().isBlank()) {

					storageService.deleteFile(book.getPdfStorageKey());
				}

				response.sendRedirect(request.getContextPath() + "/books/details?id=" + bookId);

			} else {

				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete book.");
			}

		} catch (NumberFormatException e) {

			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID.");
		}
	}
}