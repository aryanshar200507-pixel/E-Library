<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
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

    <title>Reset Password | Stories</title>

    <!-- Lucide -->
    <script src="https://unpkg.com/lucide@latest"></script>

    <!-- Reset Password CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/reset-password.css">

</head>


<body>

<div class="reset-password-page">

    <main class="reset-password-book">


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
                    A FRESH BEGINNING
                </p>


                <h1>

                    Begin your
                    <span>next chapter.</span>

                </h1>


                <p class="story-description">

                    Your identity has been verified.
                    Choose a new password and continue
                    your journey through Stories.

                </p>


                <div class="quote">

                    <i data-lucide="quote"></i>

                    <p>
                        "Every new chapter brings
                        a new beginning."
                    </p>

                </div>


                <div class="book-decoration">

                    <div class="mini-book">

                        <i data-lucide="key-round"></i>

                    </div>

                    <div class="mini-book mini-book-two">

                        <i data-lucide="lock-keyhole"></i>

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


                <!-- ICON -->

                <div class="form-icon">

                    <i data-lucide="key-round"></i>

                </div>


                <!-- HEADING -->

                <div class="form-heading">

                    <p class="form-eyebrow">
                        PASSWORD RECOVERY
                    </p>

                    <h2>

                        Create your
                        <span>new password.</span>

                    </h2>

                    <p>

                        Choose a strong password
                        to secure your Stories account.

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
                     RESET FORM
                ================================== -->

                <form
                    action="${pageContext.request.contextPath}/reset-password"
                    method="post"
                    class="reset-password-form"
                    id="resetPasswordForm">


                    <!-- NEW PASSWORD -->

                    <div class="input-group">

                        <label for="newPassword">
                            New Password
                        </label>

                        <div class="input-wrapper">

                            <i data-lucide="lock"></i>

                            <input
                                type="password"
                                id="newPassword"
                                name="newPassword"
                                placeholder="Create a new password"
                                autocomplete="new-password"
                                required>

                            <button
                                type="button"
                                class="password-toggle"
                                data-target="newPassword"
                                aria-label="Show password">

                                <i data-lucide="eye"></i>

                            </button>

                        </div>

                    </div>


                    <!-- CONFIRM PASSWORD -->

                    <div class="input-group">

                        <label for="confirmPassword">
                            Confirm New Password
                        </label>

                        <div class="input-wrapper">

                            <i data-lucide="shield-check"></i>

                            <input
                                type="password"
                                id="confirmPassword"
                                name="confirmPassword"
                                placeholder="Confirm your new password"
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

                        <small
                            class="password-feedback"
                            id="passwordFeedback">
                        </small>

                    </div>


                    <!-- RESET BUTTON -->

                    <button
                        type="submit"
                        class="primary-button">

                        <span>Reset Password</span>

                        <i data-lucide="arrow-right"></i>

                    </button>

                </form>


                <!-- BACK TO LOGIN -->

                <div class="back-link">

                    <a
                        href="${pageContext.request.contextPath}/login.jsp">

                        <i data-lucide="arrow-left"></i>

                        <span>
                            Back to Login
                        </span>

                    </a>

                </div>


                <!-- PAGE NUMBER -->

                <div class="page-number">

                    <span>06</span>

                    <div></div>

                    <span>STORIES</span>

                </div>

            </div>

        </section>

    </main>

</div>


<script
    src="${pageContext.request.contextPath}/javascript/reset-password.js">
</script>

</body>

</html>