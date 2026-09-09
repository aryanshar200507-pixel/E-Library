package com.project.elibrary.dao.readingprogress;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.project.elibrary.bean.book.Book;
import com.project.elibrary.bean.readingprogress.ReadingProgress;
import com.project.elibrary.config.DatabaseConnection;

public class ReadingProgressDaoImpl implements ReadingProgressDao {

    @Override
    public boolean saveProgress(ReadingProgress progress) {

        String sql = """
                INSERT INTO reading_progress
                (user_id, book_id, current_page)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, progress.getUserId());
            statement.setLong(2, progress.getBookId());
            statement.setInt(3, progress.getCurrentPage());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new RuntimeException("Failed to save reading progress.", e);
        }
    }

    @Override
    public ReadingProgress findProgress(Long userId, Long bookId) {

        String sql = """
                SELECT progress_id, user_id, book_id,
                       current_page, updated_at
                FROM reading_progress
                WHERE user_id = ? AND book_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setLong(2, bookId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    ReadingProgress progress = new ReadingProgress();

                    progress.setProgressId(
                            resultSet.getLong("progress_id"));

                    progress.setUserId(
                            resultSet.getLong("user_id"));

                    progress.setBookId(
                            resultSet.getLong("book_id"));

                    progress.setCurrentPage(
                            resultSet.getInt("current_page"));

                    Timestamp timestamp =
                            resultSet.getTimestamp("updated_at");

                    if (timestamp != null) {
                        progress.setUpdatedAt(
                                timestamp.toLocalDateTime());
                    }

                    return progress;
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find reading progress.", e);
        }

        return null;
    }

    @Override
    public boolean updateProgress(ReadingProgress progress) {

        String sql = """
                UPDATE reading_progress
                SET current_page = ?
                WHERE user_id = ? AND book_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, progress.getCurrentPage());
            statement.setLong(2, progress.getUserId());
            statement.setLong(3, progress.getBookId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to update reading progress.", e);
        }
    }
    
    @Override
    public List<Book> findRecentlyRead(Long userId, int limit) {

        List<Book> books = new ArrayList<>();

        String sql = """
                SELECT b.book_id,
                       b.title,
                       b.author,
                       b.description,
                       b.category_id,
                       b.published_at,
                       b.cover_storage_key,
                       b.pdf_storage_key,
                       b.views,
                       b.created_at,
                       b.updated_at
                FROM reading_progress rp
                JOIN books b
                    ON rp.book_id = b.book_id
                WHERE rp.user_id = ?
                ORDER BY rp.updated_at DESC
                LIMIT ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, userId);
            statement.setInt(2, limit);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Book book = new Book();

                    book.setBookId(resultSet.getLong("book_id"));
                    book.setTitle(resultSet.getString("title"));
                    book.setAuthor(resultSet.getString("author"));
                    book.setDescription(resultSet.getString("description"));
                    book.setCategoryId(resultSet.getLong("category_id"));

                    Date publishedDate = resultSet.getDate("published_at");
                    if (publishedDate != null) {
                        book.setPublishedAt(
                            publishedDate.toLocalDate()
                        );
                    }

                    book.setCoverStorageKey(
                        resultSet.getString("cover_storage_key")
                    );

                    book.setPdfStorageKey(
                        resultSet.getString("pdf_storage_key")
                    );

                    book.setViews(
                        resultSet.getLong("views")
                    );

                    Timestamp createdTimestamp =
                        resultSet.getTimestamp("created_at");

                    if (createdTimestamp != null) {
                        book.setCreatedAt(
                            createdTimestamp.toLocalDateTime()
                        );
                    }

                    Timestamp updatedTimestamp =
                        resultSet.getTimestamp("updated_at");

                    if (updatedTimestamp != null) {
                        book.setUpdatedAt(
                            updatedTimestamp.toLocalDateTime()
                        );
                    }

                    books.add(book);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Failed to find recently read books.", e
            );
        }

        return books;
    }
}