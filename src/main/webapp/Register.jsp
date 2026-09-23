<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    // If already logged in, do not show registration page.
    if (session.getAttribute("loggedInUser") != null) {
        response.sendRedirect(
            request.getContextPath() + "/user/dashboard"
        );
        return;
    }

    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Begin Your Journey | Stories</title>

    <!-- Lucide -->
    <script src="https://unpkg.com/lucide@latest"></script>

    <!-- Registration CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/register.css">
          
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

    <!-- =========================
         BOOK
    ========================== -->

    <main class="story-book">

        <!-- =========================
             LEFT PAGE
        ========================== -->

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
                    YOUR STORY BEGINS HERE
                </p>

                <h1>
                    Start Your
                    <span>Journey</span>
                </h1>

                <p class="story-description">
                    Every story begins with a single page.
                    Create your account and discover a world
                    of stories waiting for you.
                </p>

                <div class="quote">

                    <i data-lucide="quote"></i>

                    <p>
                        "A reader lives a thousand lives
                        before he dies."
                    </p>

                </div>

                <div class="book-decoration">

                    <div class="mini-book">
                        <i data-lucide="book"></i>
                    </div>

                    <div class="mini-book mini-book-two">
                        <i data-lucide="book-open"></i>
                    </div>

                    <div class="leaf leaf-one">✦</div>
                    <div class="leaf leaf-two">✦</div>

                </div>

            </div>

        </section>


        <!-- =========================
             RIGHT PAGE
        ========================== -->

        <section class="book-page form-page">

            <div class="page-content">

                <div class="form-heading">

                    <p class="form-eyebrow">
                        CREATE YOUR ACCOUNT
                    </p>

                    <h2>
                        Begin a new
                        <span>chapter.</span>
                    </h2>

                    <p>
                        Join Stories and make your reading
                        journey your own.
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
                     REGISTRATION FORM
                ========================== -->

                <form
                    action="${pageContext.request.contextPath}/register"
                    method="post"
                    class="auth-form"
                    id="registerForm">


                    <!-- NAME -->

                    <div class="input-group">

                        <label for="name">
                            Full Name
                        </label>

                        <div class="input-wrapper">

                            <i data-lucide="user"></i>

                            <input
                                type="text"
                                id="name"
                                name="name"
                                placeholder="Enter your name"
                                autocomplete="name"
                                required>

                        </div>

                    </div>


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
                                placeholder="Create a password"
                                autocomplete="new-password"
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


                    <!-- CONFIRM PASSWORD -->

                    <div class="input-group">

                        <label for="confirmPassword">
                            Confirm Password
                        </label>

                        <div class="input-wrapper">

                            <i data-lucide="lock-keyhole"></i>

                            <input
                                type="password"
                                id="confirmPassword"
                                name="confirmPassword"
                                placeholder="Confirm your password"
                                autocomplete="new-password"
                                required>

                            <button
                                type="button"
                                class="password-toggle"
                                data-target="confirmPassword"
                                aria-label="Show password">

                                <i data-lucide="eye"></i>

                            </button>

                        </div>

                    </div>


                    <!-- REGISTER BUTTON -->

                    <button
                        type="submit"
                        class="primary-button">

                        <span>Create Account</span>

                        <i data-lucide="arrow-right"></i>

                    </button>

                </form>


                <!-- =========================
                     LOGIN LINK
                ========================== -->

                <div class="switch-auth">

                    <span>
                        Already have an account?
                    </span>

                    <a
                        href="${pageContext.request.contextPath}/login.jsp"
                        class="page-turn-link"
                        data-direction="login">

                        Sign in

                        <i data-lucide="arrow-up-right"></i>

                    </a>

                </div>


                <div class="page-number">
                    <span>01</span>
                    <div></div>
                    <span>STORIES</span>
                </div>

            </div>

        </section>

    </main>


    <!-- Page turning overlay -->

    <div class="page-turn-overlay" id="pageTurnOverlay"></div>

</div>

<script src="${pageContext.request.contextPath}/javascript/auth-transition.js"></script>
<script src="${pageContext.request.contextPath}/javascript/register.js"></script>

</body>
</html>