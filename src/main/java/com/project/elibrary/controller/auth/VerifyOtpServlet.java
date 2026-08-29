package com.project.elibrary.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet implementation class VerifyOtpServlet
 */
@WebServlet("/verify-otp")
public class VerifyOtpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/verifyOtp.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);

		// No password-reset session exists.
		if (session == null || session.getAttribute("resetOtp") == null) {

			response.sendRedirect(request.getContextPath() + "/forgot-password");

			return;
		}

		String enteredOtp = request.getParameter("otp");

		String storedOtp = (String) session.getAttribute("resetOtp");

		Long expiryTime = (Long) session.getAttribute("otpExpiry");

		// Check whether the OTP has expired.
		if (expiryTime == null || System.currentTimeMillis() > expiryTime) {

			session.removeAttribute("resetOtp");
			session.removeAttribute("otpExpiry");
			session.removeAttribute("resetUserId");

			request.setAttribute("error", "OTP has expired. Please request a new OTP.");

			request.getRequestDispatcher("/forgotPassword.jsp").forward(request, response);

			return;
		}

		// Check whether the entered OTP is correct.
		if (enteredOtp == null || !enteredOtp.equals(storedOtp)) {

			request.setAttribute("error", "Invalid OTP.");

			request.getRequestDispatcher("/verifyOtp.jsp").forward(request, response);

			return;
		}

		/*
		 * OTP is correct.
		 *
		 * We don't change the password yet. We only mark the reset process as verified.
		 */
		session.setAttribute("otpVerified", true);

		// OTP should not be usable again.
		session.removeAttribute("resetOtp");
		session.removeAttribute("otpExpiry");

		response.sendRedirect(request.getContextPath() + "/reset-password");
	}

}
