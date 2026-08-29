package com.project.elibrary.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.userservice.UserService;
import com.project.elibrary.service.userservice.UserServiceImpl;

/**
 * Servlet implementation class ChangePasswordServlet
 */
@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final UserService userService = new UserServiceImpl();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/changePassword.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		
		User user = (User) session.getAttribute("loggedInUser");
		
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		
		String currentPassword = request.getParameter("currentPassword");
		String newPassword = request.getParameter("newPassword");
		String confirmPassword = request.getParameter("confirmPassword");
		
//		Check whether the new passwords match .
		if(!newPassword.equals(confirmPassword)) {
			request.setAttribute("error", "New passwords do not match.");
			request.getRequestDispatcher("/changePassword.jsp").forward(request, response);
			return;
		}
		
		boolean changed = userService.changePassword(user.getUserId(), currentPassword, newPassword);
		
		if(!changed) {
			request.setAttribute("error", "Current password is incorrect");
			
			request.getRequestDispatcher("/changePassword.jsp").forward(request, response);
		}
	}

}
