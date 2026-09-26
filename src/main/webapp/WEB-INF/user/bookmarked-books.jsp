<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<%@ page import="com.project.elibrary.bean.book.Book"%>
<%@ page import="com.project.elibrary.bean.user.User"%>
<%@ page import="com.project.elibrary.bean.enums.Role"%>

<%
List<Book> books = (List<Book>) request.getAttribute("books");

Map<Long, String> coverUrlMap =
		(Map<Long, String>) request.getAttribute("coverUrlMap");

Integer currentPageObj =
		(Integer) request.getAttribute("currentPage");

Integer totalPagesObj =
		(Integer) request.getAttribute("totalPages");

int currentPage =
		currentPageObj != null ? currentPageObj : 1;

int totalPages =
		totalPagesObj != null ? totalPagesObj : 1;

User loggedInUser =
		(User) session.getAttribute("loggedInUser");

boolean isUser =
		loggedInUser != null
		&& loggedInUser.getRole() == Role.USER;
%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport"
	content="width=device-width, initial-scale=1.0">

<meta name="theme-color" content="#2C1810">

<title>Bookmarks | Stories E-LIBRARY</title>

<!-- Google Fonts -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

<link
	href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap"
	rel="stylesheet">

<!-- Lucide Icons -->
<script src="https://unpkg.com/lucide@latest"></script>

<!-- Background -->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/background.css">

<!-- Page CSS -->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/bookmarked-books.css?v=3">

</head>

<body>

<div class="page-shell">

	<!-- =====================================================
	     HEADER
	====================================================== -->

	<header class="site-header">

    <div class="header-inner">

        <!-- BRAND -->
        <a href="<%=request.getContextPath()%>/user/dashboard" class="brand">

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
        <nav class="desktop-nav" aria-label="Main navigation">

            <a href="<%=request.getContextPath()%>/user/dashboard">
                <i data-lucide="layout-dashboard"></i>
                <span>Dashboard</span>
            </a>

            <a href="<%=request.getContextPath()%>/books">
                <i data-lucide="library"></i>
                <span>Categories</span>
            </a>

            <a href="<%=request.getContextPath()%>/book-request">
                <i data-lucide="book-plus"></i>
                <span>Book Requests</span>
            </a>

            <a href="<%=request.getContextPath()%>/books/bookmark-book"
               class="active">
                <i data-lucide="bookmark"></i>
                <span>Bookmarks</span>
            </a>

            <a href="<%=request.getContextPath()%>/user/suggestion">
                <i data-lucide="message-square-plus"></i>
                <span>Suggestions</span>
            </a>

            <a href="<%=request.getContextPath()%>/books/history">
                <i data-lucide="history"></i>
                <span>History</span>
            </a>

            <a href="<%=request.getContextPath()%>/profile">
                <i data-lucide="user"></i>
                <span>Profile</span>
            </a>

        </nav>


        <!-- HEADER ACTIONS -->
        <div class="header-actions">

            <a href="<%=request.getContextPath()%>/logout"
               class="logout-link">

                <i data-lucide="log-out"></i>
                <span>Logout</span>

            </a>

            <button
                type="button"
                class="mobile-menu-button"
                id="mobileMenuButton"
                aria-label="Open navigation"
                aria-expanded="false">

                <i data-lucide="menu"></i>

            </button>

        </div>

    </div>


    <!-- MOBILE NAV -->
    <nav
        class="mobile-nav"
        id="mobileNav"
        aria-label="Mobile navigation">

        <a href="<%=request.getContextPath()%>/user/dashboard">
            <i data-lucide="layout-dashboard"></i>
            Dashboard
        </a>

        <a href="<%=request.getContextPath()%>/books">
            <i data-lucide="library"></i>
            Categories
        </a>

        <a href="<%=request.getContextPath()%>/book-request">
            <i data-lucide="book-plus"></i>
            Book Requests
        </a>

        <a href="<%=request.getContextPath()%>/books/bookmark-book"
           class="active">
            <i data-lucide="bookmark"></i>
            Bookmarks
        </a>

        <a href="<%=request.getContextPath()%>/user/suggestion">
            <i data-lucide="message-square-plus"></i>
            Suggestions
        </a>

        <a href="<%=request.getContextPath()%>/books/history">
            <i data-lucide="history"></i>
            History
        </a>

        <a href="<%=request.getContextPath()%>/profile">
            <i data-lucide="user"></i>
            Profile
        </a>

        <a href="<%=request.getContextPath()%>/logout">
            <i data-lucide="log-out"></i>
            Logout
        </a>

    </nav>

