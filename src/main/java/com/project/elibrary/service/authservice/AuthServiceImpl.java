package com.project.elibrary.service.authservice;

import com.project.elibrary.bean.enums.AccountStatus;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.dao.userdao.UserDao;
import com.project.elibrary.dao.userdao.UserDaoImpl;
import com.project.elibrary.util.PasswordUtil;

public class AuthServiceImpl implements AuthService {

	private final UserDao userDao;

	public AuthServiceImpl() {
		this.userDao = new UserDaoImpl();
	}

	@Override
	public User login(String email, String password) {
		User user = userDao.findByEmail(email);

		if (user == null) {
			return null;
		}
		if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
			return null;
		}

		if (user.getStatus() != AccountStatus.ACTIVE) {
			return null;
		}
		return user;
	}

}
