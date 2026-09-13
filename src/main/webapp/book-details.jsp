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

<html>

<head>

<meta charset="UTF-8">

<title>Book Details - E-Library</title>


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
	background: white;
	padding: 20px;
	border-bottom: 1px solid #ccc;
}

header h1 {
	margin: 0 0 15px 0;
}

header a {
	text-decoration: none;
	margin-right: 20px;
}


/* ================= MAIN CONTAINER ================= */

.container {
	max-width: 1100px;
	margin: 30px auto;
	padding: 30px;
	background: white;
}


/* ================= BOOK DETAILS ================= */

.book-details {
	display: flex;
	gap: 40px;
	padding-bottom: 30px;
	border-bottom: 1px solid #ddd;
}


/* ================= COVER ================= */

.cover-container {
	width: 250px;
	flex-shrink: 0;
}

.book-cover {
	width: 250px;
	height: 360px;
	object-fit: cover;
	border: 1px solid #ccc;
}


/* ================= BOOK INFORMATION ================= */

.book-information {
	flex: 1;
}

.book-information h2 {
	margin-top: 0;
	font-size: 32px;
}

.book-info {
	margin: 12px 0;
}


/* ================= DESCRIPTION ================= */

.description {
	margin-top: 25px;
	line-height: 1.7;
}


/* ================= BOOK ACTIONS ================= */

.book-actions {
	margin-top: 25px;
}


/* ================= READ BUTTON ================= */

.read-button {
	display: inline-block;
	padding: 12px 25px;
	background: #333;
	color: white;
	text-decoration: none;
	margin-right: 10px;
}


/* ================= BOOKMARK BUTTON ================= */

.bookmark-button {
	display: inline-block;
	padding: 12px 25px;
	border: 1px solid #333;
	background: white;
	cursor: pointer;
	font-size: 14px;
}

.bookmark-button.bookmarked {
	background: #333;
	color: white;
}


/* ================= ADMIN ACTIONS ================= */

.admin-actions {
	margin-top: 25px;
	padding: 20px;
	border: 1px solid #ddd;
	background: #fafafa;
}

.admin-actions h3 {
	margin-top: 0;
}

.admin-button {
	display: inline-block;
	padding: 10px 18px;
	margin-right: 10px;
	margin-top: 5px;
	text-decoration: none;
	color: white;
	background: #333;
	cursor: pointer;
	border: none;
	font-size: 14px;
}

.delete-button {
	background: #b00020;
}


/* ================= RATING ================= */

.rating-section {
	margin-top: 30px;
	padding-top: 20px;
	border-top: 1px solid #ddd;
}

.current-rating {
	font-size: 18px;
	margin-bottom: 15px;
}

.rating-input {
	margin-top: 20px;
}

.touch-stars {
	display: flex;
	align-items: center;
	gap: 0;
	margin: 10px 0 15px 0;
}

.touch-star {
	font-size: 32px;
	line-height: 1;
	padding: 2px;
	margin: 0;
	cursor: pointer;
	user-select: none;
	color: #f5b301;
	display: inline-block;
	transition: transform 0.1s;
}

.touch-star:hover {
	transform: scale(1.2);
}

.rating-input button {
	padding: 10px 20px;
	cursor: pointer;
}

.rating-input button:disabled {
	cursor: not-allowed;
	opacity: 0.5;
}


/* ================= COMMENTS ================= */

.comments-section {
	margin-top: 40px;
	padding-top: 20px;
	border-top: 1px solid #ddd;
}

.comment {
	padding: 15px;
	margin-bottom: 15px;
	border: 1px solid #ddd;
	background: #fafafa;
}

.comment-text {
	margin: 10px 0;
	line-height: 1.5;
}

.comment-date {
	font-size: 12px;
	color: #777;
	margin-bottom: 10px;
}


/* ================= COMMENT ACTIONS ================= */

.comment-actions {
	margin-top: 10px;
}

.comment-actions button {
	margin-right: 5px;
	padding: 7px 12px;
	cursor: pointer;
}


/* ================= EDIT FORM ================= */

.edit-form {
	margin-top: 10px;
}

.edit-form textarea {
	width: 100%;
	max-width: 600px;
	min-height: 80px;
	padding: 10px;
	box-sizing: border-box;
	resize: vertical;
}

