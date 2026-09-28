<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.project.elibrary.bean.user.User"%>

<%
    User user = (User) request.getAttribute("user");

    String success = request.getParameter("success");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>My Profile - Stories E-Library</title>

    <!-- Google Fonts -->
    <link rel="preconnect"
          href="https://fonts.googleapis.com">

    <link rel="preconnect"
          href="https://fonts.gstatic.com"
          crossorigin>

    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@600;700&display=swap"
          rel="stylesheet">
    
    <link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/background.css">
      
    <!-- Dashboard base styling -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/dashboard.css">

    <!-- Profile styling -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/profile.css">

    <!-- Lucide -->
    <script src="https://unpkg.com/lucide@latest"></script>

</head>


<body>

<div class="page-shell">


    <!-- =====================================================
         HEADER
    ====================================================== -->

    <header class="site-header">

        <div class="header-inner">


            <!-- BRAND -->

            <a class="brand"
               href="${pageContext.request.contextPath}/user/dashboard">

                <span class="brand-icon">
                    <i data-lucide="library"></i>
                </span>

                <span class="brand-text">

                    <span class="brand-name">
                        Stories
                    </span>

                    <span class="brand-subtitle">
                        E-LIBRARY
                    </span>

                </span>

            </a>


            <!-- DESKTOP NAVIGATION -->

            <nav class="desktop-nav">


                <a href="${pageContext.request.contextPath}/user/dashboard">

                    <i data-lucide="layout-dashboard"></i>

                    <span>Dashboard</span>

                </a>


                <a href="${pageContext.request.contextPath}/books">

                    <i data-lucide="library"></i>

                    <span>Categories</span>

                </a>


                <a href="${pageContext.request.contextPath}/book-request">

                    <i data-lucide="book-plus"></i>

                    <span>Book Requests</span>

                </a>


                <a href="${pageContext.request.contextPath}/books/bookmark-book">

                    <i data-lucide="bookmark"></i>

                    <span>Bookmarks</span>

                </a>


                <a href="${pageContext.request.contextPath}/user/suggestion">

                    <i data-lucide="message-square-plus"></i>

                    <span>Suggestions</span>

                </a>


                <a href="${pageContext.request.contextPath}/books/history">

                    <i data-lucide="history"></i>

                    <span>History</span>

                </a>


                <!-- ACTIVE PROFILE -->

                <a href="${pageContext.request.contextPath}/profile"
                   class="active">

                    <i data-lucide="user"></i>

                    <span>Profile</span>

                </a>

            </nav>


            <!-- HEADER ACTIONS -->

            <div class="header-actions">

                <a class="logout-link"
                   href="${pageContext.request.contextPath}/logout">

                    <i data-lucide="log-out"></i>

                    <span>Logout</span>

                </a>


                <!-- MOBILE MENU -->

                <button type="button"
                        class="mobile-menu-button"
                        id="mobileMenuButton"
                        aria-label="Open menu"
                        aria-expanded="false">

                    <i data-lucide="menu"></i>

                </button>

            </div>

        </div>


        <!-- MOBILE NAVIGATION -->

        <nav class="mobile-nav"
             id="mobileNav">


            <a href="${pageContext.request.contextPath}/user/dashboard">

                <i data-lucide="layout-dashboard"></i>

                <span>Dashboard</span>

            </a>


            <a href="${pageContext.request.contextPath}/books">

                <i data-lucide="library"></i>

                <span>Categories</span>

            </a>


            <a href="${pageContext.request.contextPath}/book-request">

                <i data-lucide="book-plus"></i>

                <span>Book Requests</span>

            </a>


            <a href="${pageContext.request.contextPath}/books/bookmark-book">

                <i data-lucide="bookmark"></i>

                <span>Bookmarks</span>

            </a>


            <a href="${pageContext.request.contextPath}/user/suggestion">

                <i data-lucide="message-square-plus"></i>

                <span>Suggestions</span>

            </a>


            <a href="${pageContext.request.contextPath}/books/history">

                <i data-lucide="history"></i>

                <span>History</span>

            </a>


            <a href="${pageContext.request.contextPath}/profile"
               class="active">

                <i data-lucide="user"></i>

                <span>Profile</span>

            </a>


            <a href="${pageContext.request.contextPath}/logout">

                <i data-lucide="log-out"></i>

                <span>Logout</span>

            </a>

        </nav>

    </header>



    <!-- =====================================================
         PROFILE MAIN
    ====================================================== -->

    <main class="profile-page">


        <div class="profile-container">


            <!-- PAGE HEADING -->

            <div class="profile-heading">

                <div>

                    <span class="profile-eyebrow">
                        ACCOUNT
                    </span>

                    <h1>
                        My Profile
                    </h1>

                    <p>
                        Manage your personal information and account settings.
                    </p>

                </div>

            </div>



            <!-- MESSAGES -->

            <% if (success != null) { %>

                <div class="profile-message profile-success">

                    <span class="message-icon">
                        <i data-lucide="check-circle-2"></i>
                    </span>

                    <span>
                        <%= success %>
                    </span>

                    <button type="button"
                            class="message-close"
                            aria-label="Close message">

                        <i data-lucide="x"></i>

                    </button>

                </div>

            <% } %>


            <% if (error != null) { %>

                <div class="profile-message profile-error">

                    <span class="message-icon">
                        <i data-lucide="alert-circle"></i>
                    </span>

                    <span>
                        <%= error %>
                    </span>

                    <button type="button"
                            class="message-close"
                            aria-label="Close message">

                        <i data-lucide="x"></i>

                    </button>

                </div>

            <% } %>



            <!-- PROFILE CARD -->

            <section class="profile-card">


                <!-- PROFILE HERO -->

                <div class="profile-hero">


                    <div class="profile-avatar">

                        <span>

                            <%
                                String userName = user.getName();

                                if (userName != null
                                        && !userName.trim().isEmpty()) {

                                    out.print(
                                        userName
                                            .trim()
                                            .substring(0, 1)
                                            .toUpperCase()
                                    );

                                } else {

                                    out.print("U");

                                }
                            %>

                        </span>

                    </div>


                    <div class="profile-identity">

                        <div class="profile-identity-top">

                            <h2>
                                <%= user.getName() %>
                            </h2>

                            <span class="profile-status">

                                <span class="status-dot"></span>

                                <%= user.getStatus() %>

                            </span>

                        </div>


                        <p class="profile-email">

                            <i data-lucide="mail"></i>

                            <%= user.getEmail() %>

                        </p>


                        <p class="profile-description">

                            Your personal library account

                        </p>

                    </div>

                </div>



                <!-- PROFILE CONTENT -->

                <div class="profile-content">


                    <!-- PERSONAL INFORMATION -->

                    <div class="profile-section">


                        <div class="profile-section-header">

                            <div class="profile-section-heading">

                                <span class="section-icon">

                                    <i data-lucide="user-round"></i>

                                </span>

                                <div>

                                    <h3>
                                        Personal Information
                                    </h3>

                                    <p>
                                        Update your name and email address.
                                    </p>

                                </div>

                            </div>


                            <button type="button"
                                    class="profile-edit-button"
                                    id="editButton">

                                <i data-lucide="pencil"></i>

                                <span>Edit Profile</span>

                            </button>

                        </div>



                        <!-- FORM -->

                        <form id="profileForm"
                              action="${pageContext.request.contextPath}/profile"
                              method="post">


                            <div class="profile-form-grid">


                                <!-- NAME -->

                                <div class="profile-field">

                                    <label for="name">
                                        Full Name
                                    </label>

                                    <div class="profile-input-wrap">

                                        <i data-lucide="user"></i>

                                        <input
                                            type="text"
                                            id="name"
                                            name="name"
                                            value="<%= user.getName() %>"
                                            readonly
                                            required>

                                    </div>

                                </div>


                                <!-- EMAIL -->

                                <div class="profile-field">

                                    <label for="email">
                                        Email Address
                                    </label>

                                    <div class="profile-input-wrap">

                                        <i data-lucide="mail"></i>

                                        <input
                                            type="email"
                                            id="email"
                                            name="email"
                                            value="<%= user.getEmail() %>"
                                            readonly
                                            required>

                                    </div>

                                </div>

                            </div>



                            <!-- ACCOUNT INFORMATION -->

                            <div class="account-section">


                                <div class="account-section-heading">

                                    <div>

                                        <h3>
                                            Account Information
                                        </h3>

                                        <p>
                                            Basic information about your library account.
                                        </p>

                                    </div>

                                </div>


                                <div class="account-grid">


                                    <!-- STATUS -->

                                    <div class="account-box">

                                        <div class="account-box-icon">

                                            <i data-lucide="shield-check"></i>

                                        </div>

                                        <div>

                                            <span class="account-label">
                                                Account Status
                                            </span>

                                            <strong class="account-value">
                                                <%= user.getStatus() %>
                                            </strong>

                                        </div>

                                    </div>


                                    <!-- ROLE -->

                                    <div class="account-box">

                                        <div class="account-box-icon">

                                            <i data-lucide="badge-check"></i>

                                        </div>

                                        <div>

                                            <span class="account-label">
                                                Account Type
                                            </span>

                                            <strong class="account-value">
                                                <%= user.getRole() %>
                                            </strong>

                                        </div>

                                    </div>

                                </div>

                            </div>



                            <!-- ACTIONS -->

                            <div class="profile-actions">


                                <div class="profile-left-actions">


                                    <button type="submit"
                                            class="profile-save-button"
                                            id="saveButton"
                                            style="display:none;">

                                        <i data-lucide="save"></i>

                                        <span>Save Changes</span>

                                    </button>


                                    <button type="button"
                                            class="profile-cancel-button"
                                            id="cancelButton"
                                            style="display:none;">

                                        <i data-lucide="rotate-ccw"></i>

                                        <span>Cancel</span>

                                    </button>

                                </div>


                                <div class="profile-right-actions">

                                    <button type="button"
                                            class="profile-password-button"
                                            onclick="window.location.href='${pageContext.request.contextPath}/change-password'">

                                        <i data-lucide="key-round"></i>

                                        <span>Change Password</span>

                                    </button>

                                </div>

                            </div>


                        </form>

                    </div>

                </div>

            </section>

        </div>

    </main>



    <!-- =====================================================
         FOOTER
    ====================================================== -->

    <footer class="site-footer">


        <div class="footer-container">


            <!-- FOOTER BRAND -->

            <div class="footer-brand">

                <div class="footer-brand-title">

                    <span class="footer-brand-icon">

                        <i data-lucide="library"></i>

                    </span>

                    <span>
                        Stories
                    </span>

                </div>

                <p>
                    Your digital library for discovering,
                    reading, and enjoying great stories.
                </p>

            </div>


            <!-- QUICK LINKS -->

            <div class="footer-column">

                <h3>
                    Quick Links
                </h3>

                <a href="${pageContext.request.contextPath}/user/dashboard">
                    Dashboard
                </a>

                <a href="${pageContext.request.contextPath}/books">
                    Categories
                </a>

                <a href="${pageContext.request.contextPath}/books/bookmark-book">
                    Bookmarks
                </a>

            </div>


            <!-- SUPPORT -->

            <div class="footer-column">

                <h3>
                    Support
                </h3>

                <a href="${pageContext.request.contextPath}/book-request">
                    Book Requests
                </a>

                <a href="${pageContext.request.contextPath}/user/suggestion">
                    Suggestions
                </a>

                <a href="${pageContext.request.contextPath}/books/history">
                    History
                </a>

            </div>


            <!-- ACCOUNT -->

            <div class="footer-column">

                <h3>
                    Account
                </h3>

                <a href="${pageContext.request.contextPath}/profile">
                    Profile
                </a>

                <a href="${pageContext.request.contextPath}/change-password">
                    Change Password
                </a>

                <a href="${pageContext.request.contextPath}/logout">
                    Logout
                </a>

            </div>

        </div>


        <div class="footer-bottom">

            <span>
                ©️ 2026 Stories E-Library
            </span>

            <span>
                Read. Discover. Enjoy.
            </span>

        </div>

    </footer>

</div>


<!-- PROFILE JAVASCRIPT -->

<script src="${pageContext.request.contextPath}/javascript/profile.js"></script>

</body>

</html>