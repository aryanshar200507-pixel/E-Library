<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.project.elibrary.bean.category.Category"%>
<%@ page import="com.project.elibrary.bean.book.Book"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Books - E-Library</title>

<style>

/* ================= BODY ================= */
body {
	font-family: Arial, sans-serif;
	margin: 0;
	padding: 0;
	background: #f5f5f5;
}

/* ================= HEADER ================= */
header {
	padding: 20px 40px;
	background: white;
	border-bottom: 1px solid #ddd;
}

header h1 {
	margin: 0 0 15px 0;
}

header a {
	margin-right: 20px;
	text-decoration: none;
	color: #333;
}

/* ================= SEARCH ================= */
.search-container {
	margin-top: 20px;
}

.search-form {
	display: flex;
	max-width: 600px;
}

.search-form input {
	flex: 1;
	padding: 12px;
	border: 1px solid #ccc;
	border-radius: 6px 0 0 6px;
	font-size: 15px;
}

.search-form button {
	padding: 12px 20px;
	border: none;
	background: #333;
	color: white;
	border-radius: 0 6px 6px 0;
	cursor: pointer;
}

.search-form button:hover {
	background: #555;
}

/* ================= CONTAINER ================= */
.container {
	padding: 40px;
}

.container h2 {
	margin-bottom: 30px;
}

/* ================= CATEGORY GRID ================= */
.category-grid {
	display: grid;
	grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
	gap: 25px;
}

/* ================= CATEGORY CARD ================= */
.category-card {
	background: white;
	border: 1px solid #ddd;
	border-radius: 10px;
	overflow: hidden;
	text-decoration: none;
	color: #222;
	transition: transform 0.2s, box-shadow 0.2s;
}

.category-card:hover {
	transform: translateY(-5px);
	box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
}

/* ================= CATEGORY COVER ================= */
.category-cover {
	width: 100%;
	height: 280px;
	object-fit: cover;
	display: block;
}

/* ================= CATEGORY NAME ================= */
.category-name {
	padding: 18px;
	text-align: center;
}

.category-name h3 {
	margin: 0;
	font-size: 20px;
}

/* ================= NO COVER ================= */
.no-cover {
	height: 280px;
	display: flex;
	align-items: center;
	justify-content: center;
	background: #eee;
	color: #777;
}

/* ================= BOOK GRID ================= */
.book-grid {
	display: grid;
	grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
	gap: 25px;
}

/* ================= BOOK CARD ================= */
.book-card {
	background: white;
	border: 1px solid #ddd;
	border-radius: 10px;
	overflow: hidden;
}

.book-cover {
	width: 100%;
	height: 280px;
	object-fit: cover;
	display: block;
}

.book-info {
	padding: 18px;
}

.book-info h3 {
	margin: 0 0 8px 0;
}

.book-author {
	color: #666;
	margin-bottom: 10px;
}

.book-description {
	color: #555;
	font-size: 14px;
	line-height: 1.5;
}

/* ================= RATING ================= */
.rating {
	margin: 10px 0;
}

.stars {
	color: #f5b301;
	font-size: 18px;
}

.rating-count {
	color: #777;
	font-size: 13px;
}

/* ================= READ MORE ================= */
.read-button {
	display: inline-block;
	margin-top: 10px;
	padding: 9px 15px;
	background: #333;
	color: white;
	text-decoration: none;
	border-radius: 5px;
}

.read-button:hover {
	background: #555;
}

/* ================= ADMIN BUTTONS ================= */
.admin-actions {
	margin-top: 15px;
}

.admin-actions a {
	text-decoration: none;
	color: #333;
	margin-right: 10px;
}

.admin-actions a:hover {
	text-decoration: underline;
}

/* ================= PAGINATION ================= */
.pagination {
	margin-top: 40px;
	text-align: center;
}

.pagination a, .pagination span {
	display: inline-block;
	padding: 8px 12px;
	margin: 0 4px;
	border: 1px solid #ddd;
	text-decoration: none;
	border-radius: 5px;
}

.pagination a {
	background: white;
	color: #333;
}

.pagination .current {
	background: #333;
	color: white;
	border-color: #333;
}

/* ================= MOBILE ================= */
@media ( max-width : 600px) {
	header {
		padding: 20px;
	}
	.container {
		padding: 20px;
	}
	.search-form {
		width: 100%;
	}
}
</style>

</head>


