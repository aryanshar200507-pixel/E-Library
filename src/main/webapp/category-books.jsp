<%@ page language="java"
	contentType="text/html; charset=UTF-8"
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

	<meta name="viewport"
		  content="width=device-width, initial-scale=1.0">

	<title>Category Books - E-Library</title>


	<!-- =================================================
	     CSS
	     ================================================= -->

	<link rel="stylesheet"
		  href="${pageContext.request.contextPath}/css/category-books.css">


	<!-- =================================================
	     LUCIDE ICONS
	     ================================================= -->

	<script src="https://unpkg.com/lucide@latest"></script>

</head>


<body>


<%
/* =====================================================
   GET DATA FROM SERVLET
   ===================================================== */


/* Selected category */

Category category =
		(Category) request.getAttribute("category");


/* Books */

List<Book> books =
		(List<Book>) request.getAttribute("books");


/* Cover URL map */

Map<Long, String> coverUrlMap =
		(Map<Long, String>) request.getAttribute("coverUrlMap");


/* Average rating map */

Map<Long, Double> averageRatingMap =
		(Map<Long, Double>)
		request.getAttribute("averageRatingMap");


/* Rating count map */

Map<Long, Integer> ratingCountMap =
		(Map<Long, Integer>)
		request.getAttribute("ratingCountMap");


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


/*
 * Encode keyword for HTML data attribute.
 *
 * JavaScript will decode it again.
 *
 * This prevents quotes and special characters
 * in the search keyword from breaking the HTML.
 */

String encodedKeyword =
		java.net.URLEncoder.encode(
			keyword,
			"UTF-8"
		);


/* Logged-in user */

User loggedInUser =
		(User) session.getAttribute("loggedInUser");


/* Only normal USER can bookmark */

boolean isUser =
		loggedInUser != null &&
		loggedInUser.getRole() == Role.USER;


/* Safe pagination defaults */

int safeCurrentPage =
		currentPage != null
			? currentPage
			: 1;


int safeTotalPages =
		totalPages != null
			? totalPages
			: 1;

%>



<!-- =====================================================
     HEADER
     ===================================================== -->

<header class="site-header">


	<div class="header-inner">


		<!-- ================= BRAND ================= -->

		<div class="brand">


			<div class="brand-icon">

				<i data-lucide="library"></i>

			</div>


			<div class="brand-text">

				<h1>Stories</h1>

				<span>E-Library</span>

			</div>


		</div>



		<!-- ================= BACK ================= -->

		<a
			class="back-link"
			href="${pageContext.request.contextPath}/books">

			<i data-lucide="arrow-left"></i>

			<span>Categories</span>

		</a>
		
		<a href="${pageContext.request.contextPath}/user/dashboard"> <i
				data-lucide="layout-dashboard"></i> Dashboard
			</a> 
			
			<a href="${pageContext.request.contextPath}/books/bookmark-book">
				<i data-lucide="bookmark"></i> Bookmarks
			</a>
			
			<a href="${pageContext.request.contextPath}/books/history"
				class="nav-link"> <i data-lucide="history"></i> <span>History</span>
			</a> 


	</div>

</header>



<!-- =====================================================
     MAIN CONTENT
     ===================================================== -->

