<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ page import="com.project.elibrary.bean.user.User" %>

<%
    User loggedInUser =
        (User) request.getAttribute("loggedInUser");

    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");

    String userName =
        loggedInUser != null && loggedInUser.getName() != null
        ? loggedInUser.getName() : "";

    String userEmail =
        loggedInUser != null && loggedInUser.getEmail() != null
        ? loggedInUser.getEmail() : "";
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Suggestions | Stories E-Library</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/dashboard.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/user-suggestion.css">

    <script src="https://unpkg.com/lucide@latest"></script>

    <script
        src="${pageContext.request.contextPath}/javascript/user-suggestion.js"
        defer>
    </script>
    
    <link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/background.css">
</head>

<body>

<!-- ================= HEADER ================= -->

<header class="site-header">

    <div class="header-inner">

        <!-- Brand -->
        <a href="${pageContext.request.contextPath}/user/dashboard"
           class="brand">

            <span class="brand-icon">
                <i data-lucide="library"></i>
            </span>

            <span class="brand-text">
                <span class="brand-name">Stories</span>
                <span class="brand-subtitle">E-LIBRARY</span>
            </span>

        </a>


        <!-- Desktop Navigation -->
        <nav class="desktop-nav" aria-label="Main navigation">

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

            <a href="${pageContext.request.contextPath}/user/suggestion"
               class="active"
               aria-current="page">
                <i data-lucide="message-square-plus"></i>
                <span>Suggestions</span>
            </a>

            <a href="${pageContext.request.contextPath}/books/history">
                <i data-lucide="history"></i>
                <span>History</span>
            </a>

            <a href="${pageContext.request.contextPath}/profile">
                <i data-lucide="user"></i>
                <span>Profile</span>
            </a>

        </nav>


        <!-- Header Actions -->
        <div class="header-actions">

            <a href="${pageContext.request.contextPath}/logout"
               class="logout-link">

                <i data-lucide="log-out"></i>
                <span>Logout</span>

            </a>


            <!-- Mobile Menu Button -->
            <button type="button"
                    class="mobile-menu-button"
                    id="mobileMenuButton"
                    aria-label="Open navigation"
                    aria-expanded="false">

                <i data-lucide="menu"></i>

            </button>

        </div>

    </div>


    <!-- Mobile Navigation -->
    <nav class="mobile-nav" id="mobileNav">

        <a href="${pageContext.request.contextPath}/user/dashboard">
            <i data-lucide="layout-dashboard"></i>
            Dashboard
        </a>

        <a href="${pageContext.request.contextPath}/books">
            <i data-lucide="library"></i>
            Categories
        </a>

        <a href="${pageContext.request.contextPath}/book-request">
            <i data-lucide="book-plus"></i>
            Book Requests
        </a>

        <a href="${pageContext.request.contextPath}/books/bookmark-book">
            <i data-lucide="bookmark"></i>
            Bookmarks
        </a>

        <a href="${pageContext.request.contextPath}/user/suggestion"
           class="active">
            <i data-lucide="message-square-plus"></i>
            Suggestions
        </a>

        <a href="${pageContext.request.contextPath}/books/history">
            <i data-lucide="history"></i>
            History
        </a>

        <a href="${pageContext.request.contextPath}/profile">
            <i data-lucide="user"></i>
            Profile
        </a>

        <a href="${pageContext.request.contextPath}/logout">
            <i data-lucide="log-out"></i>
            Logout
        </a>

    </nav>

</header>

<!-- ================= MAIN CONTENT ================= -->

