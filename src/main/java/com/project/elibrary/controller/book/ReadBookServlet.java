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

@WebServlet("/books/read")
public class ReadBookServlet extends HttpServlet {

	private BookService bookService;
	private StorageService storageService;


	public ReadBookServlet() {

		// Service used to get book information from the database.
		bookService = new BookServiceImpl();

		// Service used to generate the PDF URL from S3.
		storageService = new S3StorageServiceImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Get the book ID from the URL.
		// Example: /books/read?id=5
		String idParam = request.getParameter("id");

		// Check whether the ID was provided.
		if (idParam == null || idParam.isBlank()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required.");
			return;
		}

		Long bookId;

		try {

			// Convert the ID from String to Long.
			bookId = Long.parseLong(idParam);

		} catch (NumberFormatException e) {

			// The user provided something that is not a valid number.
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID.");
			return;
		}

		// Get the book from the database.
		Book book = bookService.getBookById(bookId);

		// Check whether the book actually exists.
		if (book == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found.");
			return;
		}

		// Get the PDF's storage key from the Book object.
		String pdfStorageKey = book.getPdfStorageKey();

		// Make sure the book actually has a PDF.
		if (pdfStorageKey == null || pdfStorageKey.isBlank()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "PDF file not found for this book.");
			return;
		}
		
		// Increase the view count before opening the PDF.
		//
		// Every time a user clicks "Read Book",
		// the book's views will increase by 1.
		bookService.incrementVIews(bookId);

		// Generate a temporary presigned URL for the PDF.
		String pdfUrl = storageService.getFileUrl(pdfStorageKey);

		// Send the user to the PDF.
		response.sendRedirect(pdfUrl);
	}
}