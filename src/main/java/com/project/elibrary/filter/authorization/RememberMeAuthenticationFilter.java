package com.project.elibrary.filter.authorization;

import java.io.IOException;

import com.project.elibrary.bean.remember.RememberMe;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.remembermeservice.RememberMeService;
import com.project.elibrary.service.remembermeservice.RememberMeServiceImpl;
import com.project.elibrary.service.userservice.UserService;
import com.project.elibrary.service.userservice.UserServiceImpl;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Automatically restores a user's login session when a valid
 * Remember-Me cookie is found.
 *
 * The filter does not replace the normal HttpSession.
 * It only restores the session when the user has no active session.
 */
@WebFilter("/*")
public class RememberMeAuthenticationFilter
        extends HttpFilter implements Filter {

    private static final long serialVersionUID = 1L;

    private final RememberMeService rememberMeService;
    private final UserService userService;


    public RememberMeAuthenticationFilter() {

        rememberMeService =
                new RememberMeServiceImpl();

        userService =
                new UserServiceImpl();
    }


    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;


        /*
         * Check whether the current request already
         * has a logged-in user in the session.
         *
         * If it does, there is nothing to restore.
         */
        HttpSession session =
                httpRequest.getSession(false);


        if (session != null
                && session.getAttribute("loggedInUser") != null) {

            chain.doFilter(request, response);
            return;
        }


        /*
         * No active login session.
         *
         * Look for the Remember-Me cookie.
         */
        Cookie rememberMeCookie =
                findRememberMeCookie(httpRequest);


        if (rememberMeCookie != null) {

            String token =
                    rememberMeCookie.getValue();


            /*
             * Validate the token.
             *
             * This checks:
             * - token exists in database
             * - token has not expired
             */
            RememberMe rememberMe =
                    rememberMeService.validateToken(token);


            if (rememberMe != null) {

                /*
                 * Find the user associated with
                 * this remember-me token.
                 */
                User user =
                        userService.findById(
                                rememberMe.getUserId()
                        );


                if (user != null) {

                    /*
                     * Check that the account is still
                     * allowed to log in.
                     */
                    if (user.getStatus()
                            == com.project.elibrary.bean.enums.AccountStatus.ACTIVE) {

                        /*
                         * Create a new session.
                         */
                        session =
                                httpRequest.getSession(true);


                        /*
                         * Restore the logged-in user.
                         */
                        session.setAttribute(
                                "loggedInUser",
                                user
                        );
                    }
                }
            }
        }


        /*
         * Continue with the original request.
         */
        chain.doFilter(request, response);
    }


    /**
     * Finds the Remember-Me cookie from the request.
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