.edit-form button {
	margin-top: 8px;
	margin-right: 5px;
	padding: 7px 12px;
	cursor: pointer;
}


/* ================= ADD COMMENT ================= */

.comment-form {
	margin-top: 25px;
}

.comment-form textarea {
	width: 100%;
	max-width: 600px;
	height: 100px;
	padding: 10px;
	box-sizing: border-box;
	resize: vertical;
}

.comment-form button {
	margin-top: 10px;
	padding: 10px 20px;
	cursor: pointer;
}


/* ================= RESPONSIVE ================= */

@media (max-width: 700px) {

	.book-details {
		flex-direction: column;
	}

	.cover-container {
		width: 100%;
	}

	.book-cover {
		display: block;
		margin: auto;
	}

}

</style>

</head>


<body>


<%

/* =====================================================
 * GET BOOK DATA
 * ===================================================== */

Book book =
		(Book) request.getAttribute("book");

String categoryName =
		(String) request.getAttribute("categoryName");

String coverUrl =
		(String) request.getAttribute("coverUrl");

Double averageRating =
		(Double) request.getAttribute("averageRating");

Integer ratingCount =
		(Integer) request.getAttribute("ratingCount");

Rating userRating =
		(Rating) request.getAttribute("userRating");

List<Comment> comments =
		(List<Comment>) request.getAttribute("comments");

Map<Long, String> commentUserNameMap =
		(Map<Long, String>) request.getAttribute(
				"commentUserNameMap");


/* Logged-in user */

User loggedInUser =
		(User) session.getAttribute("loggedInUser");


/* Check admin */

boolean isAdmin =
		loggedInUser != null &&
		loggedInUser.getRole() == Role.ADMIN;


/* Check normal USER */

boolean isUser =
		loggedInUser != null &&
		loggedInUser.getRole() == Role.USER;

%>


<!-- =====================================================
     HEADER
     ===================================================== -->

<header>

	<h1>E-Library</h1>


	<a href="${pageContext.request.contextPath}/books">
		Categories
	</a>


	<a href="${pageContext.request.contextPath}/books/category?id=<%=book.getCategoryId()%>">
		Back to Books
	</a>


	<%

	if (isAdmin) {

	%>


		<a href="${pageContext.request.contextPath}/books/add">
			Add Book
		</a>


	<%

	}

	%>

</header>


<!-- =====================================================
     MAIN CONTAINER
     ===================================================== -->

