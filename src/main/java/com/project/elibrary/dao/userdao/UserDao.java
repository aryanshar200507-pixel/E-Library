package com.project.elibrary.dao.userdao;

import com.project.elibrary.bean.enums.AccountStatus;
import com.project.elibrary.bean.user.User;

public interface UserDao {
	public boolean save(User user); // Register new user

	public boolean update(User user); // Update user information
	// We are not going to delete the user we gave user the option to deactive and
	// we have the access to bloced and activate user

	public boolean updateStatus(Long userId, AccountStatus status);

	User findById(Long userId);// Find user by userId

	User findByEmail(String email);// Find existing user during login

	boolean existsByEmail(String email);// checks that the email is already registered or not

	boolean updateProfile(User user); //User can update profile

	boolean updatePassword(Long userId, String password); //usercan update password needs improvement 

	boolean emailExistsForAnotherUser(String email, Long userId); // for checking that another user don't create account or change there email with existing email 
}
