package com.project.elibrary.config;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.project.elibrary.bean.enums.AccountStatus;
import com.project.elibrary.bean.enums.Role;
import com.project.elibrary.util.PasswordUtil;

/**
 * Initialize the database 
 * this class is responsible for 
 * Creating the databse if not exists
 * creating the required tables currently it just create usertable we will add other tables later 
 * creating the default admin account if it does not exist.
 */
public final class DatabaseIniti {

	private DatabaseIniti() {
		   // Prevent object creation because this class only contains
	    // static methods and is used as a utility class.
	}
// Runs all db init steps , this is called when the application starts 
	public static void initialize() {
		createDatabase();
		createTables();
		createDefaultAdmin();
	}
	 /**
     * Creates the E-Library database if it does not already exist.
     *
     * This connection is made to the MySQL server instead of the
     * E-Library database because the database may not exist yet.
     */
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

    /**
     * Creates all required application tables.
     *
     * New table creation methods should be added here as
     * the E-Library project grows.
     */

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
	
	public static void createDefaultAdmin() {
		String sql = """
					INSERT INTO users (name,email,password,role,status)
					SELECT ?,?,?,?,?
					WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = ? )
				""";
		
		  String hashedPassword =
		            PasswordUtil.hasPassword(
		                    DatabaseConfig.getAdminPassword());

		    try (Connection connection =
		                 DatabaseConnection.getConnection();

		         PreparedStatement statement =
		                 connection.prepareStatement(sql)) {

		        statement.setString(1, "E-Library Admin");
		        statement.setString(2, DatabaseConfig.getAdminEmail());
		        statement.setString(3, hashedPassword);
		        statement.setString(4, Role.ADMIN.name());
		        statement.setString(5, AccountStatus.ACTIVE.name());
		        statement.setString(6, DatabaseConfig.getAdminEmail());

		        int rowsAffected = statement.executeUpdate();

		        if (rowsAffected > 0) {
		            System.out.println("Default admin account created.");
		        }

		    } catch (SQLException e) {
		        throw new RuntimeException(
		                "Failed to create default admin.", e);
		    }
	}
}