<div class="container">


	<!-- =================================================
	     BOOK DETAILS
	     ================================================= -->

	<div class="book-details">


		<!-- ================= COVER ================= -->

		<div class="cover-container">


			<%

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


		<!-- ================= INFORMATION ================= -->

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


			<!-- CATEGORY -->

			<div class="book-info">

				<strong>Category:</strong>

				<%=categoryName != null ? categoryName : "Unknown"%>

			</div>


			<!-- PUBLISHED -->

			<div class="book-info">

				<strong>Published:</strong>

				<%=book.getPublishedAt() != null
						? book.getPublishedAt()
						: "Not available"%>

			</div>


			<!-- VIEWS -->

			<div class="book-info">

				<strong>Views:</strong>

				<%=book.getViews() != null
						? book.getViews()
						: 0%>

			</div>


			<!-- RATING -->

			<div class="book-info">

				<strong>Rating:</strong>


				<%

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


			<!-- ================= DESCRIPTION ================= -->

			<div class="description">

				<h3>
					Description
				</h3>


				<%

				if (book.getDescription() != null &&
						!book.getDescription().isBlank()) {

				%>


					<%=book.getDescription()%>


				<%

				} else {

				%>


					No description available.


				<%

				}

				%>


			</div>


			<!-- ================= BOOK ACTIONS ================= -->

			<div class="book-actions">


				<!-- ================= BOOKMARK BOOK ================= -->

				<%

				if (isUser) {

				%>


					<button
						type="button"
						id="bookBookmarkButton"
						class="bookmark-button"
						data-book-id="<%=book.getBookId()%>"
						onclick="toggleBookBookmark(this)">

						🔖 Bookmark

					</button>


				<%

				}

				%>


				<!-- ================= READ BOOK ================= -->

				<%

				if (book.getPdfStorageKey() != null &&
						!book.getPdfStorageKey().isBlank()) {

				%>


					<a
						class="read-button"
						href="${pageContext.request.contextPath}/books/read?id=<%=book.getBookId()%>">

						Read Book

					</a>


				<%

				} else {

				%>


					<p>
						Book file is not available.
					</p>


				<%

				}

				%>


			</div>


			<!-- =================================================
			     ADMIN BOOK ACTIONS
			     ================================================= -->

			<%

			if (isAdmin) {

			%>


				<div class="admin-actions">

					<h3>
						Admin Actions
					</h3>


					<!-- EDIT BOOK -->

					<a
						class="admin-button"
						href="${pageContext.request.contextPath}/admin/books/edit?id=<%=book.getBookId()%>">

						Edit Book

					</a>


					<!-- DELETE BOOK -->

					<form
						action="${pageContext.request.contextPath}/admin/books/delete"
						method="post"
						style="display: inline;">


						<input
							type="hidden"
							name="bookId"
							value="<%=book.getBookId()%>">


						<button
							type="submit"
							class="admin-button delete-button"
							onclick="return confirm('Are you sure you want to delete this book?');">

							Delete Book

						</button>


					</form>


				</div>


			<%

			}

			%>


		</div>


	</div>


	<!-- =================================================
	     RATING SECTION
	     ================================================= -->

	<%

	if (loggedInUser != null && !isAdmin) {

	%>


	<div class="rating-section">


		<h3>
			Your Rating
		</h3>


		<!-- CURRENT USER RATING -->

		<div class="current-rating">


			<%

			if (userRating != null) {

			%>


				You rated this book:

				<strong>
					<%=userRating.getRating()%> / 5
				</strong>


			<%

			} else {

			%>


				You have not rated this book yet.


			<%

			}

			%>


		</div>


		<!-- TOUCH STAR RATING -->

		<div class="rating-input">


			<p>
				<strong>
					Choose your rating:
				</strong>
			</p>


			<form
				action="${pageContext.request.contextPath}/rating"
				method="post"
				id="ratingForm">


				<input
					type="hidden"
					name="bookId"
					value="<%=book.getBookId()%>">


				<input
					type="hidden"
					name="rating"
					id="ratingValue"
					value="">


				<!-- STARS -->

				<div class="touch-stars">


					<%

					for (int star = 1; star <= 5; star++) {

					%>


						<span
							class="touch-star"
							data-rating="<%=star%>">


							<%

							if (userRating != null &&
									star <= userRating.getRating()) {

							%>

								★

							<%

							} else {

							%>

								☆

							<%

							}

							%>


						</span>


					<%

					}

					%>


				</div>


				<!-- SUBMIT -->

				<button
					type="submit"
					id="ratingSubmit"
					disabled>

					<%=userRating == null
							? "Submit Rating"
							: "Update Rating"%>

				</button>


			</form>


		</div>


	</div>


	<%

	}

	%>


	<!-- =================================================
	     COMMENTS SECTION
	     ================================================= -->

	<div class="comments-section">


		<h2>
			Comments
		</h2>


		<!-- EXISTING COMMENTS -->

		<%

		if (comments == null || comments.isEmpty()) {

		%>


			<p>
				No comments yet.
			</p>


		<%

		} else {

			for (Comment comment : comments) {

		%>


			<div class="comment">


				<!-- COMMENT USER -->

				<strong>


					<%

					String commentUserName =
							commentUserNameMap != null
							? commentUserNameMap.get(
									comment.getUserId())
							: null;


					if (commentUserName != null) {

					%>


						<%=commentUserName%>


					<%

					} else {

					%>


						User #<%=comment.getUserId()%>


					<%

					}

					%>


				</strong>


				<!-- COMMENT TEXT -->

				<div class="comment-text">

					<%=comment.getComment()%>

				</div>


				<!-- COMMENT DATE -->

				<div class="comment-date">

					<%=comment.getCreatedAt()%>

				</div>


				<%

				boolean isCommentOwner =
						loggedInUser != null &&
						loggedInUser.getUserId().equals(
								comment.getUserId());


				/* USER can edit own comment */

				if (isCommentOwner && !isAdmin) {

				%>


					<div class="comment-actions">


						<button
							type="button"
							onclick="showEditForm(<%=comment.getCommentId()%>)">

							Edit

						</button>


						<!-- HIDDEN EDIT FORM -->

						<div
							id="edit-form-<%=comment.getCommentId()%>"
							class="edit-form"
							style="display: none;">


							<form
								action="${pageContext.request.contextPath}/comment"
								method="post">


								<input
									type="hidden"
									name="action"
									value="update">


								<input
									type="hidden"
									name="commentId"
									value="<%=comment.getCommentId()%>">


								<input
									type="hidden"
									name="bookId"
									value="<%=book.getBookId()%>">


								<textarea
									name="comment"
									required><%=comment.getComment()%></textarea>


								<br>


								<button type="submit">
									Save
								</button>


								<button
									type="button"
									onclick="hideEditForm(<%=comment.getCommentId()%>)">

									Cancel

								</button>


							</form>


						</div>


					</div>


				<%

				}


				/* USER can delete own comment
				 * ADMIN can delete any comment
				 */

				if (isCommentOwner || isAdmin) {

				%>


					<form
						action="${pageContext.request.contextPath}/comment"
						method="post"
						style="display: inline;">


						<input
							type="hidden"
							name="action"
							value="delete">


						<input
							type="hidden"
							name="commentId"
							value="<%=comment.getCommentId()%>">


						<input
							type="hidden"
							name="bookId"
							value="<%=book.getBookId()%>">


						<button
							type="submit"
							onclick="return confirm('Delete this comment?');">

							Delete

						</button>


					</form>


				<%

				}

				%>


			</div>


		<%

			}

		}

		%>


		<!-- =================================================
		     ADD COMMENT
		     ================================================= -->

		<%

		if (loggedInUser != null) {

		%>


			<div class="comment-form">


				<h3>
					Add Comment
				</h3>


				<form
					action="${pageContext.request.contextPath}/comment"
					method="post">


					<input
						type="hidden"
						name="action"
						value="add">


					<input
						type="hidden"
						name="bookId"
						value="<%=book.getBookId()%>">


					<textarea
						name="comment"
						placeholder="Write your comment..."
						required></textarea>


					<br>


					<button type="submit">
						Add Comment
					</button>


				</form>


			</div>


		<%

		} else {

		%>


			<p>
				Login to rate the book or add comments.
			</p>


		<%

		}

		%>


	</div>


