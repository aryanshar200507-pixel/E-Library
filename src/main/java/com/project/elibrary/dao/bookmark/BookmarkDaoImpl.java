package com.project.elibrary.dao.bookmark;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
}