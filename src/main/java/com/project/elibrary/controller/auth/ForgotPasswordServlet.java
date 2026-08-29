package com.project.elibrary.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.emailservice.EmailService;
import com.project.elibrary.service.emailservice.EmailServiceImpl;
import com.project.elibrary.service.userservice.UserService;
import com.project.elibrary.service.userservice.UserServiceImpl;
import com.project.elibrary.util.OtpUtil;

/**
 * Servlet implementation class ForgotPasswordServlet
 */
@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final UserService userService = new UserServiceImpl();
	private final EmailService emailService = new EmailServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/forget-password").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		if(email == null || email.isBlank()) {
			
			request.setAttribute("error", "Please enter your email");
			request.getRequestDispatcher("/forgetPassword.jsp").forward(request, response);
			return ;
		}
		
		User user = userService.findByEmail(email);
		
		if(user == null) {
			request.setAttribute("error", "No account found with this email.");
			request.getRequestDispatcher("/forgotPassword.jsp").forward(request, response);
			return;
		}
		
		String otp = OtpUtil.generateOtp();
		
		long expiryTime = System.currentTimeMillis() + (5*60*1000);
		HttpSession session = request.getSession();
		session.setAttribute("resetUserId", user.getUserId());
		session.setAttribute("resetOtp", otp);
		session.setAttribute("otpExpiry", expiryTime);
		emailService.sendEmail(user.getEmail(), "E-Library Password Reset OTP", "Your password reset OTP is: " + otp + "\n\nThis OTP is valid for 5 minutes.");
		response.sendRedirect(request.getContextPath()+ "/verify-otp");
	}

}
