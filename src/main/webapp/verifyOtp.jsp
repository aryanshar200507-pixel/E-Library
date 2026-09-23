<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    String error =
        (String) request.getAttribute("error");
%>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Verify OTP | Stories</title>

    <!-- Lucide -->
    <script src="https://unpkg.com/lucide@latest"></script>

    <!-- Verify OTP CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/verify-otp.css">

</head>


<body>

<div class="otp-page">

    <main class="otp-book">


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
                    ONE MORE PAGE
                </p>


                <h1>

                    Verify your
                    <span>chapter.</span>

                </h1>


                <p class="story-description">

                    We've sent a verification code
                    to your email. Enter it here to
                    continue your journey.

                </p>


                <div class="quote">

                    <i data-lucide="quote"></i>

                    <p>
                        "Every page brings you
                        closer to the story."
                    </p>

                </div>


                <div class="book-decoration">

                    <div class="mini-book">

                        <i data-lucide="shield-check"></i>

                    </div>

                    <div class="mini-book mini-book-two">

                        <i data-lucide="mail-check"></i>

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


                <!-- Icon -->

                <div class="form-icon">

                    <i data-lucide="shield-check"></i>

                </div>


                <!-- Heading -->

                <div class="form-heading">

                    <p class="form-eyebrow">
                        EMAIL VERIFICATION
                    </p>

                    <h2>

                        Enter your
                        <span>OTP.</span>

                    </h2>

                    <p>

                        Enter the 6-digit verification
                        code sent to your email address.

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
                     OTP FORM
                ================================== -->

                <form
                    action="${pageContext.request.contextPath}/verify-otp"
                    method="post"
                    class="otp-form"
                    id="otpForm">


                    <label class="otp-label">

                        Verification Code

                    </label>


                    <!-- Hidden actual value -->

                    <input
                        type="hidden"
                        name="otp"
                        id="otpValue">


                    <!-- OTP BOXES -->

                    <div class="otp-inputs">

                        <input
                            type="text"
                            class="otp-box"
                            maxlength="1"
                            inputmode="numeric"
                            autocomplete="one-time-code"
                            autofocus>

                        <input
                            type="text"
                            class="otp-box"
                            maxlength="1"
                            inputmode="numeric">

                        <input
                            type="text"
                            class="otp-box"
                            maxlength="1"
                            inputmode="numeric">

                        <input
                            type="text"
                            class="otp-box"
                            maxlength="1"
                            inputmode="numeric">

                        <input
                            type="text"
                            class="otp-box"
                            maxlength="1"
                            inputmode="numeric">

                        <input
                            type="text"
                            class="otp-box"
                            maxlength="1"
                            inputmode="numeric">

                    </div>


                    <p class="otp-hint">

                        Enter all 6 digits to continue.

                    </p>


                    <!-- VERIFY BUTTON -->

                    <button
                        type="submit"
                        class="primary-button">

                        <span>Verify OTP</span>

                        <i data-lucide="arrow-right"></i>

                    </button>

                </form>


                <!-- BACK -->

                <div class="back-link">

                    <a
                        href="${pageContext.request.contextPath}/forgotPassword.jsp">

                        <i data-lucide="arrow-left"></i>

                        <span>
                            Back to password recovery
                        </span>

                    </a>

                </div>


                <!-- PAGE NUMBER -->

                <div class="page-number">

                    <span>04</span>

                    <div></div>

                    <span>STORIES</span>

                </div>

            </div>

        </section>

    </main>

</div>


<script
    src="${pageContext.request.contextPath}/javascript/verify-otp.js">
</script>

</body>

</html>