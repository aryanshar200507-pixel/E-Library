package com.project.elibrary.service.bookservice;

import java.util.List;

import com.project.elibrary.bean.book.Book;

public interface BookService {
	boolean addBook(Book book);

	Book getBookById(Long bookId);

	List<Book> getAllBooks(int page, int pageSize);

	int getTotalBooks();

	List<Book> searchBooks(String keyword, int page, int pageSize);

	int getTotalSearchResults(String keyword);

	List<Book> getBookByCategory(Long categoryId, int page, int pageSize);

	int getTotalBookByCategory(Long categoryId);

	boolean updateBook(Book book);

	boolean deleteBook(Long bookId);

}