<main class="container">


	<!-- =================================================
	     CATEGORY HEADER
	     ================================================= -->

	<section class="category-header">


		<div>


			<span class="eyebrow">

				<i data-lucide="layers"></i>

				Category

			</span>


			<h2>

				<%= category.getCategoryName() %>

			</h2>


			<p>

				Explore books available in this category.

			</p>


		</div>


	</section>



	<!-- =================================================
	     SEARCH
	     ================================================= -->

	<form
		method="get"
		action="${pageContext.request.contextPath}/books/category"
		class="category-search">


		<!-- Category -->

		<input
			type="hidden"
			name="id"
			value="<%= category.getCategoryId() %>">



		<!-- Search input -->

		<div class="search-input-wrapper">


			<i data-lucide="search"></i>


			<input
				type="text"
				name="keyword"
				value="<%= keyword %>"
				placeholder="Search books in this category..."
				autocomplete="off">


		</div>



		<!-- Search button -->

		<button
			type="submit"
			class="search-button">

			<i data-lucide="search"></i>

			<span>Search</span>

		</button>



		<!-- Clear -->

		<%

		if (!keyword.isBlank()) {

		%>

			<a
				class="clear-button"
				href="${pageContext.request.contextPath}/books/category?id=<%= category.getCategoryId() %>">

				<i data-lucide="x"></i>

				<span>Clear</span>

			</a>

		<%

		}

		%>


	</form>



	<!-- =================================================
	     BOOKS SECTION
	     ================================================= -->

	<section
		class="books-section"
		id="booksSection"

		data-category-id="<%= category.getCategoryId() %>"

		data-keyword="<%= encodedKeyword %>"

		data-current-page="<%= safeCurrentPage %>"

		data-total-pages="<%= safeTotalPages %>">


		<%

		/* =================================================
		   EMPTY STATE
		   ================================================= */

		if (books == null || books.isEmpty()) {

		%>


			<div class="empty-state">


				<div class="empty-icon">

					<i data-lucide="book-open"></i>

				</div>


				<%

				if (!keyword.isBlank()) {

				%>


					<h3>
						No books found
					</h3>


					<p>

						No books found matching

						<strong>
							"<%= keyword %>"
						</strong>

						in this category.

					</p>


				<%

				} else {

				%>


					<h3>
						No books available
					</h3>


					<p>
						There are currently no books
						in this category.
					</p>


				<%

				}

				%>


			</div>


		<%

		}

		else {


			/* =================================================
			   DISPLAY BOOKS
			   ================================================= */

			for (Book book : books) {

		%>


			<!-- =============================================
			     BOOK CARD
			     ============================================= -->

			<article class="book-card">


				<!-- ================= COVER ================= -->

				<div class="book-cover-container">


					<%

					String coverUrl =
							coverUrlMap.get(
								book.getBookId()
							);


					if (
						coverUrl != null &&
						!coverUrl.isBlank()
					) {

					%>


						<img
							class="book-cover"
							src="<%= coverUrl %>"
							alt="<%= book.getTitle() %> cover"
							loading="lazy">


					<%

					}

					else {

					%>


						<div class="cover-placeholder">

							<i data-lucide="book-open"></i>

							<span>
								No Cover
							</span>

						</div>


					<%

					}

					%>


				</div>



				<!-- ================= BOOK INFORMATION ================= -->

				<div class="book-information">


					<!-- TITLE -->

					<h3 class="book-title">

						<%= book.getTitle() %>

					</h3>



					<!-- AUTHOR -->

					<div class="book-meta">

						<i data-lucide="user"></i>

						<span>

							<%= book.getAuthor() %>

						</span>

					</div>



					<!-- RATING -->

					<div class="book-rating">


						<%

						Double averageRating =
								averageRatingMap.get(
									book.getBookId()
								);


						Integer ratingCount =
								ratingCountMap.get(
									book.getBookId()
								);


						if (
							ratingCount != null &&
							ratingCount > 0
						) {

						%>


							<i
								data-lucide="star"
								class="rating-icon">
							</i>


							<strong>

								<%= String.format(
									"%.1f",
									averageRating
								) %>

							</strong>


							<span>
								/ 5
							</span>


							<span class="rating-count">

								(<%= ratingCount %> ratings)

							</span>


						<%

						}

						else {

						%>


							<i
								data-lucide="star">
							</i>


							<span>
								No ratings yet
							</span>


						<%

						}

						%>


					</div>



					<!-- DESCRIPTION -->

					<div class="description">


						<span class="description-label">

							Description

						</span>


						<%

						String description =
								book.getDescription();


						if (
							description == null ||
							description.isBlank()
						) {

						%>


							<p>
								No description available.
							</p>


						<%

						}

						else {


							if (
								description.length() > 250
							) {

								description =
									description.substring(
										0,
										250
									) + "...";

							}

						%>


							<p>

								<%= description %>

							</p>


						<%

						}

						%>


					</div>



					<!-- ================= ACTIONS ================= -->

					<div class="book-actions">


						<%

						/* Only USER can bookmark */

						if (isUser) {

						%>


							<button
								type="button"
								class="bookmark-button"
								data-book-id="<%= book.getBookId() %>"
								aria-label="Bookmark <%= book.getTitle() %>">


								<i
									data-lucide="bookmark">
								</i>


								<span>
									Bookmark
								</span>


							</button>


						<%

						}

						%>



						<!-- READ MORE -->

						<a
							class="read-more"
							href="${pageContext.request.contextPath}/books/details?id=<%= book.getBookId() %>">


							<span>
								Read More
							</span>


							<i
								data-lucide="arrow-right">
							</i>


						</a>


					</div>


				</div>


			</article>


		<%

			}

		}

		%>


	</section>



	<!-- =================================================
	     INFINITE SCROLL LOADER
	     ================================================= -->

	<div
		id="booksLoader"
		class="books-loader">


		<div class="loader-spinner">

			<i data-lucide="loader-circle"></i>

		</div>


		<span>
			Loading more books...
		</span>


	</div>

	


	<!-- =================================================
	     END MESSAGE
	     ================================================= -->

	<div
		id="booksEndMessage"
		class="books-end-message">


		<i data-lucide="check"></i>


		<span>
			You have reached the end.
		</span>


	</div>


</main>



<!-- =====================================================
     JAVASCRIPT CONFIG
     ===================================================== -->

<script>

	const contextPath =
		"${pageContext.request.contextPath}";

</script>



<!-- =====================================================
     CATEGORY BOOKS JAVASCRIPT
     ===================================================== -->

<script
	src="${pageContext.request.contextPath}/javascript/category-books.js">
</script>



<!-- =====================================================
     LUCIDE
     ===================================================== -->

<script>

	if (
		typeof lucide !== "undefined"
	) {

		lucide.createIcons();

	}

</script>


</body>

</html>