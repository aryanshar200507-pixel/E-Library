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
<title>User Dashboard - E-Library</title>

<!-- Custom CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/dashboard.css">

<!-- Lucide Icons CDN -->
<script src="https://unpkg.com/lucide@latest"></script>
</head>
<body>

	<!-- HEADER -->
	<header class="header">
		<h2>E-Library</h2>

		<nav class="nav">

			<a href="${pageContext.request.contextPath}/user/dashboard"> <i
				data-lucide="layout-dashboard"></i> Dashboard
			</a> <a href="${pageContext.request.contextPath}/books"> <i
				data-lucide="library"></i> Categories
			</a> <a href="${pageContext.request.contextPath}/book-request"> <i
				data-lucide="book-plus"></i> Book Requests
			</a> <a href="${pageContext.request.contextPath}/books/bookmark-book">
				<i data-lucide="bookmark"></i> Bookmarks
			</a>

			<!-- Suggestions -->
			<a href="${pageContext.request.contextPath}/user/suggestion"
				class="nav-link"> <i data-lucide="message-square-plus"></i> <span>Suggestions</span>
			</a> <a href="${pageContext.request.contextPath}/books/history"
				class="nav-link"> <i data-lucide="history"></i> <span>History</span>
			</a> <a href="${pageContext.request.contextPath}/logout"> <i
				data-lucide="log-out"></i> Logout
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
		Map<Long, String> bookCoverUrlMap = (Map<Long, String>) request.getAttribute("bookCoverUrlMap");
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


		<!-- RECOMMENDED BOOKS -->
		<%
		List<Book> recommendedBooks = (List<Book>) request.getAttribute("recommendedBooks");

		Map<Long, String> coverMap = (Map<Long, String>) request.getAttribute("bookCoverUrlMap");
		%>

		<%
		if (recommendedBooks != null && !recommendedBooks.isEmpty()) {
		%>

		<section class="recommended-section">

			<div class="recommended-header">
				<h2>Recommended Books</h2>
			</div>

			<div class="book-row">

				<%
				for (Book book : recommendedBooks) {

					Long bookId = book.getBookId();
					String coverUrl = coverMap.get(bookId);
				%>

				<div class="book-card ">

					<%
					if (coverUrl != null && !coverUrl.isBlank()) {
					%>

					<img src="<%=coverUrl%>" alt="<%=book.getTitle()%>"
						class="book-cover">

					<%
					} else {
					%>

					<div class="no-cover">
						<span>No Cover</span>
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
							<%=book.getDescription() != null ? book.getDescription() : "No description available."%>
						</div>

						<a
							href="${pageContext.request.contextPath}/books/details?id=<%= bookId %>"
							class="read-button"> Read Now </a>

					</div>

				</div>

				<%
				}
				%>


			</div>
		</section>

	</div>
	<%
	}
	%>
	<!-- Custom JS Script -->
	<script
		src="${pageContext.request.contextPath}/javascript/dashboard.js"></script>
</body>
</html>