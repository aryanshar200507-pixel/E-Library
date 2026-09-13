package com.project.elibrary.controller.home;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private BookService bookService;
	private StorageService storageService;
    public HomeServlet() {
        super();
        bookService = new BookServiceImpl();
        storageService = new S3StorageServiceImpl();

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int page = 1;
		int pageSize = 6;
		List<Book> books = bookService.getAllBooks(page, pageSize);
		Map<Long, String> bookCoverUrlMap = new HashMap<>();

		for (Book book : books) {
		    String storageKey = book.getCoverStorageKey();

		    if (storageKey != null && !storageKey.isBlank()) {
		        String coverUrl = storageService.getFileUrl(storageKey);
		        bookCoverUrlMap.put(book.getBookId(), coverUrl);
		    }
		}		request.setAttribute("bookCoverUrlMap", bookCoverUrlMap);
		request.setAttribute("books", books);
		request.getRequestDispatcher("/home.jsp")
	       .forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
