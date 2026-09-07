package com.project.elibrary.service.bookservice;

import java.util.List;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.dao.book.BookDao;
import com.project.elibrary.dao.book.BookDaoImpl;

public class BookServiceImpl implements BookService {

	private final BookDao bookDao;

	public BookServiceImpl() {
		this.bookDao = new BookDaoImpl();
	}

	@Override
	public boolean addBook(Book book) {
		if (book == null) {
			throw new IllegalArgumentException("book cannot be null.");
		}

		return bookDao.save(book);
	}

	@Override
	public Book getBookById(Long bookId) {
		if (bookId == null || bookId <= 0) {
			throw new IllegalArgumentException("Invalid book Id");
		}

		return bookDao.findById(bookId);
	}

	@Override
	public List<Book> getAllBooks(int page, int pageSize) {
		validatePagination(page, pageSize);
		int offset = calculateOffset(page, pageSize);
		return bookDao.findAll(offset, pageSize);

	}

	@Override
	public int getTotalBooks() {
		return bookDao.countAll();
	}

	@Override
	public List<Book> searchBooks(String keyword, int page, int pageSize) {
		validatePagination(page, pageSize);
		if (keyword == null) {
			keyword = "";
		}

		keyword = keyword.trim();
		int offset = calculateOffset(page, pageSize);
		return bookDao.search(keyword, offset, pageSize);
	}

	@Override
	public int getTotalSearchResults(String keyword) {
		if (keyword == null) {
			keyword = "";
		}
		return bookDao.countSearch(keyword.trim());
	}

	@Override
	public List<Book> getBookByCategory(Long categoryId, int page, int pageSize) {
		validatePagination(page, pageSize);
		if (categoryId == null || categoryId <= 0) {
			throw new IllegalArgumentException("Invalid category ID.");
		}

		int offset = calculateOffset(page, pageSize);

		return bookDao.findByCategory(categoryId, offset, pageSize);
	}

	@Override
	public int getTotalBookByCategory(Long categoryId) {

		if (categoryId == null || categoryId <= 0) {
			throw new IllegalArgumentException("Invalid category ID.");
		}

		return bookDao.countByCategory(categoryId);

	}

	@Override
	public boolean updateBook(Book book) {
		if (book == null) {
			throw new IllegalArgumentException("Book cannot be null.");
		}

		if (book.getBookId() == null || book.getBookId() <= 0) {
			throw new IllegalArgumentException("Invalid book id.");
		}

		return bookDao.update(book);
	}

	@Override
	public boolean deleteBook(Long bookId) {
		if (bookId == null || bookId <= 0) {
			throw new IllegalArgumentException("Invalid book id");
		}
		return bookDao.delete(bookId);
	}

	private int calculateOffset(int page, int pageSize) {
		return (page - 1) * pageSize;
	}

	private void validatePagination(int page, int pageSize) {

		if (page < 1) {
			throw new IllegalArgumentException("Page number must be greater than 0.");
		}

		if (pageSize < 1) {
			throw new IllegalArgumentException("Page size must be greater than 0.");
		}
	}

	@Override
	public boolean incrementVIews(Long bookId) {
		// Make sure the book ID is valid before accessing the database.
		if (bookId == null || bookId <= 0) {
			throw new IllegalArgumentException("Invalid book Id");
		}

		// Ask the DAO to increase the view count.
		return bookDao.incrementViews(bookId);
	}

	@Override
	public Book getHighestRatedBookByCategory(Long categoryId) {

		// Make sure the category ID is valid before accessing the database.
		if (categoryId == null || categoryId <= 0) {
			throw new IllegalArgumentException("Invalid category ID.");
		}

		// Ask the DAO to find the highest-rated book in this category.
		return bookDao.findHighestRatedBookByCategory(categoryId);
	}

}
