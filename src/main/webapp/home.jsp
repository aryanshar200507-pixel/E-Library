<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.project.elibrary.bean.book.Book"%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>E-Library | Home</title>

<!-- Google Fonts: Playfair Display (bookish serif) + Inter (clean body) -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Playfair+Display:wght@600;700;800&family=Inter:wght@400;500;600;700&display=swap"
	rel="stylesheet">

<!-- Stylesheet -->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/css/home.css">

</head>

<body>
	

	<!-- ================= NAVBAR ================= -->

	<nav class="navbar" id="navbar">

		<a href="<%=request.getContextPath()%>/home" class="logo"> <i
			data-lucide="book-marked" class="logo-icon"></i> <span>E-Library</span>
		</a>

		<button class="nav-toggle" id="navToggle"
			aria-label="Toggle navigation">
			<i data-lucide="menu"></i>
		</button>

		<div class="nav-links" id="navLinks">

			<a href="<%=request.getContextPath()%>/home"> Home </a> <a
				href="<%=request.getContextPath()%>/books"> Books </a> <a
				href="#latest-books"> Explore </a>

			<div class="nav-auth">

				<a href="<%=request.getContextPath()%>/login.jsp"
					class="nav-login"> Login </a> <a
					href="<%=request.getContextPath()%>/Register.jsp"
					class="nav-register"> Register </a>

			</div>

		</div>

	</nav>


	<!-- ================= HERO SECTION ================= -->

	<section class="hero">

		<div class="hero-bg-shapes" aria-hidden="true">
			<i data-lucide="book-open" class="float-icon icon-1"></i> <i
				data-lucide="bookmark" class="float-icon icon-2"></i> <i
				data-lucide="feather" class="float-icon icon-3"></i> <i
				data-lucide="scroll-text" class="float-icon icon-4"></i>
		</div>

		<div class="hero-content">

			<span class="hero-eyebrow"> <i data-lucide="sparkles"></i>
				Your story starts here
			</span>

			<h1>
				A Library Without <span class="highlight">Walls</span>
			</h1>

			<p>Read online and lose yourself in a growing shelf of stories
				&mdash; anytime, anywhere.</p>

			<div class="hero-actions">

				<a href="#latest-books" class="btn btn-primary"> <i
					data-lucide="compass"></i> Explore Books
				</a> <a href="<%=request.getContextPath()%>/Register.jsp"
					class="btn btn-secondary"> <i data-lucide="user-plus"></i> Join
					the Library
				</a>

			</div>

			<div class="hero-stats">

				<div class="stat">
					<i data-lucide="library"></i>
					<div>
						<strong>Read Online</strong> <span>No downloads needed</span>
					</div>
				</div>

				<div class="stat">
					<i data-lucide="download"></i>
					<div>
						<strong>PDF Access</strong> <span>Take it offline</span>
					</div>
				</div>

				<div class="stat">
					<i data-lucide="clock"></i>
					<div>
						<strong>Open 24/7</strong> <span>Always on the shelf</span>
					</div>
				</div>

			</div>

		</div>

	</section>


	<!-- ================= BOOK SECTION ================= -->

	<main class="container" id="latest-books">

		<div class="section-title">

			<span class="section-tag"> <i data-lucide="sparkle"></i> Fresh
				on the Shelf
			</span>

			<h2>Latest Books</h2>

			<p>Explore the latest books available in our library.</p>

		</div>


		<%
		List<Book> books = (List<Book>) request.getAttribute("books");

		Map<Long, String> bookCoverUrlMap = (Map<Long, String>) request.getAttribute("bookCoverUrlMap");

		if (books != null && !books.isEmpty()) {
		%>


		<div class="book-grid">


			<%
			for (Book book : books) {

				String coverUrl = null;

				if (bookCoverUrlMap != null) {

					coverUrl = bookCoverUrlMap.get(book.getBookId());

				}
			%>


			<!-- ================= BOOK CARD ================= -->

			<div class="book-card">


				<div class="book-cover-container">

					<%
					if (coverUrl != null) {
					%>

					<img src="<%=coverUrl%>" alt="<%=book.getTitle()%>"
						class="book-cover">

					<%
					} else {
					%>

					<div class="no-cover">
						<i data-lucide="book-open"></i> <span>No Cover Available</span>
					</div>

					<%
					}
					%>

					<span class="cover-ribbon"> <i data-lucide="bookmark"></i>
					</span>

				</div>


				<div class="book-info">


					<h3 class="book-title">
						<%=book.getTitle()%>
					</h3>


					<p class="book-author">
						<i data-lucide="feather"></i> By
						<%=book.getAuthor()%>
					</p>


					<p class="book-description">
						<%=book.getDescription()%>
					</p>


					<a
						href="<%=request.getContextPath()%>/books/details?id=<%=book.getBookId()%>"
						class="view-button"> <i
						data-lucide="book-open"></i> View Details
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


		<div class="no-books">

			<i data-lucide="library"></i>

			<h3>No books available</h3>

			<p>There are currently no books available in the library.</p>

		</div>


		<%
		}
		%>


	</main>


	<!-- ================= FOOTER ================= -->

	<footer class="footer">

		<div class="footer-content">

			<div class="footer-brand">
				<i data-lucide="book-marked"></i> <span>E-Library</span>
			</div>

			<p>&copy; 2026 E-Library. All rights reserved.</p>

		</div>

	</footer>
	
<!-- Lucide Icons -->
<script src="https://unpkg.com/lucide@latest"></script>
	<!-- Script -->
	<script src="<%=request.getContextPath()%>/javascript/home.js"></script>

</body>

</html>
