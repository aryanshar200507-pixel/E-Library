<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.project.elibrary.bean.category.Category" %>
<%@ page import="com.project.elibrary.bean.user.User"%>
<%@ page import="java.util.List"%>

<!DOCTYPE html>
<html>

<head>

<title>E-Library Admin Dashboard</title>

<style>
* {
	box-sizing: border-box;
}

body {
	margin: 0;
	font-family: Arial, sans-serif;
	background-color: #f5f6fa;
	color: #222;
}

/* =========================
           ADMIN HEADER
           ========================= */
.admin-header {
	background-color: #ffffff;
	padding: 18px 40px;
	display: flex;
	justify-content: space-between;
	align-items: center;
	border-bottom: 1px solid #ddd;
}

.header-title h2 {
	margin: 0;
	font-size: 24px;
}

.admin-nav {
	display: flex;
	gap: 25px;
}

.admin-nav a {
	text-decoration: none;
	color: #333;
	font-weight: 500;
}

.admin-nav a:hover {
	color: #007bff;
}

/* =========================
           MAIN CONTAINER
           ========================= */
.dashboard-container {
	padding: 30px 40px;
	max-width: 1400px;
	margin: auto;
}

/* =========================
           SECTION
           ========================= */
.dashboard-section {
	background-color: #ffffff;
	padding: 25px;
	margin-bottom: 25px;
	border-radius: 10px;
	border: 1px solid #e1e1e1;
}

.dashboard-section h2 {
	margin-top: 0;
	margin-bottom: 20px;
}

/* =========================
           OVERVIEW
           ========================= */
.overview-cards {
	display: flex;
	gap: 20px;
}

.overview-card {
	flex: 1;
	padding: 22px;
	background-color: #f8f9fc;
	border: 1px solid #e2e2e2;
	border-radius: 8px;
}

.overview-card h3 {
	margin: 0 0 10px 0;
	font-size: 16px;
	color: #666;
}

.overview-card p {
	margin: 0;
	font-size: 30px;
	font-weight: bold;
}

/* =========================
           MANAGEMENT LINKS
           ========================= */
.management-row {
	display: flex;
	gap: 20px;
}

.management-box {
	flex: 1;
	padding: 20px;
	border: 1px solid #ddd;
	border-radius: 8px;
	background-color: #fafafa;
}

.management-box h3 {
	margin-top: 0;
}

.management-box p {
	color: #666;
}

.management-button {
	display: inline-block;
	padding: 10px 16px;
	background-color: #007bff;
	color: white;
	text-decoration: none;
	border-radius: 5px;
}

.management-button:hover {
	background-color: #0056b3;
}

/* =========================
           FORMS
           ========================= */
.form-row {
	display: flex;
	gap: 10px;
	align-items: center;
}

.form-row input[type="text"] {
	padding: 10px;
	width: 300px;
	border: 1px solid #ccc;
	border-radius: 5px;
}

button {
	padding: 10px 16px;
	border: none;
	border-radius: 5px;
	cursor: pointer;
}

.primary-button {
	background-color: #007bff;
	color: white;
}

.primary-button:hover {
	background-color: #0056b3;
}

.block-button {
	background-color: #dc3545;
	color: white;
}

.block-button:hover {
	background-color: #b02a37;
}

.recover-button {
	background-color: #28a745;
	color: white;
}

.recover-button:hover {
	background-color: #1e7e34;
}

/* =========================
           USER TABLE
           ========================= */
.table-container {
	overflow-x: auto;
}

table {
	width: 100%;
	border-collapse: collapse;
}

th, td {
	padding: 12px;
	border-bottom: 1px solid #ddd;
	text-align: left;
}

th {
	background-color: #f1f2f6;
}

tr:hover {
	background-color: #fafafa;
}

td form {
	margin: 0;
}

/* =========================
           EMPTY MESSAGE
           ========================= */
.empty-message {
	color: #777;
	padding: 10px 0;
}

/* =========================
           RESPONSIVE
           ========================= */
