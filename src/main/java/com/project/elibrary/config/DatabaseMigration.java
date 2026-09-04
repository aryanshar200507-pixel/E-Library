package com.project.elibrary.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseMigration {

    private DatabaseMigration() {
        // Prevent object creation.
    }

    public static void migrate() {

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            // Create migration tracking table
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS database_migrations (
                        version INT PRIMARY KEY,
                        applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                    """);

            // Migration 1
            if (!migrationExists(connection, 1)) {

                statement.executeUpdate(
                        "ALTER TABLE users MODIFY user_id BIGINT NOT NULL AUTO_INCREMENT"
                );

                statement.executeUpdate(
                        "ALTER TABLE category MODIFY category_id BIGINT NOT NULL AUTO_INCREMENT"
                );

                statement.executeUpdate(
                        "INSERT INTO database_migrations (version) VALUES (1)"
                );

                System.out.println("Migration 1 completed: ID types changed to BIGINT.");
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Database migration failed.", e
            );
        }
    }

    private static boolean migrationExists(Connection connection, int version)
            throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM database_migrations WHERE version = " + version;

        try (Statement statement = connection.createStatement();
             var resultSet = statement.executeQuery(sql)) {

            resultSet.next();

            return resultSet.getInt(1) > 0;
        }
    }
}