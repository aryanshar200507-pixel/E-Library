<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>

<%@ page import="com.project.elibrary.bean.enums.Role"%>
<%@ page import="com.project.elibrary.bean.book.Book"%>
<%@ page import="com.project.elibrary.bean.comment.Comment"%>
<%@ page import="com.project.elibrary.bean.ratings.Rating"%>
<%@ page import="com.project.elibrary.bean.user.User"%>

<!DOCTYPE html>

<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title><%=request.getAttribute("categoryName") != null
		? request.getAttribute("categoryName") + " - Stories"
		: "Book Details - Stories"%></title>


<!-- =====================================================
         GOOGLE FONTS
         ===================================================== -->

<link rel="preconnect" href="https://fonts.googleapis.com">

<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

<link
	href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:wght@500;600;700&display=swap"
	rel="stylesheet">


<!-- =====================================================
         LUCIDE ICONS
         ===================================================== -->

<script src="https://unpkg.com/lucide@latest"></script>


<!-- =====================================================
         BOOK DETAILS CSS
         ===================================================== -->

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/book-details.css">

</head>


<body>

	<%
	/* =========================================================
	   GET BOOK DATA
	   ========================================================= */

	Book book = (Book) request.getAttribute("book");

	String categoryName = (String) request.getAttribute("categoryName");

	String coverUrl = (String) request.getAttribute("coverUrl");

	Double averageRating = (Double) request.getAttribute("averageRating");

	Integer ratingCount = (Integer) request.getAttribute("ratingCount");

	Rating userRating = (Rating) request.getAttribute("userRating");

	List<Comment> comments = (List<Comment>) request.getAttribute("comments");

	Map<Long, String> commentUserNameMap = (Map<Long, String>) request.getAttribute("commentUserNameMap");

	/* =========================================================
	   LOGGED IN USER
	   ========================================================= */

	User loggedInUser = (User) session.getAttribute("loggedInUser");

	/* =========================================================
	   ROLE CHECKS
	   ========================================================= */

	boolean isAdmin = loggedInUser != null && loggedInUser.getRole() == Role.ADMIN;

	boolean isUser = loggedInUser != null && loggedInUser.getRole() == Role.USER;

	/* =========================================================
	   CATEGORY THEME

	   Currently testing Romance.
	   More categories can be added later.
	   ========================================================= */

	String categoryKey = "default";

	if (categoryName != null && !categoryName.isBlank()) {

		categoryKey = categoryName.trim().toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
	}
	%>


	<!-- =========================================================
     PAGE WRAPPER
     ========================================================= -->

	<div class="book-details-page theme-<%=categoryKey%>"
		style="--category-bg: url('<%=request.getContextPath()%>/images/category/<%=categoryKey%>.jpg');">


		<!-- =====================================================
         HEADER
         ===================================================== -->

		<header class="site-header">

			<div class="header-inner">


				<!-- BRAND -->

				<a class="brand" href="${pageContext.request.contextPath}/books">


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


					<!-- Categories -->

					<a href="${pageContext.request.contextPath}/books" class="nav-link">

						<i data-lucide="arrow-left"></i> <span> Categories </span>

					</a>


					<!-- Back to Books -->

					<a
						href="${pageContext.request.contextPath}/books/category?id=<%=book.getCategoryId()%>"
						class="nav-link"> <i data-lucide="library"></i> <span>
							Back to Books </span>

					</a>


					<!-- Bookmarks -->

					<%
					if (loggedInUser != null && !isAdmin) {
					%>

					<a href="${pageContext.request.contextPath}/books/bookmark-book"
						class="nav-link"> <i data-lucide="bookmark"></i> <span>
							Bookmarks </span>

					</a>

					<%
					}
					%>


					<!-- Dashboard -->

					<%
					if (loggedInUser != null) {
					%>

					<a href="${pageContext.request.contextPath}/user/dashboard"
						class="nav-link"> <i data-lucide="layout-dashboard"></i> <span>
							Dashboard </span>

					</a>

					<%
					}
					%>


					<!-- Admin -->

					<%
					if (isAdmin) {
					%>

					<a href="${pageContext.request.contextPath}/books/add"
						class="nav-link"> <i data-lucide="plus"></i> <span> Add
							Book </span>

					</a>

					<%
					}
					%>


				</nav>


				<!-- USER AVATAR -->

				<%
				if (loggedInUser != null) {
				%>

				<div class="user-circle">

					<%=loggedInUser.getName() != null && !loggedInUser.getName().isBlank()
		? loggedInUser.getName().substring(0, 1).toUpperCase()
		: "U"%>

				</div>

				<%
				}
				%>


			</div>

		</header>


		<!-- =====================================================
         MAIN CONTENT
         ===================================================== -->

		<main class="page-content">

			<!-- =================================================
             BOOK DETAILS
             ================================================= -->

			<section class="book-main-card">


				<!-- =================================================
                 BOOK COVER
                 ================================================= -->

				<div class="cover-container">

					<%
					if (coverUrl != null && !coverUrl.isBlank()) {
					%>

					<div class="cover-wrapper">

						<img class="book-cover" src="<%=coverUrl%>"
							alt="<%=book.getTitle()%> cover">


						<div class="cover-shadow"></div>

					</div>

					<%
					} else {
					%>

					<div class="no-cover">

						<i data-lucide="book-open"></i> <span> No cover available </span>

					</div>

					<%
					}
					%>

				</div>


				<!-- =================================================
                 BOOK INFORMATION
                 ================================================= -->

				<div class="book-information">


					<!-- CATEGORY -->

					<div class="category-badge">

						<i data-lucide="heart"></i> <span> <%=categoryName != null ? categoryName : "Unknown"%>
						</span>

					</div>


					<!-- TITLE -->

					<h2 class="book-title">

						<%=book.getTitle()%>

					</h2>


					<!-- AUTHOR -->

					<div class="author-line">

						<i data-lucide="user-round"></i> <span> <%=book.getAuthor()%>
						</span>

					</div>


					<!-- =================================================
                     BOOK META
                     ================================================= -->

					<div class="book-meta">


						<!-- Published -->

						<div class="meta-item">

							<i data-lucide="calendar-days"></i>

							<div>

								<span class="meta-label"> Published </span> <strong> <%=book.getPublishedAt() != null ? book.getPublishedAt() : "Not available"%>

								</strong>

							</div>

						</div>


						<!-- Views -->

						<div class="meta-item">

							<i data-lucide="eye"></i>

							<div>

								<span class="meta-label"> Views </span> <strong> <%=book.getViews() != null ? book.getViews() : 0%>

								</strong>

							</div>

						</div>


						<!-- Rating -->

						<div class="meta-item">

							<i data-lucide="star"></i>

							<div>

								<span class="meta-label"> Rating </span> <strong> <%
 if (ratingCount != null && ratingCount > 0) {
 %> <%=String.format("%.1f", averageRating)%> / 5 <span
									class="rating-count"> (<%=ratingCount%>)
								</span> <%
 } else {
 %> No ratings yet <%
 }
 %>

								</strong>

							</div>

						</div>


					</div>


					<!-- =================================================
                     DESCRIPTION
                     ================================================= -->

					<div class="description">

						<h3>About this book</h3>


						<p>

							<%
							if (book.getDescription() != null && !book.getDescription().isBlank()) {
							%>

							<%=book.getDescription()%>

							<%
							} else {
							%>

							No description available.

							<%
							}
							%>

						</p>

					</div>


					<!-- =================================================
                     BOOK ACTIONS
                     ================================================= -->

					<div class="book-actions">


						<!-- Bookmark -->

						<%
						if (isUser) {
						%>

						<button type="button" id="bookBookmarkButton"
							class="bookmark-button" data-book-id="<%=book.getBookId()%>"
							onclick="toggleBookBookmark(this)">

							<i data-lucide="bookmark"></i> <span> Bookmark </span>

						</button>

						<%
						}
						%>


						<!-- Read -->

						<%
						if (book.getPdfStorageKey() != null && !book.getPdfStorageKey().isBlank()) {
						%>

						<a class="read-button"
							href="${pageContext.request.contextPath}/books/read?id=<%=book.getBookId()%>">

							<i data-lucide="book-open"></i> <span> Read Book </span> <i
							data-lucide="arrow-right"></i>

						</a>

						<%
						} else {
						%>

						<span class="unavailable-book"> Book file is not available.

						</span>

						<%
						}
						%>


					</div>


					<!-- =================================================
                     ADMIN ACTIONS
                     ================================================= -->

					<%
					if (isAdmin) {
					%>

					<div class="admin-actions">


						<div class="admin-heading">

							<i data-lucide="shield"></i> <strong> Admin Actions </strong>

						</div>


						<div class="admin-buttons">


							<a class="admin-button"
								href="${pageContext.request.contextPath}/admin/books/edit?id=<%=book.getBookId()%>">

								<i data-lucide="pencil"></i> <span> Edit Book </span>

							</a>


							<form
								action="${pageContext.request.contextPath}/admin/books/delete"
								method="post">

								<input type="hidden" name="bookId" value="<%=book.getBookId()%>">


								<button type="submit" class="admin-button delete-button"
									onclick="return confirm('Are you sure you want to delete this book?');">

									<i data-lucide="trash-2"></i> <span> Delete Book </span>

								</button>

							</form>


						</div>

					</div>

					<%
					}
					%>


				</div>

			</section>


			<!-- =====================================================
             RATING SECTION
             ===================================================== -->

			<%
			if (loggedInUser != null && !isAdmin) {
			%>

			<section class="content-card rating-section">


				<div class="section-heading">


					<div class="section-icon">

						<i data-lucide="star"></i>

					</div>


					<div>

						<h2>Your Rating</h2>

						<p>Tell us what you thought about this book.</p>

					</div>


				</div>


				<!-- CURRENT RATING -->

				<div class="current-rating">

					<%
					if (userRating != null) {
					%>

					You rated this book: <strong> <%=userRating.getRating()%>
						/ 5
					</strong>

					<%
					} else {
					%>

					You have not rated this book yet.

					<%
					}
					%>

				</div>


				<!-- RATING FORM -->

				<form action="${pageContext.request.contextPath}/rating"
					method="post" id="ratingForm">


					<input type="hidden" name="bookId" value="<%=book.getBookId()%>">


					<input type="hidden" name="rating" id="ratingValue" value="">


					<p class="rating-label">Choose your rating</p>


					<div class="touch-stars">

						<%
						for (int star = 1; star <= 5; star++) {
						%>

						<span class="touch-star" data-rating="<%=star%>"> <%=userRating != null && star <= userRating.getRating() ? "★" : "☆"%>

						</span>

						<%
						}
						%>

					</div>


					<button type="submit" id="ratingSubmit" class="secondary-button"
						disabled>

						<i data-lucide="send"></i> <span> <%=userRating == null ? "Submit Rating" : "Update Rating"%>
						</span>

					</button>


				</form>


			</section>

			<%
			}
			%>


			<!-- =====================================================
             COMMENTS SECTION
             ===================================================== -->

			<section class="content-card comments-section">


				<!-- HEADER -->

				<div class="section-heading">

					<div class="section-icon">

						<i data-lucide="message-circle"></i>

					</div>


					<div>

						<h2>Comments</h2>

						<p>What are readers saying?</p>

					</div>

				</div>


				<!-- =================================================
                 ADD COMMENT FIRST
                 ================================================= -->

				<%
				if (loggedInUser != null) {
				%>

				<div class="comment-form">


					<div class="comment-form-heading">

						<div>

							<h3>Share your thoughts</h3>

							<p>What did you think about this book?</p>

						</div>


						<div class="comment-form-icon">

							<i data-lucide="pen-line"></i>

						</div>

					</div>


					<form action="${pageContext.request.contextPath}/comment"
						method="post" id="addCommentForm">


						<input type="hidden" name="action" value="add"> <input
							type="hidden" name="bookId" value="<%=book.getBookId()%>">


						<textarea name="comment"
							placeholder="Write your thoughts about this book..." required></textarea>


						<button type="submit" class="primary-button">

							<i data-lucide="send"></i> <span> Add Comment </span>

						</button>


					</form>

				</div>

				<%
				} else {
				%>

				<div class="login-message">

					<i data-lucide="log-in"></i> <span> Login to rate the book
						or add comments. </span>

				</div>

				<%
				}
				%>


				<!-- =================================================
                 DIVIDER
                 ================================================= -->

				<div class="comments-divider"></div>


				<!-- =================================================
                 EXISTING COMMENTS
                 ================================================= -->

				<div class="comments-list">


					<%
					if (comments == null || comments.isEmpty()) {
					%>

					<div class="empty-comments">

						<i data-lucide="message-square"></i>

						<p>No comments yet.</p>

						<span> Be the first to share your thoughts. </span>

					</div>

					<%
					} else {
					%>


					<%
					for (Comment comment : comments) {
					%>

					<div class="comment">


						<!-- COMMENT HEADER -->

						<div class="comment-header">


							<div class="comment-user">


								<div class="comment-avatar">

									<%
									String commentUserName = commentUserNameMap != null ? commentUserNameMap.get(comment.getUserId()) : null;
									%>


									<%=commentUserName != null && !commentUserName.isBlank() ? commentUserName.substring(0, 1).toUpperCase() : "U"%>


								</div>


								<strong> <%=commentUserName != null && !commentUserName.isBlank() ? commentUserName : "User #" + comment.getUserId()%>

								</strong>


							</div>


							<span class="comment-date"> <%=comment.getCreatedAt()%>

							</span>


						</div>


						<!-- COMMENT TEXT -->

						<div class="comment-text">

							<%=comment.getComment()%>

						</div>


						<%
						boolean isCommentOwner = loggedInUser != null && loggedInUser.getUserId().equals(comment.getUserId());
						%>


						<!-- COMMENT ACTIONS -->

						<div class="comment-actions">


							<!-- EDIT -->

							<%
							if (isCommentOwner && !isAdmin) {
							%>

							<button type="button"
								onclick="showEditForm(<%=comment.getCommentId()%>)">

								<i data-lucide="pencil"></i> <span> Edit </span>

							</button>


							<!-- EDIT FORM -->

							<div id="edit-form-<%=comment.getCommentId()%>" class="edit-form"
								style="display: none;">


								<form action="${pageContext.request.contextPath}/comment"
									method="post">


									<input type="hidden" name="action" value="update"> <input
										type="hidden" name="commentId"
										value="<%=comment.getCommentId()%>"> <input
										type="hidden" name="bookId" value="<%=book.getBookId()%>">


									<textarea name="comment" required><%=comment.getComment()%></textarea>


									<div class="edit-buttons">


										<button type="submit">

											<i data-lucide="save"></i> <span> Save </span>

										</button>


										<button type="button"
											onclick="hideEditForm(<%=comment.getCommentId()%>)">

											<i data-lucide="x"></i> <span> Cancel </span>

										</button>


									</div>


								</form>

							</div>


							<%
							}
							%>


							<!-- DELETE -->

							<%
							if (isCommentOwner || isAdmin) {
							%>

							<form action="${pageContext.request.contextPath}/comment"
								method="post" class="delete-comment-form">


								<input type="hidden" name="action" value="delete"> <input
									type="hidden" name="commentId"
									value="<%=comment.getCommentId()%>"> <input
									type="hidden" name="bookId" value="<%=book.getBookId()%>">


								<button type="submit"
									onclick="return confirm('Delete this comment?');">

									<i data-lucide="trash-2"></i> <span> Delete </span>

								</button>


							</form>

							<%
							}
							%>


						</div>


					</div>


					<%
					}
					%>


					<%
					}
					%>


				</div>


			</section>


		</main>
		
		


		<!-- =====================================================
         FOOTER
         ===================================================== -->

		<footer class="site-footer">

			<span> Stories E-Library </span> <span> Books connect us. </span>

		</footer>


	</div>


	<!-- =====================================================
     JAVASCRIPT
     ===================================================== -->

	<script
		src="${pageContext.request.contextPath}/javascript/book-details.js">
</script>


</body>

</html>
