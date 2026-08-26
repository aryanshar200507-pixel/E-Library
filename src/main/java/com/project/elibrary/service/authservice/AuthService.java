package com.project.elibrary.service.authservice;

import com.project.elibrary.bean.user.User;

public interface AuthService {
	User login(String email , String password);
}