<main class="suggestion-page">

    <!-- Breadcrumb -->
    <div class="breadcrumb">
        <a href="${pageContext.request.contextPath}/user/dashboard">
            <i data-lucide="house"></i>
            Dashboard
        </a>

        <i data-lucide="chevron-right" class="breadcrumb-arrow"></i>

        <span>Suggestion</span>
    </div>

    <!-- Hero Section -->
    <section class="suggestion-hero">

        <div class="hero-content">

            <span class="hero-eyebrow">
                <i data-lucide="sparkles"></i>
                YOUR VOICE MATTERS
            </span>

            <h1>
                Have an idea?<br>
                <span>Let's make it happen.</span>
            </h1>

            <p>
                Help us make Stories E-Library better.
                Share your thoughts, suggest a feature, or tell us
                what would make your reading experience more enjoyable.
            </p>

            <div class="hero-note">
                <i data-lucide="heart"></i>
                Every suggestion helps us build a better reading community.
            </div>

        </div>

        <div class="hero-art" aria-hidden="true">

            <div class="art-circle art-circle-one"></div>
            <div class="art-circle art-circle-two"></div>

            <div class="idea-card">
                <div class="idea-icon">
                    <i data-lucide="lightbulb"></i>
                </div>

                <span class="idea-label">A LITTLE IDEA</span>

                <div class="idea-lines">
                    <span></span>
                    <span></span>
                    <span></span>
                </div>

                <div class="idea-footer">
                    <i data-lucide="sparkles"></i>
                    <span>Big possibilities</span>
                </div>
            </div>

            <div class="floating-icon floating-book">
                <i data-lucide="book-open"></i>
            </div>

            <div class="floating-icon floating-message">
                <i data-lucide="message-square"></i>
            </div>

        </div>

    </section>

    <!-- ================= FORM SECTION ================= -->

    <section class="suggestion-layout">

        <!-- Left Information Panel -->
        <aside class="suggestion-sidebar">

            <div class="sidebar-heading">
                <span class="sidebar-kicker">YOUR IDEAS</span>
                <h2>Help us write the next chapter.</h2>
                <p>
                    Your feedback can inspire features that make
                    reading easier, more enjoyable, and more personal.
                </p>
            </div>

            <div class="benefit-list">

                <div class="benefit-item">
                    <span class="benefit-icon">
                        <i data-lucide="messages-square"></i>
                    </span>

                    <div>
                        <h3>Share your thoughts</h3>
                        <p>Tell us what you'd love to see in the app.</p>
                    </div>
                </div>

                <div class="benefit-item">
                    <span class="benefit-icon">
                        <i data-lucide="wand-sparkles"></i>
                    </span>

                    <div>
                        <h3>Suggest something new</h3>
                        <p>Share ideas for features and improvements.</p>
                    </div>
                </div>

                <div class="benefit-item">
                    <span class="benefit-icon">
                        <i data-lucide="book-heart"></i>
                    </span>

                    <div>
                        <h3>Shape your reading experience</h3>
                        <p>Help us make your library feel more personal.</p>
                    </div>
                </div>

            </div>

            <div class="sidebar-quote">
                <i data-lucide="quote"></i>
                <p>
                    Great things begin with a simple idea.
                </p>
                <span>— Stories E-Library</span>
            </div>

        </aside>

        <!-- Suggestion Form -->
        <div class="suggestion-card">

            <div class="form-header">

                <div class="form-header-icon">
                    <i data-lucide="message-square-plus"></i>
                </div>

                <div>
                    <span class="form-kicker">WE'RE LISTENING</span>
                    <h2>Suggest an App Feature</h2>
                    <p>Tell us about your idea. We'd love to hear it.</p>
                </div>

            </div>

            <!-- Success Message -->
            <% if (success != null && !success.isBlank()) { %>

                <div class="message success-message" role="status">
                    <i data-lucide="check-circle-2"></i>
                    <span><%= success %></span>
                </div>

            <% } %>

            <!-- Error Message -->
            <% if (error != null && !error.isBlank()) { %>

                <div class="message error-message" role="alert">
                    <i data-lucide="alert-circle"></i>
                    <span><%= error %></span>
                </div>

            <% } %>

            <form
                action="${pageContext.request.contextPath}/user/suggestion"
                method="post"
                id="suggestionForm">

                <!-- User Information -->
                <div class="form-section-label">
                    <i data-lucide="user-round"></i>
                    YOUR INFORMATION
                </div>

                <div class="form-row">

                    <div class="form-group">
                        <label for="name">Full Name</label>

                        <div class="input-wrapper readonly-wrapper">
                            <i data-lucide="user-round"></i>

                            <input
                                type="text"
                                id="name"
                                class="form-control"
                                value="<%= userName %>"
                                readonly>

                            <i data-lucide="lock" class="field-lock"></i>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="email">Email Address</label>

                        <div class="input-wrapper readonly-wrapper">
                            <i data-lucide="mail"></i>

                            <input
                                type="email"
                                id="email"
                                class="form-control"
                                value="<%= userEmail %>"
                                readonly>

                            <i data-lucide="lock" class="field-lock"></i>
                        </div>
                    </div>

                </div>

                <div class="account-note">
                    <i data-lucide="shield-check"></i>
                    Your account information is securely linked to your suggestion.
                </div>

                <div class="form-divider"></div>

                <!-- Suggestion -->
                <div class="form-section-label">
                    <i data-lucide="lightbulb"></i>
                    YOUR IDEA
                </div>

                <div class="form-group">

                    <label for="description">
                        Your Suggestion
                        <span class="required-mark">*</span>
                    </label>

                    <div class="textarea-wrapper">

                        <textarea
                            id="description"
                            name="description"
                            class="form-control suggestion-textarea"
                            minlength="10"
                            maxlength="1000"
                            required
                            placeholder="I'd love to see a feature that...&#10;&#10;Tell us what you'd like to improve, why it would be helpful, and how you'd use it."
                        ></textarea>

                        <div class="textarea-footer">

                            <span class="textarea-hint">
                                <i data-lucide="pen-line"></i>
                                Be as descriptive as you'd like.
                            </span>

                            <span class="character-counter" id="characterCounter">
                                <strong id="characterCount">0</strong> / 1000
                            </span>

                        </div>

                    </div>

                    <div class="form-note">
                        <i data-lucide="info"></i>
                        Please enter at least 10 characters.
                    </div>

                    <p class="validation-message" id="validationMessage" hidden></p>

                </div>

                <!-- Footer -->
                <div class="form-footer">

                    <p class="privacy-note">
                        <i data-lucide="shield-check"></i>
                        Your suggestion will be shared with our admin team.
                    </p>

                    <div class="form-actions">

                        <a href="${pageContext.request.contextPath}/user/dashboard"
                           class="back-button">
                            <i data-lucide="arrow-left"></i>
                            Back
                        </a>

                        <button type="submit"
                                class="submit-button"
                                id="submitButton">

                            <span class="submit-text">
                                Submit Suggestion
                            </span>

                            <i data-lucide="send"></i>
                        </button>

                    </div>

                </div>

            </form>

        </div>

    </section>

    <!-- Bottom Note -->
    <div class="bottom-note">
        <i data-lucide="book-open"></i>
        <span>Stories E-Library</span>
        <span class="bottom-divider"></span>
        <span>Read. Discover. Explore.</span>
    </div>

</main>

</body>
</html>