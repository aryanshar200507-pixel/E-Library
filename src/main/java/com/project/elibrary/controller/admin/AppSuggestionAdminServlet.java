package com.project.elibrary.controller.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.elibrary.bean.suggestions.AppSuggestions;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.appsuggestionservice.AppSuggestionService;
import com.project.elibrary.service.appsuggestionservice.AppSuggestionServiceImpl;
import com.project.elibrary.service.userservice.UserService;
import com.project.elibrary.service.userservice.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/suggestions")
public class AppSuggestionAdminServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AppSuggestionService appSuggestionService;
    private UserService userService;

    public AppSuggestionAdminServlet() {
        appSuggestionService = new AppSuggestionServiceImpl();
        userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        List<AppSuggestions> suggestions =
                appSuggestionService.findAllActiveSuggestions();

        /*
         * Fetch user details using userId.
         * The AppSuggestions bean remains unchanged.
         */
        Map<Long, User> suggestionUsers = new HashMap<>();

        for (AppSuggestions suggestion : suggestions) {

            Long userId = suggestion.getUserId();

            if (userId != null && !suggestionUsers.containsKey(userId)) {

                User user = userService.findById(userId);

                if (user != null) {
                    suggestionUsers.put(userId, user);
                }
            }
        }

        request.setAttribute("suggestions", suggestions);
        request.setAttribute("suggestionUsers", suggestionUsers);

        String success = request.getParameter("success");

        if ("accepted".equals(success)) {
            request.setAttribute(
                    "success",
                    "Suggestion accepted successfully."
            );
        } else if ("deleted".equals(success)) {
            request.setAttribute(
                    "success",
                    "Suggestion deleted successfully."
            );
        }

        request.getRequestDispatcher(
                "/WEB-INF/admin/suggestions.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        String suggestionIdParameter =
                request.getParameter("suggestionId");

        Long suggestionId;

        try {
            suggestionId = Long.parseLong(suggestionIdParameter);
        } catch (Exception e) {
            response.sendRedirect(
                    request.getContextPath() + "/admin/suggestions"
            );
            return;
        }

        if ("accept".equals(action)) {

            boolean accepted =
                    appSuggestionService.acceptSuggestion(suggestionId);

            if (accepted) {
                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/suggestions?success=accepted"
                );
                return;
            }

        } else if ("delete".equals(action)) {

            boolean deleted =
                    appSuggestionService.deleteSuggestion(suggestionId);

            if (deleted) {
                response.sendRedirect(
                        request.getContextPath()
                                + "/admin/suggestions?success=deleted"
                );
                return;
            }
        }

        response.sendRedirect(
                request.getContextPath() + "/admin/suggestions"
        );
    }
}