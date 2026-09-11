package com.project.elibrary.controller.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.project.elibrary.bean.enums.Role;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.authservice.AuthService;
import com.project.elibrary.service.authservice.AuthServiceImpl;
import com.project.elibrary.service.remembermeservice.RememberMeService;
import com.project.elibrary.service.remembermeservice.RememberMeServiceImpl;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AuthService authService =
            new AuthServiceImpl();

    private final RememberMeService rememberMeService =
            new RememberMeServiceImpl();


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String email =
                request.getParameter("email");

        String password =
                request.getParameter("password");


        /*
         * Check whether the user selected
         * "Remember Me".
         */
        boolean rememberMe =
                "true".equals(
                        request.getParameter("rememberMe")
                );


        /*
         * Authenticate the user using the
         * existing authentication system.
         */
        User user =
                authService.login(
                        email,
                        password
                );


        /*
         * Login failed.
         */
        if (user == null) {

            request.setAttribute(
                    "error",
                    "Invalid email or password"
            );

            request.getRequestDispatcher(
                    "/login.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }


        /*
         * ============================================
         * CREATE NORMAL HTTP SESSION
         * ============================================
         */

        HttpSession session =
                request.getSession();

        session.setAttribute(
                "loggedInUser",
                user
        );


        /*
         * ============================================
         * REMEMBER ME
         * ============================================
         */

        if (rememberMe) {

            /*
             * Create a secure 7-day token.
             *
             * The service stores only the hash
             * in the database.
             */
            String token =
                    rememberMeService.createToken(
                            user.getUserId()
                    );


            if (token != null) {

                /*
                 * Create the browser cookie.
                 */
                Cookie cookie =
                        new Cookie(
                                "rememberMe",
                                token
                        );


                /*
                 * Cookie is valid for 7 days.
                 */
                cookie.setMaxAge(
                        7 * 24 * 60 * 60
                );


                /*
                 * Cookie is available throughout
                 * the application.
                 */
                cookie.setPath(
                        request.getContextPath()
                );


                /*
                 * Prevent JavaScript from reading
                 * the authentication token.
                 */
                cookie.setHttpOnly(true);


                /*
                 * Add the cookie to the response.
                 */
                response.addCookie(cookie);
            }
        }


        /*
         * ============================================
         * REDIRECT BASED ON ROLE
         * ============================================
         */

        if (user.getRole() == Role.ADMIN) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/admin/dashboard"
            );

        } else {

            response.sendRedirect(
                    request.getContextPath()
                    + "/user/dashboard"
            );
        }
    }
}