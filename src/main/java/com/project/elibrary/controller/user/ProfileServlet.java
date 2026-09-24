package com.project.elibrary.controller.user;

import java.io.IOException;

import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.userservice.UserService;
import com.project.elibrary.service.userservice.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        // User must be logged in
        if (session == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );
            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );
            return;
        }

        // Get the latest user information from database
        User user =
                userService.findById(
                        loggedInUser.getUserId()
                );

        if (user == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );
            return;
        }

        // Send user information to JSP
        request.setAttribute("user", user);

        request.getRequestDispatcher(
                "/WEB-INF/user/profile.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        // User must be logged in
        if (session == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );
            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );
            return;
        }

        // Get updated values from the form
        String name =
                request.getParameter("name");

        String email =
                request.getParameter("email");

        // Create User object for update
        User user = new User();

        user.setUserId(
                loggedInUser.getUserId()
        );

        user.setName(name);

        user.setEmail(email);

        // Update profile
        boolean updated =
                userService.updateProfile(user);

        if (updated) {

            /*
             * Refresh the session user so that
             * the new name/email is immediately
             * available throughout the application.
             */
            User updatedUser =
                    userService.findById(
                            loggedInUser.getUserId()
                    );

            session.setAttribute(
                    "loggedInUser",
                    updatedUser
            );

            response.sendRedirect(
                    request.getContextPath()
                    + "/profile?success=Profile updated successfully"
            );

        } else {

            request.setAttribute(
                    "error",
                    "Email may already be in use or profile update failed."
            );

            // Load the current user again
            User userFromDatabase =
                    userService.findById(
                            loggedInUser.getUserId()
                    );

            request.setAttribute(
                    "user",
                    userFromDatabase
            );

            request.getRequestDispatcher(
                    "/WEB-INF/user/profile.jsp"
            ).forward(
                    request,
                    response
            );
        }
    }
}