<body>


	<%
	/*
	 * ============================================================
	 * GET SEARCH MODE
	 * ============================================================
	 *
	 * false = display categories
	 * true  = display book search results
	 */

	Boolean searchModeAttribute = (Boolean) request.getAttribute("searchMode");

	boolean searchMode = searchModeAttribute != null && searchModeAttribute;

	/*
	 * ============================================================
	 * GET LOGGED-IN USER
	 * ============================================================
	 */

	com.project.elibrary.bean.user.User loggedInUser = (com.project.elibrary.bean.user.User) session
			.getAttribute("loggedInUser");

	/*
	 * ============================================================
	 * CHECK ADMIN
	 * ============================================================
	 */

	boolean isAdmin = loggedInUser != null && loggedInUser.getRole() == com.project.elibrary.bean.enums.Role.ADMIN;
	%>


	<!-- ================= HEADER ================= -->

	<header>

		<h1>E-Library</h1>

		<a href="${pageContext.request.contextPath}/books"> Categories </a>


		<%
		if (isAdmin) {
		%>

		<a href="${pageContext.request.contextPath}/admin/books/add"> Add
			Book </a>

		<%
		}
		%>


		<!-- ================= SEARCH ================= -->

		<div class="search-container">

			<form class="search-form"
				action="${pageContext.request.contextPath}/books" method="get">

				<input type="text" name="keyword"
					placeholder="Search books by title or author..."
					value="<%=request.getAttribute("keyword") != null ? request.getAttribute("keyword") : ""%>">

				<button type="submit">Search</button>

			</form>

		</div>

	</header>



	<%
	/*
	 * ============================================================
	 * SEARCH MODE
	 * ============================================================
	 */

	if (searchMode) {

		List<Book> books = (List<Book>) request.getAttribute("books");

		Map<Long, Double> bookAverageRatingMap = (Map<Long, Double>) request.getAttribute("bookAverageRatingMap");

		Map<Long, Integer> bookRatingCountMap = (Map<Long, Integer>) request.getAttribute("bookRatingCountMap");

		Map<Long, String> bookCoverUrlMap = (Map<Long, String>) request.getAttribute("bookCoverUrlMap");

		String keyword = (String) request.getAttribute("keyword");
	%>


	<!-- ================= SEARCH RESULTS ================= -->

	<div class="container">

		<h2>
			Search Results for "<%=keyword%>"
		</h2>


		<%
		/*
		 * ========================================================
		 * NO SEARCH RESULTS
		 * ========================================================
		 */

		if (books == null || books.isEmpty()) {
		%>

		<p>
			No books found for "<%=keyword%>".
		</p>

		<%
		} else {
		%>


		<!-- ================= BOOK GRID ================= -->

		<div class="book-grid">


			<%
			/*
			 * ====================================================
			 * LOOP THROUGH SEARCH RESULTS
			 * ====================================================
			 */

			for (Book book : books) {

				Long bookId = book.getBookId();

				String coverUrl = bookCoverUrlMap.get(bookId);

				Double averageRating = bookAverageRatingMap.get(bookId);

				Integer ratingCount = bookRatingCountMap.get(bookId);
			%>


			<!-- ================= BOOK CARD ================= -->

			<div class="book-card">


				<!-- ================= COVER ================= -->

				<%
				if (coverUrl != null && !coverUrl.isBlank()) {
				%>

				<img class="book-cover" src="<%=coverUrl%>"
					alt="<%=book.getTitle()%>">

				<%
				} else {
				%>

				<div class="no-cover">No cover available</div>

				<%
				}
				%>


				<!-- ================= BOOK INFO ================= -->

				<div class="book-info">


					<h3>
						<%=book.getTitle()%>
					</h3>


					<div class="book-author">

						Author:
						<%=book.getAuthor()%>

					</div>


					<!-- ================= RATING ================= -->

					<div class="rating">

						<span class="stars"> <%
 if (averageRating != null) {

 	int roundedRating = (int) Math.round(averageRating);

 	for (int i = 1; i <= 5; i++) {

 		if (i <= roundedRating) {
 %> ★ <%
 } else {
 %> ☆ <%
 }

 }

 } else {
 %> ☆☆☆☆☆ <%
 }
 %>

						</span> <span class="rating-count"> <%
 if (averageRating != null) {
 %> <%=String.format("%.1f", averageRating)%> <%
 } else {
 %> No rating <%
 }
 %> ( <%=ratingCount != null ? ratingCount : 0%> )

						</span>

					</div>


					<!-- ================= DESCRIPTION ================= -->

					<div class="book-description">

						<%
						String description = book.getDescription();

						if (description != null && description.length() > 120) {
						%>

						<%=description.substring(0, 120)%>...

						<%
						} else {
						%>

						<%=description != null ? description : "No description available."%>

						<%
						}
						%>

					</div>


					<!-- ================= READ MORE ================= -->

					<a class="read-button"
						href="${pageContext.request.contextPath}/books/details?id=<%= bookId %>">
						Read More </a>


					<!-- ================= ADMIN ACTIONS ================= -->

					<%
					if (isAdmin) {
					%>

					<div class="admin-actions">

						<a
							href="${pageContext.request.contextPath}/admin/books/edit?id=<%= bookId %>">
							Edit </a> <a
							href="${pageContext.request.contextPath}/admin/books/delete?id=<%= bookId %>">
							Delete </a>

					</div>

					<%
					}
					%>


				</div>

			</div>


			<%
			/*
			 * Close for(Book book : books)
			 */

			}
			%>


		</div>


		<!-- ================= SEARCH PAGINATION ================= -->

		<%
		Integer currentPage = (Integer) request.getAttribute("currentPage");

		Integer totalPages = (Integer) request.getAttribute("totalPages");

		if (totalPages != null && totalPages >= 1) {
		%>


		<div class="pagination">


			<!-- ================= PREVIOUS ================= -->

			<%
			if (currentPage > 1) {
			%>

			<a
				href="${pageContext.request.contextPath}/books?keyword=<%= java.net.URLEncoder.encode(keyword, "UTF-8") %>&page=<%= currentPage - 1 %>">
				Previous </a>

			<%
			}
			%>


			<!-- ================= PAGE NUMBERS ================= -->

			<%
			for (int i = 1; i <= totalPages; i++) {

				if (i == currentPage) {
			%>

			<span class="current"> <%=i%>
			</span>

			<%
			} else {
			%>

			<a
				href="${pageContext.request.contextPath}/books?keyword=<%= java.net.URLEncoder.encode(keyword, "UTF-8") %>&page=<%= i %>">
				<%=i%>
			</a>

			<%
			}

			}
			%>


			<!-- ================= NEXT ================= -->

			<%
			if (currentPage < totalPages) {
			%>

			<a
				href="${pageContext.request.contextPath}/books?keyword=<%= java.net.URLEncoder.encode(keyword, "UTF-8") %>&page=<%= currentPage + 1 %>">
				Next </a>

			<%
			}
			%>


		</div>


		<%
		}
		%>


		<%
		/*
		 * IMPORTANT:
		 * This closes:
		 *
		 * if (books == null || books.isEmpty())
		 * else
		 */

		}
		%>


	</div>


	<%
	/*
	 * ============================================================
	 * CATEGORY MODE
	 * ============================================================
	 *
	 * This is the ELSE belonging to:
	 *
	 * if (searchMode)
	 */

	} else {

	List<Category> categories = (List<Category>) request.getAttribute("categories");

	Map<Long, Book> highestRatedBookMap = (Map<Long, Book>) request.getAttribute("highestRatedBookMap");

	Map<Long, String> categoryCoverUrlMap = (Map<Long, String>) request.getAttribute("categoryCoverUrlMap");
	%>


	<!-- ================= CATEGORIES ================= -->

	<div class="container">

		<h2>Browse Categories</h2>


		<%
		if (categories == null || categories.isEmpty()) {
		%>

		<p>No categories available.</p>

		<%
		} else {
		%>


		<!-- ================= CATEGORY GRID ================= -->

		<div class="category-grid">


			<%
			for (Category category : categories) {

				Long categoryId = category.getCategoryId();

				String coverUrl = categoryCoverUrlMap.get(categoryId);
			%>


			<!-- ================= CATEGORY CARD ================= -->

			<a class="category-card"
				href="${pageContext.request.contextPath}/books/category?id=<%= categoryId %>">


				<%
				if (coverUrl != null && !coverUrl.isBlank()) {
				%> <img class="category-cover" src="<%=coverUrl%>"
				alt="<%=category.getCategoryName()%>">

				<%
				} else {
				%>

				<div class="no-cover">No cover available</div> <%
 }
 %>


				<div class="category-name">

					<h3>
						<%=category.getCategoryName()%>
					</h3>

				</div>


			</a>


			<%
			}
			%>


		</div>


		<!-- ================= CATEGORY PAGINATION ================= -->

		<%
		Integer currentPage = (Integer) request.getAttribute("currentPage");

		Integer totalPages = (Integer) request.getAttribute("totalPages");

		if (totalPages != null && totalPages >= 1) {
		%>


		<div class="pagination">


			<!-- ================= PREVIOUS ================= -->

			<%
			if (currentPage > 1) {
			%>

			<a
				href="${pageContext.request.contextPath}/books?page=<%= currentPage - 1 %>">
				Previous </a>

			<%
			}
			%>


			<!-- ================= PAGE NUMBERS ================= -->

			<%
			for (int i = 1; i <= totalPages; i++) {

				if (i == currentPage) {
			%>

			<span class="current"> <%=i%>
			</span>

			<%
			} else {
			%>

			<a href="${pageContext.request.contextPath}/books?page=<%= i %>">
				<%=i%>
			</a>

			<%
			}

			}
			%>


			<!-- ================= NEXT ================= -->

			<%
			if (currentPage < totalPages) {
			%>

			<a
				href="${pageContext.request.contextPath}/books?page=<%= currentPage + 1 %>">
				Next </a>

			<%
			}
			%>


		</div>


		<%
		}
		%>


		<%
		/*
		 * Close:
		 *
		 * if (categories == null || categories.isEmpty())
		 * else
		 */

		}
		%>


	</div>


	<%
	/*
	 * Close:
	 *
	 * if (searchMode)
	 * else
	 */

	}
	%>


</body>

</html>