<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.project.elibrary.bean.book.Book"%>
<%@ page import="com.project.elibrary.bean.category.Category"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Edit Book | Stories E-Library</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/background.css">

<!-- Reuse the exact Add Book design -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/add-book.css">

<!-- Edit-specific adjustments -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/edit-book.css">
</head>

<body class="add-book-page">

	<%
	Book book = (Book) request.getAttribute("book");

	List<Category> categories = (List<Category>) request.getAttribute("categories");

	String error = (String) request.getAttribute("error");

	// This must be a browser-accessible URL, not an S3 storage key.
	String coverUrl = (String) request.getAttribute("coverUrl");

	if (book == null) {
		throw new ServletException("Book information is missing.");
	}
	%>

	<div class="add-book-container">

		<!-- TOP NAVIGATION -->
		<header class="page-header">

			<a href="${pageContext.request.contextPath}/books" class="back-link">
				<i data-lucide="arrow-left"></i> <span>Back to Books</span>
			</a>

			<div class="header-brand">
				<i data-lucide="book-open"></i> <span>Stories <strong>Admin</strong></span>
			</div>

		</header>

		<!-- PAGE HEADING -->
		<section class="page-intro">

			<div class="intro-icon">
				<i data-lucide="pen"></i>
			</div>

			<div>
				<span class="eyebrow">LIBRARY MANAGEMENT</span>
				<h1>Edit Book</h1>
				<p>Update your book details, replace its cover, or upload a new
					PDF. Leave files unchanged to keep the existing ones.</p>
			</div>

		</section>

		<!-- ERROR MESSAGE -->
		<%
		if (error != null && !error.isBlank()) {
		%>
		<div class="edit-error-message" role="alert">
			<i data-lucide="alert-circle"></i> <span><%=error%></span>
		</div>
		<%
		}
		%>

		<!-- MAIN FORM -->
		<form id="editBookForm"
			action="${pageContext.request.contextPath}/admin/books/edit"
			method="post" enctype="multipart/form-data">

			<input type="hidden" name="bookId" value="<%=book.getBookId()%>">

			<div class="add-book-layout">

				<!-- ========================================
                 LEFT: COVER PREVIEW
            ========================================= -->
				<aside class="cover-panel">

					<div class="panel-heading">
						<div>
							<span class="section-label">BOOK PREVIEW</span>
							<h2>Your Book Cover</h2>
						</div>
						<i data-lucide="image"></i>
					</div>

					<div class="cover-stage">

						<div class="book-mockup" id="bookMockup">

							<div class="cover-placeholder" id="coverPlaceholder"
								<%=coverUrl != null && !coverUrl.isBlank() ? "hidden" : ""%>>

								<i data-lucide="book-open"></i> <span>YOUR BOOK</span> <small>Cover
									preview</small>
							</div>

							<img id="coverPreview" class="cover-preview"
								alt="Book cover preview"
								src="<%=coverUrl != null ? coverUrl : ""%>"
								<%=coverUrl == null || coverUrl.isBlank() ? "hidden" : ""%>>
						</div>

					</div>

					<div class="preview-caption">
						<span class="preview-dot"></span> <span id="previewCaptionText">
							<%=coverUrl != null && !coverUrl.isBlank() ? "Current cover preview" : "No cover preview available"%>
						</span>
					</div>

					<div class="cover-tip">
						<i data-lucide="lightbulb"></i>
						<p>Select a new cover to preview it here. If you do not upload
							a replacement, the existing cover will remain.</p>
					</div>

				</aside>

				<!-- ========================================
                 RIGHT: BOOK DETAILS
            ========================================= -->
				<section class="details-panel">

					<div class="panel-heading">
						<div>
							<span class="section-label">BOOK INFORMATION</span>
							<h2>Book Details</h2>
						</div>

						<span class="required-note"> <span>*</span> Required fields
						</span>
					</div>

					<!-- TITLE -->
					<div class="form-group">
						<label for="title"> Book Title <span>*</span>
						</label>

						<div class="input-wrapper">
							<i data-lucide="book-text"></i> <input type="text" id="title"
								name="title"
								value="<%=book.getTitle() != null ? book.getTitle() : ""%>"
								placeholder="Enter the book title" maxlength="255" required>
						</div>
					</div>

					<!-- AUTHOR -->
					<div class="form-group">
						<label for="author"> Author Name <span>*</span>
						</label>

						<div class="input-wrapper">
							<i data-lucide="user-round"></i> <input type="text" id="author"
								name="author"
								value="<%=book.getAuthor() != null ? book.getAuthor() : ""%>"
								placeholder="Enter the author's name" maxlength="255" required>
						</div>
					</div>

					<!-- DESCRIPTION -->
					<div class="form-group">
						<label for="description"> Book Description <span
							class="optional-label">OPTIONAL</span>
						</label>

						<div class="textarea-wrapper">
							<textarea id="description" name="description" rows="5"
								maxlength="5000"
								placeholder="Write a short description about this book..."><%=book.getDescription() != null ? book.getDescription() : ""%></textarea>

							<span class="character-count" id="charCount"> <%=book.getDescription() != null ? book.getDescription().length() : 0%>
								/ 5000
							</span>
						</div>
					</div>

					<!-- CATEGORY + PUBLISHED DATE -->
					<div class="form-row">

						<div class="form-group">
							<label for="categoryId"> Category <span>*</span>
							</label>

							<div class="input-wrapper select-wrapper">
								<i data-lucide="layers"></i> <select id="categoryId"
									name="categoryId" required>
									<option value="">Select a category</option>

									<%
									if (categories != null) {
										for (Category category : categories) {
									%>

									<option value="<%=category.getCategoryId()%>"
										<%=category.getCategoryId() == book.getCategoryId() ? "selected" : ""%>>
										<%=category.getCategoryName()%>
									</option>

									<%
									}
									}
									%>
								</select> <i data-lucide="chevron-down" class="select-chevron"></i>
							</div>
						</div>

						<div class="form-group">
							<label for="publishedAt"> Published Date <span
								class="optional-label">OPTIONAL</span>
							</label>

							<div class="input-wrapper">
								<i data-lucide="calendar-days"></i> <input type="date"
									id="publishedAt" name="publishedAt"
									value="<%=book.getPublishedAt() != null ? book.getPublishedAt().toString() : ""%>">
							</div>
						</div>

					</div>

					<!-- ========================================
                     FILE UPLOAD SECTION
                ========================================= -->
					<div class="upload-section">

						<div class="upload-section-heading">
							<div>
								<span class="section-label">FILES & DOCUMENTS</span>
								<h3>Replace Book Files</h3>
							</div>
						</div>

						<p class="edit-upload-note">Both files are optional. Leave a
							field empty to keep the existing file.</p>

						<div class="upload-grid">

							<!-- COVER UPLOAD -->
							<div class="upload-card">

								<label for="cover" class="upload-dropzone" id="coverDropzone">

									<div class="upload-icon cover-upload-icon">
										<i data-lucide="image-plus"></i>
									</div> <strong>New Book Cover</strong> <span
									class="upload-description"> Drag & drop or <b>browse
											image</b>
								</span> <small>JPG, PNG, WebP</small>
								</label> <input type="file" id="cover" name="cover"
									accept="image/jpeg,image/png,image/webp,.jpg,.jpeg,.png,.webp"
									hidden>

								<div class="file-status" id="coverFileStatus">
									<i data-lucide="file-image"></i> <span>No replacement
										selected</span>
								</div>

								<p class="file-error" id="coverError"></p>
							</div>

							<!-- PDF UPLOAD -->
							<div class="upload-card">

								<label for="pdf" class="upload-dropzone" id="pdfDropzone">

									<div class="upload-icon pdf-upload-icon">
										<i data-lucide="file-up"></i>
									</div> <strong>New Book PDF</strong> <span class="upload-description">
										Drag & drop or <b>browse PDF</b>
								</span> <small>PDF documents only</small>
								</label> <input type="file" id="pdf" name="pdf"
									accept="application/pdf,.pdf" hidden>

								<div class="file-status" id="pdfFileStatus">
									<i data-lucide="file-text"></i> <span>No replacement
										selected</span>
								</div>

								<p class="file-error" id="pdfError"></p>
							</div>

						</div>
					</div>

					<!-- FORM ACTIONS -->
					<div class="form-actions">

						<a href="${pageContext.request.contextPath}/books"
							class="btn-cancel"> Cancel </a>

						<button type="submit" class="btn-submit" id="submitBtn">

							<i data-lucide="save"></i> <span>Update Book</span>
						</button>

					</div>

				</section>
			</div>
		</form>

		<!-- FOOTER -->
		<footer class="page-footer">
			<i data-lucide="book-open"></i> <span>Stories E-Library</span> <span
				class="footer-separator">•</span> <span>Library Management</span>
		</footer>

	</div>

<!-- Lucide Icons Library -->
<script src="https://unpkg.com/lucide@latest"></script>

<!-- Edit Book Script -->
<script
    src="${pageContext.request.contextPath}/javascript/edit-book.js"
    defer>
</script>

</body>
</html>