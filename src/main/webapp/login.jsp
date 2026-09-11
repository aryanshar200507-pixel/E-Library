<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    /*
     * Check whether the user is already logged in.
     *
     * This can happen in two ways:
     *
     * 1. Normal HttpSession is still active.
     * 2. Remember-Me filter restored the session
     *    using the 7-day cookie.
     */
    if (session.getAttribute("loggedInUser") != null) {

        response.sendRedirect(
            request.getContextPath() + "/user/dashboard"
        );

        return;
    }
%>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <title>Login</title>

</head>

<body>

    <form action="${pageContext.request.contextPath}/login"
          method="post">

        <input type="email"
               name="email"
               placeholder="Email"
               required>

        <input type="password"
               name="password"
               placeholder="Password"
               required>

        <label>

            <input type="checkbox"
                   name="rememberMe"
                   value="true">

            Remember Me!

        </label>

        <button type="submit">
            Login
        </button>

        <a href="${pageContext.request.contextPath}/forgotPassword.jsp">
            FORGOT PASSWORD?
        </a>

        <a href="${pageContext.request.contextPath}/Register.jsp">
            REGISTER
        </a>

    </form>

</body>

</html>