package com.project.elibrary.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseIniti {

	private DatabaseIniti() {
		// Prevent object creation
	}

	public static void initialize() {
		createDatabase();
		createTables();
	}

	private static void createDatabase() {

		try {

			// Explicitly load MySQL JDBC driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			String sql = "CREATE DATABASE IF NOT EXISTS " + DatabaseConfig.getDatabaseName();

			try (Connection connection = DriverManager.getConnection(DatabaseConfig.getServerUrl(),
					DatabaseConfig.getUsername(), DatabaseConfig.getPassword());

					Statement statement = connection.createStatement()) {

				statement.executeUpdate(sql);

				System.out.println("Database is ready: " + DatabaseConfig.getDatabaseName());
			}

		} catch (ClassNotFoundException e) {

			throw new RuntimeException("MySQL JDBC Driver not found.", e);

		} catch (SQLException e) {

			throw new RuntimeException("Failed to create database.", e);
		}
	}

	private static void createTables() {

		createUserTable();

		// Other table creation methods will be added here.
	}

	private static void createUserTable() {

		String sql = """
				CREATE TABLE IF NOT EXISTS users (
				    user_id INT PRIMARY KEY AUTO_INCREMENT,
				    name VARCHAR(255) NOT NULL,
				    email VARCHAR(255) NOT NULL UNIQUE,
				    password VARCHAR(255) NOT NULL,
				    role VARCHAR(28) NOT NULL,
				    status VARCHAR(28) NOT NULL,
				    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
				)
				""";

		try (Connection connection = DatabaseConnection.getConnection();

				Statement statement = connection.createStatement()) {

			statement.executeUpdate(sql);

			System.out.println("Users table is ready.");

		} catch (SQLException e) {

			throw new RuntimeException("Failed to create users table.", e);
		}
	}
}