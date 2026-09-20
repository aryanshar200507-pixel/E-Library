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
 * Initialize the database this class is responsible for Creating the databse if
 * not exists creating the required tables currently it just create usertable we
 * will add other tables later creating the default admin account if it does not
 * exist.
 */
public final class DatabaseIniti {

	private DatabaseIniti() {
		// Prevent object creation because this class only contains
		// static methods and is used as a utility class.
	}

// Runs all db init steps , this is called when the application starts 
	public static void initialize() {
		createDatabase();
		DatabaseMigration.migrate();
		createTables();
		createDefaultAdmin();
	}

	/**
	 * Creates the E-Library database if it does not already exist.
	 *
	 * This connection is made to the MySQL server instead of the E-Library database
	 * because the database may not exist yet.
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
	 * New table creation methods should be added here as the E-Library project
	 * grows.
	 */

	private static void createTables() {

		createUserTable();
		createCategoryTable();
		createBookTable();
		createRatingTable();
		createCommentTable();
		createReadingProgressTable();
		createBookmarkTable();
		createHighlightTable();
		createRememberMeTable();
		createSuggestionTable();
		createBookRequestTable();
		createBookRequestVoteTable();
		// Other table creation methods will be added here.
	}

	private static void createUserTable() {

		String sql = """
				CREATE TABLE IF NOT EXISTS users (
				    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
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

		String hashedPassword = PasswordUtil.hasPassword(DatabaseConfig.getAdminPassword());

		try (Connection connection = DatabaseConnection.getConnection();

				PreparedStatement statement = connection.prepareStatement(sql)) {

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
			throw new RuntimeException("Failed to create default admin.", e);
		}
	}

	public static void createCategoryTable() {
		String sql = """
								CREATE TABLE IF NOT EXISTS category  (
				    category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
				    category_name VARCHAR(100) NOT NULL UNIQUE,
				    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
				);""";

		try (Connection connection = DatabaseConnection.getConnection();

				Statement statement = connection.createStatement()) {

			statement.executeUpdate(sql);

			System.out.println("Category table is ready.");

		} catch (SQLException e) {

			throw new RuntimeException("Failed to create category table.", e);
		}
	}

	public static void createBookTable() {
		String sql = """
									CREATE TABLE IF NOT EXISTS books (
				    book_id BIGINT PRIMARY KEY AUTO_INCREMENT,
				    title VARCHAR(255) NOT NULL,
				    author VARCHAR(255) NOT NULL,
				    description TEXT,
				    category_id BIGINT NOT NULL,
				    published_at DATE,
				    cover_storage_key VARCHAR(500),
				    pdf_storage_key VARCHAR(500),
				    views BIGINT DEFAULT 0,
				    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
				    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

				    CONSTRAINT fk_books_category
				        FOREIGN KEY (category_id)
				        REFERENCES category(category_id)
				);
								""";

		try (Connection connection = DatabaseConnection.getConnection();
				Statement statement = connection.createStatement()) {
			statement.executeUpdate(sql);
			System.out.println("Book Table is created");
		} catch (SQLException e) {
			throw new RuntimeException("Book table not created", e);
		}

	}

	public static void createRatingTable() {
		String sql = """
				CREATE TABLE IF NOT EXISTS ratings (
				    rating_id BIGINT PRIMARY KEY AUTO_INCREMENT,

				    user_id BIGINT NOT NULL,

				    book_id BIGINT NOT NULL,

				    rating INT NOT NULL,

				    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

				    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
				        ON UPDATE CURRENT_TIMESTAMP,

				    CONSTRAINT fk_ratings_user
				        FOREIGN KEY (user_id)
				        REFERENCES users(user_id)
				        ON DELETE CASCADE,

				    CONSTRAINT fk_ratings_book
				        FOREIGN KEY (book_id)
				        REFERENCES books(book_id)
				        ON DELETE CASCADE,

				    CONSTRAINT chk_rating_value
				        CHECK (rating BETWEEN 1 AND 5),

				    CONSTRAINT unique_user_book_rating
				        UNIQUE (user_id, book_id)
				);
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				Statement statement = connection.createStatement()) {

			statement.executeUpdate(sql);

			System.out.println("Rating table is ready.");

		} catch (SQLException e) {

			throw new RuntimeException("Failed to create rating table.", e);
		}

	}

	public static void createCommentTable() {
		String sql = """
				  CREATE TABLE IF NOT EXISTS comments (
				            comment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
				            user_id BIGINT NOT NULL,
				            book_id BIGINT NOT NULL,
				            comment TEXT NOT NULL,
				            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
				            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
				                ON UPDATE CURRENT_TIMESTAMP,

				            CONSTRAINT fk_comments_user
				                FOREIGN KEY (user_id)
				                REFERENCES users(user_id)
				                ON DELETE CASCADE,

				            CONSTRAINT fk_comments_book
				                FOREIGN KEY (book_id)
				                REFERENCES books(book_id)
				                ON DELETE CASCADE
				        );
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				Statement statement = connection.createStatement()) {

			statement.executeUpdate(sql);

			System.out.println("Comment table is ready.");

		} catch (SQLException e) {

			throw new RuntimeException("Failed to create comment table.", e);
		}
		
		
	}
	
	public static void createReadingProgressTable() {

	    String sql = """
	            CREATE TABLE IF NOT EXISTS reading_progress (
	                progress_id BIGINT PRIMARY KEY AUTO_INCREMENT,

	                user_id BIGINT NOT NULL,

	                book_id BIGINT NOT NULL,

	                current_page INT NOT NULL DEFAULT 1,

	                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	                    ON UPDATE CURRENT_TIMESTAMP,

	                CONSTRAINT fk_progress_user
	                    FOREIGN KEY (user_id)
	                    REFERENCES users(user_id)
	                    ON DELETE CASCADE,

	                CONSTRAINT fk_progress_book
	                    FOREIGN KEY (book_id)
	                    REFERENCES books(book_id)
	                    ON DELETE CASCADE,

	                CONSTRAINT unique_user_book_progress
	                    UNIQUE (user_id, book_id)
	            );
	            """;

	    try (Connection connection = DatabaseConnection.getConnection();
	            Statement statement = connection.createStatement()) {

	        statement.executeUpdate(sql);

	        System.out.println("Reading progress table is ready.");

	    } catch (SQLException e) {

	        throw new RuntimeException(
	                "Failed to create reading progress table.", e);
	    }
	}
	
	public static void createBookmarkTable() {

	    String sql = """
	            CREATE TABLE IF NOT EXISTS bookmark (
	                bookmark_id BIGINT PRIMARY KEY AUTO_INCREMENT,
	                user_id BIGINT NOT NULL,
	                book_id BIGINT NOT NULL,
	                page_number INT NOT NULL,
	                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

	                CONSTRAINT fk_bookmark_user
	                    FOREIGN KEY (user_id)
	                    REFERENCES users(user_id)
	                    ON DELETE CASCADE,

	                CONSTRAINT fk_bookmark_book
	                    FOREIGN KEY (book_id)
	                    REFERENCES books(book_id)
	                    ON DELETE CASCADE,

	                CONSTRAINT unique_user_book_page
	                    UNIQUE (user_id, book_id, page_number)
	            );
	            """;

	    try (Connection connection = DatabaseConnection.getConnection();
	         Statement statement = connection.createStatement()) {

	        statement.executeUpdate(sql);

	        System.out.println("Bookmark table is ready.");

	    } catch (SQLException e) {

	        throw new RuntimeException(
	                "Failed to create bookmark table.", e);
	    }
	}
	
	public static void createHighlightTable() {

	    String sql = """
	            CREATE TABLE IF NOT EXISTS highlight (
	                highlight_id BIGINT PRIMARY KEY AUTO_INCREMENT,

	                user_id BIGINT NOT NULL,

	                book_id BIGINT NOT NULL,

	                page_number INT NOT NULL,

	                selected_text TEXT NOT NULL,

	                start_offset INT NOT NULL,

	                end_offset INT NOT NULL,
	                
	                rectangles_json TEXT NULL,

	                color VARCHAR(20) NOT NULL DEFAULT 'yellow',

	                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

	                CONSTRAINT fk_highlight_user
	                    FOREIGN KEY (user_id)
	                    REFERENCES users(user_id)
	                    ON DELETE CASCADE,

	                CONSTRAINT fk_highlight_book
	                    FOREIGN KEY (book_id)
	                    REFERENCES books(book_id)
	                    ON DELETE CASCADE
	            );
	            """;

	    try (Connection connection = DatabaseConnection.getConnection();
	         Statement statement = connection.createStatement()) {

	        statement.executeUpdate(sql);

	        System.out.println("Highlight table is ready.");

	    } catch (SQLException e) {

	        throw new RuntimeException(
	                "Failed to create highlight table.",
	                e
	        );
	    }
	}
	
	public static void createRememberMeTable() {

	    String sql = """
	            CREATE TABLE IF NOT EXISTS remember_me (
	                token_id BIGINT PRIMARY KEY AUTO_INCREMENT,

	                user_id BIGINT NOT NULL,

	                token_hash VARCHAR(255) NOT NULL UNIQUE,

	                expires_at TIMESTAMP NOT NULL,

	                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

	                CONSTRAINT fk_remember_me_user
	                    FOREIGN KEY (user_id)
	                    REFERENCES users(user_id)
	                    ON DELETE CASCADE
	            );
	            """;

	    try (Connection connection = DatabaseConnection.getConnection();
	         Statement statement = connection.createStatement()) {

	        statement.executeUpdate(sql);

	        System.out.println("Remember Me table is ready.");

	    } catch (SQLException e) {

	        throw new RuntimeException(
	                "Failed to create remember me table.",
	                e
	        );
	    }
	}
	
	public static void createSuggestionTable() {

	    String sql = """
	            CREATE TABLE IF NOT EXISTS app_suggestion (
	                suggestion_id BIGINT PRIMARY KEY AUTO_INCREMENT,
	                user_id BIGINT NOT NULL,
	                description TEXT NOT NULL,
	                status VARCHAR(28) NOT NULL,
	                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	                CONSTRAINT fk_app_suggestion_user
	                    FOREIGN KEY (user_id)
	                    REFERENCES users(user_id)
	                    ON DELETE CASCADE
	            );
	            """;

	    try (Connection connection = DatabaseConnection.getConnection();
	         Statement statement = connection.createStatement()) {

	        statement.executeUpdate(sql);

	        System.out.println("App Suggestion table is ready.");

	    } catch (SQLException e) {

	        throw new RuntimeException(
	                "Failed to create app suggestion table.",
	                e
	        );
	    }
	}
	
	// =========================================================
	// BOOK REQUEST TABLE
	// =========================================================

	public static void createBookRequestTable() {

	    String sql = """
	            CREATE TABLE IF NOT EXISTS book_request (
	                request_id BIGINT PRIMARY KEY AUTO_INCREMENT,

	                user_id BIGINT NOT NULL,

	                title VARCHAR(255) NOT NULL,

	                author VARCHAR(255),

	                week_start DATE NOT NULL,

	                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

	                CONSTRAINT fk_book_request_user
	                    FOREIGN KEY (user_id)
	                    REFERENCES users(user_id)
	                    ON DELETE CASCADE
	            )
	            """;

	    try (Connection connection = DatabaseConnection.getConnection();
	         Statement statement = connection.createStatement()) {

	        statement.executeUpdate(sql);

	        System.out.println(
	                "Book Request table is ready.");

	    } catch (SQLException e) {

	        throw new RuntimeException(
	                "Failed to create book request table.",
	                e);
	    }
	}


	// =========================================================
	// BOOK REQUEST VOTE TABLE
	// =========================================================

	public static void createBookRequestVoteTable() {

	    String sql = """
	            CREATE TABLE IF NOT EXISTS book_request_vote (
	                vote_id BIGINT PRIMARY KEY AUTO_INCREMENT,

	                request_id BIGINT NOT NULL,

	                user_id BIGINT NOT NULL,

	                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

	                CONSTRAINT fk_book_request_vote_request
	                    FOREIGN KEY (request_id)
	                    REFERENCES book_request(request_id)
	                    ON DELETE CASCADE,

	                CONSTRAINT fk_book_request_vote_user
	                    FOREIGN KEY (user_id)
	                    REFERENCES users(user_id)
	                    ON DELETE CASCADE,

	                CONSTRAINT unique_request_user_vote
	                    UNIQUE (request_id, user_id)
	            )
	            """;

	    try (Connection connection = DatabaseConnection.getConnection();
	         Statement statement = connection.createStatement()) {

	        statement.executeUpdate(sql);

	        System.out.println(
	                "Book Request Vote table is ready.");

	    } catch (SQLException e) {

	        throw new RuntimeException(
	                "Failed to create book request vote table.",
	                e);
	    }
	}
}