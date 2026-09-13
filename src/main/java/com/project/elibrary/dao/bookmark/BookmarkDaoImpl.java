package com.project.elibrary.dao.bookmark;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.bean.bookmark.Bookmark;
import com.project.elibrary.config.DatabaseConnection;

public class BookmarkDaoImpl implements BookmarkDao {

    @Override
    public boolean saveBookmark(Bookmark bookmark) {

        String sql = """
                INSERT INTO bookmark
                (user_id, book_id, page_number)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, bookmark.getUserId());
            statement.setLong(2, bookmark.getBookId());
            statement.setInt(3, bookmark.getPageNumber());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to save bookmark.", e);
        }
    }

    @Override
    public boolean deleteBookmark(Long userId, Long bookId, int pageNumber) {

        String sql = """
                DELETE FROM bookmark
                WHERE user_id = ?
                AND book_id = ?
                AND page_number = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setLong(2, bookId);
            statement.setInt(3, pageNumber);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to delete bookmark.", e);
        }
    }

    @Override
    public Bookmark findBookmark(
            Long userId,
            Long bookId,
            int pageNumber) {

        String sql = """
                SELECT bookmark_id,
                       user_id,
                       book_id,
                       page_number,
                       created_at
                FROM bookmark
                WHERE user_id = ?
                AND book_id = ?
                AND page_number = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setLong(2, bookId);
            statement.setInt(3, pageNumber);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    Bookmark bookmark = new Bookmark();

                    bookmark.setBookmarkId(
                            resultSet.getLong("bookmark_id"));

                    bookmark.setUserId(
                            resultSet.getLong("user_id"));

                    bookmark.setBookId(
                            resultSet.getLong("book_id"));

                    bookmark.setPageNumber(
                            resultSet.getInt("page_number"));

                    Timestamp timestamp =
                            resultSet.getTimestamp("created_at");

                    if (timestamp != null) {
                        bookmark.setCreatedAt(
                                timestamp.toLocalDateTime());
                    }

                    return bookmark;
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find bookmark.", e);
        }

        return null;
    }

    @Override
    public List<Bookmark> findBookmarks(
            Long userId,
            Long bookId) {

        List<Bookmark> bookmarks = new ArrayList<>();

        String sql = """
                SELECT bookmark_id,
                       user_id,
                       book_id,
                       page_number,
                       created_at
                FROM bookmark
                WHERE user_id = ?
                AND book_id = ?
                ORDER BY page_number ASC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setLong(2, bookId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Bookmark bookmark = new Bookmark();

                    bookmark.setBookmarkId(
                            resultSet.getLong("bookmark_id"));

                    bookmark.setUserId(
                            resultSet.getLong("user_id"));

                    bookmark.setBookId(
                            resultSet.getLong("book_id"));

                    bookmark.setPageNumber(
                            resultSet.getInt("page_number"));

                    Timestamp timestamp =
                            resultSet.getTimestamp("created_at");

                    if (timestamp != null) {
                        bookmark.setCreatedAt(
                                timestamp.toLocalDateTime());
                    }

                    bookmarks.add(bookmark);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find bookmarks.", e);
        }

        return bookmarks;
    }

    // =========================================================
    // WHOLE BOOK BOOKMARK
    // =========================================================

    @Override
    public boolean saveBookBookmark(Long userId, Long bookId) {

        String sql = """
                INSERT INTO bookmark
                (user_id, book_id, page_number)
                VALUES (?, ?, 0)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setLong(2, bookId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to save book bookmark.", e);
        }
    }

    @Override
    public boolean deleteBookBookmark(Long userId, Long bookId) {

        String sql = """
                DELETE FROM bookmark
                WHERE user_id = ?
                AND book_id = ?
                AND page_number = 0
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setLong(2, bookId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to delete book bookmark.", e);
        }
    }

    @Override
    public boolean isBookBookmarked(Long userId, Long bookId) {

        String sql = """
                SELECT bookmark_id
                FROM bookmark
                WHERE user_id = ?
                AND book_id = ?
                AND page_number = 0
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setLong(2, bookId);

            try (ResultSet resultSet = statement.executeQuery()) {

                return resultSet.next();
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to check book bookmark.", e);
        }
    }

    // =========================================================
    // FIND BOOKMARKED BOOKS WITH PAGINATION
    // =========================================================

    @Override
    public List<Book> findBookmarkedBooks(
            Long userId,
            int offset,
            int limit) {

        List<Book> books = new ArrayList<>();

        String sql = """
                SELECT b.*
                FROM bookmark bm
                JOIN books b
                    ON bm.book_id = b.book_id
                WHERE bm.user_id = ?
                AND bm.page_number = 0
                ORDER BY bm.created_at DESC
                LIMIT ? OFFSET ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setInt(2, limit);
            statement.setInt(3, offset);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    books.add(mapBook(resultSet));
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find bookmarked books.", e);
        }

        return books;
    }

    // =========================================================
    // COUNT BOOKMARKED BOOKS
    // =========================================================

    @Override
    public int countBookmarkedBooks(Long userId) {

        String sql = """
                SELECT COUNT(*)
                FROM bookmark bm
                JOIN books b
                    ON bm.book_id = b.book_id
                WHERE bm.user_id = ?
                AND bm.page_number = 0
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to count bookmarked books.", e);
        }

        return 0;
    }

    // =========================================================
    // MAP RESULTSET TO BOOK
    // =========================================================

    private Book mapBook(ResultSet resultSet) throws SQLException {

        Book book = new Book();

        book.setBookId(
                resultSet.getLong("book_id"));

        book.setTitle(
                resultSet.getString("title"));

        book.setAuthor(
                resultSet.getString("author"));

        book.setDescription(
                resultSet.getString("description"));

        book.setCategoryId(
                resultSet.getLong("category_id"));

        java.sql.Date publishedDate =
                resultSet.getDate("published_at");

        if (publishedDate != null) {
            book.setPublishedAt(
                    publishedDate.toLocalDate());
        }

        book.setCoverStorageKey(
                resultSet.getString("cover_storage_key"));

        book.setPdfStorageKey(
                resultSet.getString("pdf_storage_key"));

        book.setViews(
                resultSet.getLong("views"));

        Timestamp createdTimestamp =
                resultSet.getTimestamp("created_at");

        if (createdTimestamp != null) {
            book.setCreatedAt(
                    createdTimestamp.toLocalDateTime());
        }

        Timestamp updatedTimestamp =
                resultSet.getTimestamp("updated_at");

        if (updatedTimestamp != null) {
            book.setUpdatedAt(
                    updatedTimestamp.toLocalDateTime());
        }

        return book;
    }
}