</header>

	<!-- =====================================================
	     MAIN
	====================================================== -->

	<main class="main-content">


		<!-- PAGE INTRO -->

		<section class="page-intro">

			<div class="intro-copy">

				<div class="eyebrow">
					YOUR SAVED COLLECTION
				</div>

				<h1>
					Your <em>bookmarks.</em>
				</h1>

				<p>
					Keep the stories that caught your attention
					close at hand, ready whenever you are.
				</p>

			</div>


			<!-- SUMMARY -->

			<div class="bookmark-summary">

				<div class="summary-icon">
					<i data-lucide="bookmark"></i>
				</div>

				<div class="summary-content">

					<span class="summary-label">
						SAVED BOOKS
					</span>

					<strong class="summary-number">
						<%=request.getAttribute("totalBooks") != null
							? request.getAttribute("totalBooks")
							: (books != null ? books.size() : 0)%>
					</strong>

					<span class="summary-note">
						in your collection
					</span>

				</div>

			</div>

		</section>


		<!-- =================================================
		     BOOKMARK SECTION
		================================================== -->

		<section class="bookmark-section">


			<div class="section-heading">

				<div>

					<span class="section-label">
						SAVED FOR LATER
					</span>

					<h2>
						Your bookmarks
					</h2>

				</div>


				<%
				if (books != null && !books.isEmpty()) {
				%>

				<div class="bookmark-count">

					<i data-lucide="bookmark"></i>

					<span>
						<%=books.size()%>
						<%=books.size() == 1 ? "book" : "books"%>
					</span>

				</div>

				<%
				}
				%>

			</div>


			<%
			if (books == null || books.isEmpty()) {
			%>


			<!-- =================================================
			     EMPTY STATE
			================================================== -->

			<div class="empty-bookmarks">

				<div class="empty-bookmarks-icon">
					<i data-lucide="bookmark-x"></i>
				</div>

				<span class="section-label">
					A QUIET SHELF
				</span>

				<h2>
					Nothing saved yet.
				</h2>

				<p>
					When you find a book you want to come back to,
					bookmark it and it will appear here.
				</p>

				<a
					href="<%=request.getContextPath()%>/books"
					class="browse-button">

					<i data-lucide="library"></i>

					<span>
						Explore books
					</span>

				</a>

			</div>


			<%
			} else {
			%>


			<!-- =================================================
			     BOOK GRID
			================================================== -->

			<div class="book-grid">

				<%
				for (Book book : books) {

					String coverUrl = null;

					if (coverUrlMap != null) {
						coverUrl =
								coverUrlMap.get(book.getBookId());
					}
				%>


				<article
					class="book-card"
					data-book-id="<%=book.getBookId()%>">


					<!-- COVER -->

					<div class="book-cover">

						<%
						if (coverUrl != null && !coverUrl.isBlank()) {
						%>

						<img
							src="<%=coverUrl%>"
							alt="Cover of <%=book.getTitle()%>"
							loading="lazy">

						<%
						} else {
						%>

						<div class="no-cover">

							<i data-lucide="book-open"></i>

							<span>
								No Cover
							</span>

						</div>

						<%
						}
						%>


						<!-- BOOKMARK BADGE -->

						<div class="bookmark-badge">
							<i data-lucide="bookmark"></i>
						</div>

					</div>


					<!-- INFORMATION -->

					<div class="book-information">

						<h3
							class="book-title"
							title="<%=book.getTitle()%>">

							<%=book.getTitle()%>

						</h3>


						<p class="book-author">

							<i data-lucide="pen-line"></i>

							<span>
								<%=book.getAuthor() != null
									&& !book.getAuthor().isBlank()
									? book.getAuthor()
									: "Unknown author"%>
							</span>

						</p>


						<%
						if (book.getDescription() != null
								&& !book.getDescription().isBlank()) {
						%>

						<p class="book-description">
							<%=book.getDescription()%>
						</p>

						<%
						}
						%>


						<!-- ACTIONS -->

						<div class="book-actions">

							<a
								href="<%=request.getContextPath()%>/books/details?id=<%=book.getBookId()%>"
								class="read-button">

								<span>
									Read More
								</span>

								<i data-lucide="arrow-up-right"></i>

							</a>


							<%
							if (isUser) {
							%>

							<button
								type="button"
								class="remove-bookmark-button"
								onclick="removeBookBookmark(this, <%=book.getBookId()%>)">

								<i data-lucide="bookmark-minus"></i>

								<span>
									Remove
								</span>

							</button>

							<%
							}
							%>

						</div>

					</div>

				</article>


				<%
				}
				%>

			</div>


			<!-- =================================================
			     PAGINATION
			================================================== -->

			<%
			if (totalPages > 1) {
			%>

			<nav
				class="pagination"
				aria-label="Bookmarked books pagination">


				<!-- PREVIOUS -->

				<%
				if (currentPage > 1) {
				%>

				<a
					href="<%=request.getContextPath()%>/books/bookmark-book?page=<%=currentPage - 1%>"
					class="pagination-button">

					<i data-lucide="chevron-left"></i>

					<span>
						Previous
					</span>

				</a>

				<%
				} else {
				%>

				<span class="pagination-button disabled">

					<i data-lucide="chevron-left"></i>

					<span>
						Previous
					</span>

				</span>

				<%
				}
				%>


				<!-- PAGE NUMBERS -->

				<div class="page-numbers">

					<%
					for (
						int pageNumber = 1;
						pageNumber <= totalPages;
						pageNumber++
					) {
					%>

					<%
					if (pageNumber == currentPage) {
					%>

					<span class="page-number active">
						<%=pageNumber%>
					</span>

					<%
					} else {
					%>

					<a
						href="<%=request.getContextPath()%>/books/bookmark-book?page=<%=pageNumber%>"
						class="page-number">

						<%=pageNumber%>

					</a>

					<%
					}
					%>

					<%
					}
					%>

				</div>


				<!-- NEXT -->

				<%
				if (currentPage < totalPages) {
				%>

				<a
					href="<%=request.getContextPath()%>/books/bookmark-book?page=<%=currentPage + 1%>"
					class="pagination-button">

					<span>
						Next
					</span>

					<i data-lucide="chevron-right"></i>

				</a>

				<%
				} else {
				%>

				<span class="pagination-button disabled">

					<span>
						Next
					</span>

					<i data-lucide="chevron-right"></i>

				</span>

				<%
				}
				%>

			</nav>

			<%
			}
			%>


			<%
			}
			%>

		</section>

	</main>


	<!-- =====================================================
	     FOOTER
	====================================================== -->

	<footer class="site-footer">

		<span>
			STORIES E-LIBRARY
		</span>

		<span class="footer-separator">
			·
		</span>

		<span>
			READ · DISCOVER · SHARE
		</span>

	</footer>

</div>


<!-- =====================================================
     JAVASCRIPT
====================================================== -->

<script
	src="<%=request.getContextPath()%>/javascript/bookmarked-books.js?v=3">
</script>

</body>

</html>