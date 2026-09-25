<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.project.elibrary.bean.bookrequest.BookRequest"%>
<%@ page import="com.project.elibrary.bean.user.User"%>
<%@ page import="com.project.elibrary.bean.enums.Role"%>
<%@ page
	import="com.project.elibrary.service.bookrequestservice.BookRequestVoteService"%>

<%
List<BookRequest> requests = (List<BookRequest>) request.getAttribute("requests");

Integer requestCount = (Integer) request.getAttribute("requestCount");

String error = (String) request.getAttribute("error");

String success = (String) request.getAttribute("success");

String voteSuccess = (String) session.getAttribute("voteSuccess");

String voteError = (String) session.getAttribute("voteError");

String deleteSuccess = (String) session.getAttribute("deleteSuccess");

String deleteError = (String) session.getAttribute("deleteError");

session.removeAttribute("voteSuccess");
session.removeAttribute("voteError");
session.removeAttribute("deleteSuccess");
session.removeAttribute("deleteError");

User loggedInUser = (User) session.getAttribute("loggedInUser");

boolean isAdmin = loggedInUser != null && loggedInUser.getRole() == Role.ADMIN;

int totalRequests = requestCount != null ? requestCount : 0;

int remainingRequests = Math.max(0, 10 - totalRequests);

BookRequestVoteService voteService = new BookRequestVoteService();
%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<meta name="theme-color" content="#2C1810">

<title>Book Requests | Stories</title>

<!-- Google Font -->
<link rel="preconnect" href="https://fonts.googleapis.com">

<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

<link
	href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&family=Playfair+Display:ital,wght@0,400;0,500;0,600;1,400;1,500&display=swap"
	rel="stylesheet">

<!-- Lucide -->
<script src="https://unpkg.com/lucide@latest"></script>


<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/confirm-modal.css">
	
	<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/background.css">

<!-- Book Request CSS -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/book-request.css">

</head>

