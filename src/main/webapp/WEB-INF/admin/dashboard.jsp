<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.project.elibrary.bean.category.Category"%>
<%@ page import="com.project.elibrary.bean.user.User"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">

    <meta name="viewport"
        content="width=device-width, initial-scale=1.0">

    <title>Stories — Admin Dashboard</title>

    <!-- Lucide Icons -->
    <script src="https://unpkg.com/lucide@latest"></script>

    <!-- Page CSS -->
    <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/admin-dashboard.css">
        
        <link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/background.css">
</head>

<body>

    <!-- =====================================================
         ADMIN HEADER
         ===================================================== -->

    <header class="admin-header">

        <div class="header-brand">

            <a href="${pageContext.request.contextPath}/admin/dashboard"
               class="brand-link">

                <div class="brand-icon">
                    <i data-lucide="library"></i>
                </div>

                <div class="brand-text">
                    <span class="brand-name">Stories</span>
                    <span class="brand-subtitle">E-Library</span>
                </div>

            </a>

        </div>


        <nav class="admin-nav">

            <a href="${pageContext.request.contextPath}/admin/dashboard"
               class="nav-link active">

                <i data-lucide="layout-dashboard"></i>
                <span>Dashboard</span>

            </a>


            <a href="${pageContext.request.contextPath}/admin/books/add"
               class="nav-link">

                <i data-lucide="book-plus"></i>
                <span>Add Book</span>

            </a>


            <a href="${pageContext.request.contextPath}/books"
               class="nav-link">

                <i data-lucide="library-big"></i>
                <span>Books</span>

            </a>


            <a href="${pageContext.request.contextPath}/admin/suggestions"
               class="nav-link">

                <i data-lucide="message-square-plus"></i>
                <span>Suggestions</span>

            </a>


            <a href="${pageContext.request.contextPath}/logout"
               class="nav-link logout-link">

                <i data-lucide="log-out"></i>
                <span>Logout</span>

            </a>

        </nav>

    </header>


    <!-- =====================================================
         MAIN CONTENT
         ===================================================== -->

    <main class="dashboard-container">


        <!-- =================================================
             PAGE INTRO
             ================================================= -->

        <section class="page-intro">

            <div>

                <span class="eyebrow">
                    ADMIN PANEL
                </span>

                <h1>
                    Welcome back, Admin
                </h1>

                <p>
                    Manage your library, books, categories and users
                    from one place.
                </p>

            </div>

            <div class="intro-icon">

                <i data-lucide="library-big"></i>

            </div>

        </section>


        <!-- =================================================
             OVERVIEW
             ================================================= -->

        <section class="dashboard-section">

            <div class="section-heading">

                <div>

                    <span class="section-label">
                        OVERVIEW
                    </span>

                    <h2>
                        Library Overview
                    </h2>

                </div>

                <div class="section-heading-icon">
                    <i data-lucide="bar-chart-3"></i>
                </div>

            </div>


            <div class="overview-cards">


                <!-- USERS -->

                <div class="overview-card">

                    <div class="overview-icon">
                        <i data-lucide="users"></i>
                    </div>

                    <div class="overview-content">

                        <span>
                            Total Users
                        </span>

                        <strong>
                            ${totalUsers}
                        </strong>

                    </div>

                </div>


                <!-- CATEGORIES -->

                <div class="overview-card">

                    <div class="overview-icon">
                        <i data-lucide="tags"></i>
                    </div>

                    <div class="overview-content">

                        <span>
                            Total Categories
                        </span>

                        <strong>
                            ${totalCategories}
                        </strong>

                    </div>

                </div>


                <!-- BOOKS -->

                <div class="overview-card">

                    <div class="overview-icon">
                        <i data-lucide="book-open"></i>
                    </div>

                    <div class="overview-content">

                        <span>
                            Total Books
                        </span>

                        <strong>
                            ${totalBooks}
                        </strong>

                    </div>

                </div>


            </div>

        </section>


        <!-- =================================================
             BOOK MANAGEMENT
             ================================================= -->

        <section class="dashboard-section">

            <div class="section-heading">

                <div>

                    <span class="section-label">
                        LIBRARY
                    </span>

                    <h2>
                        Book Management
                    </h2>

                </div>

                <div class="section-heading-icon">
                    <i data-lucide="book-copy"></i>
                </div>

            </div>


            <div class="management-grid">


                <!-- ADD BOOK -->

                <div class="management-card">

                    <div class="management-card-icon">
                        <i data-lucide="book-plus"></i>
                    </div>

                    <div class="management-card-content">

                        <h3>
                            Add Book
                        </h3>

                        <p>
                            Add a new book to your E-Library.
                        </p>

                        <a
                            href="${pageContext.request.contextPath}/admin/books/add"
                            class="management-button">

                            <span>Add Book</span>

                            <i data-lucide="arrow-right"></i>

                        </a>

                    </div>

                </div>


                <!-- MANAGE BOOKS -->

                <div class="management-card">

                    <div class="management-card-icon">
                        <i data-lucide="library-big"></i>
                    </div>

                    <div class="management-card-content">

                        <h3>
                            Manage Books
                        </h3>

                        <p>
                            View, edit and delete books.
                        </p>

                        <a
                            href="${pageContext.request.contextPath}/books"
                            class="management-button">

                            <span>Manage Books</span>

                            <i data-lucide="arrow-right"></i>

                        </a>

                    </div>

                </div>


                <!-- BOOK REQUESTS -->

                <div class="management-card">

                    <div class="management-card-icon">
                        <i data-lucide="inbox"></i>
                    </div>

                    <div class="management-card-content">

                        <h3>
                            Book Requests
                        </h3>

                        <p>
                            View and manage weekly book requests.
                        </p>

                        <a
                            href="${pageContext.request.contextPath}/book-request"
                            class="management-button">

                            <span>View Requests</span>

                            <i data-lucide="arrow-right"></i>

                        </a>

                    </div>

                </div>


            </div>

        </section>


        <!-- =================================================
             CATEGORY MANAGEMENT
             ================================================= -->

        <section class="dashboard-section">

            <div class="section-heading">

                <div>

                    <span class="section-label">
                        ORGANIZATION
                    </span>

                    <h2>
                        Category Management
                    </h2>

                </div>

                <div class="section-heading-icon">
                    <i data-lucide="folders"></i>
                </div>

            </div>


            <!-- ADD CATEGORY -->

            <form
                action="${pageContext.request.contextPath}/admin/dashboard"
                method="post"
                class="category-add-form">

                <input
                    type="hidden"
                    name="action"
                    value="addCategory">

                <div class="category-input-wrapper">

                    <i data-lucide="tag"></i>

                    <input
                        type="text"
                        name="category"
                        placeholder="Enter category name"
                        autocomplete="off"
                        required>

                </div>

                <button
                    type="submit"
                    class="primary-button">

                    <i data-lucide="plus"></i>

                    <span>
                        Add Category
                    </span>

                </button>

            </form>


            <!-- CATEGORY TABLE -->

            <div class="subsection-heading">

                <div>

                    <h3>
                        Categories
                    </h3>

                    <p>
                        Manage your library categories.
                    </p>

                </div>

                <span class="count-badge">
                    ${totalCategories}
                </span>

            </div>


            <%

            List<Category> categories =
                (List<Category>) request.getAttribute("categories");

            if (categories != null && !categories.isEmpty()) {

            %>

            <div class="table-container">

                <table class="data-table">

                    <thead>

                        <tr>

                            <th>
                                Category
                            </th>

                            <th class="action-column">
                                Edit
                            </th>

                            <th class="action-column">
                                Delete
                            </th>

                        </tr>

                    </thead>

                    <tbody>

                    <%

                    for (Category category : categories) {

                    %>

                        <tr>

                            <!-- CATEGORY -->

                            <td>

                                <div class="category-name">

                                    <div class="category-icon">
                                        <i data-lucide="folder"></i>
                                    </div>

                                    <span>
                                        <%=category.getCategoryName()%>
                                    </span>

                                </div>

                            </td>


                            <!-- EDIT -->

                            <td class="action-column">

                                <button
                                    type="button"
                                    class="icon-button edit-icon"
                                    data-category-id="<%=category.getCategoryId()%>"
                                    title="Edit category"
                                    aria-label="Edit category">

                                    <i data-lucide="pencil"></i>

                                </button>


                                <!-- Hidden Edit Form -->

                                <form
                                    id="edit-form-<%=category.getCategoryId()%>"
                                    action="${pageContext.request.contextPath}/admin/dashboard"
                                    method="post"
                                    class="edit-form">

                                    <input
                                        type="hidden"
                                        name="action"
                                        value="updateCategory">

                                    <input
                                        type="hidden"
                                        name="categoryId"
                                        value="<%=category.getCategoryId()%>">

                                    <div class="edit-input-wrapper">

                                        <i data-lucide="tag"></i>

                                        <input
                                            type="text"
                                            name="categoryName"
                                            value="<%=category.getCategoryName()%>"
                                            required>

                                    </div>

                                    <button
                                        type="submit"
                                        class="icon-button save-icon"
                                        title="Save"
                                        aria-label="Save">

                                        <i data-lucide="check"></i>

                                    </button>

                                    <button
                                        type="button"
                                        class="icon-button cancel-icon"
                                        data-category-id="<%=category.getCategoryId()%>"
                                        title="Cancel"
                                        aria-label="Cancel">

                                        <i data-lucide="x"></i>

                                    </button>

                                </form>

                            </td>


                            <!-- DELETE -->

                            <td class="action-column">

                                <form
                                    action="${pageContext.request.contextPath}/admin/dashboard"
                                    method="post"
                                    class="delete-category-form">

                                    <input
                                        type="hidden"
                                        name="action"
                                        value="deleteCategory">

                                    <input
                                        type="hidden"
                                        name="categoryId"
                                        value="<%=category.getCategoryId()%>">

                                    <button
                                        type="submit"
                                        class="icon-button delete-icon"
                                        title="Delete category"
                                        aria-label="Delete category">

                                        <i data-lucide="trash-2"></i>

                                    </button>

                                </form>

                            </td>

                        </tr>

                    <%

                    }

                    %>

                    </tbody>

                </table>

            </div>

            <%

            } else {

            %>

                <div class="empty-state">

                    <div class="empty-icon">
                        <i data-lucide="folder-open"></i>
                    </div>

                    <h3>
                        No categories found
                    </h3>

                    <p>
                        Add your first category to organize the library.
                    </p>

                </div>

            <%

            }

            %>

        </section>


        <!-- =================================================
             USER MANAGEMENT
             ================================================= -->

        <section class="dashboard-section">

            <div class="section-heading">

                <div>

                    <span class="section-label">
                        ACCOUNTS
                    </span>

                    <h2>
                        User Management
                    </h2>

                </div>

                <div class="section-heading-icon">
                    <i data-lucide="users-round"></i>
                </div>

            </div>


            <!-- SEARCH USERS -->

            <form
                action="${pageContext.request.contextPath}/admin/dashboard"
                method="get"
                class="user-search-form">

                <div class="search-input-wrapper">

                    <i data-lucide="search"></i>

                    <input
                        type="text"
                        name="keyword"
                        placeholder="Search users..."
                        autocomplete="off">

                </div>

                <button
                    type="submit"
                    class="primary-button">

                    <i data-lucide="search"></i>

                    <span>
                        Search
                    </span>

                </button>

            </form>


            <!-- USER TABLE -->

            <div class="subsection-heading">

                <div>

                    <h3>
                        Users
                    </h3>

                    <p>
                        Manage registered library users.
                    </p>

                </div>

            </div>


            <%

            List<User> users =
                (List<User>) request.getAttribute("users");

            if (users != null && !users.isEmpty()) {

            %>

            <div class="table-container">

                <table class="data-table user-table">

                    <thead>

                        <tr>

                            <th>
                                ID
                            </th>

                            <th>
                                User
                            </th>

                            <th>
                                Email
                            </th>

                            <th>
                                Role
                            </th>

                            <th>
                                Status
                            </th>

                            <th class="action-column">
                                Block
                            </th>

                            <th class="action-column">
                                Recover
                            </th>

                        </tr>

                    </thead>

                    <tbody>

                    <%

                    for (User usr : users) {

                    %>

                        <tr>

                            <td>

                                <span class="user-id">
                                    #<%=usr.getUserId()%>
                                </span>

                            </td>


                            <td>

                                <div class="user-name">

                                    <div class="user-avatar">

                                        <i data-lucide="user"></i>

                                    </div>

                                    <span>
                                        <%=usr.getName()%>
                                    </span>

                                </div>

                            </td>


                            <td>

                                <span class="user-email">
                                    <%=usr.getEmail()%>
                                </span>

                            </td>


                            <td>

                                <span class="role-badge">
                                    <%=usr.getRole()%>
                                </span>

                            </td>


                            <td>

                                <span class="status-badge">
                                    <%=usr.getStatus()%>
                                </span>

                            </td>


                            <!-- BLOCK -->

                            <td class="action-column">

                                <form
                                    action="${pageContext.request.contextPath}/admin/dashboard"
                                    method="post">

                                    <input
                                        type="hidden"
                                        name="userId"
                                        value="<%=usr.getUserId()%>">

                                    <input
                                        type="hidden"
                                        name="action"
                                        value="block">

                                    <button
                                        type="submit"
                                        class="icon-button block-icon"
                                        title="Block user"
                                        aria-label="Block user">

                                        <i data-lucide="user-round-x"></i>

                                    </button>

                                </form>

                            </td>


                            <!-- RECOVER -->

                            <td class="action-column">

                                <form
                                    action="${pageContext.request.contextPath}/admin/dashboard"
                                    method="post">

                                    <input
                                        type="hidden"
                                        name="action"
                                        value="recover">

                                    <input
                                        type="hidden"
                                        name="userId"
                                        value="<%=usr.getUserId()%>">

                                    <button
                                        type="submit"
                                        class="icon-button recover-icon"
                                        title="Recover user"
                                        aria-label="Recover user">

                                        <i data-lucide="user-round-check"></i>

                                    </button>

                                </form>

                            </td>

                        </tr>

                    <%

                    }

                    %>

                    </tbody>

                </table>

            </div>

            <%

            } else {

            %>

                <div class="empty-state">

                    <div class="empty-icon">
                        <i data-lucide="users-round"></i>
                    </div>

                    <h3>
                        No users found
                    </h3>

                    <p>
                        There are currently no users to display.
                    </p>

                </div>

            <%

            }

            %>

        </section>


    </main>


    <!-- =====================================================
         DELETE CONFIRMATION MODAL
         ===================================================== -->

    <div
        id="deleteModal"
        class="delete-modal"
        aria-hidden="true">

        <div class="delete-modal-backdrop"></div>

        <div
            class="delete-modal-card"
            role="dialog"
            aria-modal="true"
            aria-labelledby="deleteModalTitle">

            <div class="delete-modal-icon">

                <i data-lucide="trash-2"></i>

            </div>

            <div class="delete-modal-content">

                <h3 id="deleteModalTitle">
                    Delete Category?
                </h3>

                <p>
                    Are you sure you want to delete this category?
                    This action cannot be undone.
                </p>

            </div>

            <div class="delete-modal-actions">

                <button
                    type="button"
                    id="deleteCancelButton"
                    class="modal-cancel-button">

                    Cancel

                </button>

                <button
                    type="button"
                    id="deleteConfirmButton"
                    class="modal-delete-button">

                    <i data-lucide="trash-2"></i>

                    <span>
                        Delete
                    </span>

                </button>

            </div>

        </div>

    </div>


    <!-- Page JavaScript -->
    <script
        src="${pageContext.request.contextPath}/javascript/admin-dashboard.js">
    </script>

</body>

</html>