package com.project.elibrary.controller.bookrequest;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.project.elibrary.bean.enums.Role;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.bookrequestservice.BookRequestService;

@WebServlet("/book-request/delete")
public class BookRequestDeleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final BookRequestService bookRequestService =
            new BookRequestService();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Get the existing login session
        HttpSession session = request.getSession(false);

        // Check whether the user is logged in
        if (session == null ||
                session.getAttribute("loggedInUser") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );

            return;
        }

        // Get the logged-in user from the session
        User user =
                (User) session.getAttribute("loggedInUser");

        // Only ADMIN can delete a book request
        if (user.getRole() != Role.ADMIN) {

            response.sendRedirect(
                    request.getContextPath() + "/book-request"
            );

            return;
        }

        // Get request ID from the form
        String requestIdParameter =
                request.getParameter("requestId");

        if (requestIdParameter == null) {

            response.sendRedirect(
                    request.getContextPath() + "/book-request"
            );

            return;
        }

        long requestId;

        try {

            requestId =
                    Long.parseLong(requestIdParameter);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    request.getContextPath() + "/book-request"
            );

            return;
        }

     // Make sure the request belongs to the current week
        if (!bookRequestService.isCurrentWeekRequest(requestId)) {

            session.setAttribute(
                    "deleteError",
                    "This book request is no longer active."
            );

            response.sendRedirect(
                    request.getContextPath() + "/book-request"
            );

            return;
        }
        
        // Delete the book request
        boolean deleted =
                bookRequestService.deleteRequest(requestId);

        // Send feedback message
        if (deleted) {

            session.setAttribute(
                    "deleteSuccess",
                    "Book request deleted successfully."
            );

        } else {

            session.setAttribute(
                    "deleteError",
                    "Book request could not be deleted."
            );
        }

        // Return to the request list
        response.sendRedirect(
                request.getContextPath() + "/book-request"
        );
    }
}