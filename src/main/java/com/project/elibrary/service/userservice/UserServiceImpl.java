package com.project.elibrary.service.userservice;

import java.util.List;

import com.project.elibrary.bean.enums.AccountStatus;
import com.project.elibrary.bean.enums.Role;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.dao.userdao.UserDao;
import com.project.elibrary.dao.userdao.UserDaoImpl;
import com.project.elibrary.util.PasswordUtil;

/**
 * Handles business logic related to normal users.
 *
 * This class sits between the controller/application layer and the UserDao.
 *
 * Responsibilities: - Validate user input. - Apply default role and account
 * status during registration. - Hash passwords before storing them. - Check
 * whether an email is already being used. - Handle profile and password
 * updates. - Handle account deactivation.
 *
 * Database operations are handled by UserDao.
 */
public class UserServiceImpl implements UserService {

	/*
	 * UserDao handles communication with the users table.
	 *
	 * The service does not write SQL directly.
	 */
	private final UserDao userDao;

	/**
	 * Creates the user service and its DAO.
	 *
	 * TODO [FUTURE]: Dependency injection can be introduced later when the project
	 * architecture becomes more advanced.
	 */
	public UserServiceImpl() {
		this.userDao = new UserDaoImpl();
	}

	/**
	 * Registers a new normal user.
	 *
	 * Registration performs basic validation, checks whether the email is already
	 * registered, hashes the password and sets the default role and account status.
	 *
	 * New users are always created as: - Role.USER - AccountStatus.ACTIVE
	 *
	 * @param user user information provided during registration
	 * @return true if registration succeeds
	 */
	@Override
	public boolean register(User user) {

		// A user object is required for registration.
		if (user == null) {
			return false;
		}

		// Name is required.
		if (user.getName() == null || user.getName().isBlank()) {
			return false;
		}

		// Email is required.
		if (user.getEmail() == null || user.getEmail().isBlank()) {
			return false;
		}

		// Password is required.
		if (user.getPassword() == null || user.getPassword().isBlank()) {
			return false;
		}

		/*
		 * Email must be unique.
		 *
		 * This check is done before attempting the INSERT so that we can reject
		 * duplicate registration in the service layer.
		 */
		if (emailExists(user.getEmail())) {
			return false;
		}

		/*
		 * Passwords must never be stored as plain text.
		 *
		 * Hash the password before sending the User object to the DAO.
		 */
		String hashedPassword = PasswordUtil.hasPassword(user.getPassword());

		user.setPassword(hashedPassword);

		/*
		 * A user registering through the normal registration flow must not be able to
		 * choose their own role or status.
		 */
		user.setRole(Role.USER);
		user.setStatus(AccountStatus.ACTIVE);

		// DAO performs the actual database INSERT.
		return userDao.save(user);
	}

	/**
	 * Updates a user's profile information.
	 *
	 * Currently this updates the user's name and email.
	 *
	 * @param user user containing the updated profile information
	 * @return true if the profile was successfully updated
	 */
	@Override
	public boolean updateProfile(User user) {

		// A valid user ID is required to know which account to update.
		if (user == null || user.getUserId() == null) {
			return false;
		}

		// Name is required.
		if (user.getName() == null || user.getName().isBlank()) {
			return false;
		}

		// Email is required.
		if (user.getEmail() == null || user.getEmail().isBlank()) {
			return false;
		}

		/*
		 * The email may belong to the current user.
		 *
		 * We only reject it if another user's account already uses the same email.
		 */
		if (userDao.emailExistsForAnotherUser(user.getEmail(), user.getUserId())) {

			return false;
		}

		// DAO performs the actual UPDATE.
		return userDao.updateProfile(user);
	}

	/**
	 * Finds a user by their ID.
	 *
	 * @param userId ID of the user
	 * @return User if found, otherwise null
	 */
	@Override
	public User findById(Long userId) {

		if (userId == null) {
			return null;
		}

		return userDao.findById(userId);
	}

	/**
	 * Checks whether an email is already registered.
	 *
	 * @param email email to check
	 * @return true if the email exists
	 */
	@Override
	public boolean emailExists(String email) {

		if (email == null || email.isBlank()) {
			return false;
		}

		return userDao.existsByEmail(email);
	}

	/**
	 * Updates a user's password.
	 *
	 * The password is hashed before it is passed to the DAO.
	 *
	 * Note: This method only performs the password update. Verification of the
	 * current password belongs to the change-password service flow.
	 *
	 * @param userId      ID of the user
	 * @param newPassword new password
	 * @return true if the password was successfully updated
	 */
	@Override
	public boolean updatePassword(Long userId, String newPassword) {

		if (userId == null || newPassword == null || newPassword.isBlank()) {
			return false;
		}

		// Hash the new password before storing it.
		String hashedPassword = PasswordUtil.hasPassword(newPassword);

		return userDao.updatePassword(userId, hashedPassword);
	}

	/**
	 * Deactivates a user's account.
	 *
	 * The account is not deleted from the database. Its status is changed to
	 * INACTIVE instead.
	 *
	 * @param userId ID of the user
	 * @return true if the account was successfully deactivated
	 */
	@Override
	public boolean deactiveAccount(Long userId) {

		if (userId == null) {
			return false;
		}

		return userDao.updateStatus(userId, AccountStatus.INACTIVE);
	}
	@Override
	public int countAllUsers() 
	{
		
		return userDao.countAllUsers();
	}
	
	@Override
	public List<User> findAllUsers()
	{
		return userDao.findAllUsers();
	}
	@Override
	public List<User> searchUsers(String keyword)
	{
		return userDao.searchUsers(keyword);
	}
	@Override
	public boolean deactivateUser(Long userId) {
		
		return userDao.deactivateUser(userId);
	}
	
	@Override
	public boolean recoverUser(Long userId) {
		
		return userDao.recoverUser(userId);
	}
	
	
	
}