@media ( max-width : 800px) {
	.admin-header {
		flex-direction: column;
		gap: 15px;
		align-items: flex-start;
	}
	.admin-nav {
		flex-wrap: wrap;
	}
	.overview-cards {
		flex-direction: column;
	}
	.management-row {
		flex-direction: column;
	}
	.dashboard-container {
		padding: 20px;
	}
}

.category-edit {
    display: flex;
    align-items: center;
    gap: 8px;
}

.edit-icon,
.save-icon,
.cancel-icon {
    border: none;
    background: none;
    cursor: pointer;
    font-size: 18px;
    padding: 4px;
}

.edit-icon:hover {
    transform: scale(1.15);
}

.edit-form {
    display: none;
    align-items: center;
    gap: 5px;
}

.edit-form input {
    width: 150px;
    padding: 6px 8px;
    border: 1px solid #ccc;
    border-radius: 5px;
}

.save-icon {
    color: green;
}

.cancel-icon {
    color: red;
}
</style>

</head>


<body>


	<!-- =========================
     ADMIN HEADER
     ========================= -->

	<header class="admin-header">

		<div class="header-title">
			<h2>E-Library Admin</h2>
		</div>

		<nav class="admin-nav">

			<a href="${pageContext.request.contextPath}/admin/dashboard">
				Dashboard </a> <a
				href="${pageContext.request.contextPath}/admin/books/add"> Add
				Book </a> <a href="${pageContext.request.contextPath}/books">
				Books </a>

		</nav>

	</header>


	<!-- =========================
     DASHBOARD CONTENT
     ========================= -->

	<div class="dashboard-container">


		<!-- =========================
         OVERVIEW SECTION
         ========================= -->

		<section class="dashboard-section">

			<h2>Overview</h2>

			<div class="overview-cards">

				<div class="overview-card">

					<h3>Total Users</h3>

					<p>${totalUsers}</p>

				</div>


				<div class="overview-card">

					<h3>Total Categories</h3>

					<p>${totalCategories}</p>

				</div>


				<div class="overview-card">

					<h3>Total Books</h3>

					<p>${totalBooks}</p>

				</div>

			</div>

		</section>


		<!-- =========================
         BOOK MANAGEMENT
         ========================= -->

		<section class="dashboard-section">

			<h2>Book Management</h2>

			<div class="management-row">


				<div class="management-box">

					<h3>Add Book</h3>

					<p>Add a new book to the E-Library.</p>

					<a class="management-button"
						href="${pageContext.request.contextPath}/admin/books/add"> Add
						Book </a>

				</div>


				<div class="management-box">

					<h3>Manage Books</h3>

					<p>View, edit and delete books.</p>

					<a class="management-button"
						href="${pageContext.request.contextPath}/books"> Manage
						Books </a>

				</div>

			</div>

		</section>


		<!-- =========================
         CATEGORY MANAGEMENT
         ========================= -->

