package com.project.elibrary.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.project.elibrary.bean.enums.Role;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.authservice.AuthService;
import com.project.elibrary.service.authservice.AuthServiceImpl;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	AuthService authService = new AuthServiceImpl();
//    public LoginServlet() {
//        super();
//        // TODO Auto-generated constructor stub
//    }

//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		User user = authService.login(email, password);

		if (user == null) {
			request.setAttribute("error", "Invalid email or password");
			request.getRequestDispatcher("/login.jsp").forward(request, response);
			return;
		}

		HttpSession session = request.getSession();
		session.setAttribute("loggedInUser", user);

		if (user.getRole() == Role.ADMIN) {
			response.sendRedirect(request.getContextPath() + "/admin/dashboard");
		} else {
			response.sendRedirect(request.getContextPath() + "/user/dashboard");
		}
	}

}
