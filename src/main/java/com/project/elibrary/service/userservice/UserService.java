package com.project.elibrary.service.userservice;

import java.util.List;

import com.project.elibrary.bean.user.User;

public interface UserService {
	boolean register(User user);

	boolean updateProfile(User user);

	boolean deactiveAccount(Long userId);

	User findById(Long userId);

	boolean emailExists(String email);

	boolean updatePassword(Long userId, String newPassword);

	User findByEmail(String email);
	
	boolean changePassword(Long userID,String currentPassword,String newPassword);

	int countAllUsers();

	List<User> findAllUsers();

	List<User> searchUsers(String keyword);

	boolean deactivateUser(Long userId);

	boolean recoverUser(Long userId);
}
