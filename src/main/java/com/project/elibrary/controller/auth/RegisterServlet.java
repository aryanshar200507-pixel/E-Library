
package com.project.elibrary.controller.auth;

import java.io.IOException;

import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.userservice.UserService;
import com.project.elibrary.service.userservice.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/Register.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Preserve entered values if registration fails
        request.setAttribute("name", name);
        request.setAttribute("email", email);

        // Basic server-side validation
        if (name == null || name.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || password == null || password.isEmpty()) {

            request.setAttribute(
                    "error",
                    "Please fill in all required fields."
            );

            request.getRequestDispatcher("/Register.jsp")
                   .forward(request, response);
            return;
        }

        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.trim());
        user.setPassword(password);

        boolean registered = userService.register(user);

        if (registered) {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );

        } else {

            request.setAttribute(
                    "error",
                    "This email is already registered. Please use another email."
            );

            request.getRequestDispatcher("/Register.jsp")
                   .forward(request, response);
        }
    }
}