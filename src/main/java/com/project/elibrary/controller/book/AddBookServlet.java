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

@WebServlet("/admin/books/add")
@MultipartConfig(
    maxFileSize = 50 * 1024 * 1024,
    maxRequestSize = 55 * 1024 * 1024
)
public class AddBookServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private BookService bookService;
    private StorageService storageService;
    private CategoryService categoryService;

    public AddBookServlet() {
        super();

        bookService = new BookServiceImpl();
        storageService = new S3StorageServiceImpl();
        categoryService = new CategoryServiceImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List<Category> categories =
                categoryService.getAllCategories();

        request.setAttribute(
                "categories",
                categories
        );

        request.getRequestDispatcher(
                "/WEB-INF/admin/addBook.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("ADD BOOK POST RECEIVED");

        /*
         * ============================================
         * GET FORM DATA
         * ============================================
         */

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
         * GET UPLOADED FILES
         * ============================================
         */

        Part coverPart =
                request.getPart("cover");

        Part pdfPart =
                request.getPart("pdf");


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

        if (coverPart == null ||
                coverPart.getSize() == 0) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Book cover is required."
            );

            return;
        }

        if (pdfPart == null ||
                pdfPart.getSize() == 0) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "PDF file is required."
            );

            return;
        }


        /*
         * ============================================
         * PARSE CATEGORY ID
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


        /*
         * Category IDs must be positive.
         */

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
         *
         * We do this BEFORE uploading anything to S3.
         */

        boolean categoryExists = false;

        List<Category> categories =
                categoryService.getAllCategories();

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
         * PUBLISHED DATE VALIDATION
         * ============================================
         */

        LocalDate publishedAt = null;

        if (publishedAtParam != null &&
                !publishedAtParam.isBlank()) {

            try {

                publishedAt =
                        LocalDate.parse(publishedAtParam);

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
         * GET FILE NAMES
         * ============================================
         */

        String coverFileName =
                getFileName(coverPart);

        String pdfFileName =
                getFileName(pdfPart);


        /*
         * ============================================
         * FILE NAME VALIDATION
         * ============================================
         */

        if (coverFileName == null ||
                coverFileName.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid cover file."
            );

            return;
        }

        if (pdfFileName == null ||
                pdfFileName.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid PDF file."
            );

            return;
        }


        /*
         * ============================================
         * PDF FILE TYPE VALIDATION
         * ============================================
         */

        if (!isPdfFile(pdfFileName)) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Only PDF files are allowed."
            );

            return;
        }


        /*
         * ============================================
         * COVER FILE TYPE VALIDATION
         * ============================================
         */

        if (!isValidCoverFile(coverFileName)) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Only JPG, JPEG, PNG and WEBP cover images are allowed."
            );

            return;
        }


        /*
         * ============================================
         * FILE SIZE VALIDATION
         * ============================================
         *
         * @MultipartConfig already limits the request,
         * but we also explicitly validate the files here.
         */

        long maxFileSize =
                50L * 1024 * 1024;


        if (coverPart.getSize() > maxFileSize) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Cover file is too large. Maximum size is 50 MB."
            );

            return;
        }


        if (pdfPart.getSize() > maxFileSize) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "PDF file is too large. Maximum size is 50 MB."
            );

            return;
        }


        /*
         * ============================================
         * STORAGE KEYS
         * ============================================
         */

        String coverStorageKey = null;

        String pdfStorageKey = null;


        try {

            /*
             * ========================================
             * UPLOAD COVER TO S3
             * ========================================
             */

            System.out.println(
                    "Uploading cover: " +
                    coverFileName
            );

            try (var inputStream =
                    coverPart.getInputStream()) {

                coverStorageKey =
                        storageService.uploadCover(
                                inputStream,
                                coverFileName,
                                coverPart.getSize()
                        );
            }

            System.out.println(
                    "Cover uploaded: " +
                    coverStorageKey
            );


            /*
             * ========================================
             * UPLOAD PDF TO S3
             * ========================================
             */

            System.out.println(
                    "Uploading PDF: " +
                    pdfFileName
            );

            try (var inputStream =
                    pdfPart.getInputStream()) {

                pdfStorageKey =
                        storageService.uploadPdf(
                                inputStream,
                                pdfFileName,
                                pdfPart.getSize()
                        );
            }

            System.out.println(
                    "PDF uploaded: " +
                    pdfStorageKey
            );


            /*
             * ========================================
             * CREATE BOOK OBJECT
             * ========================================
             */

            Book book = new Book();

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

            book.setCoverStorageKey(
                    coverStorageKey
            );

            book.setPdfStorageKey(
                    pdfStorageKey
            );

            book.setViews(0L);


            /*
             * ========================================
             * SAVE BOOK TO MYSQL
             * ========================================
             */

            boolean saved =
                    bookService.addBook(book);


            /*
             * ========================================
             * DATABASE SAVE FAILED
             * ========================================
             *
             * Remove files already uploaded to S3.
             */

            if (!saved) {

                if (coverStorageKey != null) {

                    storageService.deleteFile(
                            coverStorageKey
                    );
                }

                if (pdfStorageKey != null) {

                    storageService.deleteFile(
                            pdfStorageKey
                    );
                }

                response.sendError(
                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Failed to save book."
                );

                return;
            }


            /*
             * ========================================
             * SUCCESS
             * ========================================
             */

            response.sendRedirect(
                    request.getContextPath() +
                    "/books"
            );

        } catch (Exception e) {

            /*
             * ========================================
             * S3 CLEANUP
             * ========================================
             *
             * If anything fails after an upload,
             * remove the uploaded files.
             */

            if (coverStorageKey != null) {

                storageService.deleteFile(
                        coverStorageKey
                );
            }

            if (pdfStorageKey != null) {

                storageService.deleteFile(
                        pdfStorageKey
                );
            }

            throw new ServletException(
                    "Failed to add book.",
                    e
            );
        }
    }


    /*
     * ============================================
     * CHECK PDF EXTENSION
     * ============================================
     */

    private boolean isPdfFile(String fileName) {

        return fileName
                .toLowerCase()
                .endsWith(".pdf");
    }


    /*
     * ============================================
     * CHECK COVER EXTENSION
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


                /*
                 * Remove directory information.
                 */

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