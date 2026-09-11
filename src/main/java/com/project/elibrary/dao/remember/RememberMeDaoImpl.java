package com.project.elibrary.dao.remember;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.project.elibrary.bean.remember.RememberMe;
import com.project.elibrary.config.DatabaseConnection;

public class RememberMeDaoImpl implements RememberMeDao {

    @Override
    public boolean save(RememberMe rememberMe) {

        String sql = """
                INSERT INTO remember_me
                (user_id, token_hash, expires_at)
                VALUES (?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    rememberMe.getUserId()
            );

            statement.setString(
                    2,
                    rememberMe.getTokenHash()
            );

            statement.setObject(
                    3,
                    rememberMe.getExpiresAt()
            );

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to save remember-me token.",
                    e
            );
        }
    }


    @Override
    public RememberMe findByTokenHash(
            String tokenHash) {

        String sql = """
                SELECT
                    token_id,
                    user_id,
                    token_hash,
                    expires_at,
                    created_at
                FROM remember_me
                WHERE token_hash = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, tokenHash);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    RememberMe rememberMe =
                            new RememberMe();

                    rememberMe.setTokenId(
                            resultSet.getLong("token_id")
                    );

                    rememberMe.setUserId(
                            resultSet.getLong("user_id")
                    );

                    rememberMe.setTokenHash(
                            resultSet.getString("token_hash")
                    );

                    rememberMe.setExpiresAt(
                            resultSet.getObject(
                                    "expires_at",
                                    java.time.LocalDateTime.class
                            )
                    );

                    rememberMe.setCreatedAt(
                            resultSet.getObject(
                                    "created_at",
                                    java.time.LocalDateTime.class
                            )
                    );

                    return rememberMe;
                }
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to find remember-me token.",
                    e
            );
        }

        return null;
    }


    @Override
    public boolean deleteByTokenHash(
            String tokenHash) {

        String sql = """
                DELETE FROM remember_me
                WHERE token_hash = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, tokenHash);

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to delete remember-me token.",
                    e
            );
        }
    }


    @Override
    public boolean deleteByUserId(Long userId) {

        String sql = """
                DELETE FROM remember_me
                WHERE user_id = ?
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, userId);

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected >= 0;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Failed to delete user remember-me tokens.",
                    e
            );
        }
    }
}