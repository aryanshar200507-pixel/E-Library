<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>

<%@ page import="com.project.elibrary.bean.category.Category"%>
<%@ page import="com.project.elibrary.bean.book.Book"%>
<%@ page import="com.project.elibrary.bean.user.User"%>
<%@ page import="com.project.elibrary.bean.enums.Role"%>

<!DOCTYPE html>

<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Stories - E-Library</title>


<!-- =====================================================
         GOOGLE FONTS
         ===================================================== -->

<link rel="preconnect" href="https://fonts.googleapis.com">

<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

<link
	href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap"
	rel="stylesheet">


<!-- =====================================================
         LUCIDE
         ===================================================== -->

<script src="https://unpkg.com/lucide@latest"></script>


<!-- =====================================================
         CSS
         ===================================================== -->

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/books.css">

</head>


<body>


	<%

/* =========================================================
   SEARCH MODE
   ========================================================= */

Boolean searchModeAttribute =
        (Boolean) request.getAttribute("searchMode");

boolean searchMode =
        searchModeAttribute != null
        && searchModeAttribute;


/* =========================================================
   LOGGED-IN USER
   ========================================================= */

User loggedInUser =
        (User) session.getAttribute("loggedInUser");


/* =========================================================
   ADMIN
   ========================================================= */

boolean isAdmin =
        loggedInUser != null
        && loggedInUser.getRole() == Role.ADMIN;

%>


	<!-- =========================================================
     PAGE
     ========================================================= -->

	<div class="books-page">


		<!-- =====================================================
         HEADER
         ===================================================== -->

		<header class="site-header">

			<div class="header-inner">


				<!-- BRAND -->

				<a href="${pageContext.request.contextPath}/books" class="brand">

					<div class="brand-logo">

						<i data-lucide="library"></i>

					</div>


					<div class="brand-text">

						<span class="brand-name"> Stories </span> <span
							class="brand-subtitle"> E-Library </span>

					</div>

				</a>


				<!-- NAVIGATION -->

				<nav class="main-nav">


					<a href="${pageContext.request.contextPath}/books"
						class="nav-link active"> <i data-lucide="library"></i> <span>
							Categories </span>

					</a>


					<%
if (loggedInUser != null) {

    String dashboardUrl;

    if (loggedInUser.getRole() == Role.ADMIN) {
        dashboardUrl =
            pageContext.getServletContext().getContextPath()
            + "/admin/dashboard";
    } else {
        dashboardUrl =
            pageContext.getServletContext().getContextPath()
            + "/user/dashboard";
    }
%>

					<a href="<%=dashboardUrl%>" class="nav-link"> <i
						data-lucide="layout-dashboard"></i> <span> Dashboard </span>

					</a>

					<%
}
%>


					<%
                    if (!isAdmin) {
                    %>

					<a href="${pageContext.request.contextPath}/books/bookmark-book"
						class="nav-link"> <i data-lucide="bookmark"></i> <span>
							Bookmarks </span>

					</a>

					<%
                    }
                    %>




					<%
                if (isAdmin) {
                %>

					<a href="${pageContext.request.contextPath}/admin/books/add"
						class="nav-link"> <i data-lucide="plus"></i> <span> Add
							Book </span>

					</a>

					<%
                }
                %>


				</nav>


				<!-- USER -->

				<%
            if (loggedInUser != null) {
            %>

				<div class="user-avatar" title="<%=loggedInUser.getName()%>">

					<%= loggedInUser.getName() != null
                            && !loggedInUser.getName().isBlank()
                            ? loggedInUser.getName()
                                .substring(0, 1)
                                .toUpperCase()
                            : "U" %>

				</div>

				<%
            }
            %>


			</div>

		</header>


		<!-- =====================================================
         MAIN
         ===================================================== -->

		<main class="page-content">


			<%

/* =========================================================
   SEARCH MODE
   ========================================================= */

