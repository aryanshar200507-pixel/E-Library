package com.project.elibrary.controller.bookrequest;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.project.elibrary.bean.bookrequest.BookRequest;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.bookrequestservice.BookRequestService;

import com.project.elibrary.bean.enums.Role;

@WebServlet("/book-request")
public class BookRequestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final BookRequestService bookRequestService =
            new BookRequestService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("loggedInUser") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );

            return;
        }

        request.setAttribute(
                "requests",
                bookRequestService.getRequests()
        );

        request.setAttribute(
                "requestCount",
                bookRequestService.getRequestCount()
        );

        request.getRequestDispatcher(
                "/book-request.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("loggedInUser") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );

            return;
        }

        User user =
                (User) session.getAttribute("loggedInUser");

     // Only normal USER accounts can submit book requests
        if (user.getRole() == Role.ADMIN) {

            response.sendRedirect(
                    request.getContextPath() + "/book-request"
            );

            return;
        }
        
        String title = request.getParameter("title");
        String author = request.getParameter("author");

        if (title == null || title.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "Book title is required."
            );

            doGet(request, response);
            return;
        }

        BookRequest bookRequest = new BookRequest();

        bookRequest.setUserId(user.getUserId());
        bookRequest.setTitle(title.trim());
        bookRequest.setAuthor(
                author == null || author.trim().isEmpty()
                        ? null
                        : author.trim()
        );

        boolean added =
                bookRequestService.addRequest(bookRequest);

        if (added) {

            request.setAttribute(
                    "success",
                    "Book request submitted successfully."
            );

        } else {

            request.setAttribute(
                    "error",
                    "Book request could not be submitted. "
                    + "The book may already exist, "
                    + "already be requested this week, "
                    + "or the weekly limit of 10 has been reached."
            );
        }

        doGet(request, response);
    }
}
