<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<%@ page import="com.project.elibrary.bean.book.Book"%>
<%@ page import="com.project.elibrary.bean.user.User"%>
<%@ page import="com.project.elibrary.bean.enums.Role"%>

<%
List<Book> books = (List<Book>) request.getAttribute("books");

Map<Long, String> coverUrlMap = (Map<Long, String>) request.getAttribute("coverUrlMap");

Integer currentPageObj = (Integer) request.getAttribute("currentPage");

Integer totalPagesObj = (Integer) request.getAttribute("totalPages");

int currentPage = currentPageObj != null ? currentPageObj : 1;

int totalPages = totalPagesObj != null ? totalPagesObj : 1;

User loggedInUser = (User) session.getAttribute("loggedInUser");

boolean isUser = loggedInUser != null && loggedInUser.getRole() == Role.USER;
%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Bookmarked Books - E-Library</title>

<!-- Lucide Icons -->
<script src="https://unpkg.com/lucide@latest"></script>

<!-- Page CSS -->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/bookmarked-books.css">

</head>

<body>

	<!-- ================= HEADER ================= -->

	<header class="page-header">

		<div class="header-left">

			<a href="<%=request.getContextPath()%>/user/dashboard"
				class="back-button"> <i data-lucide="arrow-left"></i> <span>Dashboard</span>

			</a>

		</div>

		<div class="header-title">

			<i data-lucide="bookmark"></i>

			<h1>Bookmarked Books</h1>

		</div>

		<div class="header-right">

			<a href="<%=request.getContextPath()%>/books" class="browse-button">

				<i data-lucide="library"></i> <span>Browse Books</span>

			</a>

		</div>

	</header>


	<!-- ================= MAIN CONTENT ================= -->

	<main class="bookmark-container">

		<div class="page-introduction">

			<h2>Your Bookmarks</h2>

			<p>Books you have saved for later reading.</p>

		</div>


		<%
		if (books == null || books.isEmpty()) {
		%>

		<!-- ================= EMPTY STATE ================= -->

		<section class="empty-state">

			<div class="empty-icon">

				<i data-lucide="bookmark-x"></i>

			</div>

			<h2>No Bookmarked Books</h2>

			<p>You haven't bookmarked any books yet.</p>

			<a href="<%=request.getContextPath()%>/books"
				class="browse-books-button"> <i data-lucide="library"></i> <span>Browse
					Books</span>

			</a>

		</section>

		<%
		} else {
		%>


		<!-- ================= BOOK GRID ================= -->

		<section class="book-grid">

			<%
			for (Book book : books) {

				String coverUrl = null;

				if (coverUrlMap != null) {
					coverUrl = coverUrlMap.get(book.getBookId());
				}
			%>

			<article class="book-card" data-book-id="<%=book.getBookId()%>">


				<!-- BOOK COVER -->

				<div class="book-cover">

					<%
					if (coverUrl != null && !coverUrl.isBlank()) {
					%>

					<img src="<%=coverUrl%>" alt="Cover of <%=book.getTitle()%>"
						loading="lazy">

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


				<!-- BOOK INFORMATION -->

				<div class="book-information">

					<h3 class="book-title" title="<%=book.getTitle()%>">

						<%=book.getTitle()%>

					</h3>


					<p class="book-author">

						<i data-lucide="user"></i> <span> <%=book.getAuthor()%>
						</span>

					</p>


					<%
					if (book.getDescription() != null && !book.getDescription().isBlank()) {
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
							class="read-button"> <i data-lucide="book-open"></i> <span>Read
								More</span>

						</a>


						<%
						if (isUser) {
						%>

						<button type="button" class="remove-bookmark-button"
							onclick="removeBookBookmark(this, <%=book.getBookId()%>)">

							<i data-lucide="bookmark-minus"></i> <span>Remove</span>

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

		</section>


		<!-- ================= PAGINATION ================= -->

		<%
		if (totalPages > 1) {
		%>

		<nav class="pagination" aria-label="Bookmarked books pagination">

			<!-- PREVIOUS -->

			<%
			if (currentPage > 1) {
			%>

			<a
				href="<%=request.getContextPath()%>/books/bookmark-book?page=<%=currentPage - 1%>"
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


			<!-- PAGE NUMBERS -->

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
					href="<%=request.getContextPath()%>/books/bookmark-book?page=<%=pageNumber%>"
					class="page-number"> <%=pageNumber%>

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

	</main>


	<!-- Page JavaScript -->
	<script
		src="<%=request.getContextPath()%>/javascript/bookmarked-books.js"></script>

</body>

</html>