if (searchMode) {


    List<Book> books =
            (List<Book>)
            request.getAttribute("books");


    Map<Long, Double> bookAverageRatingMap =
            (Map<Long, Double>)
            request.getAttribute("bookAverageRatingMap");


    Map<Long, Integer> bookRatingCountMap =
            (Map<Long, Integer>)
            request.getAttribute("bookRatingCountMap");


    Map<Long, String> bookCoverUrlMap =
            (Map<Long, String>)
            request.getAttribute("bookCoverUrlMap");


    String keyword =
            (String)
            request.getAttribute("keyword");


    Integer currentPage =
            (Integer)
            request.getAttribute("currentPage");


    Integer totalPages =
            (Integer)
            request.getAttribute("totalPages");

%>


			<!-- =================================================
             SEARCH HERO
             ================================================= -->

			<section class="search-hero">

				<div class="search-hero-icon">

					<i data-lucide="search"></i>

				</div>


				<span class="eyebrow"> Search Library </span>


				<h1>Find your next story.</h1>


				<p>Search through the books available in your library.</p>


				<!-- SEARCH -->

				<form class="search-form"
					action="${pageContext.request.contextPath}/books" method="get">

					<div class="search-input-wrapper">

						<i data-lucide="search"></i> <input type="text" name="keyword"
							placeholder="Search by title or author..."
							value="<%= keyword != null ? keyword : "" %>" autocomplete="off">

						<button type="submit" aria-label="Search">

							<i data-lucide="arrow-right"></i>

						</button>

					</div>

				</form>

			</section>


			<!-- =================================================
             SEARCH RESULTS
             ================================================= -->

			<section class="results-section">


				<div class="section-heading">

					<div>

						<span class="eyebrow"> Your Library </span>

						<h2>Search results</h2>

					</div>


					<%
                if (keyword != null &&
                    !keyword.isBlank()) {
                %>

					<span class="search-query"> <i data-lucide="quote"></i> <%=keyword%>

					</span>

					<%
                }
                %>

				</div>


				<%

if (books != null && !books.isEmpty()) {

%>


				<div class="book-grid">


					<%

    for (Book book : books) {

        Long bookId =
                book.getBookId();


        String coverUrl =
                bookCoverUrlMap != null
                    ? bookCoverUrlMap.get(bookId)
                    : null;


        Double averageRating =
                bookAverageRatingMap != null
                    ? bookAverageRatingMap.get(bookId)
                    : null;


        Integer ratingCount =
                bookRatingCountMap != null
                    ? bookRatingCountMap.get(bookId)
                    : 0;

%>


					<!-- =================================================
                     BOOK CARD
                     ================================================= -->

					<article class="book-card">


						<!-- COVER -->

						<div class="book-cover-container">

							<%
                        if (coverUrl != null &&
                            !coverUrl.isBlank()) {
%>

							<img class="book-cover" src="<%=coverUrl%>"
								alt="<%=book.getTitle()%> cover" loading="lazy">

							<%
                        } else {
%>

							<div class="no-cover">

								<i data-lucide="book-open"></i> <span> No cover </span>

							</div>

							<%
                        }
%>


							<!-- CATEGORY DECORATION -->

							<div class="cover-shine"></div>

						</div>


						<!-- BOOK INFO -->

						<div class="book-info">


							<h3>
								<%=book.getTitle()%>
							</h3>


							<div class="book-author">

								<i data-lucide="user-round"></i> <span> <%=book.getAuthor()%>
								</span>

							</div>


							<!-- RATING -->

							<div class="rating">

								<span class="stars"> <%
                            if (averageRating != null) {

                                int roundedRating =
                                    (int)
                                    Math.round(
                                        averageRating
                                    );

                                for (
                                    int i = 1;
                                    i <= 5;
                                    i++
                                ) {

                                    if (
                                        i <= roundedRating
                                    ) {
%> ★ <%
                                    } else {
%> ☆ <%
                                    }

                                }

                            } else {
%> ☆☆☆☆☆ <%
                            }
%>

								</span> <span class="rating-value"> <%
                            if (averageRating != null) {
%> <%=String.format(
                                    "%.1f",
                                    averageRating
                                )%> <%
                            } else {
%> — <%
                            }
%>

								</span> <span class="rating-count"> (<%=ratingCount != null
                                    ? ratingCount
                                    : 0%>)

								</span>

							</div>


							<!-- DESCRIPTION -->

							<div class="book-description">

								<%
                        String description =
                                book.getDescription();

                        if (description != null &&
                            description.length() > 120) {
%>

								<%=description.substring(0, 120)%>...

								<%
                        } else {
%>

								<%=description != null
                                ? description
                                : "No description available."%>

								<%
                        }
%>

							</div>


							<!-- ACTION -->

							<div class="book-card-footer">

								<a class="read-button"
									href="${pageContext.request.contextPath}/books/details?id=<%=bookId%>">

									<span> Read More </span> <i data-lucide="arrow-up-right"></i>

								</a>


								<%
                            if (isAdmin) {
                            %>

								<div class="admin-actions">

									<a
										href="${pageContext.request.contextPath}/admin/books/edit?id=<%=bookId%>"
										title="Edit"> <i data-lucide="pencil"></i>

									</a> <a
										href="${pageContext.request.contextPath}/admin/books/delete?id=<%=bookId%>"
										title="Delete"
										onclick="return confirm('Are you sure you want to delete this book?');">

										<i data-lucide="trash-2"></i>

									</a>

								</div>

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


				<%
} else {
%>


				<!-- EMPTY -->

				<div class="empty-state">

					<div class="empty-icon">

						<i data-lucide="search-x"></i>

					</div>

					<h3>No books found</h3>

					<p>
						We couldn't find any books matching "<%=keyword != null ? keyword : ""%>".
					</p>


					<a href="${pageContext.request.contextPath}/books"
						class="back-button"> <i data-lucide="arrow-left"></i> <span>
							Browse Categories </span>

					</a>

				</div>


				<%
}
%>


			</section>


			<%
    /*
     * SEARCH PAGINATION
     */

    if (totalPages != null &&
        totalPages > 1) {
%>


			<nav class="pagination" aria-label="Search pagination">


				<%
        if (currentPage != null &&
            currentPage > 1) {
%>

				<a
					href="${pageContext.request.contextPath}/books?keyword=<%=java.net.URLEncoder.encode(keyword != null ? keyword : "", "UTF-8")%>&page=<%=currentPage - 1%>"
					class="page-button"> <i data-lucide="chevron-left"></i>

					Previous

				</a>

				<%
        }
%>


				<div class="page-numbers">

					<%
        for (
            int i = 1;
            i <= totalPages;
            i++
        ) {
%>

					<%
                if (currentPage != null &&
                    i == currentPage) {
                %>

					<span class="page-number current"> <%=i%>
					</span>

					<%
                } else {
                %>

					<a class="page-number"
						href="${pageContext.request.contextPath}/books?keyword=<%=java.net.URLEncoder.encode(keyword != null ? keyword : "", "UTF-8")%>&page=<%=i%>">

						<%=i%>

					</a>

					<%
                }
                %>

					<%
        }
%>

				</div>


				<%
        if (currentPage != null &&
            currentPage < totalPages) {
%>

				<a
					href="${pageContext.request.contextPath}/books?keyword=<%=java.net.URLEncoder.encode(keyword != null ? keyword : "", "UTF-8")%>&page=<%=currentPage + 1%>"
					class="page-button"> Next <i data-lucide="chevron-right"></i>

				</a>

				<%
        }
%>


			</nav>


			<%
    }
%>


			<%
/* =========================================================
   CATEGORY MODE
   ========================================================= */

} else {


    List<Category> categories =
            (List<Category>)
            request.getAttribute("categories");


    Map<Long, Book> highestRatedBookMap =
            (Map<Long, Book>)
            request.getAttribute("highestRatedBookMap");


    Map<Long, String> categoryCoverUrlMap =
            (Map<Long, String>)
            request.getAttribute("categoryCoverUrlMap");


    Integer currentPage =
            (Integer)
            request.getAttribute("currentPage");


    Integer totalPages =
            (Integer)
            request.getAttribute("totalPages");

%>


			<!-- =================================================
             CATEGORY HERO
             ================================================= -->

			<section class="category-hero">


				<div class="hero-content">

					<span class="eyebrow"> <i data-lucide="sparkles"></i>

						Explore the library

					</span>


					<h1>
						Find a story<br> <em>that feels like you.</em>
					</h1>


					<p>Explore books by mood, genre and imagination. Your next
						favourite story might be one click away.</p>


					<!-- SEARCH -->

					<form class="search-form hero-search"
						action="${pageContext.request.contextPath}/books" method="get">

						<div class="search-input-wrapper">

							<i data-lucide="search"></i> <input type="text" name="keyword"
								placeholder="Search books by title or author..."
								autocomplete="off">

							<button type="submit" aria-label="Search">

								<i data-lucide="arrow-right"></i>

							</button>

						</div>

					</form>

				</div>


				<div class="hero-decoration">

					<div class="floating-book book-one">

						<i data-lucide="book-open"></i>

					</div>

					<div class="floating-book book-two">

						<i data-lucide="book-marked"></i>

					</div>

					<div class="floating-book book-three">

						<i data-lucide="heart"></i>

					</div>

				</div>


			</section>


			<!-- =================================================
             CATEGORIES
             ================================================= -->

			<section class="categories-section">


				<div class="section-heading">

					<div>

						<span class="eyebrow"> Browse </span>

						<h2>Choose your world.</h2>

					</div>


					<span class="category-count"> <%=categories != null
                        ? categories.size()
                        : 0%> categories

					</span>

				</div>


				<%
    if (categories == null ||
        categories.isEmpty()) {
%>


				<div class="empty-state">

					<div class="empty-icon">

						<i data-lucide="library-big"></i>

					</div>

					<h3>No categories available</h3>

					<p>Categories will appear here once they are added.</p>

				</div>


				<%
    } else {
%>


				<div class="category-grid">


					<%
        for (Category category : categories) {

            Long categoryId =
                    category.getCategoryId();


            String coverUrl =
                    categoryCoverUrlMap != null
                        ? categoryCoverUrlMap.get(categoryId)
                        : null;


            Book highestRatedBook =
                    highestRatedBookMap != null
                        ? highestRatedBookMap.get(categoryId)
                        : null;

%>


					<!-- =================================================
                     CATEGORY CARD
                     ================================================= -->

					<a class="category-card"
						href="${pageContext.request.contextPath}/books/category?id=<%=categoryId%>">


						<div class="category-cover-container">


							<%
                    if (coverUrl != null &&
                        !coverUrl.isBlank()) {
%>

							<img class="category-cover" src="<%=coverUrl%>"
								alt="<%=category.getCategoryName()%>" loading="lazy">

							<%
                    } else {
%>

							<div class="category-no-cover">

								<i data-lucide="book-open"></i> <span> No cover </span>

							</div>

							<%
                    }
%>


							<div class="category-overlay">

								<span> Explore </span> <i data-lucide="arrow-up-right"></i>

							</div>

						</div>


						<div class="category-info">


							<div>

								<h3>
									<%=category.getCategoryName()%>
								</h3>


								<%
                            if (highestRatedBook != null) {
%>

								<p>

									<i data-lucide="star"></i>

									<%=highestRatedBook.getTitle()%>

								</p>

								<%
                            } else {
%>

								<p>Discover this collection</p>

								<%
                            }
%>

							</div>


							<div class="category-arrow">

								<i data-lucide="arrow-right"></i>

							</div>


						</div>


					</a>


					<%
        }
%>


				</div>


				<%
    }
%>


			</section>


			<%
    /*
     * CATEGORY PAGINATION
     */

    if (totalPages != null &&
        totalPages > 1) {
%>


			<nav class="pagination" aria-label="Category pagination">


				<%
        if (currentPage != null &&
            currentPage > 1) {
%>

				<a
					href="${pageContext.request.contextPath}/books?page=<%=currentPage - 1%>"
					class="page-button"> <i data-lucide="chevron-left"></i>

					Previous

				</a>

				<%
        }
%>


				<div class="page-numbers">

					<%
        for (
            int i = 1;
            i <= totalPages;
            i++
        ) {
%>

					<%
                if (currentPage != null &&
                    i == currentPage) {
                %>

					<span class="page-number current"> <%=i%>
					</span>

					<%
                } else {
                %>

					<a class="page-number"
						href="${pageContext.request.contextPath}/books?page=<%=i%>"> <%=i%>

					</a>

					<%
                }
                %>

					<%
        }
%>

				</div>


				<%
        if (currentPage != null &&
            currentPage < totalPages) {
%>

				<a
					href="${pageContext.request.contextPath}/books?page=<%=currentPage + 1%>"
					class="page-button"> Next <i data-lucide="chevron-right"></i>

				</a>

				<%
        }
%>


			</nav>


			<%
    }

}


%>


		</main>


		<!-- =====================================================
         FOOTER
         ===================================================== -->

		<footer class="site-footer">

			<div>

				<i data-lucide="library"></i> <span> Stories E-Library </span>

			</div>


			<span> Every story has a place. </span>

		</footer>


	</div>


	<!-- =========================================================
     JAVASCRIPT
     ========================================================= -->

	<script src="${pageContext.request.contextPath}/javascript/books.js">
		
	</script>


</body>

</html>