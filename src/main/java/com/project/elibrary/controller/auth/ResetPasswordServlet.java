package com.project.elibrary.controller.auth;

import java.io.IOException;

import com.project.elibrary.service.userservice.UserService;
import com.project.elibrary.service.userservice.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private final UserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null || !Boolean.TRUE.equals(session.getAttribute("otpVerified"))) {

			response.sendRedirect(request.getContextPath() + "/forgot-password");

			return;
		}

		request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);

		if (session == null || !Boolean.TRUE.equals(session.getAttribute("otpVerified"))) {

			response.sendRedirect(request.getContextPath() + "/forgot-password");

			return;
		}

		Long userId = (Long) session.getAttribute("resetUserId");

		String newPassword = request.getParameter("newPassword");

		String confirmPassword = request.getParameter("confirmPassword");

		if (newPassword == null || newPassword.isBlank() || confirmPassword == null || confirmPassword.isBlank()) {

			request.setAttribute("error", "Password fields are required.");

			request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);

			return;
		}

		if (!newPassword.equals(confirmPassword)) {

			request.setAttribute("error", "Passwords do not match.");

			request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);

			return;
		}

		boolean updated = userService.updatePassword(userId, newPassword);

		if (!updated) {

			request.setAttribute("error", "Failed to reset password.");

			request.getRequestDispatcher("/resetPassword.jsp").forward(request, response);

			return;
		}

		/*
		 * Password has been successfully changed.
		 *
		 * Remove all password-reset information from the session so the same OTP
		 * verification cannot be reused.
		 */
		session.removeAttribute("resetUserId");
		session.removeAttribute("otpVerified");

		request.setAttribute("success", "Password reset successfully. You can now login.");

		request.getRequestDispatcher("/login.jsp").forward(request, response);
	}
}