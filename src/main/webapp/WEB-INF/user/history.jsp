<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>

<%
    /*
     * Keep your existing history backend logic here.
     * Replace the example history variable below with
     * your existing request/session attribute if needed.
     *
     * Example:
     * List<?> historyList =
     *     (List<?>) request.getAttribute("history");
     */

    List<?> historyList =
        (List<?>) request.getAttribute("history");
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <meta name="theme-color"
          content="#2C1810">

    <title>History | Stories E-LIBRARY</title>

    <!-- Fonts -->
    <link rel="preconnect"
          href="https://fonts.googleapis.com">

    <link rel="preconnect"
          href="https://fonts.gstatic.com"
          crossorigin>

    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:ital,wght@0,400;0,500;0,600;0,700;1,400;1,500&display=swap"
          rel="stylesheet">

    <!-- Lucide -->
    <script src="https://unpkg.com/lucide@latest"></script>

    <!-- CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/background.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/history.css?v=2">

</head>


<body>


<div class="page-shell">


    <!-- =====================================================
         HEADER
         ===================================================== -->

    <header class="site-header">

        <div class="header-inner">


            <!-- BRAND -->

            <a href="${pageContext.request.contextPath}/user/dashboard"
               class="brand">

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


            <!-- DESKTOP NAV -->

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

                <a href="${pageContext.request.contextPath}/user/suggestions">
                    <i data-lucide="message-square-plus"></i>
                    <span>Suggestions</span>
                </a>

                <a href="${pageContext.request.contextPath}/books/history"
                   class="active">

                    <i data-lucide="history"></i>
                    <span>History</span>

                </a>

                <a href="${pageContext.request.contextPath}/profile">
                    <i data-lucide="user-round"></i>
                    <span>Profile</span>
                </a>

            </nav>


            <!-- HEADER ACTIONS -->

            <div class="header-actions">

                <a href="${pageContext.request.contextPath}/logout"
                   class="logout-link">

                    <i data-lucide="log-out"></i>

                    <span>Logout</span>

                </a>


                <!-- MOBILE MENU -->

                <button type="button"
                        id="mobileMenuButton"
                        class="mobile-menu-button"
                        aria-label="Open navigation"
                        aria-expanded="false">

                    <i data-lucide="menu"></i>

                </button>

            </div>

        </div>


        <!-- MOBILE NAV -->

        <nav id="mobileNav"
             class="mobile-nav">

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

            <a href="${pageContext.request.contextPath}/books/history"
               class="active">

                <i data-lucide="history"></i>
                <span>History</span>

            </a>

            <a href="${pageContext.request.contextPath}/profile">
                <i data-lucide="user-round"></i>
                <span>Profile</span>
            </a>

            <a href="${pageContext.request.contextPath}/logout">

                <i data-lucide="log-out"></i>
                <span>Logout</span>

            </a>

        </nav>

    </header>



    <!-- =====================================================
         MAIN
         ===================================================== -->

    <main class="main-content">


        <!-- PAGE INTRO -->

        <section class="page-intro">

            <div class="intro-copy">

                <span class="eyebrow">
                    YOUR READING JOURNEY
                </span>

                <h1>
                    Reading <em>history.</em>
                </h1>

                <p>
                    A quiet record of the books you've explored,
                    discovered, and returned to along the way.
                </p>

            </div>


            <!-- HISTORY SUMMARY -->

            <div class="history-summary">

                <div class="summary-icon">
                    <i data-lucide="book-open"></i>
                </div>

                <div class="summary-content">

                    <span class="summary-label">
                        BOOKS EXPLORED
                    </span>

                    <div class="summary-number">

                        <strong>
                            <%= historyList != null
                                ? historyList.size()
                                : 0 %>
                        </strong>

                        <span>books</span>

                    </div>

                    <span class="summary-note">
                        Your reading trail
                    </span>

                </div>

            </div>

        </section>



        <!-- =================================================
             HISTORY SECTION
             ================================================= -->

        <section class="history-section">


            <div class="section-heading">

                <div>

                    <span class="section-label">
                        RECENTLY EXPLORED
                    </span>

                    <h2>
                        Your history
                    </h2>

                </div>


                <div class="history-count">

                    <i data-lucide="clock-3"></i>

                    <span>
                        <%= historyList != null
                            ? historyList.size()
                            : 0 %>
                        entries
                    </span>

                </div>

            </div>



            <!-- =================================================
                 HISTORY LIST
                 ================================================= -->

            <%
                if (historyList != null &&
                    !historyList.isEmpty()) {
            %>


            <div class="history-list">


                <%
                    int historyNumber = 1;

                    for (Object history : historyList) {
                %>


                <article class="history-card">


                    <!-- NUMBER -->

                    <div class="history-number">

                        <span>
                            <%= String.format(
                                "%02d",
                                historyNumber++
                            ) %>
                        </span>

                    </div>


                    <!-- BOOK ICON -->

                    <div class="history-book-icon">

                        <i data-lucide="book-open"></i>

                    </div>


                    <!-- BOOK INFORMATION -->

                    <div class="history-info">

                        <h3>
                            Book title
                        </h3>

                        <div class="history-author">

                            <i data-lucide="pen-line"></i>

                            <span>
                                Author name
                            </span>

                        </div>

                    </div>


                    <!-- DATE -->

                    <div class="history-date">

                        <span class="date-label">
                            READ
                        </span>

                        <span class="date-value">
                            Recently
                        </span>

                    </div>


                    <!-- ACTION -->

                    <a href="#"
                       class="history-action"
                       aria-label="Open book">

                        <i data-lucide="arrow-up-right"></i>

                    </a>


                </article>


                <%
                    }
                %>


            </div>


            <%
                } else {
            %>


            <!-- =================================================
                 EMPTY STATE
                 ================================================= -->

            <div class="empty-history">

                <div class="empty-history-icon">

                    <i data-lucide="history"></i>

                </div>

                <span class="section-label">
                    A QUIET SHELF
                </span>

                <h2>
                    Nothing here yet.
                </h2>

                <p>
                    Books you explore will appear here,
                    creating a personal record of your
                    reading journey.
                </p>

                <a href="${pageContext.request.contextPath}/books"
                   class="browse-button">

                    <i data-lucide="library"></i>

                    <span>
                        Explore books
                    </span>

                </a>

            </div>


            <%
                }
            %>


        </section>

    </main>



    <!-- =====================================================
         FOOTER
         ===================================================== -->

    <footer class="site-footer">

        <span>
            STORIES E-LIBRARY
        </span>

        <span class="footer-line"></span>

        <span>
            READ · DISCOVER · SHARE
        </span>

    </footer>


</div>


<!-- =====================================================
     JAVASCRIPT
     ===================================================== -->

<script src="${pageContext.request.contextPath}/javascript/history.js?v=2"></script>

</body>

</html>