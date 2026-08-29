package com.project.elibrary.service.userservice;

import java.util.List;

import com.project.elibrary.bean.enums.AccountStatus;
import com.project.elibrary.bean.enums.Role;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.dao.userdao.UserDao;
import com.project.elibrary.dao.userdao.UserDaoImpl;
import com.project.elibrary.util.PasswordUtil;

/**
 * Handles business logic related to users.
 *
 * This class sits between the controller and UserDao.
 *
 * The service layer:
 * - Validates user input.
 * - Applies user-related business rules.
 * - Handles password hashing and verification.
 * - Calls the DAO for database operations.
 *
 * SQL queries are handled by UserDao, not by this class.
 */
public class UserServiceImpl implements UserService {

    // DAO is responsible for communicating with the users table.
    private final UserDao userDao;

    /**
     * Creates the user service and its DAO.
     *
     * TODO [FUTURE]:
     * Dependency injection can be introduced later.
     */
    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    /**
     * Registers a new normal user.
     *
     * Before saving the user:
     * - Required information is checked.
     * - Email uniqueness is checked.
     * - Password is hashed.
     * - USER role and ACTIVE status are assigned.
     *
     * @param user user information from registration
     * @return true if registration succeeds
     */
    @Override
    public boolean register(User user) {

        // User object is required.
        if (user == null) {
            return false;
        }

        // Name is required.
        if (user.getName() == null
                || user.getName().isBlank()) {
            return false;
        }

        // Email is required.
        if (user.getEmail() == null
                || user.getEmail().isBlank()) {
            return false;
        }

        // Password is required.
        if (user.getPassword() == null
                || user.getPassword().isBlank()) {
            return false;
        }

        // A user cannot register with an email already in use.
        if (emailExists(user.getEmail())) {
            return false;
        }

        /*
         * Passwords must never be stored as plain text.
         * Hash the password before sending the user to the DAO.
         */
        String hashedPassword =
                PasswordUtil.hasPassword(user.getPassword());

        user.setPassword(hashedPassword);

        /*
         * Normal registration always creates a USER.
         * The user should not be able to choose ADMIN themselves.
         */
        user.setRole(Role.USER);
        user.setStatus(AccountStatus.ACTIVE);

        // DAO performs the actual INSERT.
        return userDao.save(user);
    }

    /**
     * Updates a user's profile.
     *
     * Currently updates:
     * - Name
     * - Email
     *
     * @param user user containing updated information
     * @return true if the profile was updated
     */
    @Override
    public boolean updateProfile(User user) {

        // A user ID is required to identify the account.
        if (user == null || user.getUserId() == null) {
            return false;
        }

        // Name is required.
        if (user.getName() == null
                || user.getName().isBlank()) {
            return false;
        }

        // Email is required.
        if (user.getEmail() == null
                || user.getEmail().isBlank()) {
            return false;
        }

        /*
         * The current user can keep their own email.
         * We only reject the update if another user has it.
         */
        if (userDao.emailExistsForAnotherUser(
                user.getEmail(),
                user.getUserId())) {

            return false;
        }

        return userDao.updateProfile(user);
    }

    /**
     * Finds a user by their ID.
     *
     * @param userId user ID
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
     * @return true if email exists
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
     * The password is hashed before being sent to the DAO.
     *
     * This method does not verify the current password.
     * That verification is handled by changePassword().
     *
     * @param userId user ID
     * @param newPassword new password
     * @return true if the password was updated
     */
    @Override
    public boolean updatePassword(
            Long userId,
            String newPassword) {

        if (userId == null
                || newPassword == null
                || newPassword.isBlank()) {

            return false;
        }

        // Never store the plain-text password.
        String hashedPassword =
                PasswordUtil.hasPassword(newPassword);

        return userDao.updatePassword(
                userId,
                hashedPassword);
    }

    /**
     * Deactivates a user's account.
     *
     * The account is not deleted.
     * Its status is changed to INACTIVE.
     *
     * @param userId user ID
     * @return true if the account was deactivated
     */
    @Override
    public boolean deactiveAccount(Long userId) {

        if (userId == null) {
            return false;
        }

        return userDao.updateStatus(
                userId,
                AccountStatus.INACTIVE);
    }

    /**
     * Returns the total number of users.
     *
     * Mainly used by the admin dashboard.
     *
     * @return total number of users
     */
    @Override
    public int countAllUsers() {

        return userDao.countAllUsers();
    }

    /**
     * Returns all users.
     *
     * Mainly used for the admin user-management page.
     *
     * @return list of users
     */
    @Override
    public List<User> findAllUsers() {

        return userDao.findAllUsers();
    }

    /**
     * Searches users using a keyword.
     *
     * Mainly used by the admin user-management page.
     *
     * @param keyword search text
     * @return matching users
     */
    @Override
    public List<User> searchUsers(String keyword) {

        return userDao.searchUsers(keyword);
    }

    /**
     * Deactivates a user account.
     *
     * This method is used by the admin user-management functionality.
     *
     * TODO [FUTURE]:
     * This currently overlaps with deactiveAccount().
     * Keep only one method for deactivation once the service
     * interface is finalized.
     *
     * @param userId user ID
     * @return true if the account was deactivated
     */
    @Override
    public boolean deactivateUser(Long userId) {

        return userDao.deactivateUser(userId);
    }

    /**
     * Recovers a previously deactivated user account.
     *
     * The DAO changes the user's status back to the appropriate
     * active state.
     *
     * @param userId user ID
     * @return true if the account was recovered
     */
    @Override
    public boolean recoverUser(Long userId) {

        return userDao.recoverUser(userId);
    }

    /**
     * Finds a user by email.
     *
     * @param email user's email
     * @return User if found, otherwise null
     */
    @Override
    public User findByEmail(String email) {

        if (email == null || email.isBlank()) {
            return null;
        }

        return userDao.findByEmail(email);
    }

    /**
     * Changes the password of an already logged-in user.
     *
     * The current password must be correct before the new password
     * can be saved.
     *
     * Flow:
     * 1. Validate input.
     * 2. Find the user.
     * 3. Verify the current password.
     * 4. Hash the new password.
     * 5. Save the new password.
     *
     * @param userId logged-in user's ID
     * @param currentPassword current password
     * @param newPassword new password
     * @return true if the password was changed
     */
    @Override
    public boolean changePassword(
            Long userId,
            String currentPassword,
            String newPassword) {

        // All values are required.
        if (userId == null
                || currentPassword == null
                || currentPassword.isBlank()
                || newPassword == null
                || newPassword.isBlank()) {

            return false;
        }

        // Find the user's stored password hash.
        User user = userDao.findById(userId);

        if (user == null) {
            return false;
        }

        /*
         * Check the entered current password against the
         * hashed password stored in the database.
         */
        if (!PasswordUtil.verifyPassword(
                currentPassword,
                user.getPassword())) {

            return false;
        }

        /*
         * updatePassword() hashes the new password and
         * saves it through the DAO.
         */
        return updatePassword(userId, newPassword);
    }
}