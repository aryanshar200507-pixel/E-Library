<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.project.elibrary.bean.book.Book"%>

<%
List<Book> books = (List<Book>) request.getAttribute("books");

Map<Long, String> coverUrlMap = (Map<Long, String>) request.getAttribute("coverUrlMap");

Integer currentPage = (Integer) request.getAttribute("currentPage");

Integer totalPages = (Integer) request.getAttribute("totalPages");

Integer totalBooks = (Integer) request.getAttribute("totalBooks");

if (books == null) {
	books = List.of();
}

if (coverUrlMap == null) {
	coverUrlMap = Map.of();
}

if (currentPage == null) {
	currentPage = 1;
}

if (totalPages == null) {
	totalPages = 1;
}

if (totalBooks == null) {
	totalBooks = 0;
}
%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Reading History - E-Library</title>

<!-- History CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/history.css">

<!-- Lucide Icons -->
<script src="https://unpkg.com/lucide@latest"></script>

</head>

<body>

	<!-- ================= HEADER ================= -->

	<header class="main-header">

		<div class="header-left">

			<a href="${pageContext.request.contextPath}/user/dashboard"
				class="logo"> <i data-lucide="library"></i> <span>E-Library</span>

			</a>

		</div>


		<nav class="header-nav">

			<a href="${pageContext.request.contextPath}/user/dashboard"
				class="nav-link"> <i data-lucide="layout-dashboard"></i> <span>Dashboard</span>

			</a> <a href="${pageContext.request.contextPath}/books" class="nav-link">

				<i data-lucide="book-open"></i> <span>Browse Books</span>

			</a> <a href="${pageContext.request.contextPath}/books/bookmark-book"
				class="nav-link"> <i data-lucide="bookmark"></i> <span>Bookmarks</span>

			</a> <a href="${pageContext.request.contextPath}/books/history"
				class="nav-link active"> <i data-lucide="history"></i> <span>History</span>

			</a>

		</nav>

	</header>


	<!-- ================= MAIN ================= -->

	<main class="history-container">

		<!-- Page Heading -->

		<section class="page-heading">

			<div class="heading-content">

				<div class="heading-icon">

					<i data-lucide="history"></i>

				</div>

				<div>

					<h1>Reading History</h1>

					<p>Books you have recently read</p>

				</div>

			</div>

			<div class="book-count">

				<i data-lucide="book-open"></i> <span> <%=totalBooks%> <%=totalBooks == 1 ? "Book" : "Books"%>
				</span>

			</div>

		</section>


		<!-- ================= BOOK LIST ================= -->

		<section class="history-section">

			<%
			if (books.isEmpty()) {
			%>

			<!-- Empty State -->

			<div class="empty-state">

				<div class="empty-icon">

					<i data-lucide="history"></i>

				</div>

				<h2>No Reading History</h2>

				<p>Books you start reading will appear here.</p>

				<a href="${pageContext.request.contextPath}/books"
					class="browse-button"> <i data-lucide="book-open"></i> Browse
					Books

				</a>

			</div>

			<%
			} else {
			%>

			<div class="history-grid">

				<%
				for (Book book : books) {

					String coverUrl = coverUrlMap.get(book.getBookId());

					String description = book.getDescription();

					if (description == null) {
						description = "";
					}

					if (description.length() > 140) {
						description = description.substring(0, 140) + "...";
					}
				%>

				<article class="history-card">

					<!-- Book Cover -->

					<div class="book-cover-container">

						<%
						if (coverUrl != null && !coverUrl.isBlank()) {
						%>

						<img src="<%=coverUrl%>" alt="Cover of <%=book.getTitle()%>"
							class="book-cover">

						<%
						} else {
						%>

						<div class="cover-placeholder">

							<i data-lucide="book-open"></i>

						</div>

						<%
						}
						%>

					</div>


					<!-- Book Information -->

					<div class="book-info">

						<h2 class="book-title">

							<%=book.getTitle()%>

						</h2>


						<p class="book-author">

							<i data-lucide="user"></i> <span> <%=book.getAuthor()%>
							</span>

						</p>


						<%
						if (!description.isBlank()) {
						%>

						<p class="book-description">

							<%=description%>

						</p>

						<%
						}
						%>


						<!-- Continue Reading -->

						<a
							href="${pageContext.request.contextPath}/books/read?id=<%= book.getBookId() %>"
							class="continue-button"> <i data-lucide="book-open"></i>

							Continue Reading

						</a>

					</div>

				</article>

				<%
				}
				%>

			</div>


			<!-- ================= PAGINATION ================= -->

			<%
			if (totalPages > 1) {
			%>

			<nav class="pagination" aria-label="Reading history pagination">

				<!-- Previous -->

				<%
				if (currentPage > 1) {
				%>

				<a
					href="${pageContext.request.contextPath}/books/history?page=<%= currentPage - 1 %>"
					class="pagination-button"> <i data-lucide="chevron-left"></i> <span>Previous</span>

				</a>

				<%
				} else {
				%>

				<span class="pagination-button disabled"> <i
					data-lucide="chevron-left"></i> <span>Previous</span>

				</span>

				<%
				}
				%>


				<!-- Page Numbers -->

				<div class="page-numbers">

					<%
					for (int pageNumber = 1; pageNumber <= totalPages; pageNumber++) {
					%>

					<%
					if (pageNumber == currentPage) {
					%>

					<span class="page-number active"> <%=pageNumber%>
					</span>

					<%
					} else {
					%>

					<a
						href="${pageContext.request.contextPath}/books/history?page=<%= pageNumber %>"
						class="page-number"> <%=pageNumber%>

					</a>

					<%
					}
					%>

					<%
					}
					%>

				</div>


				<!-- Next -->

				<%
				if (currentPage < totalPages) {
				%>

				<a
					href="${pageContext.request.contextPath}/books/history?page=<%= currentPage + 1 %>"
					class="pagination-button"> <span>Next</span> <i
					data-lucide="chevron-right"></i>

				</a>

				<%
				} else {
				%>

				<span class="pagination-button disabled"> <span>Next</span> <i
					data-lucide="chevron-right"></i>

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


	<!-- History JavaScript -->
	<script src="${pageContext.request.contextPath}/javascript/history.js"></script>

</body>

</html>