<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.project.elibrary.bean.book.Book"%>
<%@ page import="com.project.elibrary.bean.category.Category"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="theme-color" content="#2C1810">
<title>User Dashboard - E-Library</title>

<!-- Google Fonts -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap"
	rel="stylesheet">
	
	<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/background.css">

<!-- Custom CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/dashboard.css">

<!-- Lucide Icons CDN -->
<script src="https://unpkg.com/lucide@latest"></script>
</head>
<body>

	<div class="page-shell">

	<!-- HEADER -->
	<header class="site-header">

		<div class="header-inner">

			<!-- BRAND -->
			<a href="${pageContext.request.contextPath}/user/dashboard" class="brand">

				<span class="brand-icon">
					<i data-lucide="library"></i>
				</span>

				<span class="brand-text">
					<span class="brand-name">Stories</span>
					<span class="brand-subtitle">E-LIBRARY</span>
				</span>

			</a>

			<!-- DESKTOP NAV -->
			<nav class="desktop-nav" aria-label="Main navigation">

				<a href="${pageContext.request.contextPath}/user/dashboard" class="active">
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
				<a href="${pageContext.request.contextPath}/profile">
					<i data-lucide="user"></i>
					<span>Profile</span>
				</a>

			</nav>

			<!-- HEADER ACTIONS -->
			<div class="header-actions">

				<a href="${pageContext.request.contextPath}/logout" class="logout-link">
					<i data-lucide="log-out"></i>
					<span>Logout</span>
				</a>

				<button type="button" class="mobile-menu-button" id="mobileMenuButton"
					aria-label="Open navigation" aria-expanded="false">
					<i data-lucide="menu"></i>
				</button>

			</div>

		</div>

		<!-- MOBILE NAV -->
		<nav class="mobile-nav" id="mobileNav" aria-label="Mobile navigation">

			<a href="${pageContext.request.contextPath}/user/dashboard" class="active">
				<i data-lucide="layout-dashboard"></i> Dashboard
			</a>
			<a href="${pageContext.request.contextPath}/books">
				<i data-lucide="library"></i> Categories
			</a>
			<a href="${pageContext.request.contextPath}/book-request">
				<i data-lucide="book-plus"></i> Book Requests
			</a>
			<a href="${pageContext.request.contextPath}/books/bookmark-book">
				<i data-lucide="bookmark"></i> Bookmarks
			</a>
			<a href="${pageContext.request.contextPath}/user/suggestion">
				<i data-lucide="message-square-plus"></i> Suggestions
			</a>
			<a href="${pageContext.request.contextPath}/books/history">
				<i data-lucide="history"></i> History
			</a>
			<a href="${pageContext.request.contextPath}/profile">
				<i data-lucide="user"></i> Profile
			</a>
			<a href="${pageContext.request.contextPath}/logout">
				<i data-lucide="log-out"></i> Logout
			</a>

		</nav>
	</header>

	<!-- MAIN CONTAINER -->
	<div class="dashboard-container">

		<!-- SEARCH SECTION -->
		<section class="search-section">
			<h2>Search Books</h2>
			<form class="search-form"
				action="${pageContext.request.contextPath}/books" method="get">
				<input class="search-input" type="text" name="keyword"
					placeholder="Search by title or author">
				<button class="search-button" type="submit">
					<i data-lucide="search"></i> Search
				</button>
			</form>
		</section>

		<!-- RECOMMENDED BOOKS SLIDER -->
		<%
		List<Book> recommendedBooks = (List<Book>) request.getAttribute("recommendedBooks");
		Map<Long, String> bookCoverUrlMap = (Map<Long, String>) request.getAttribute("bookCoverUrlMap");

		if (recommendedBooks != null && !recommendedBooks.isEmpty()) {
		%>

		<section class="recommended-section">

			<div class="recommended-header">
				<div class="recommended-title">
					<i data-lucide="sparkles"></i>
					<h2>Recommended For You</h2>
				</div>
			</div>

			<div class="slider" id="recommendedSlider">

				<div class="slider-track">

					<%
					for (Book book : recommendedBooks) {

						Long bookId = book.getBookId();
						String coverUrl = (bookCoverUrlMap != null) ? bookCoverUrlMap.get(bookId) : null;
					%>

					<div class="slide">
						<div class="slide-card">

							<div class="slide-cover">
								<%
								if (coverUrl != null && !coverUrl.isBlank()) {
								%>
								<img src="<%=coverUrl%>" alt="<%=book.getTitle()%>">
								<%
								} else {
								%>
								<div class="no-cover">
									<i data-lucide="book-open"></i> <span>No Cover</span>
								</div>
								<%
								}
								%>
							</div>

							<div class="slide-info">
								<h3 class="slide-title"><%=book.getTitle()%></h3>
								<div class="slide-author">By <%=book.getAuthor()%></div>
								<div class="slide-description">
									<%
									String description = book.getDescription();
									if (description != null && !description.isBlank()) {
										out.print(description.length() > 160 ? description.substring(0, 160) + "..." : description);
									} else {
										out.print("No description available.");
									}
									%>
								</div>
								<a class="read-button"
									href="${pageContext.request.contextPath}/books/details?id=<%= bookId %>">
									<i data-lucide="book-open"></i> Read Now
								</a>
							</div>

						</div>
					</div>

					<%
					}
					%>

				</div>

				<button type="button" class="slider-arrow slider-arrow-left" id="sliderPrev" aria-label="Previous">
					<i data-lucide="chevron-left"></i>
				</button>
				<button type="button" class="slider-arrow slider-arrow-right" id="sliderNext" aria-label="Next">
					<i data-lucide="chevron-right"></i>
				</button>

				<div class="slider-dots" id="sliderDots"></div>

			</div>

		</section>

		<%
		}
		%>

		<!-- BOOKMARKED BOOKS -->

		<%
		List<Book> bookmarkedBooks = (List<Book>) request.getAttribute("bookmarkedBooks");

		Map<Long, String> bookmarkedBookCoverUrlMap = (Map<Long, String>) request.getAttribute("bookmarkedBookCoverUrlMap");

		if (bookmarkedBooks != null && !bookmarkedBooks.isEmpty()) {
		%>

		<section class="bookmarked-section">

			<div class="bookmarked-header">

				<div class="bookmarked-title">

					<i data-lucide="bookmark"></i>

					<h2>Bookmarked Books</h2>

				</div>

				<a class="view-all-button"
					href="${pageContext.request.contextPath}/books/bookmark-book">

					View All <i data-lucide="arrow-right"></i>

				</a>

			</div>


			<div class="book-row">

				<%
				for (Book book : bookmarkedBooks) {

					Long bookId = book.getBookId();

					String coverUrl = (bookmarkedBookCoverUrlMap != null) ? bookmarkedBookCoverUrlMap.get(bookId) : null;
				%>

				<div class="book-card">

					<%
					if (coverUrl != null && !coverUrl.isBlank()) {
					%>

					<img class="book-cover" src="<%=coverUrl%>" alt="Book Cover">

					<%
					} else {
					%>

					<div class="no-cover">

						<i data-lucide="book-open"></i> <span>No Cover</span>

					</div>

					<%
					}
					%>


					<div class="book-info">

						<h3 class="book-title">
							<%=book.getTitle()%>
						</h3>


						<div class="book-author">

							By
							<%=book.getAuthor()%>

						</div>


						<div class="book-description">

							<%
							String description = book.getDescription();

							if (description != null && !description.isBlank()) {

								out.print(description.length() > 100 ? description.substring(0, 100) + "..." : description);

							} else {

								out.print("No description available.");
							}
							%>

						</div>


						<a class="read-button"
							href="${pageContext.request.contextPath}/books/details?id=<%= bookId %>">

							<i data-lucide="book-open"></i> Read More

						</a>

					</div>

				</div>

				<%
				}
				%>

			</div>

		</section>

		<%
		}
		%>


		<!-- RECENTLY READ BOOKS -->
		<%
		List<Book> recentlyReadBooks = (List<Book>) request.getAttribute("recentlyReadBooks");
		Map<Long, Double> bookAverageRatingMap = (Map<Long, Double>) request.getAttribute("bookAverageRatingMap");
		Map<Long, Integer> bookRatingCountMap = (Map<Long, Integer>) request.getAttribute("bookRatingCountMap");
		%>
		<section class="recently-read-section">
			<div class="recently-read-header">
				<h2>Recently Read</h2>
			</div>

			<%
			if (recentlyReadBooks != null && !recentlyReadBooks.isEmpty()) {
			%>
			<div class="book-row">
				<%
				for (Book book : recentlyReadBooks) {
					Long bookId = book.getBookId();
					Double averageRating = (bookAverageRatingMap != null) ? bookAverageRatingMap.get(bookId) : null;
					Integer ratingCount = (bookRatingCountMap != null) ? bookRatingCountMap.get(bookId) : null;
					String coverUrl = (bookCoverUrlMap != null) ? bookCoverUrlMap.get(bookId) : null;
				%>
				<div class="book-card">
					<%
					if (coverUrl != null && !coverUrl.isBlank()) {
					%>
					<img class="book-cover" src="<%=coverUrl%>" alt="Book Cover">
					<%
					} else {
					%>
					<div class="no-cover">
						<i data-lucide="book-open"></i> <span>No Cover</span>
					</div>
					<%
					}
					%>

					<div class="book-info">
						<h3 class="book-title"><%=book.getTitle()%></h3>
						<div class="book-author">
							By
							<%=book.getAuthor()%></div>
						<div class="book-rating">
							<span class="star">★</span>
							<%=(averageRating != null) ? String.format("%.1f", averageRating) : "No rating"%>
							<%=(ratingCount != null && ratingCount > 0) ? "(" + ratingCount + ")" : ""%>
						</div>
						<div class="book-description">
							<%
							String description = book.getDescription();
							if (description != null && !description.isBlank()) {
								out.print(description.length() > 100 ? description.substring(0, 100) + "..." : description);
							} else {
								out.print("No description available.");
							}
							%>
						</div>
						<a class="read-button"
							href="${pageContext.request.contextPath}/books/details?id=<%= bookId %>">
							<i data-lucide="book-open"></i> Read More
						</a>
					</div>
				</div>
				<%
				}
				%>
			</div>
			<%
			} else {
			%>
			<p class="recently-read-empty">You haven't read any books yet.</p>
			<%
			}
			%>
		</section>

		<!-- TOP RATED CATEGORIES -->
		<%
		List<Category> topCategories = (List<Category>) request.getAttribute("topCategories");
		Map<Long, List<Book>> categoryBooksMap = (Map<Long, List<Book>>) request.getAttribute("categoryBooksMap");

		if (topCategories != null && !topCategories.isEmpty()) {
			for (Category category : topCategories) {
				Long categoryId = category.getCategoryId();
				List<Book> books = (categoryBooksMap != null) ? categoryBooksMap.get(categoryId) : null;
		%>
		<section class="category-section">
			<div class="category-header">
				<h2><%=category.getCategoryName()%></h2>
				<a class="explore-button"
					href="${pageContext.request.contextPath}/books/category?id=<%= categoryId %>">
					Explore More <i data-lucide="arrow-right"></i>
				</a>
			</div>

			<%
			if (books != null && !books.isEmpty()) {
			%>
			<div class="book-row">
				<%
				for (Book book : books) {
					Long bookId = book.getBookId();
					Double averageRating = (bookAverageRatingMap != null) ? bookAverageRatingMap.get(bookId) : null;
					Integer ratingCount = (bookRatingCountMap != null) ? bookRatingCountMap.get(bookId) : null;
					String coverUrl = (bookCoverUrlMap != null) ? bookCoverUrlMap.get(bookId) : null;
				%>
				<div class="book-card">
					<%
					if (coverUrl != null && !coverUrl.isBlank()) {
					%>
					<img class="book-cover" src="<%=coverUrl%>" alt="Book Cover">
					<%
					} else {
					%>
					<div class="no-cover">
						<i data-lucide="book-open"></i> <span>No Cover</span>
					</div>
					<%
					}
					%>

					<div class="book-info">
						<h3 class="book-title"><%=book.getTitle()%></h3>
						<div class="book-author">
							By
							<%=book.getAuthor()%></div>
						<div class="book-rating">
							<span class="star">★</span>
							<%=(averageRating != null) ? String.format("%.1f", averageRating) : "No rating"%>
							<%=(ratingCount != null && ratingCount > 0) ? "(" + ratingCount + ")" : ""%>
						</div>
						<div class="book-description">
							<%
							String description = book.getDescription();
							if (description != null && !description.isBlank()) {
								out.print(description.length() > 100 ? description.substring(0, 100) + "..." : description);
							} else {
								out.print("No description available.");
							}
							%>
						</div>
						<a class="read-button"
							href="${pageContext.request.contextPath}/books/details?id=<%= bookId %>">
							<i data-lucide="book-open"></i> Read More
						</a>
					</div>
				</div>
				<%
				}
				%>
			</div>
			<%
			} else {
			%>
			<p class="empty-message">No books available in this category.</p>
			<%
			}
			%>
		</section>
		<%
		}
		} else {
		%>
		<p class="empty-message">No highly rated categories are available
			yet.</p>
		<%
		}
		%>

	</div>

	<!-- FOOTER -->
	<footer class="site-footer">

		<div class="footer-container">

			<div class="footer-brand">
				<div class="footer-brand-title">
					<i data-lucide="book-open"></i>
					<h3>E-Library</h3>
				</div>
				<p>Read online or download as PDF — anytime, anywhere.</p>
			</div>

			<div class="footer-links">
				<h4>Quick Links</h4>
				<a href="${pageContext.request.contextPath}/user/dashboard">
					<i data-lucide="layout-dashboard"></i> Dashboard
				</a>
				<a href="${pageContext.request.contextPath}/books">
					<i data-lucide="library"></i> Categories
				</a>
				<a href="${pageContext.request.contextPath}/books/bookmark-book">
					<i data-lucide="bookmark"></i> Bookmarks
				</a>
				<a href="${pageContext.request.contextPath}/books/history">
					<i data-lucide="history"></i> History
				</a>
			</div>

			<div class="footer-links">
				<h4>Support</h4>
				<a href="${pageContext.request.contextPath}/user/suggestion">
					<i data-lucide="message-square-plus"></i> Suggestions
				</a>
				<a href="${pageContext.request.contextPath}/book-request">
					<i data-lucide="book-plus"></i> Book Requests
				</a>
				<a href="${pageContext.request.contextPath}/profile">
					<i data-lucide="user"></i> Profile
				</a>
			</div>

			<div class="footer-contact">
				<h4>Contact</h4>
				<span><i data-lucide="mail"></i> support@elibrary.com</span>
				<span><i data-lucide="map-pin"></i> Kolkata, India</span>
			</div>

		</div>

		<div class="footer-bottom">
			<span>&copy; <%= java.time.Year.now() %> E-Library. All rights reserved.</span>
			<span class="footer-made-with"><i data-lucide="heart"></i> Made for readers</span>
		</div>

	</footer>

	</div>

	<!-- Custom JS Script -->
	<script
		src="${pageContext.request.contextPath}/javascript/dashboard.js"></script>
</body>
</html>
