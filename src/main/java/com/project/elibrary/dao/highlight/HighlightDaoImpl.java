package com.project.elibrary.dao.highlight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.project.elibrary.bean.highlight.Highlight;
import com.project.elibrary.config.DatabaseConnection;

public class HighlightDaoImpl implements HighlightDao {

	@Override
	public boolean saveHighlight(Highlight highlight) {

		String sql = """
				INSERT INTO highlight
				(
				    user_id,
				    book_id,
				    page_number,
				    selected_text,
				    start_offset,
				    end_offset,
				    rectangles_json,
				    color
				)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?)
				""";

		try (Connection connection = DatabaseConnection.getConnection();

				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setLong(1, highlight.getUserId());

			statement.setLong(2, highlight.getBookId());

			statement.setInt(3, highlight.getPageNumber());

			statement.setString(4, highlight.getSelectedText());

			statement.setInt(5, highlight.getStartOffset());

			statement.setInt(6, highlight.getEndOffset());

			statement.setString(7, highlight.getRectanglesJson());

			statement.setString(8, highlight.getColor());

			return statement.executeUpdate() > 0;

		} catch (SQLException e) {

			throw new RuntimeException("Failed to save highlight.", e);
		}
	}

	@Override
	public boolean deleteHighlight(Long highlightId, Long userId) {

		String sql = """
				DELETE FROM highlight
				WHERE highlight_id = ?
				AND user_id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();

				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setLong(1, highlightId);

			statement.setLong(2, userId);

			return statement.executeUpdate() > 0;

		} catch (SQLException e) {

			throw new RuntimeException("Failed to delete highlight.", e);
		}
	}

	@Override
	public List<Highlight> findHighlights(Long userId, Long bookId) {

		List<Highlight> highlights = new ArrayList<>();

		String sql = """
				SELECT
				    highlight_id,
				    user_id,
				    book_id,
				    page_number,
				    selected_text,
				    start_offset,
				    end_offset,
				    rectangles_json,
				    color,
				    created_at
				FROM highlight
				WHERE user_id = ?
				AND book_id = ?
				ORDER BY page_number ASC,
				         highlight_id ASC
				""";

		try (Connection connection = DatabaseConnection.getConnection();

				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setLong(1, userId);

			statement.setLong(2, bookId);

			try (ResultSet resultSet = statement.executeQuery()) {

				while (resultSet.next()) {

					Highlight highlight = new Highlight();

					highlight.setHighlightId(resultSet.getLong("highlight_id"));

					highlight.setUserId(resultSet.getLong("user_id"));

					highlight.setBookId(resultSet.getLong("book_id"));

					highlight.setPageNumber(resultSet.getInt("page_number"));

					highlight.setSelectedText(resultSet.getString("selected_text"));

					highlight.setStartOffset(resultSet.getInt("start_offset"));

					highlight.setEndOffset(resultSet.getInt("end_offset"));

					highlight.setRectanglesJson(resultSet.getString("rectangles_json"));

					highlight.setColor(resultSet.getString("color"));

					Timestamp timestamp = resultSet.getTimestamp("created_at");

					if (timestamp != null) {

						highlight.setCreatedAt(timestamp.toLocalDateTime());
					}

					highlights.add(highlight);

				}
			}

		} catch (SQLException e) {

			throw new RuntimeException("Failed to find highlights.", e);
		}

		return highlights;
	}

	@Override
	public boolean updateHighlightColor(Long highlightId, Long userId, String color) {

		String sql = """
				UPDATE highlight
				SET color = ?
				WHERE highlight_id = ?
				AND user_id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();

				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, color);
			statement.setLong(2, highlightId);
			statement.setLong(3, userId);

			return statement.executeUpdate() > 0;

		} catch (SQLException e) {

			throw new RuntimeException("Failed to update highlight color.", e);
		}
	}
}