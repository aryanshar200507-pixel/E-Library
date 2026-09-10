package com.project.elibrary.controller.book;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.bean.category.Category;
import com.project.elibrary.service.bookservice.BookService;
import com.project.elibrary.service.bookservice.BookServiceImpl;
import com.project.elibrary.service.categoryservice.CategoryService;
import com.project.elibrary.service.categoryservice.CategoryServiceImpl;
import com.project.elibrary.service.storageservice.S3StorageServiceImpl;
import com.project.elibrary.service.storageservice.StorageService;

/**
 * Servlet implementation class AddBookServlet
 */
@WebServlet("/admin/books/add")
@MultipartConfig(maxFileSize = 50 * 1024 * 1024, maxRequestSize = 55 * 1024 * 1024)
public class AddBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BookService bookService;
	private StorageService storageService;
	private CategoryService categoryService;

	public AddBookServlet() {
		super();
		this.bookService = new BookServiceImpl();
		this.storageService = new S3StorageServiceImpl();
		this.categoryService = new CategoryServiceImpl();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			List<Category> categories = categoryService.getAllCategories();
			request.setAttribute("categories", categories);
		request.getRequestDispatcher("/WEB-INF/admin/addBook.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("ADD BOOK POST RECEIVED");

		String title = request.getParameter("title");
		String author = request.getParameter("author");
		String description = request.getParameter("description");
		String categoryIdParam = request.getParameter("categoryId");
		String publishedAtParam = request.getParameter("publishedAt");

		Part coverPart = request.getPart("cover");
		Part pdfPart = request.getPart("pdf");

		if (title == null || title.isBlank() || author == null || author.isBlank() || categoryIdParam == null
				|| categoryIdParam.isBlank() || coverPart == null || coverPart.getSize() == 0 || pdfPart == null
				|| pdfPart.getSize() == 0) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Required book information is meissing.");
			return;
		}

		Long categoryId;

		try {
			categoryId = Long.parseLong(categoryIdParam);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid category ID.");
			return;
		}

		LocalDate publishedAt = null;
		if (publishedAtParam != null && !publishedAtParam.isBlank()) {
			try {
				publishedAt = LocalDate.parse(publishedAtParam);
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid published date.");
				return;
			}
		}

		String coverFileName = getFileName(coverPart);
		String pdfFileName = getFileName(pdfPart);

		String coverStorageKey = null;
		String pdfStorageKey = null;

		try {
			System.out.println("Uploading cover: " + coverFileName);
//			UPLOAD COVER TO S3
			try (var inputStream = coverPart.getInputStream()) {
				coverStorageKey = storageService.uploadCover(inputStream, coverFileName, coverPart.getSize());
			}
			System.out.println("Cover uploaded: " + coverStorageKey);
//			UPLOAD PDF TO S3
			System.out.println("Uploading PDF: " + pdfFileName);

			try (var inputStream = pdfPart.getInputStream()) {
				pdfStorageKey = storageService.uploadPdf(inputStream, pdfFileName, pdfPart.getSize());

			}
			System.out.println("PDF uploaded: " + pdfStorageKey);

//			CREATE BOOK OBJECT
			Book book = new Book();
			book.setTitle(title.trim());
			book.setAuthor(author.trim());
			book.setDescription(description == null ? null : description.trim());
			book.setCategoryId(categoryId);
			book.setPublishedAt(publishedAt);
			book.setCoverStorageKey(coverStorageKey);
			book.setPdfStorageKey(pdfStorageKey);
			book.setViews(0L);

//				SAVE BOOK METADATA TO MYSQL
			boolean saved = bookService.addBook(book);

			if (!saved) {
//					DATABASE SAVE FAILED , SO REMOVE UPLODED FILES
				storageService.deleteFile(coverStorageKey);
				storageService.deleteFile(pdfStorageKey);

				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "FAILED TO SAVE BOOK.");
				return;
			}

			response.sendRedirect(request.getContextPath() + "/books");

		} catch (Exception e) {
			// If something fails after uploading,
			// clean up files already uploaded to S3.
			if (coverStorageKey != null) {
				storageService.deleteFile(coverStorageKey);
			}

			if (pdfStorageKey != null) {
				storageService.deleteFile(pdfStorageKey);
			}

			throw new ServletException("Failed to add book.", e);
		}
	}

	private String getFileName(Part part) {

		String contentDisposition = part.getHeader("content-disposition");

		if (contentDisposition == null) {
			return "file";
		}

		for (String item : contentDisposition.split(";")) {

			if (item.trim().startsWith("filename")) {

				String fileName = item.substring(item.indexOf('=') + 1).trim().replace("\"", "");

				int lastSlash = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

				if (lastSlash >= 0) {
					fileName = fileName.substring(lastSlash + 1);
				}

				return fileName;
				
				
			}
		}

		return "file";

	}
}
