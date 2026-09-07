package com.project.elibrary.dao.book;

import java.util.List;

import com.project.elibrary.bean.book.Book;

public interface BookDao {
	boolean save(Book book);

	Book findById(Long bookId);

	List<Book> findAll(int offset, int limit);

	int countAll();

	List<Book> search(String keyword, int offset, int limit);

	int countSearch(String keyword);

	List<Book> findByCategory(Long categoryId, int offset, int limit);

	int countByCategory(Long categoryId);

	boolean update(Book book);

	boolean delete(Long bookId);
	
	boolean incrementViews(Long bookId);
	
	Book findHighestRatedBookByCategory(Long categoryId);

}