</div>


<!-- =====================================================
     JAVASCRIPT
     ===================================================== -->


<script>


/* =====================================================
 * BOOK LEVEL BOOKMARK
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
 * LOAD BOOKMARK STATUS
 * ===================================================== */

document.addEventListener(
		"DOMContentLoaded",
		function() {

			const button =
					document.getElementById(
							"bookBookmarkButton"
					);


			if (!button) {
				return;
			}


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


/* =====================================================
 * STAR RATING
 * ===================================================== */

const stars =
		document.querySelectorAll(".touch-star");


const ratingValue =
		document.getElementById("ratingValue");


const ratingSubmit =
		document.getElementById("ratingSubmit");


stars.forEach(function(star) {

	star.addEventListener("click", function() {

		const selectedRating =
				parseInt(this.dataset.rating);


		ratingValue.value =
				selectedRating;


		ratingSubmit.disabled =
				false;


		stars.forEach(function(s) {

			const starRating =
					parseInt(s.dataset.rating);


			if (starRating <= selectedRating) {

				s.textContent = "★";

			} else {

				s.textContent = "☆";

			}

		});

	});

});


/* =====================================================
 * SHOW EDIT FORM
 * ===================================================== */

function showEditForm(commentId) {

	const editForm =
			document.getElementById(
					"edit-form-" + commentId
			);


	if (editForm) {

		editForm.style.display =
				"block";

	}

}


/* =====================================================
 * HIDE EDIT FORM
 * ===================================================== */

function hideEditForm(commentId) {

	const editForm =
			document.getElementById(
					"edit-form-" + commentId
			);


	if (editForm) {

		editForm.style.display =
				"none";

	}

}

</script>


</body>

</html>