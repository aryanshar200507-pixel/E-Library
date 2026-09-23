<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    /*
     * Check whether the user is already logged in.
     *
     * This can happen through:
     *
     * 1. Normal HttpSession
     * 2. Remember-Me filter
     */

    if (session.getAttribute("loggedInUser") != null) {

        response.sendRedirect(
            request.getContextPath() + "/user/dashboard"
        );

        return;
    }

    String error =
        (String) request.getAttribute("error");

    String success =
        (String) request.getAttribute("success");
%>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Welcome Back | Stories</title>

    <!-- Lucide -->
    <script src="https://unpkg.com/lucide@latest"></script>

    <!-- Login CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/login.css">
          
          <script>
    (function () {
        try {
            var dir = sessionStorage.getItem("stories-auth-transition");
            if (dir) {
                sessionStorage.removeItem("stories-auth-transition");
                var r = document.documentElement;
                r.classList.add("auth-arriving");
                r.setAttribute("data-auth-dir", dir);
            }
        } catch (e) {}
    })();
</script>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/auth-transition.css">

</head>


<body>

<div class="auth-page" id="authPage">

    <main class="story-book">


        <!-- =================================
             LEFT PAGE
        ================================== -->

        <section class="book-page story-page">

            <div class="page-content">

                <div class="brand">

                    <div class="brand-icon">

                        <i data-lucide="book-open"></i>

                    </div>

                    <span>STORIES</span>

                </div>


                <div class="ornament"></div>


                <p class="eyebrow">
                    WELCOME BACK
                </p>


                <h1>

                    Your next
                    <span>chapter</span>
                    awaits.

                </h1>


                <p class="story-description">

                    Your stories are waiting for you.
                    Turn the page and continue your
                    journey through Stories.

                </p>


                <div class="quote">

                    <i data-lucide="quote"></i>

                    <p>
                        "There is no friend as loyal
                        as a book."
                    </p>

                </div>


                <div class="book-decoration">

                    <div class="mini-book">

                        <i data-lucide="book-open"></i>

                    </div>

                    <div class="mini-book mini-book-two">

                        <i data-lucide="bookmark"></i>

                    </div>

                    <div class="leaf leaf-one">
                        ✦
                    </div>

                    <div class="leaf leaf-two">
                        ✦
                    </div>

                </div>

            </div>

        </section>


        <!-- =================================
             RIGHT PAGE
        ================================== -->

        <section class="book-page form-page">

            <div class="page-content">


                <div class="form-heading">

                    <p class="form-eyebrow">
                        CONTINUE READING
                    </p>

                    <h2>
                        Welcome
                        <span>back.</span>
                    </h2>

                    <p>
                        Sign in and continue your
                        journey through Stories.
                    </p>

                </div>


                <!-- =========================
                     MESSAGES
                ========================== -->

                <% if (error != null) { %>

                    <div class="message error-message">

                        <i data-lucide="circle-alert"></i>

                        <span>
                            <%= error %>
                        </span>

                    </div>

                <% } %>


                <% if (success != null) { %>

                    <div class="message success-message">

                        <i data-lucide="circle-check"></i>

                        <span>
                            <%= success %>
                        </span>

                    </div>

                <% } %>


                <!-- =========================
                     LOGIN FORM
                ========================== -->

                <form
                    action="${pageContext.request.contextPath}/login"
                    method="post"
                    class="auth-form"
                    id="loginForm">


                    <!-- EMAIL -->

                    <div class="input-group">

                        <label for="email">
                            Email Address
                        </label>

                        <div class="input-wrapper">

                            <i data-lucide="mail"></i>

                            <input
                                type="email"
                                id="email"
                                name="email"
                                placeholder="you@example.com"
                                autocomplete="email"
                                required>

                        </div>

                    </div>


                    <!-- PASSWORD -->

                    <div class="input-group">

                        <label for="password">
                            Password
                        </label>

                        <div class="input-wrapper">

                            <i data-lucide="lock"></i>

                            <input
                                type="password"
                                id="password"
                                name="password"
                                placeholder="Enter your password"
                                autocomplete="current-password"
                                required>

                            <button
                                type="button"
                                class="password-toggle"
                                data-target="password"
                                aria-label="Show password">

                                <i data-lucide="eye"></i>

                            </button>

                        </div>

                    </div>


                    <!-- OPTIONS -->

                    <div class="form-options">

                        <label class="remember-me">

                            <input
                                type="checkbox"
                                name="rememberMe"
                                value="true">

                            <span>
                                Remember me
                            </span>

                        </label>


                        <a
                            href="${pageContext.request.contextPath}/forgotPassword.jsp">

                            Forgot password?

                        </a>

                    </div>


                    <!-- LOGIN BUTTON -->

                    <button
                        type="submit"
                        class="primary-button">

                        <span>Continue Reading</span>

                        <i data-lucide="arrow-right"></i>

                    </button>

                </form>


                <!-- =========================
                     REGISTER
                ========================== -->

                <div class="switch-auth">

                    <span>
                        New to Stories?
                    </span>

                    <a
                        href="${pageContext.request.contextPath}/Register.jsp"
                        class="page-turn-link"
                        data-direction="register">

                        Begin your journey

                        <i data-lucide="arrow-up-right"></i>

                    </a>

                </div>


                <div class="page-number">

                    <span>02</span>

                    <div></div>

                    <span>STORIES</span>

                </div>

            </div>

        </section>

    </main>


    <!-- Page transition -->

    <div
        class="page-turn-overlay"
        id="pageTurnOverlay">
    </div>

</div>

<script src="${pageContext.request.contextPath}/javascript/auth-transition.js"></script>
<script src="${pageContext.request.contextPath}/javascript/login.js"></script>

</body>

</html>