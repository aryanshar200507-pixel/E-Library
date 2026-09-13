<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<%@ page import="com.project.elibrary.bean.book.Book"%>
<%@ page import="com.project.elibrary.bean.category.Category"%>
<%@ page import="com.project.elibrary.bean.user.User"%>
<%@ page import="com.project.elibrary.bean.enums.Role"%>


<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Category Books - E-Library</title>


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
	padding: 20px;
	background: white;
	border-bottom: 1px solid #ccc;
}

header h1 {
	margin: 0 0 15px 0;
}

header a {
	margin-right: 20px;
	text-decoration: none;
}


/* ================= CONTAINER ================= */

.container {
	max-width: 1100px;
	margin: 30px auto;
	padding: 30px;
	background: white;
}


/* ================= CATEGORY SEARCH ================= */

.category-search {
	display: flex;
	gap: 10px;
	margin: 20px 0 10px 0;
}

.category-search input[type="text"] {
	flex: 1;
	padding: 10px;
	font-size: 16px;
	border: 1px solid #ccc;
}

.category-search button {
	padding: 10px 18px;
	border: 1px solid #333;
	background: white;
	cursor: pointer;
}

.category-search a {
	display: inline-block;
	padding: 10px 18px;
	border: 1px solid #ccc;
	text-decoration: none;
}


/* ================= BOOK ================= */

.book {
	display: flex;
	gap: 25px;
	padding: 25px 0;
	border-bottom: 1px solid #ddd;
}


/* ================= COVER ================= */

.book-cover-container {
	width: 160px;
	flex-shrink: 0;
}

.book-cover {
	width: 160px;
	height: 230px;
	object-fit: cover;
	border: 1px solid #ccc;
}


/* ================= BOOK INFORMATION ================= */

.book-information {
	flex: 1;
}

.book-information h2 {
	margin-top: 0;
	margin-bottom: 10px;
}

.book-info {
	margin-bottom: 8px;
}


/* ================= DESCRIPTION ================= */

.description {
	margin-top: 10px;
	line-height: 1.5;
}


/* ================= BOOK ACTIONS ================= */

.book-actions {
	margin-top: 15px;
}


/* ================= READ MORE ================= */

.read-more {
	display: inline-block;
	padding: 9px 16px;
	text-decoration: none;
	border: 1px solid #333;
	margin-right: 10px;
}


/* ================= BOOKMARK ================= */

.bookmark-button {
	display: inline-block;
	padding: 9px 16px;
	border: 1px solid #333;
	background: white;
	cursor: pointer;
	font-size: 14px;
}

.bookmark-button.bookmarked {
	background: #333;
	color: white;
}


/* ================= PAGINATION ================= */

.pagination {
	margin-top: 30px;
	text-align: center;
}

.pagination a {
	display: inline-block;
	margin: 5px;
	padding: 8px 14px;
	border: 1px solid #ccc;
	text-decoration: none;
}

.pagination .current {
	display: inline-block;
	margin: 5px;
	padding: 8px 14px;
	background: #ddd;
}


/* ================= RESPONSIVE ================= */

@media (max-width: 700px) {

	.book {
		flex-direction: column;
	}

	.book-cover-container {
		width: 100%;
	}

	.book-cover {
		display: block;
		margin: auto;
	}

	.category-search {
		flex-direction: column;
	}

}

</style>

</head>


<body>


<%

/* ============================================
 * GET DATA FROM SERVLET
 * ============================================ */


/* Selected category */

Category category =
		(Category) request.getAttribute("category");


/* Books belonging to this category */

List<Book> books =
		(List<Book>) request.getAttribute("books");


/* Cover URL map */

Map<Long, String> coverUrlMap =
		(Map<Long, String>) request.getAttribute("coverUrlMap");


/* Average rating map */

Map<Long, Double> averageRatingMap =
		(Map<Long, Double>) request.getAttribute("averageRatingMap");


/* Rating count map */

Map<Long, Integer> ratingCountMap =
		(Map<Long, Integer>) request.getAttribute("ratingCountMap");