<section class="dashboard-section">

    <h2>Category Management</h2>

    <!-- ADD CATEGORY -->
    <form action="${pageContext.request.contextPath}/admin/dashboard"
        method="post">

        <div class="form-row">

            <input type="text"
                   name="category"
                   placeholder="Enter category name"
                   required>

            <input type="hidden"
                   name="action"
                   value="addCategory">

            <button type="submit" class="primary-button">
                Add Category
            </button>

        </div>

    </form>

    <br>

    <!-- CATEGORY TABLE -->

    <h3>Categories</h3>

    <%
    List<Category> categories =
        (List<Category>) request.getAttribute("categories");

    if (categories != null && !categories.isEmpty()) {
    %>

    <div class="table-container">

        <table>

            <tr>
                
                <th>Category Name</th>
                <th>Edit</th>
                <th>Delete</th>
            </tr>

            <%
            for (Category category : categories) {
            %>

            <tr>

                <td>
                    <%= category.getCategoryName() %>
                </td>

<td>

    <!-- Pencil button -->
    <button type="button"
            class="edit-icon" onclick="editCategory(<%= category.getCategoryId() %>)"
            title="Edit category">
        ✏️
    </button>

    <!-- Edit form, hidden initially -->
    <form id="edit-form-<%= category.getCategoryId() %>"
          action="${pageContext.request.contextPath}/admin/dashboard"
          method="post"
          class="edit-form">

        <input type="hidden"
               name="action"
               value="updateCategory">

        <input type="hidden"
               name="categoryId"
               value="<%= category.getCategoryId() %>">

        <input type="text"
               name="categoryName"
               value="<%= category.getCategoryName() %>"
               required>

        <button type="submit"
                class="save-icon"
                title="Save">
            ✓
        </button>

        <button type="button"
                class="cancel-icon"
                onclick="cancelEdit(<%= category.getCategoryId() %>)"
                title="Cancel">
            ✕
        </button>

    </form>

</td>
				<td>
    <form action="${pageContext.request.contextPath}/admin/dashboard"
          method="post"
          onsubmit="return confirm('Are you sure you want to delete this category?');">

        <input type="hidden"
               name="action"
               value="deleteCategory">

        <input type="hidden"
               name="categoryId"
               value="<%= category.getCategoryId() %>">

        <button type="submit" class="delete-button">
            Delete
        </button>

    </form>
</td>
            </tr>

            <%
            }
            %>

        </table>

    </div>

    <%
    } else {
    %>

    <p class="empty-message">
        No categories found.
    </p>

    <%
    }
    %>

</section>


		<!-- =========================
         USER MANAGEMENT
         ========================= -->

		<section class="dashboard-section">

			<h2>User Management</h2>


			<!-- SEARCH USERS -->

			<form action="${pageContext.request.contextPath}/admin/dashboard"
				method="get">

				<div class="form-row">

					<input type="text" name="keyword" placeholder="Search user">

					<button type="submit" class="primary-button">Search</button>

				</div>

			</form>


			<br>


			<!-- USER TABLE -->

			<h3>Users</h3>


			<%
			List<User> users = (List<User>) request.getAttribute("users");

			if (users != null && !users.isEmpty()) {
			%>


			<div class="table-container">

				<table>

					<tr>

						<th>ID</th>

						<th>Name</th>

						<th>Email</th>

						<th>Role</th>

						<th>Status</th>

						<th>Block</th>

						<th>Recover</th>

					</tr>


					<%
					for (User usr : users) {
					%>


					<tr>

						<td><%=usr.getUserId()%></td>

						<td><%=usr.getName()%></td>

						<td><%=usr.getEmail()%></td>

						<td><%=usr.getRole()%></td>

						<td><%=usr.getStatus()%></td>


						<td>

							<form action="${pageContext.request.contextPath}/admin/dashboard"
								method="post">

								<input type="hidden" name="userId"
									value="<%=usr.getUserId()%>"> <input type="hidden"
									name="action" value="block">

								<button type="submit" class="block-button">Block</button>

							</form>

						</td>


						<td>

							<form action="${pageContext.request.contextPath}/admin/dashboard"
								method="post">

								<input type="hidden" name="action" value="recover"> <input
									type="hidden" name="userId" value="<%=usr.getUserId()%>">

								<button type="submit" class="recover-button">Recover</button>

							</form>

						</td>

					</tr>


					<%
					}
					%>

				</table>

			</div>


			<%
			} else {
			%>


			<p class="empty-message">No users found.</p>


			<%
			}
			%>


		</section>


	</div>
<script>

function editCategory(categoryId) {

    document.querySelector(
        ".edit-icon[onclick='editCategory(" + categoryId + ")']"
    ).style.display = "none";

    document.getElementById("edit-form-" + categoryId)
            .style.display = "flex";
}


function cancelEdit(categoryId) {

    document.querySelector(
        ".edit-icon[onclick='editCategory(" + categoryId + ")']"
    ).style.display = "inline";

    document.getElementById("edit-form-" + categoryId)
            .style.display = "none";
}

</script>

</body>

</html>