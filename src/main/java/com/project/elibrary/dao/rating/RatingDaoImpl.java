package com.project.elibrary.dao.rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.project.elibrary.bean.ratings.Rating;
import com.project.elibrary.config.DatabaseConnection;

public class RatingDaoImpl implements RatingDao {

	@Override
	public boolean save(Rating rating) {
		String sql = """
				INSERT INTO ratings (user_id,book_id,rating)VALUES(?,?,?)
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, rating.getUserId());
			statement.setLong(2, rating.getBookId());
			statement.setInt(3, rating.getRating());

			int rowsAffected = statement.executeUpdate();

			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Rating findByUserAndBook(Long userId, Long bookId) {

		String sql = """
				SELECT rating_id, user_id, book_id, rating,
				       created_at, updated_at
				FROM ratings
				WHERE user_id = ? AND book_id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setLong(1, userId);
			statement.setLong(2, bookId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {

					Rating rating = new Rating();

					rating.setRatingId(resultSet.getLong("rating_id"));
					rating.setUserId(resultSet.getLong("user_id"));
					rating.setBookId(resultSet.getLong("book_id"));
					rating.setRating(resultSet.getInt("rating"));

					if (resultSet.getTimestamp("created_at") != null) {
						rating.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
					}

					if (resultSet.getTimestamp("updated_at") != null) {
						rating.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
					}

					return rating;
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	@Override
	public boolean update(Rating rating) {
		String sql = """
				UPDATE ratings
				SET rating = ?
				WHERE rating_id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, rating.getRating());
			statement.setLong(2, rating.getRatingId());

			int rowsAffected = statement.executeUpdate();

			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Double getAverageRating(Long bookId) {
		String sql = """
				SELECT AVG(rating)
				FROM ratings
				WHERE book_id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setLong(1, bookId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {
					return resultSet.getDouble(1);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0.0;
	}

	@Override
	public int getRatingCount(Long bookId) {
		String sql = """
				SELECT COUNT(*) FROM ratings WHERE book_id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, bookId);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

}
