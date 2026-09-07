package com.project.elibrary.dao.book;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.config.DatabaseConfig;
import com.project.elibrary.config.DatabaseConnection;

public class BookDaoImpl implements BookDao {

	@Override
	public boolean save(Book book) {
		String sql = """
				INSERT INTO books(title,author,description,
				category_id,published_at,cover_storage_key,pdf_storage_key,views)
				VALUES(?,?,?,?,?,?,?,?)
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, book.getTitle());
			statement.setString(2, book.getAuthor());
			statement.setString(3, book.getDescription());
			statement.setLong(4, book.getCategoryId());

			if (book.getPublishedAt() != null) {
				statement.setDate(5, Date.valueOf(book.getPublishedAt()));
			} else {
				statement.setNull(5, java.sql.Types.DATE);
			}

			statement.setString(6, book.getCoverStorageKey());
			statement.setString(7, book.getPdfStorageKey());

			if (book.getViews() != null) {
				statement.setLong(8, book.getViews());
			} else {
				statement.setLong(8, 0);
			}

			int rowsAffected = statement.executeUpdate();
			if (rowsAffected == 0) {
				return false;
			}

			try (ResultSet resultSet = statement.getGeneratedKeys()) {
				if (resultSet.next()) {
					book.setBookId(resultSet.getLong(1));
				}
			}

			return true;
		} catch (SQLException e) {
			throw new RuntimeException("Failed to save book: ", e);
		}
	}

	@Override
	public Book findById(Long bookId) {
		String sql = """
				SELECT book_id ,title,author,
				description,category_id,published_at,
				cover_storage_key,pdf_storage_key,views,
				created_at,updated_at
				FROM books WHERE book_id = ?
				""";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, bookId);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return mapBook(resultSet);
				}
			}

			return null;
		} catch (SQLException e) {
			throw new RuntimeException("Failed to find book.", e);
		}
	}

	@Override
	public List<Book> findAll(int offset, int limit) {

		String sql = """
				SELECT book_id, title, author, description, category_id,
				       published_at, cover_storage_key, pdf_storage_key,
				       views, created_at, updated_at
				FROM books
				ORDER BY created_at DESC
				LIMIT ? OFFSET ?
				""";
		List<Book> books = new ArrayList<>();

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setInt(1, limit);
			statement.setInt(2, offset);

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					books.add(mapBook(resultSet));
				}
			}
			return books;
		} catch (SQLException e) {
			throw new RuntimeException("Failed to find books.", e);
		}
	}

	@Override
	public int countAll() {
		String sql = "SELECT COUNT(*) FROM books";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {
			if (resultSet.next()) {
				return resultSet.getInt(1);
			}

			return 0;
		} catch (SQLException e) {
			throw new RuntimeException("Failed to count books.", e);
		}
	}

	@Override
	public List<Book> search(String keyword, int offset, int limit) {
		String sql = """
				SELECT book_id, title, author, description, category_id,
				       published_at, cover_storage_key, pdf_storage_key,
				       views, created_at, updated_at
				FROM books
				WHERE title LIKE ?
				   OR author LIKE ?
				ORDER BY created_at DESC
				LIMIT ? OFFSET ?
				""";
		List<Book> books = new ArrayList<>();
		String searchKeyword = "%" + keyword + "%";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, searchKeyword);
			statement.setString(2, searchKeyword);
			statement.setInt(3, limit);
			statement.setInt(4, offset);

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					books.add(mapBook(resultSet));
				}
			}

			return books;
		} catch (SQLException e) {
			throw new RuntimeException("Failed to search books.", e);
		}
	}

	@Override
	public int countSearch(String keyword) {
		String sql = """
				SELECT COUNT(*)
				FROM books
				WHERE title LIKE ? OR author LIKE ?
				""";

		String searchKeyword = "%" + keyword + "%";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, searchKeyword);
			statement.setString(2, searchKeyword);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}

			return 0;
		} catch (SQLException e) {
			throw new RuntimeException("Failed to count search results.", e);
		}
	}

	@Override
	public List<Book> findByCategory(Long categoryId, int offset, int limit) {

		String sql = """
				SELECT book_id, title, author, description, category_id,
				       published_at, cover_storage_key, pdf_storage_key,
				       views, created_at, updated_at
				FROM books
				WHERE category_id = ?
				ORDER BY created_at DESC
				LIMIT ? OFFSET ?
				""";
		List<Book> books = new ArrayList<>();
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, categoryId);
			statement.setInt(2, limit);
			statement.setInt(3, offset);

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					books.add(mapBook(resultSet));
				}
			}

			return books;
		} catch (SQLException e) {
			throw new RuntimeException("Failed to find books by category.", e);
		}
	}

	@Override
	public int countByCategory(Long categoryId) {
		String sql = "SELECT COUNT(*) FROM books WHERE category_id = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, categoryId);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}

			return 0;
		} catch (SQLException e) {
			throw new RuntimeException("Failed to count books by category.", e);
		}
	}

	@Override
	public boolean update(Book book) {
		String sql = """
				UPDATE books
				SET title = ?,
				    author = ?,
				    description = ?,
				    category_id = ?,
				    published_at = ?,
				    cover_storage_key = ?,
				    pdf_storage_key = ?
				WHERE book_id = ?
				""";
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, book.getTitle());
			statement.setString(2, book.getAuthor());
			statement.setString(3, book.getDescription());
			statement.setLong(4, book.getCategoryId());

			if (book.getPublishedAt() != null) {
				statement.setDate(5, Date.valueOf(book.getPublishedAt()));
			} else {
				statement.setNull(5, Types.DATE);
			}

			statement.setString(6, book.getCoverStorageKey());
			statement.setString(7, book.getPdfStorageKey());
			statement.setLong(8, book.getBookId());

			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new RuntimeException("Failed to update book.", e);
		}
	}

	@Override
	public boolean delete(Long bookId) {
		String sql = "DELETE FROM books WHERE book_id = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, bookId);
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new RuntimeException("Failed to delete book.", e);
		}
	}

	private Book mapBook(ResultSet resultSet) throws SQLException {

		Book book = new Book();

		book.setBookId(resultSet.getLong("book_id"));
		book.setTitle(resultSet.getString("title"));
		book.setAuthor(resultSet.getString("author"));
		book.setDescription(resultSet.getString("description"));
		book.setCategoryId(resultSet.getLong("category_id"));

		Date publishedDate = resultSet.getDate("published_at");

		if (publishedDate != null) {
			book.setPublishedAt(publishedDate.toLocalDate());
		}

		book.setCoverStorageKey(resultSet.getString("cover_storage_key"));

		book.setPdfStorageKey(resultSet.getString("pdf_storage_key"));

		book.setViews(resultSet.getLong("views"));

		if (resultSet.getTimestamp("created_at") != null) {
			book.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
		}

		if (resultSet.getTimestamp("updated_at") != null) {
			book.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
		}

		return book;
	}

	@Override
	public boolean incrementViews(Long bookId) {
		String sql = "UPDATE books SET views = views + 1 WHERE book_id = ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			// Send the book ID to the SQL query.
			statement.setLong(1, bookId);

			// Execute the UPDATE query.
			int rowsAffected = statement.executeUpdate();

			// If one row was updated, the operation was successful.
			return rowsAffected > 0;

		} catch (SQLException e) {

			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Book findHighestRatedBookByCategory(Long categoryId) {

		String sql = """
				SELECT b.book_id, b.title, b.author, b.description, b.category_id,
				       b.published_at, b.cover_storage_key, b.pdf_storage_key,
				       b.views, b.created_at, b.updated_at
				FROM books b
				LEFT JOIN ratings r
				    ON b.book_id = r.book_id
				WHERE b.category_id = ?
				GROUP BY b.book_id, b.title, b.author, b.description, b.category_id,
				         b.published_at, b.cover_storage_key, b.pdf_storage_key,
				         b.views, b.created_at, b.updated_at
				ORDER BY AVG(r.rating) DESC, b.created_at DESC
				LIMIT 1
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setLong(1, categoryId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {
					return mapBook(resultSet);
				}
			}

			return null;

		} catch (SQLException e) {
			throw new RuntimeException("Failed to find highest rated book by category.", e);
		}
	}

}