/* Pagination */

Integer currentPage =
		(Integer) request.getAttribute("currentPage");

Integer totalPages =
		(Integer) request.getAttribute("totalPages");


/* Search keyword */

String keyword =
		(String) request.getAttribute("keyword");

if (keyword == null) {
	keyword = "";
}


/* Logged-in user */

User loggedInUser =
		(User) session.getAttribute("loggedInUser");


/* Only normal USER can bookmark books */

boolean isUser =
		loggedInUser != null &&
		loggedInUser.getRole() == Role.USER;

%>


<!-- ================= HEADER ================= -->

<header>

	<h1>E-Library</h1>

	<!-- Back to categories -->

	<a href="${pageContext.request.contextPath}/books">
		Categories
	</a>

</header>


<!-- ================= CONTENT ================= -->

<div class="container">


	<h1>
		<%=category.getCategoryName()%>
	</h1>


	<!-- ================= CATEGORY SEARCH ================= -->

	<form method="get"
		  action="${pageContext.request.contextPath}/books/category"
		  class="category-search">


		<!-- Preserve category -->

		<input type="hidden"
			   name="id"
			   value="<%=category.getCategoryId()%>">


		<!-- Search keyword -->

		<input type="text"
			   name="keyword"
			   value="<%=keyword%>"
			   placeholder="Search books in this category...">


		<button type="submit">
			Search
		</button>


		<!-- Clear search -->

		<%

		if (!keyword.isBlank()) {

		%>

			<a href="${pageContext.request.contextPath}/books/category?id=<%=category.getCategoryId()%>">
				Clear
			</a>

		<%

		}

		%>


	</form>


	<%

	/* ============================================
	 * DISPLAY BOOKS
	 * ============================================ */

	if (books == null || books.isEmpty()) {

	%>


		<%

		if (!keyword.isBlank()) {

		%>

			<p>
				No books found matching
				"<%=keyword%>"
				in this category.
			</p>

		<%

		} else {

		%>

			<p>
				No books available in this category.
			</p>

		<%

		}

		%>


	<%

	} else {


	/* Display each book */

	for (Book book : books) {

	%>


		<!-- ================= BOOK ================= -->

		<div class="book">


			<!-- ================= COVER ================= -->

			<div class="book-cover-container">


				<%

				String coverUrl =
						coverUrlMap.get(book.getBookId());


				if (coverUrl != null && !coverUrl.isBlank()) {

				%>


					<img
						class="book-cover"
						src="<%=coverUrl%>"
						alt="<%=book.getTitle()%> cover">


				<%

				} else {

				%>


					<p>
						No cover available.
					</p>


				<%

				}

				%>


			</div>


			<!-- ================= BOOK INFORMATION ================= -->

			<div class="book-information">


				<!-- TITLE -->

				<h2>
					<%=book.getTitle()%>
				</h2>


				<!-- AUTHOR -->

				<div class="book-info">

					<strong>Author:</strong>

					<%=book.getAuthor()%>

				</div>


				<!-- RATING -->

				<div class="book-info">

					<strong>Rating:</strong>


					<%

					Double averageRating =
							averageRatingMap.get(book.getBookId());

					Integer ratingCount =
							ratingCountMap.get(book.getBookId());


					if (ratingCount != null && ratingCount > 0) {

					%>


						⭐

						<%=String.format("%.1f", averageRating)%>

						/ 5

						(<%=ratingCount%> ratings)


					<%

					} else {

					%>


						No ratings yet


					<%

					}

					%>


				</div>


				<!-- DESCRIPTION -->

				<div class="description">

					<strong>Description:</strong>


					<%

					String description =
							book.getDescription();


					if (description == null ||
							description.isBlank()) {

					%>


						No description available.


					<%

					} else {


						if (description.length() > 250) {

							description =
									description.substring(0, 250) + "...";

						}

					%>


						<%=description%>


					<%

					}

					%>


				</div>


				<!-- ================= BOOK ACTIONS ================= -->

				<div class="book-actions">


					<%

					/* Only USER can bookmark */

					if (isUser) {

					%>


						<button
							type="button"
							class="bookmark-button"
							data-book-id="<%=book.getBookId()%>"
							onclick="toggleBookBookmark(this)">

							🔖 Bookmark

						</button>


					<%

					}

					%>


					<!-- READ MORE -->

					<a
						class="read-more"
						href="${pageContext.request.contextPath}/books/details?id=<%=book.getBookId()%>">

						Read More

					</a>


				</div>


			</div>


		</div>


	<%

	}

	}

	%>


	<!-- ================= PAGINATION ================= -->

	<%

	if (totalPages != null && totalPages >= 1) {

	%>


		<div class="pagination">


			<!-- ================= PREVIOUS ================= -->

			<%

			if (currentPage > 1) {

			%>


				<a
					href="${pageContext.request.contextPath}/books/category?id=<%=category.getCategoryId()%>&keyword=<%=java.net.URLEncoder.encode(keyword, "UTF-8")%>&page=<%=currentPage - 1%>">

					Previous

				</a>


			<%

			}

			%>


			<!-- ================= PAGE NUMBERS ================= -->

			<%

			for (
				int pageNumber = 1;
				pageNumber <= totalPages;
				pageNumber++
			) {


				if (pageNumber == currentPage) {

			%>


					<span class="current">
						<%=pageNumber%>
					</span>


			<%

				} else {

			%>


					<a
						href="${pageContext.request.contextPath}/books/category?id=<%=category.getCategoryId()%>&keyword=<%=java.net.URLEncoder.encode(keyword, "UTF-8")%>&page=<%=pageNumber%>">

						<%=pageNumber%>

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
					href="${pageContext.request.contextPath}/books/category?id=<%=category.getCategoryId()%>&keyword=<%=java.net.URLEncoder.encode(keyword, "UTF-8")%>&page=<%=currentPage + 1%>">

					Next

				</a>


			<%

			}

			%>


		</div>


	<%

	}

	%>


</div>


<!-- =====================================================
     JAVASCRIPT
     ===================================================== -->

<script>

/* =====================================================
 * TOGGLE BOOK BOOKMARK
 * ===================================================== */

function toggleBookBookmark(button) {

	const bookId =
			button.dataset.bookId;


	const formData =
			new URLSearchParams();


	formData.append(
			"bookId",
			bookId
	);


	fetch(
			"${pageContext.request.contextPath}/books/bookmark-book",
			{
				method: "POST",

				headers: {
					"Content-Type":
						"application/x-www-form-urlencoded"
				},

				body:
					formData.toString()
			}
	)
	.then(function(response) {

		if (!response.ok) {

			throw new Error(
					"Failed to update bookmark."
			);

		}

		return response.text();

	})
	.then(function(action) {

		if (action === "added") {

			button.textContent =
					"🔖 Bookmarked";

			button.classList.add(
					"bookmarked"
			);

		}

		else if (action === "removed") {

			button.textContent =
					"🔖 Bookmark";

			button.classList.remove(
					"bookmarked"
			);

		}

	})
	.catch(function(error) {

		console.error(error);

		alert(
				"Unable to update bookmark."
		);

	});
}


/* =====================================================
 * LOAD CURRENT BOOKMARK STATUS
 * ===================================================== */

document.addEventListener(
		"DOMContentLoaded",
		function() {

			const buttons =
					document.querySelectorAll(
							".bookmark-button"
					);


			buttons.forEach(
					function(button) {

						const bookId =
								button.dataset.bookId;


						fetch(
								"${pageContext.request.contextPath}/books/bookmark-book?bookId="
								+ encodeURIComponent(bookId)
						)
						.then(function(response) {

							if (!response.ok) {

								throw new Error(
										"Failed to check bookmark."
								);

							}

							return response.text();

						})
						.then(function(result) {

							if (result === "true") {

								button.textContent =
										"🔖 Bookmarked";

								button.classList.add(
										"bookmarked"
								);

							}

						})
						.catch(function(error) {

							console.error(error);

						});

					}
			);

		}
);

</script>


</body>

</html>