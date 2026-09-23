<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
%>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Forgot Password | Stories</title>

    <!-- Lucide -->
    <script src="https://unpkg.com/lucide@latest"></script>

    <!-- Forgot Password CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/forgot-password.css">

</head>


<body>

<div class="forgot-page">

    <main class="forgot-book">


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
                    YOUR STORY IS STILL WAITING
                </p>


                <h1>

                    Turn the
                    <span>page.</span>

                </h1>


                <p class="story-description">

                    Forgot your password?
                    Don't worry. Every story has
                    another chapter.

                </p>


                <div class="quote">

                    <i data-lucide="quote"></i>

                    <p>
                        "Sometimes the most important
                        journey is finding your way back."
                    </p>

                </div>


                <div class="book-decoration">

                    <div class="mini-book">

                        <i data-lucide="key-round"></i>

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


                <div class="form-icon">

                    <i data-lucide="key-round"></i>

                </div>


                <div class="form-heading">

                    <p class="form-eyebrow">
                        PASSWORD RECOVERY
                    </p>

                    <h2>

                        Find your
                        <span>way back.</span>

                    </h2>

                    <p>

                        Enter your registered email
                        address and we'll send you
                        a verification OTP.

                    </p>

                </div>


                <!-- =================================
                     ERROR
                ================================== -->

                <% if (error != null) { %>

                    <div class="message error-message">

                        <i data-lucide="circle-alert"></i>

                        <span>
                            <%= error %>
                        </span>

                    </div>

                <% } %>


                <!-- =================================
                     SUCCESS
                ================================== -->

                <% if (success != null) { %>

                    <div class="message success-message">

                        <i data-lucide="circle-check"></i>

                        <span>
                            <%= success %>
                        </span>

                    </div>

                <% } %>


                <!-- =================================
                     EMAIL FORM
                ================================== -->

                <form
                    action="${pageContext.request.contextPath}/forgot-password"
                    method="post"
                    class="forgot-form">


                    <div class="input-group">

                        <label for="email">
                            Registered Email
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


                    <button
                        type="submit"
                        class="primary-button">

                        <span>Send Verification OTP</span>

                        <i data-lucide="arrow-right"></i>

                    </button>

                </form>


                <!-- =================================
                     BACK TO LOGIN
                ================================== -->

                <div class="back-login">

                    <a
                        href="${pageContext.request.contextPath}/login.jsp">

                        <i data-lucide="arrow-left"></i>

                        <span>
                            Back to Login
                        </span>

                    </a>

                </div>


                <div class="page-number">

                    <span>03</span>

                    <div></div>

                    <span>STORIES</span>

                </div>

            </div>

        </section>

    </main>

</div>


<script src="${pageContext.request.contextPath}/javascript/forgot-password.js"></script>

</body>

</html>