<body>

	<div class="page-shell">

		<!-- =====================================================  
         HEADER
    ====================================================== -->

		<header class="site-header">

			<div class="header-inner">

				<!-- BRAND -->

				<!-- BRAND -->

				<a href="${pageContext.request.contextPath}/user/dashboard"
					class="brand"> <span class="brand-icon"> <i
						data-lucide="library"></i>
				</span> <span class="brand-text"> <span class="brand-name">
							Stories </span> <span class="brand-subtitle"> E-LIBRARY </span>

				</span>

				</a>


				<!-- DESKTOP NAV -->

				<nav class="desktop-nav">

					<a href="${pageContext.request.contextPath}/user/dashboard"> <i
						data-lucide="layout-dashboard"></i> <span>Dashboard</span>
					</a> <a href="${pageContext.request.contextPath}/books"> <i
						data-lucide="library"></i> <span>Categories</span>
					</a> <a href="${pageContext.request.contextPath}/book-request"
						class="active"> <i data-lucide="book-plus"></i> <span>Book
							Requests</span>
					</a> <a href="${pageContext.request.contextPath}/books/bookmark-book">
						<i data-lucide="bookmark"></i> <span>Bookmarks</span>
					</a> <a href="${pageContext.request.contextPath}/user/suggestion">
						<i data-lucide="message-square-plus"></i> <span>Suggestions</span>
					</a> <a href="${pageContext.request.contextPath}/books/history"> <i
						data-lucide="history"></i> <span>History</span>
					</a> <a href="${pageContext.request.contextPath}/profile"> <i
						data-lucide="user"></i> <span>Profile</span>
					</a>

				</nav>


				<!-- RIGHT SIDE -->

				<div class="header-actions">

					<a href="${pageContext.request.contextPath}/logout"
						class="logout-link"> <i data-lucide="log-out"></i> <span>Logout</span>

					</a>


					<button type="button" class="mobile-menu-button"
						id="mobileMenuButton" aria-label="Open navigation"
						aria-expanded="false">
						<i data-lucide="menu"></i>
					</button>

				</div>

			</div>


			<!-- MOBILE NAV -->

			<nav class="mobile-nav" id="mobileNav">

				<a href="${pageContext.request.contextPath}/user/dashboard"> <i
					data-lucide="layout-dashboard"></i> Dashboard
				</a> <a href="${pageContext.request.contextPath}/books"> <i
					data-lucide="library"></i> Categories
				</a> <a href="${pageContext.request.contextPath}/book-request"
					class="active"> <i data-lucide="book-plus"></i> Book Requests
				</a> <a href="${pageContext.request.contextPath}/books/bookmark-book">
					<i data-lucide="bookmark"></i> Bookmarks
				</a> <a href="${pageContext.request.contextPath}/user/suggestion"> <i
					data-lucide="message-square-plus"></i> Suggestions
				</a> <a href="${pageContext.request.contextPath}/books/history"> <i
					data-lucide="history"></i> History
				</a> <a href="${pageContext.request.contextPath}/profile"> <i
					data-lucide="user"></i> Profile
				</a> <a href="${pageContext.request.contextPath}/logout"> <i
					data-lucide="log-out"></i> Logout
				</a>

			</nav>

		</header>


		<!-- =====================================================
         MAIN
    ====================================================== -->

		<main class="main-content">

			<!-- PAGE INTRO -->

			<section class="page-intro">

				<div class="intro-copy">

					<span class="eyebrow"> COMMUNITY LIBRARY </span>

					<h1>
						Request a <em>book.</em>
					</h1>

					<p>Can't find the book you're looking for? Request it here and
						let the community help decide what should be added next.</p>

				</div>


				<!-- REQUEST COUNTER -->

				<div class="request-counter">

					<div class="counter-icon">
						<i data-lucide="library-big"></i>
					</div>

					<div class="counter-content">

						<span class="counter-label"> WEEKLY REQUESTS </span>

						<div class="counter-number">

							<strong> <%=totalRequests%>
							</strong> <span>/ 10</span>

						</div>

						<div class="progress-track">

							<div class="progress-bar"
								style="width: <%=(totalRequests * 10)%>%;"></div>

						</div>

						<span class="counter-note"> <%=remainingRequests%> request<%=remainingRequests == 1 ? "" : "s"%>
							remaining

						</span>

					</div>

				</div>

			</section>


			<!-- =================================================
             MESSAGES
        ================================================== -->

			<%
			if (error != null) {
			%>

			<div class="alert alert-error" role="alert">

				<span class="alert-icon"> <i data-lucide="circle-alert"></i>
				</span>

				<div>
					<strong>Request not submitted</strong>
					<p><%=error%></p>
				</div>

				<button type="button" class="alert-close" aria-label="Close message">

					<i data-lucide="x"></i>

				</button>

			</div>

			<%
			}
			%>


			<%
			if (success != null) {
			%>

			<div class="alert alert-success" role="alert">

				<span class="alert-icon"> <i data-lucide="circle-check"></i>
				</span>

				<div>
					<strong>Request submitted</strong>
					<p><%=success%></p>
				</div>

				<button type="button" class="alert-close" aria-label="Close message">

					<i data-lucide="x"></i>

				</button>

			</div>

			<%
			}
			%>


			<%
			if (voteSuccess != null) {
			%>

			<div class="alert alert-success" role="alert">

				<span class="alert-icon"> <i data-lucide="circle-check"></i>
				</span>

				<div>
					<strong>Vote added</strong>
					<p><%=voteSuccess%></p>
				</div>

				<button type="button" class="alert-close" aria-label="Close message">

					<i data-lucide="x"></i>

				</button>

			</div>

			<%
			}
			%>


			<%
			if (voteError != null) {
			%>

			<div class="alert alert-error" role="alert">

				<span class="alert-icon"> <i data-lucide="circle-alert"></i>
				</span>

				<div>
					<strong>Vote not added</strong>
					<p><%=voteError%></p>
				</div>

				<button type="button" class="alert-close" aria-label="Close message">

					<i data-lucide="x"></i>

				</button>

			</div>

			<%
			}
			%>


			<%
			if (deleteSuccess != null) {
			%>

			<div class="alert alert-success" role="alert">

				<span class="alert-icon"> <i data-lucide="circle-check"></i>
				</span>

				<div>
					<strong>Request deleted</strong>
					<p><%=deleteSuccess%></p>
				</div>

				<button type="button" class="alert-close" aria-label="Close message">

					<i data-lucide="x"></i>

				</button>

			</div>

			<%
			}
			%>


			<%
			if (deleteError != null) {
			%>

			<div class="alert alert-error" role="alert">

				<span class="alert-icon"> <i data-lucide="circle-alert"></i>
				</span>

				<div>
					<strong>Unable to delete</strong>
					<p><%=deleteError%></p>
				</div>

				<button type="button" class="alert-close" aria-label="Close message">

					<i data-lucide="x"></i>

				</button>

			</div>

			<%
			}
			%>


			<!-- =================================================
             REQUEST FORM
        ================================================== -->

			<%
			if (!isAdmin && totalRequests < 10) {
			%>

			<section class="request-form-card">

				<div class="card-heading">

					<div class="heading-icon">
						<i data-lucide="pen-line"></i>
					</div>

					<div>

						<span class="section-label"> ADD TO THE LIBRARY </span>

						<h2>What should we read next?</h2>

						<p>Tell us about a book you'd like to see in Stories.</p>

					</div>

				</div>


				<form action="${pageContext.request.contextPath}/book-request"
					method="post" class="request-form" id="bookRequestForm">


					<div class="form-grid">

						<!-- TITLE -->

						<div class="form-field field-title">

							<label for="title"> Book Title <span>*</span>
							</label>

							<div class="input-shell">

								<i data-lucide="book-open"></i> <input type="text" id="title"
									name="title" placeholder="Enter the book title" maxlength="255"
									autocomplete="off" required>

							</div>

						</div>


						<!-- AUTHOR -->

						<div class="form-field">

							<label for="author"> Author </label>

							<div class="input-shell">

								<i data-lucide="user-round"></i> <input type="text" id="author"
									name="author" placeholder="Enter the author name"
									maxlength="255" autocomplete="off">

							</div>

						</div>

					</div>


					<div class="form-footer">

						<span class="form-hint"> <i data-lucide="info"></i>
							Requests are reviewed weekly.
						</span>

						<button type="submit" class="submit-button">

							<span> Submit Request </span> <i data-lucide="arrow-up-right"></i>

						</button>

					</div>

				</form>

			</section>

			<%
			} else if (!isAdmin && totalRequests >= 10) {
			%>

			<section class="limit-card">

				<div class="limit-icon">
					<i data-lucide="circle-check"></i>
				</div>

				<div>

					<span class="section-label"> WEEKLY LIMIT REACHED </span>

					<h2>All 10 requests are in.</h2>

					<p>Check back next week to submit another book request.</p>

				</div>

			</section>

			<%
			}
			%>


			<!-- =================================================
             REQUEST LIST
        ================================================== -->

			<section class="requests-section">

				<div class="section-heading">

					<div>

						<span class="section-label"> THIS WEEK </span>

						<h2>Requested Books</h2>

					</div>

					<div class="request-count-pill">

						<i data-lucide="layers"></i> <span> <%=totalRequests%> / 10
						</span>

					</div>

				</div>


				<%
				if (requests != null && !requests.isEmpty()) {
				%>

				<div class="request-grid">

					<%
					for (BookRequest bookRequest : requests) {

						int voteCount = voteService.getVoteCount(bookRequest.getRequestId());
					%>

					<article class="request-card">

						<div class="request-card-top">

							<div class="book-number">

								<i data-lucide="book-marked"></i>

							</div>

							<span class="request-week"> THIS WEEK </span>

						</div>


						<div class="request-card-content">

							<h3>
								<%=bookRequest.getTitle()%>
							</h3>


							<%
							if (bookRequest.getAuthor() != null && !bookRequest.getAuthor().trim().isEmpty()) {
							%>

							<p class="author">

								<i data-lucide="user"></i> <span> <%=bookRequest.getAuthor()%>
								</span>

							</p>

							<%
							} else {
							%>

							<p class="author muted">

								<i data-lucide="user"></i> <span> Author not specified </span>

							</p>

							<%
							}
							%>


							<div class="request-meta">

								<span> <i data-lucide="calendar-days"></i> <%=bookRequest.getCreatedAt() != null ? bookRequest.getCreatedAt() : "Recently requested"%>

								</span>

							</div>

						</div>


						<div class="request-card-footer">

							<div class="vote-count">

								<span class="vote-icon"> <i data-lucide="heart"></i>
								</span>

								<div>

									<strong> <%=voteCount%>
									</strong> <small> vote<%=voteCount == 1 ? "" : "s"%>
									</small>

								</div>

							</div>


							<%
							if (isAdmin) {
							%>

							<form
								action="${pageContext.request.contextPath}/book-request/delete"
								method="post" class="action-form delete-form">

								<input type="hidden" name="requestId"
									value="<%=bookRequest.getRequestId()%>">

								<button type="submit" class="delete-button"
									data-confirm="Are you sure you want to delete this book request? This action cannot be undone."
									data-confirm-title="Delete Book Request?"
									data-confirm-label="Delete">

									<i data-lucide="trash-2"></i> <span>Delete</span>
								</button>
							</form>

							<%
							} else {
							%>

							<form
								action="${pageContext.request.contextPath}/book-request/vote"
								method="post" class="action-form">

								<input type="hidden" name="requestId"
									value="<%=bookRequest.getRequestId()%>">

								<button type="submit" class="vote-button">

									<i data-lucide="heart"></i> <span>Vote</span>

								</button>

							</form>

							<%
							}
							%>

						</div>

					</article>

					<%
					}
					%>

				</div>

				<%
				} else {
				%>

				<div class="empty-state">

					<div class="empty-icon">

						<i data-lucide="book-open"></i>

					</div>

					<span class="section-label"> A QUIET SHELF </span>

					<h2>No requests yet.</h2>

					<p>Be the first reader to request a book for this week's
						community list.</p>

				</div>

				<%
				}
				%>

			</section>

		</main>


		<!-- =====================================================
         FOOTER
    ====================================================== -->

		<footer class="site-footer">

			<span> STORIES E-LIBRARY </span> <span class="footer-line"></span> <span>
				READ · DISCOVER · SHARE </span>

		</footer>

	</div>

	<script
		src="${pageContext.request.contextPath}/javascript/book-request.js?v=2"></script>
	<script
		src="${pageContext.request.contextPath}/javascript/confirm-modal.js"></script>

</body>

</html>

