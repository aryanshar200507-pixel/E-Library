
package com.project.elibrary.controller.user;

import java.io.IOException;

import com.project.elibrary.bean.suggestions.AppSuggestions;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.appsuggestionservice.AppSuggestionService;
import com.project.elibrary.service.appsuggestionservice.AppSuggestionServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/user/suggestion")
public class AppSuggestionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AppSuggestionService appSuggestionService;

    public AppSuggestionServlet() {
        appSuggestionService = new AppSuggestionServiceImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * ============================================
         * GET LOGGED-IN USER
         * ============================================
         */

        HttpSession session =
                request.getSession(false);

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


        /*
         * ============================================
         * SEND USER TO JSP
         * ============================================
         */

        request.setAttribute(
                "loggedInUser",
                loggedInUser
        );


        /*
         * ============================================
         * SUCCESS MESSAGE
         * ============================================
         */

        if ("true".equals(request.getParameter("success"))) {

            request.setAttribute(
                    "success",
                    "Your suggestion has been submitted successfully."
            );
        }


        /*
         * ============================================
         * FORWARD TO SUGGESTION PAGE
         * ============================================
         */

        request.getRequestDispatcher(
                "/WEB-INF/user/suggestion.jsp"
        ).forward(
                request,
                response
        );
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        /*
         * ============================================
         * GET LOGGED-IN USER
         * ============================================
         */

        HttpSession session =
                request.getSession(false);

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


        /*
         * ============================================
         * GET SUGGESTION DESCRIPTION
         * ============================================
         */

        String description =
                request.getParameter("description");


        /*
         * ============================================
         * CREATE SUGGESTION
         * ============================================
         *
         * IMPORTANT:
         *
         * User ID comes from the logged-in session.
         * It is NOT taken from the form.
         */

        AppSuggestions suggestion =
                new AppSuggestions();

        suggestion.setUserId(
                loggedInUser.getUserId()
        );

        suggestion.setDescription(
                description
        );


        /*
         * ============================================
         * SAVE SUGGESTION
         * ============================================
         */

        boolean saved =
                appSuggestionService.submitSuggestion(
                        suggestion
                );


        /*
         * ============================================
         * SUCCESS
         * ============================================
         */

        if (saved) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/user/suggestion?success=true"
            );

            return;
        }


        /*
         * ============================================
         * VALIDATION ERROR
         * ============================================
         */

        request.setAttribute(
                "error",
                "Suggestion must be between 10 and 1000 characters."
        );

        request.setAttribute(
                "loggedInUser",
                loggedInUser
        );

        request.getRequestDispatcher(
                "/WEB-INF/user/suggestion.jsp"
        ).forward(
                request,
                response
        );
    }
}

