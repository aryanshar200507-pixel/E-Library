package com.project.elibrary.dao.userdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.elibrary.bean.enums.AccountStatus;
import com.project.elibrary.bean.enums.Role;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.config.DatabaseConnection;

/**
 * Implementation of UserDao.
 *
 * This class is responsible for performing database operations related to
 * users.
 *
 * DAO = Data Access Object.
 *
 * The DAO layer should handle: - SQL queries - Database connections - Reading
 * data from ResultSet - Converting database data into User objects
 *
 * Business rules such as login validation and password checking should be
 * handled by the service layer.
 */
public class UserDaoImpl implements UserDao {

	/**
	 * Saves a new user in the users table.
	 *
	 * @param user user object containing the data to save
	 * @return true if the user was successfully inserted
	 */
	@Override
	public boolean save(User user) {

		String sql = """
				INSERT INTO users
				(name, email, password, role, status)
				VALUES (?, ?, ?, ?, ?)
				""";

		/*
		 * PreparedStatement is used because user data is being inserted into the SQL
		 * query.
		 *
		 * It also helps protect against SQL injection.
		 */
		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getPassword());
			statement.setString(4, user.getRole().name());
			statement.setString(5, user.getStatus().name());

			int rowsAffected = statement.executeUpdate();

			// executeUpdate() returns the number of affected rows.
			return rowsAffected > 0;

		} catch (SQLException e) {
			throw new RuntimeException("Failed to save user.", e);
		}
	}

	/**
	 * Updates the main information of an existing user.
	 *
	 * This method updates name, email and password.
	 *
	 * @param user user containing updated information
	 * @return true if a user was successfully updated
	 */
	@Override
	public boolean update(User user) {

		String sql = """
				UPDATE users
				SET name = ?,
				    email = ?,
				    password = ?
				WHERE user_id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setString(3, user.getPassword());
			statement.setLong(4, user.getUserId());

			int rowsAffected = statement.executeUpdate();

			return rowsAffected > 0;

		} catch (SQLException e) {
			throw new RuntimeException("Failed to update user.", e);
		}
	}

	/**
	 * Updates only the account status of a user.
	 *
	 * Examples: ACTIVE, INACTIVE, SUSPENDED
	 *
	 * Keeping this separate from update() allows the application to change account
	 * status without changing other user data.
	 *
	 * @param userId ID of the user
	 * @param status new account status
	 * @return true if the status was updated
	 */
	@Override
	public boolean updateStatus(Long userId, AccountStatus status) {

		String sql = """
				UPDATE users
				SET status = ?
				WHERE user_id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, status.name());
			statement.setLong(2, userId);

			int rowsAffected = statement.executeUpdate();

			return rowsAffected > 0;

		} catch (SQLException e) {
			throw new RuntimeException("Failed to update user status.", e);
		}
	}

	/**
	 * Finds a user using their ID.
	 *
	 * @param userId ID of the user
	 * @return User object if found, otherwise null
	 */
	@Override
	public User findById(Long userId) {

		String sql = """
				SELECT user_id, name, email, password, role, status
				FROM users
				WHERE user_id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setLong(1, userId);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {

					/*
					 * ResultSet contains database data. We convert that data into our User bean.
					 */
					User user = new User();

					user.setUserId(resultSet.getLong("user_id"));

					user.setName(resultSet.getString("name"));

					user.setEmail(resultSet.getString("email"));

					user.setPassword(resultSet.getString("password"));

					user.setRole(Role.valueOf(resultSet.getString("role")));

					user.setStatus(AccountStatus.valueOf(resultSet.getString("status")));

					return user;
				}

				// No user with this ID was found.
				return null;

			}

		} catch (SQLException e) {
			throw new RuntimeException("Failed to find user by ID.", e);
		}
	}

	/**
	 * Finds a user using their email address.
	 *
	 * This method will be important for authentication because login normally
	 * starts by finding the account using email.
	 *
	 * @param email user's email
	 * @return User object if found, otherwise null
	 */
	@Override
	public User findByEmail(String email) {

		String sql = """
				SELECT user_id, name, email, password, role, status
				FROM users
				WHERE email = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, email);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (resultSet.next()) {

					User user = new User();

					user.setUserId(resultSet.getLong("user_id"));

					user.setName(resultSet.getString("name"));

					user.setEmail(resultSet.getString("email"));

					user.setPassword(resultSet.getString("password"));

					user.setRole(Role.valueOf(resultSet.getString("role")));

					user.setStatus(AccountStatus.valueOf(resultSet.getString("status")));

					return user;
				}
			}

			// No user with this email was found.
			return null;

		} catch (SQLException e) {
			throw new RuntimeException("Failed to find user by email.", e);
		}
	}

	/**
	 * Checks whether an email is already registered.
	 *
	 * This is useful during registration to prevent duplicate emails.
	 *
	 * @param email email to check
	 * @return true if the email already exists
	 */
	@Override
	public boolean existsByEmail(String email) {

		String sql = """
				SELECT 1
				FROM users
				WHERE email = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, email);

			try (ResultSet resultSet = statement.executeQuery()) {

				// If a row exists, the email is already registered.
				return resultSet.next();
			}

		} catch (SQLException e) {
			throw new RuntimeException("Failed to check user email.", e);
		}
	}

	/**
	 * Updates a user's profile information.
	 *
	 * Currently only name and email are changed.
	 *
	 * Password changes are handled separately by updatePassword().
	 *
	 * @param user user containing updated profile information
	 * @return true if the profile was successfully updated
	 */
	@Override
	public boolean updateProfile(User user) {

		String sql = """
				UPDATE users
				SET name = ?,
				    email = ?
				WHERE user_id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, user.getName());
			statement.setString(2, user.getEmail());
			statement.setLong(3, user.getUserId());

			int rowsAffected = statement.executeUpdate();

			return rowsAffected > 0;

		} catch (SQLException e) {
			throw new RuntimeException("Failed to update user profile.", e);
		}
	}

	/**
	 * Updates only the password of a user.
	 *
	 * The DAO does not hash the password here. Password hashing should happen
	 * before this method is called.
	 *
	 * @param userId   ID of the user
	 * @param password already-hashed password
	 * @return true if the password was updated
	 */
	@Override
	public boolean updatePassword(Long userId, String password) {

		String sql = """
				UPDATE users
				SET password = ?
				WHERE user_id = ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, password);
			statement.setLong(2, userId);

			int rowsAffected = statement.executeUpdate();

			return rowsAffected > 0;

		} catch (SQLException e) {
			throw new RuntimeException("Failed to update password.", e);
		}
	}

	/**
	 * Checks whether an email belongs to another user.
	 *
	 * This is mainly useful when an existing user changes their email.
	 *
	 * Example:
	 *
	 * User 5 wants to change their email to abc@gmail.com. If abc@gmail.com belongs
	 * to User 10, the change must be rejected.
	 *
	 * @param email  email being checked
	 * @param userId current user's ID
	 * @return true if another user already has this email
	 */
	@Override
	public boolean emailExistsForAnotherUser(String email, Long userId) {

		String sql = """
				SELECT 1
				FROM users
				WHERE email = ?
				AND user_id != ?
				""";

		try (Connection connection = DatabaseConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, email);
			statement.setLong(2, userId);

			try (ResultSet resultSet = statement.executeQuery()) {

				return resultSet.next();
			}

		} catch (SQLException e) {
			throw new RuntimeException("Failed to check email for another user.", e);
		}
	}
	
	@Override
	public int countAllUsers() {

	    String sql = """
	            SELECT COUNT(*) AS total
	            FROM users
	            """;

	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement statement = connection.prepareStatement(sql);
	         ResultSet resultSet = statement.executeQuery()) {

	        if (resultSet.next()) {
	            return resultSet.getInt("total");
	        }

	        return 0;

	    } catch (SQLException e) {
	        throw new RuntimeException("Failed to count users.", e);
	    }
	}
	
	@Override
	public List<User> findAllUsers() {

	    List<User> users = new ArrayList<>();

	    String sql = """
	            SELECT user_id, name, email, password, role, status
	            FROM users
	            ORDER BY user_id DESC
	            """;

	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement statement = connection.prepareStatement(sql);
	         ResultSet resultSet = statement.executeQuery()) {

	        while (resultSet.next()) {

	            User user = new User();

	            user.setUserId(resultSet.getLong("user_id"));
	            user.setName(resultSet.getString("name"));
	            user.setEmail(resultSet.getString("email"));
	            user.setPassword(resultSet.getString("password"));
	            user.setRole(Role.valueOf(resultSet.getString("role")));
	            user.setStatus(
	                    AccountStatus.valueOf(resultSet.getString("status"))
	            );

	            users.add(user);
	        }

	        return users;

	    } catch (SQLException e) {
	        throw new RuntimeException("Failed to retrieve users.", e);
	    }
	}
	
	@Override
	public List<User> searchUsers(String keyword){
		 List<User> users = new ArrayList<>();


		    String sql;

		    boolean isIdSearch = false;
		    Long userId = null;

		    try {
		        userId = Long.parseLong(keyword);
		        isIdSearch = true;
		    } catch (NumberFormatException e) {
		        // Keyword is not a number, so search by name/email only
		    }

		    if (isIdSearch) {

		        sql = """
		                SELECT user_id, name, email, password, role, status
		                FROM users
		                WHERE name LIKE ?
		                   OR email LIKE ?
		                   OR user_id = ?
		                ORDER BY user_id DESC
		                """;

		    } else {

		        sql = """
		                SELECT user_id, name, email, password, role, status
		                FROM users
		                WHERE name LIKE ?
		                   OR email LIKE ?
		                ORDER BY user_id DESC
		                """;
		    }


		    try (Connection connection = DatabaseConnection.getConnection();
		         PreparedStatement statement = connection.prepareStatement(sql);){
		    	
		    	   statement.setString(1, "%" + keyword + "%");
		           statement.setString(2, "%" + keyword + "%");
		           
		           if(isIdSearch) {statement.setLong(3, userId);}
		    
		      try( ResultSet resultSet = statement.executeQuery()) {
		    	 while (resultSet.next()) {

			            User user = new User();

			            user.setUserId(resultSet.getLong("user_id"));
			            user.setName(resultSet.getString("name"));
			            user.setEmail(resultSet.getString("email"));
			            user.setPassword(resultSet.getString("password"));
			            user.setRole(Role.valueOf(resultSet.getString("role")));
			            user.setStatus(
			                    AccountStatus.valueOf(resultSet.getString("status"))
			            );

			            users.add(user);
			        }
		          }
			        return users;

			    } catch (SQLException e) {
			        throw new RuntimeException("Failed to find user", e);
			    }
		
		
	}
	@Override
	public boolean deactivateUser(Long userId) {
		String sql;
		 sql = """
	               UPDATE users SET status = ? WHERE user_id = ?
		 		""" ;
		 try (Connection connection = DatabaseConnection.getConnection();
		         PreparedStatement statement = connection.prepareStatement(sql);){
			 statement.setString(1,AccountStatus.BLOCKED.name());
			 statement.setLong(2, userId);
				int rowsUpdated = statement.executeUpdate();
				return rowsUpdated > 0;
		 }catch(SQLException e) {
			 throw new RuntimeException("Failed to Block user", e);
		 }
		 
	
	}
	
	@Override
	public boolean recoverUser(Long userId) {
		String sql;
		 sql = """
	               UPDATE users SET status = ? WHERE user_id = ?
		 		""" ;
		 try (Connection connection = DatabaseConnection.getConnection();
		         PreparedStatement statement = connection.prepareStatement(sql);){
			 statement.setString(1,AccountStatus.ACTIVE.name());
			 statement.setLong(2, userId);
				int rowsUpdated = statement.executeUpdate();
				return rowsUpdated > 0;
		 }catch(SQLException e) {
			 throw new RuntimeException("Failed to recover user", e);
		 }
	}
}