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
import com.project.elibrary.service.bookrequestservice.BookRequestVoteService;
import com.project.elibrary.service.bookrequestservice.BookRequestService;

@WebServlet("/book-request/vote")
public class BookRequestVoteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final BookRequestVoteService voteService =
            new BookRequestVoteService();
    
    private final BookRequestService bookRequestService =
            new BookRequestService();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check login
        if (session == null ||
                session.getAttribute("loggedInUser") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );

            return;
        }

        User user =
                (User) session.getAttribute("loggedInUser");

        // ADMIN cannot vote
        if (user.getRole() == Role.ADMIN) {

            response.sendRedirect(
                    request.getContextPath() + "/book-request"
            );

            return;
        }

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
                    "voteError",
                    "This book request is no longer active."
            );

            response.sendRedirect(
                    request.getContextPath() + "/book-request"
            );

            return;
        }
        
        // Add vote
        boolean voted =
                voteService.addVote(
                        requestId,
                        user.getUserId()
                );

        // Feedback message
        if (voted) {

            session.setAttribute(
                    "voteSuccess",
                    "Your vote has been added."
            );

        } else {

            session.setAttribute(
                    "voteError",
                    "You have already voted for this book."
            );
        }

        response.sendRedirect(
                request.getContextPath() + "/book-request"
        );
    }
}
