package com.project.elibrary.dao.categorydao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.elibrary.bean.category.Category;
import com.project.elibrary.config.DatabaseConnection;
//import java.util.List;

//import com.project.elibrary.bean.category.Category;
import com.project.elibrary.config.DatabaseConnection;

public class CategoryDaoImpl implements CategoryDao {

	@Override
	public int countAllCategories() {

		String sql = """
				SELECT COUNT(*) AS total
				FROM category
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			if (resultSet.next()) {
				return resultSet.getInt("total");
			}

			return 0;

		} catch (SQLException e) {
			throw new RuntimeException("Failed to count categories.", e);
		}
	}

	@Override
	public boolean addCategories(Category categories) {
		String sql = """
				INSERT INTO category (category_name) VALUES (?);

				 """;

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);) {
			statement.setString(1, categories.getCategoryName());
			int rowsAffected = statement.executeUpdate();

			return rowsAffected > 0;

		} catch (SQLException e) {
			throw new RuntimeException("Failed to count categories.", e);
		}

	}

	@Override
	public List<Category> findAllCategories() {

		String sql = """
				SELECT category_id, category_name
				FROM category
				ORDER BY category_name ASC
				""";

		List<Category> categories = new ArrayList<>();

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			while (resultSet.next()) {

				Category category = new Category();

				category.setCategoryId(resultSet.getLong("category_id"));
				category.setCategoryName(resultSet.getString("category_name"));

				categories.add(category);
			}

			return categories;

		} catch (SQLException e) {
			throw new RuntimeException("Failed to find categories.", e);
		}
	}

	@Override
	public List<Category> findCategories(int offset, int limit) {

		List<Category> categories = new ArrayList<>();

		String sql = "SELECT category_id, category_name " + "FROM category " + "ORDER BY category_name ASC "
				+ "LIMIT ? OFFSET ?";

		try (Connection connection = DatabaseConnection.getConnection();

				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, limit);

			statement.setInt(2, offset);

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {

				Category category = new Category();

				category.setCategoryId(resultSet.getLong("category_id"));

				category.setCategoryName(resultSet.getString("category_name"));

				categories.add(category);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return categories;
	}

	@Override
	public List<Category> findTopRatedCategories(int limit) {

		List<Category> categories = new ArrayList<>();

		String sql = "SELECT c.category_id, c.category_name " + "FROM category c "
				+ "JOIN books b ON c.category_id = b.category_id " + "JOIN ratings r ON b.book_id = r.book_id "
				+ "GROUP BY c.category_id, c.category_name " + "ORDER BY AVG(r.rating) DESC " + "LIMIT ?";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setInt(1, limit);

			try (ResultSet resultSet = statement.executeQuery()) {

				while (resultSet.next()) {

					Category category = new Category();

					category.setCategoryId(resultSet.getLong("category_id"));

					category.setCategoryName(resultSet.getString("category_name"));

					categories.add(category);
				}
			}

		} catch (SQLException e) {

			throw new RuntimeException("Failed to find top rated categories.", e);
		}

		return categories;
	}

}
