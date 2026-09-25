
<%@ page import="com.project.elibrary.bean.category.Category" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Book | Stories E-Library</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/background.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/add-book.css">

</head>

<body class="add-book-page">

<div class="add-book-container">

    <!-- Top Navigation -->
    <header class="page-header">
        <a href="${pageContext.request.contextPath}/books"
           class="back-link">
            <i data-lucide="arrow-left"></i>
            <span>Back to Books</span>
        </a>

        <div class="header-brand">
            <i data-lucide="book-open"></i>
            <span>Stories <strong>Admin</strong></span>
        </div>
    </header>

    <!-- Page Heading -->
    <section class="page-intro">
        <div class="intro-icon">
            <i data-lucide="book-plus"></i>
        </div>

        <div>
            <span class="eyebrow">LIBRARY MANAGEMENT</span>
            <h1>Add a New Book</h1>
            <p>
                Bring a new story to life. Add book details, upload the cover,
                and make it available in your library.
            </p>
        </div>
    </section>

    <!-- Main Form -->
    <form id="addBookForm"
          action="${pageContext.request.contextPath}/admin/books/add"
          method="post"
          enctype="multipart/form-data">

        <div class="add-book-layout">

            <!-- LEFT: Cover Preview -->
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
                        <div class="cover-placeholder" id="coverPlaceholder">
                            <i data-lucide="book-open"></i>
                            <span>YOUR BOOK</span>
                            <small>Cover preview</small>
                        </div>

                        <img id="coverPreview"
                             class="cover-preview"
                             alt="Selected book cover preview"
                             hidden>
                    </div>
                </div>

                <div class="preview-caption">
                    <span class="preview-dot"></span>
                    <span>Live cover preview</span>
                </div>

                <div class="cover-tip">
                    <i data-lucide="lightbulb"></i>
                    <p>
                        Use a high-quality cover image in JPG, PNG, or WebP
                        format for the best reading experience.
                    </p>
                </div>

            </aside>

            <!-- RIGHT: Book Details -->
            <section class="details-panel">

                <div class="panel-heading">
                    <div>
                        <span class="section-label">BOOK INFORMATION</span>
                        <h2>Book Details</h2>
                    </div>
                    <span class="required-note">
                        <span>*</span> Required fields
                    </span>
                </div>

                <!-- Title -->
                <div class="form-group">
                    <label for="title">
                        Book Title <span>*</span>
                    </label>

                    <div class="input-wrapper">
                        <i data-lucide="book-text"></i>
                        <input type="text"
                               id="title"
                               name="title"
                               placeholder="Enter the book title"
                               maxlength="255"
                               required>
                    </div>
                </div>

                <!-- Author -->
                <div class="form-group">
                    <label for="author">
                        Author Name <span>*</span>
                    </label>

                    <div class="input-wrapper">
                        <i data-lucide="user-round"></i>
                        <input type="text"
                               id="author"
                               name="author"
                               placeholder="Enter the author's name"
                               maxlength="255"
                               required>
                    </div>
                </div>

                <!-- Description -->
                <div class="form-group">
                    <label for="description">
                        Book Description
                        <span class="optional-label">OPTIONAL</span>
                    </label>

                    <div class="textarea-wrapper">
                        <textarea id="description"
                                  name="description"
                                  rows="5"
                                  maxlength="5000"
                                  placeholder="Write a short description about this book..."></textarea>
                        <span class="character-count" id="charCount">0 / 5000</span>
                    </div>
                </div>

                <!-- Category + Published Date -->
                <div class="form-row">

                    <div class="form-group">
                        <label for="categoryId">
                            Category <span>*</span>
                        </label>

                        <div class="input-wrapper select-wrapper">
                            <i data-lucide="layers"></i>

                            <select id="categoryId"
                                    name="categoryId"
                                    required>
                                <option value="">Select a category</option>

                                <%
                                    List<Category> categories =
                                        (List<Category>) request.getAttribute("categories");

                                    if (categories != null) {
                                        for (Category category : categories) {
                                %>
                                    <option value="<%= category.getCategoryId() %>">
                                        <%= category.getCategoryName() %>
                                    </option>
                                <%
                                        }
                                    }
                                %>
                            </select>

                            <i data-lucide="chevron-down" class="select-chevron"></i>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="publishedAt">
                            Published Date
                            <span class="optional-label">OPTIONAL</span>
                        </label>

                        <div class="input-wrapper">
                            <i data-lucide="calendar-days"></i>
                            <input type="date"
                                   id="publishedAt"
                                   name="publishedAt">
                        </div>
                    </div>

                </div>

                <!-- Upload Section -->
                <div class="upload-section">

                    <div class="upload-section-heading">
                        <div>
                            <span class="section-label">FILES & DOCUMENTS</span>
                            <h3>Upload Book Files</h3>
                        </div>
                    </div>

                    <div class="upload-grid">

                        <!-- Cover Upload -->
                        <div class="upload-card">
                            <label for="cover" class="upload-dropzone" id="coverDropzone">

                                <div class="upload-icon cover-upload-icon">
                                    <i data-lucide="image-plus"></i>
                                </div>

                                <strong>Book Cover <span>*</span></strong>
                                <span class="upload-description">
                                    Drag & drop or <b>browse image</b>
                                </span>
                                <small>JPG, PNG, WebP</small>

                            </label>

                            <input type="file"
                                   id="cover"
                                   name="cover"
                                   accept="image/jpeg,image/png,image/webp"
                                   required
                                   hidden>

                            <div class="file-status" id="coverFileStatus">
                                <i data-lucide="file-image"></i>
                                <span>No cover selected</span>
                            </div>
                            
                            <p id="coverError" class="file-error" role="alert" hidden></p>
                        </div>

                        <!-- PDF Upload -->
                        <div class="upload-card">
                            <label for="pdf" class="upload-dropzone" id="pdfDropzone">

                                <div class="upload-icon pdf-upload-icon">
                                    <i data-lucide="file-up"></i>
                                </div>

                                <strong>Book PDF <span>*</span></strong>
                                <span class="upload-description">
                                    Drag & drop or <b>browse PDF</b>
                                </span>
                                <small>PDF documents only</small>

                            </label>

                            <input type="file"
                                   id="pdf"
                                   name="pdf"
                                   accept="application/pdf,.pdf"
                                   required
                                   hidden>

                            <div class="file-status" id="pdfFileStatus">
                                <i data-lucide="file-text"></i>
                                <span>No PDF selected</span>
                            </div>
                            <p id="pdfError" class="file-error" role="alert" hidden></p>
                        </div>

                    </div>
                </div>

                <!-- Form Actions -->
                <div class="form-actions">

                    <a href="${pageContext.request.contextPath}/books"
                       class="btn-cancel">
                        Cancel
                    </a>

                    <button type="submit" class="btn-submit" id="submitBtn">
                        <i data-lucide="plus-circle"></i>
                        <span>Add Book to Library</span>
                    </button>

                </div>

            </section>
        </div>
    </form>

    <footer class="page-footer">
        <i data-lucide="book-open"></i>
        <span>Stories E-Library</span>
        <span class="footer-separator">•</span>
        <span>Library Management</span>
    </footer>

</div>
	<!-- Lucide Icons -->
<script src="https://unpkg.com/lucide@latest"></script>

<!-- Initialize Lucide Icons -->
<script>
    document.addEventListener("DOMContentLoaded", function () {
        if (window.lucide) {
            lucide.createIcons();
        } else {
            console.error("Lucide library failed to load.");
        }
    });
</script>

<!-- Your page JavaScript -->
<script src="${pageContext.request.contextPath}/javascript/add-book.js"></script>	
</body>
</html>