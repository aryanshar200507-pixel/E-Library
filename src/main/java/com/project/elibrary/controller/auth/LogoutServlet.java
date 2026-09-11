package com.project.elibrary.controller.auth;

import java.io.IOException;

import com.project.elibrary.service.remembermeservice.RememberMeService;
import com.project.elibrary.service.remembermeservice.RememberMeServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final RememberMeService rememberMeService =
            new RememberMeServiceImpl();


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        logout(request, response);
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        logout(request, response);
    }


    /**
     * Performs the complete logout process.
     *
     * 1. Delete Remember-Me token from database.
     * 2. Delete Remember-Me cookie from browser.
     * 3. Invalidate the current HttpSession.
     * 4. Redirect to login page.
     */
    private void logout(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {


        /*
         * ============================================
         * DELETE REMEMBER-ME TOKEN
         * ============================================
         */

        Cookie rememberMeCookie =
                findRememberMeCookie(request);

        if (rememberMeCookie != null) {

            String token =
                    rememberMeCookie.getValue();

            rememberMeService.deleteToken(token);
        }


        /*
         * ============================================
         * DELETE REMEMBER-ME COOKIE
         * ============================================
         */

        if (rememberMeCookie != null) {

            Cookie deleteCookie =
                    new Cookie(
                            "rememberMe",
                            ""
                    );

            /*
             * Must use the same path as the
             * original cookie.
             */
            deleteCookie.setPath(
                    request.getContextPath()
            );

            /*
             * MaxAge = 0 tells the browser
             * to delete the cookie immediately.
             */
            deleteCookie.setMaxAge(0);

            deleteCookie.setHttpOnly(true);

            response.addCookie(deleteCookie);
        }


        /*
         * ============================================
         * INVALIDATE NORMAL SESSION
         * ============================================
         */

        HttpSession session =
                request.getSession(false);

        if (session != null) {

            session.invalidate();
        }


        /*
         * ============================================
         * REDIRECT TO LOGIN
         * ============================================
         */

        response.sendRedirect(
                request.getContextPath()
                + "/login.jsp"
        );
    }


    /**
     * Finds the Remember-Me cookie from
     * the incoming request.
     */
    private Cookie findRememberMeCookie(
            HttpServletRequest request) {

        Cookie[] cookies =
                request.getCookies();


        if (cookies == null) {
            return null;
        }


        for (Cookie cookie : cookies) {

            if ("rememberMe".equals(
                    cookie.getName())) {

                return cookie;
            }
        }


        return null;
    }
}