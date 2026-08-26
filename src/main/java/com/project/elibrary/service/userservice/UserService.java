package com.project.elibrary.service.userservice;

import com.project.elibrary.bean.user.User;

public interface UserService {
	boolean register(User user);
	boolean updateProfile(User user);
	boolean deactiveAccount(Long userId);
	User findById(Long userId);
	boolean emailExists(String email);
	boolean updatePassword(Long userId, String newPassword);
}
