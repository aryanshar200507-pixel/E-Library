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

    <title>Change Password | Stories</title>

    <!-- Lucide -->
    <script src="https://unpkg.com/lucide@latest"></script>

    <!-- Change Password CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/change-password.css">

</head>


<body>

<div class="change-password-page">

    <main class="change-password-book">


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
                    WRITE A NEW CHAPTER
                </p>


                <h1>

                    Create a
                    <span>new secret.</span>

                </h1>


                <p class="story-description">

                    Keep your account protected with
                    a password only you know.
                    Your next chapter starts here.

                </p>


                <div class="quote">

                    <i data-lucide="quote"></i>

                    <p>
                        "Every new chapter is a chance
                        to begin again."
                    </p>

                </div>


                <div class="book-decoration">

                    <div class="mini-book">

                        <i data-lucide="lock-keyhole"></i>

                    </div>

                    <div class="mini-book mini-book-two">

                        <i data-lucide="key-round"></i>

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

                    <i data-lucide="lock-keyhole"></i>

                </div>


                <!-- HEADING -->

                <div class="form-heading">

                    <p class="form-eyebrow">
                        ACCOUNT SECURITY
                    </p>

                    <h2>

                        Change your
                        <span>password.</span>

                    </h2>

                    <p>

                        Enter your current password
                        and choose a new one for
                        your account.

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
                     FORM
                ================================== -->

                <form
                    action="${pageContext.request.contextPath}/change-password"
                    method="post"
                    class="change-password-form"
                    id="changePasswordForm">


                    <!-- CURRENT PASSWORD -->

                    <div class="input-group">

                        <label for="currentPassword">
                            Current Password
                        </label>

                        <div class="input-wrapper">

                            <i data-lucide="lock"></i>

                            <input
                                type="password"
                                id="currentPassword"
                                name="currentPassword"
                                placeholder="Enter current password"
                                autocomplete="current-password"
                                required>

                            <button
                                type="button"
                                class="password-toggle"
                                data-target="currentPassword"
                                aria-label="Show password">

                                <i data-lucide="eye"></i>

                            </button>

                        </div>

                    </div>


                    <!-- NEW PASSWORD -->

                    <div class="input-group">

                        <label for="newPassword">
                            New Password
                        </label>

                        <div class="input-wrapper">

                            <i data-lucide="key-round"></i>

                            <input
                                type="password"
                                id="newPassword"
                                name="newPassword"
                                placeholder="Create new password"
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
                                placeholder="Confirm new password"
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


                    <!-- SUBMIT -->

                    <button
                        type="submit"
                        class="primary-button">

                        <span>Change Password</span>

                        <i data-lucide="arrow-right"></i>

                    </button>

                </form>


                <!-- BACK -->

                <div class="back-link">

                    <a
                        href="${pageContext.request.contextPath}/user/dashboard">

                        <i data-lucide="arrow-left"></i>

                        <span>
                            Back to Dashboard
                        </span>

                    </a>

                </div>


                <!-- PAGE NUMBER -->

                <div class="page-number">

                    <span>05</span>

                    <div></div>

                    <span>STORIES</span>

                </div>

            </div>

        </section>

    </main>

</div>


<script
    src="${pageContext.request.contextPath}/javascript/change-password.js">
</script>

</body>

</html>