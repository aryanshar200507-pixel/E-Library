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

		// Validate book ID
		if (idParam == null || idParam.isBlank()) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Book ID is required."
			);

			return;
		}

		try {

			Long bookId = Long.parseLong(idParam);

			// Book ID must be positive
			if (bookId <= 0) {

				response.sendError(
						HttpServletResponse.SC_BAD_REQUEST,
						"Invalid book ID."
				);

				return;
			}

			/*
			 * Find the book before deleting it.
			 *
			 * We need the storage keys because after the database
			 * record is deleted, we can no longer retrieve them.
			 */
			Book book = bookService.getBookById(bookId);

			if (book == null) {

				response.sendError(
						HttpServletResponse.SC_NOT_FOUND,
						"Book not found."
				);

				return;
			}

			/*
			 * Delete the database record first.
			 *
			 * Because the related tables use ON DELETE CASCADE,
			 * ratings, comments, reading progress, bookmarks
			 * and highlights related to this book will also be deleted.
			 */
			boolean deleted = bookService.deleteBook(bookId);

			if (!deleted) {

				response.sendError(
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Failed to delete book."
				);

				return;
			}

			/*
			 * Delete the PDF from S3.
			 */
			if (book.getPdfStorageKey() != null
					&& !book.getPdfStorageKey().isBlank()) {

				boolean pdfDeleted =
						storageService.deleteFile(book.getPdfStorageKey());

				if (!pdfDeleted) {

					System.err.println(
							"Warning: Failed to delete PDF from S3 for book ID: "
									+ bookId
					);
				}
			}

			/*
			 * Delete the cover from S3.
			 */
			if (book.getCoverStorageKey() != null
					&& !book.getCoverStorageKey().isBlank()) {

				boolean coverDeleted =
						storageService.deleteFile(book.getCoverStorageKey());

				if (!coverDeleted) {

					System.err.println(
							"Warning: Failed to delete cover from S3 for book ID: "
									+ bookId
					);
				}
			}

			/*
			 * Book has been successfully removed from the database.
			 *
			 * Redirect to the shared /books page because the deleted
			 * book's details page no longer exists.
			 */
			response.sendRedirect(
					request.getContextPath() + "/books"
			);

		} catch (NumberFormatException e) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Invalid book ID."
			);

		} catch (RuntimeException e) {

			throw new ServletException(
					"Failed to delete book.",
					e
			);
		}
	}
}