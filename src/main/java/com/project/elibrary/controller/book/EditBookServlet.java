package com.project.elibrary.controller.book;

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

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/admin/books/edit")
@MultipartConfig(
		maxFileSize = 50 * 1024 * 1024,
		maxRequestSize = 55 * 1024 * 1024
)
public class EditBookServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private BookService bookService;
	private CategoryService categoryService;
	private StorageService storageService;

	public EditBookServlet() {
		super();

		bookService = new BookServiceImpl();
		categoryService = new CategoryServiceImpl();
		storageService = new S3StorageServiceImpl();
	}

	@Override
	protected void doGet(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * ============================================
		 * GET BOOK ID
		 * ============================================
		 */

		String idParam = request.getParameter("id");

		if (idParam == null || idParam.isBlank()) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Book ID is required."
			);

			return;
		}

		Long bookId;

		try {

			bookId = Long.parseLong(idParam);

		} catch (NumberFormatException e) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Invalid book ID."
			);

			return;
		}

		if (bookId <= 0) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Invalid book ID."
			);

			return;
		}

		/*
		 * ============================================
		 * FIND BOOK
		 * ============================================
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
		 * ============================================
		 * GET CATEGORIES
		 * ============================================
		 */

		List<Category> categories =
				categoryService.getAllCategories();

		request.setAttribute("book", book);
		request.setAttribute("categories", categories);

		request.getRequestDispatcher(
				"/WEB-INF/admin/editBook.jsp"
		).forward(request, response);
	}

	@Override
	protected void doPost(
			HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		/*
		 * ============================================
		 * GET FORM VALUES
		 * ============================================
		 */

		String bookIdParam =
				request.getParameter("bookId");

		String title =
				request.getParameter("title");

		String author =
				request.getParameter("author");

		String description =
				request.getParameter("description");

		String categoryIdParam =
				request.getParameter("categoryId");

		String publishedAtParam =
				request.getParameter("publishedAt");

		/*
		 * ============================================
		 * GET OPTIONAL FILES
		 * ============================================
		 */

		Part coverPart =
				request.getPart("cover");

		Part pdfPart =
				request.getPart("pdf");

		/*
		 * ============================================
		 * BOOK ID VALIDATION
		 * ============================================
		 */

		if (bookIdParam == null ||
				bookIdParam.isBlank()) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Book ID is required."
			);

			return;
		}

		Long bookId;

		try {

			bookId = Long.parseLong(bookIdParam);

		} catch (NumberFormatException e) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Invalid book ID."
			);

			return;
		}

		if (bookId <= 0) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Invalid book ID."
			);

			return;
		}

		/*
		 * ============================================
		 * REQUIRED FIELD VALIDATION
		 * ============================================
		 */

		if (title == null || title.isBlank()) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Book title is required."
			);

			return;
		}

		if (author == null || author.isBlank()) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Book author is required."
			);

			return;
		}

		if (categoryIdParam == null ||
				categoryIdParam.isBlank()) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Category is required."
			);

			return;
		}

		/*
		 * ============================================
		 * CATEGORY ID VALIDATION
		 * ============================================
		 */

		Long categoryId;

		try {

			categoryId =
					Long.parseLong(categoryIdParam);

		} catch (NumberFormatException e) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Invalid category ID."
			);

			return;
		}

		if (categoryId <= 0) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Invalid category ID."
			);

			return;
		}

		/*
		 * ============================================
		 * VERIFY CATEGORY EXISTS
		 * ============================================
		 */

		List<Category> categories =
				categoryService.getAllCategories();

		boolean categoryExists = false;

		for (Category category : categories) {

			if (category.getCategoryId() == categoryId) {

				categoryExists = true;

				break;
			}
		}

		if (!categoryExists) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Selected category does not exist."
			);

			return;
		}

		/*
		 * ============================================
		 * FIND EXISTING BOOK
		 * ============================================
		 */

		Book book =
				bookService.getBookById(bookId);

		if (book == null) {

			response.sendError(
					HttpServletResponse.SC_NOT_FOUND,
					"Book not found."
			);

			return;
		}

		/*
		 * ============================================
		 * PUBLISHED DATE VALIDATION
		 * ============================================
		 */

		LocalDate publishedAt = null;

		if (publishedAtParam != null &&
				!publishedAtParam.isBlank()) {

			try {

				publishedAt =
						LocalDate.parse(
								publishedAtParam
						);

			} catch (Exception e) {

				response.sendError(
						HttpServletResponse.SC_BAD_REQUEST,
						"Invalid published date."
				);

				return;
			}
		}

		/*
		 * ============================================
		 * CHECK NEW COVER
		 * ============================================
		 */

		boolean newCoverSelected =
				coverPart != null &&
				coverPart.getSize() > 0;

		/*
		 * ============================================
		 * CHECK NEW PDF
		 * ============================================
		 */

		boolean newPdfSelected =
				pdfPart != null &&
				pdfPart.getSize() > 0;

		/*
		 * ============================================
		 * FILE VALIDATION
		 * ============================================
		 */

		String newCoverFileName = null;
		String newPdfFileName = null;

		if (newCoverSelected) {

			newCoverFileName =
					getFileName(coverPart);

			if (newCoverFileName == null ||
					newCoverFileName.isBlank()) {

				response.sendError(
						HttpServletResponse.SC_BAD_REQUEST,
						"Invalid cover file."
				);

				return;
			}

			if (!isValidCoverFile(newCoverFileName)) {

				response.sendError(
						HttpServletResponse.SC_BAD_REQUEST,
						"Only JPG, JPEG, PNG and WEBP cover images are allowed."
				);

				return;
			}
		}

		if (newPdfSelected) {

			newPdfFileName =
					getFileName(pdfPart);

			if (newPdfFileName == null ||
					newPdfFileName.isBlank()) {

				response.sendError(
						HttpServletResponse.SC_BAD_REQUEST,
						"Invalid PDF file."
				);

				return;
			}

			if (!isPdfFile(newPdfFileName)) {

				response.sendError(
						HttpServletResponse.SC_BAD_REQUEST,
						"Only PDF files are allowed."
				);

				return;
			}
		}

		/*
		 * ============================================
		 * FILE SIZE VALIDATION
		 * ============================================
		 */

		long maxFileSize =
				50L * 1024 * 1024;

		if (newCoverSelected &&
				coverPart.getSize() > maxFileSize) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"Cover file is too large. Maximum size is 50 MB."
			);

			return;
		}

		if (newPdfSelected &&
				pdfPart.getSize() > maxFileSize) {

			response.sendError(
					HttpServletResponse.SC_BAD_REQUEST,
					"PDF file is too large. Maximum size is 50 MB."
			);

			return;
		}

		/*
		 * ============================================
		 * REMEMBER OLD STORAGE KEYS
		 * ============================================
		 *
		 * We must keep these until the database update
		 * succeeds.
		 */

		String oldCoverStorageKey =
				book.getCoverStorageKey();

		String oldPdfStorageKey =
				book.getPdfStorageKey();

		/*
		 * These hold newly uploaded files.
		 */

		String newCoverStorageKey = null;
		String newPdfStorageKey = null;

		try {

			/*
			 * ========================================
			 * UPLOAD NEW COVER
			 * ========================================
			 */

			if (newCoverSelected) {

				System.out.println(
						"Uploading new cover: " +
						newCoverFileName
				);

				try (var inputStream =
						coverPart.getInputStream()) {

					newCoverStorageKey =
							storageService.uploadCover(
									inputStream,
									newCoverFileName,
									coverPart.getSize()
							);
				}

				System.out.println(
						"New cover uploaded: " +
						newCoverStorageKey
				);

				book.setCoverStorageKey(
						newCoverStorageKey
				);
			}

			/*
			 * ========================================
			 * UPLOAD NEW PDF
			 * ========================================
			 */

			if (newPdfSelected) {

				System.out.println(
						"Uploading new PDF: " +
						newPdfFileName
				);

				try (var inputStream =
						pdfPart.getInputStream()) {

					newPdfStorageKey =
							storageService.uploadPdf(
									inputStream,
									newPdfFileName,
									pdfPart.getSize()
							);
				}

				System.out.println(
						"New PDF uploaded: " +
						newPdfStorageKey
				);

				book.setPdfStorageKey(
						newPdfStorageKey
				);
			}

			/*
			 * ========================================
			 * UPDATE BOOK METADATA
			 * ========================================
			 */

			book.setTitle(
					title.trim()
			);

			book.setAuthor(
					author.trim()
			);

			book.setDescription(
					description == null
							? null
							: description.trim()
			);

			book.setCategoryId(
					categoryId
			);

			book.setPublishedAt(
					publishedAt
			);

			/*
			 * ========================================
			 * UPDATE DATABASE
			 * ========================================
			 */

			boolean updated =
					bookService.updateBook(book);

			if (!updated) {

				/*
				 * Database update failed.
				 *
				 * Remove newly uploaded files.
				 *
				 * Old files are untouched.
				 */

				if (newCoverStorageKey != null) {

					storageService.deleteFile(
							newCoverStorageKey
					);
				}

				if (newPdfStorageKey != null) {

					storageService.deleteFile(
							newPdfStorageKey
					);
				}

				request.setAttribute(
						"error",
						"Failed to update book."
				);

				request.setAttribute(
						"book",
						book
				);

				request.setAttribute(
						"categories",
						categories
				);

				request.getRequestDispatcher(
						"/WEB-INF/admin/editBook.jsp"
				).forward(request, response);

				return;
			}

			/*
			 * ========================================
			 * DATABASE UPDATE SUCCEEDED
			 * ========================================
			 *
			 * Now it is safe to remove the old files.
			 */

			if (newCoverStorageKey != null &&
					oldCoverStorageKey != null &&
					!oldCoverStorageKey.isBlank()) {

				storageService.deleteFile(
						oldCoverStorageKey
				);
			}

			if (newPdfStorageKey != null &&
					oldPdfStorageKey != null &&
					!oldPdfStorageKey.isBlank()) {

				storageService.deleteFile(
						oldPdfStorageKey
				);
			}

			/*
			 * ========================================
			 * SUCCESS
			 * ========================================
			 */

			response.sendRedirect(
					request.getContextPath()
					+ "/books/details?id="
					+ bookId
			);

		} catch (Exception e) {

			/*
			 * ========================================
			 * CLEAN UP NEW FILES
			 * ========================================
			 *
			 * If something fails before the database
			 * update completes, remove newly uploaded
			 * files.
			 */

			if (newCoverStorageKey != null) {

				storageService.deleteFile(
						newCoverStorageKey
				);
			}

			if (newPdfStorageKey != null) {

				storageService.deleteFile(
						newPdfStorageKey
				);
			}

			throw new ServletException(
					"Failed to update book.",
					e
			);
		}
	}

	/*
	 * ============================================
	 * PDF VALIDATION
	 * ============================================
	 */

	private boolean isPdfFile(String fileName) {

		return fileName
				.toLowerCase()
				.endsWith(".pdf");
	}

	/*
	 * ============================================
	 * COVER VALIDATION
	 * ============================================
	 */

	private boolean isValidCoverFile(
			String fileName) {

		String lowerCaseFileName =
				fileName.toLowerCase();

		return lowerCaseFileName.endsWith(".jpg")
				|| lowerCaseFileName.endsWith(".jpeg")
				|| lowerCaseFileName.endsWith(".png")
				|| lowerCaseFileName.endsWith(".webp");
	}

	/*
	 * ============================================
	 * EXTRACT FILE NAME
	 * ============================================
	 */

	private String getFileName(Part part) {

		String contentDisposition =
				part.getHeader("content-disposition");

		if (contentDisposition == null) {

			return "file";
		}

		for (String item :
				contentDisposition.split(";")) {

			if (item.trim()
					.startsWith("filename")) {

				String fileName =
						item.substring(
								item.indexOf('=') + 1
						)
						.trim()
						.replace("\"", "");

				int lastSlash =
						Math.max(
								fileName.lastIndexOf('/'),
								fileName.lastIndexOf('\\')
						);

				if (lastSlash >= 0) {

					fileName =
							fileName.substring(
									lastSlash + 1
							);
				}

				return fileName;
			}
		}

		return "file";
	}
}