package com.project.elibrary.dao.appsuggestiondao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.elibrary.bean.suggestions.AppSuggestions;
import com.project.elibrary.config.DatabaseConnection;

public class AppSuggestionDaoImpl implements AppSuggestionDao {

    @Override
    public boolean save(AppSuggestions suggestion) {

        String sql = """
                INSERT INTO app_suggestion
                (user_id, description, status)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, suggestion.getUserId());
            statement.setString(2, suggestion.getDescription());
            statement.setString(3, suggestion.getStatus());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save app suggestion.", e);
        }
    }

    @Override
    public List<AppSuggestions> findAllActive() {

        List<AppSuggestions> suggestions = new ArrayList<>();

        String sql = """
                SELECT suggestion_id, user_id, description, status, created_at
                FROM app_suggestion
                WHERE status IN ('NEW', 'ACCEPTED')
                ORDER BY suggestion_id DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                AppSuggestions suggestion = new AppSuggestions();

                suggestion.setSuggestionId(
                        resultSet.getLong("suggestion_id")
                );

                suggestion.setUserId(
                        resultSet.getLong("user_id")
                );

                suggestion.setDescription(
                        resultSet.getString("description")
                );

                suggestion.setStatus(
                        resultSet.getString("status")
                );

                suggestion.setCreatedAt(
                        resultSet.getTimestamp("created_at")
                );

                suggestions.add(suggestion);
            }

            return suggestions;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to retrieve app suggestions.",
                    e
            );
        }
    }

    @Override
    public boolean updateStatus(Long suggestionId, String status) {

        String sql = """
                UPDATE app_suggestion
                SET status = ?
                WHERE suggestion_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setLong(2, suggestionId);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to update app suggestion status.",
                    e
            );
        }
    }

    @Override
    public boolean delete(Long suggestionId) {

        String sql = """
                DELETE FROM app_suggestion
                WHERE suggestion_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, suggestionId);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to delete app suggestion.",
                    e
            );
        }
    }
}