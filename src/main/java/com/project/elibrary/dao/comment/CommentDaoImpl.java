package com.project.elibrary.dao.comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.elibrary.bean.comment.Comment;
import com.project.elibrary.config.DatabaseConnection;

public class CommentDaoImpl implements CommentDao {

	// =====================================================
	// SAVE COMMENT
	// =====================================================

	@Override
	public boolean save(Comment comment) {

		String sql = """
				INSERT INTO comments (user_id, book_id, comment)
				VALUES (?, ?, ?)
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setLong(1, comment.getUserId());
			statement.setLong(2, comment.getBookId());
			statement.setString(3, comment.getComment());

			int rowsAffected = statement.executeUpdate();

			return rowsAffected > 0;

		} catch (SQLException e) {

			e.printStackTrace();

			return false;
		}
	}

	// =====================================================
	// FIND COMMENT BY ID
	// =====================================================

	@Override
	public Comment findById(Long commentId) {

		String sql = """
				SELECT comment_id,
				       user_id,
				       book_id,
				       comment,
				       created_at,
				       updated_at
				FROM comments
				WHERE comment_id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setLong(1, commentId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {

					Comment comment = new Comment();

					comment.setCommentId(resultSet.getLong("comment_id"));

					comment.setUserId(resultSet.getLong("user_id"));

					comment.setBookId(resultSet.getLong("book_id"));

					comment.setComment(resultSet.getString("comment"));

					if (resultSet.getTimestamp("created_at") != null) {

						comment.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
					}

					if (resultSet.getTimestamp("updated_at") != null) {

						comment.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
					}

					return comment;
				}
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return null;
	}

	// =====================================================
	// FIND COMMENTS BY BOOK
	// =====================================================

	@Override
	public List<Comment> findByBookId(Long bookId) {

		List<Comment> comments = new ArrayList<>();

		String sql = """
				SELECT comment_id,
				       user_id,
				       book_id,
				       comment,
				       created_at,
				       updated_at
				FROM comments
				WHERE book_id = ?
				ORDER BY created_at DESC
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setLong(1, bookId);

			try (ResultSet resultSet = statement.executeQuery()) {

				while (resultSet.next()) {

					Comment comment = new Comment();

					comment.setCommentId(resultSet.getLong("comment_id"));

					comment.setUserId(resultSet.getLong("user_id"));

					comment.setBookId(resultSet.getLong("book_id"));

					comment.setComment(resultSet.getString("comment"));

					if (resultSet.getTimestamp("created_at") != null) {

						comment.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
					}

					if (resultSet.getTimestamp("updated_at") != null) {

						comment.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
					}

					comments.add(comment);
				}
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return comments;
	}

	// =====================================================
	// UPDATE COMMENT
	// =====================================================

	@Override
	public boolean update(Comment comment) {

		String sql = """
				UPDATE comments
				SET comment = ?
				WHERE comment_id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, comment.getComment());
			statement.setLong(2, comment.getCommentId());

			int rowsAffected = statement.executeUpdate();

			return rowsAffected > 0;

		} catch (SQLException e) {

			e.printStackTrace();

			return false;
		}
	}

	// =====================================================
	// DELETE COMMENT
	// =====================================================

	@Override
	public boolean delete(Long commentId) {

		String sql = """
				DELETE FROM comments
				WHERE comment_id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setLong(1, commentId);

			int rowsAffected = statement.executeUpdate();

			return rowsAffected > 0;

		} catch (SQLException e) {

			e.printStackTrace();

			return false;
		}
	}
}