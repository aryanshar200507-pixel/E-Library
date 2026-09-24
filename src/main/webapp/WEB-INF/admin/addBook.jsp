<%@page import="com.project.elibrary.bean.category.Category"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Book</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/background.css">
</head>

<body>

	<h2>Add New Book</h2>

	<form action="${pageContext.request.contextPath}/admin/books/add"
		method="post" enctype="multipart/form-data">

		<div>
			<label for="title">Title:</label> <input type="text" id="title"
				name="title" required>
		</div>

		<br>

		<div>
			<label for="author">Author:</label> <input type="text" id="author"
				name="author" required>
		</div>

		<br>

		<div>
			<label for="description">Description:</label>
			<textarea id="description" name="description" rows="5"></textarea>
		</div>

		<br>

		<div>
			<label for="categoryId">Category:</label> <select id="categoryId"
				name="categoryId" required>
				<option value="">Select Category</option>

				<%
				List<Category> categories = (List<Category>) request.getAttribute("categories");

				for (Category category : categories) {
				%>
				<option value="<%=category.getCategoryId()%>">
					<%=category.getCategoryName()%>
				</option>
				<%
				}
				%>
			</select>
		</div>

		<br>

		<div>
			<label for="publishedAt">Published Date:</label> <input type="date"
				id="publishedAt" name="publishedAt">
		</div>

		<br>

		<div>
			<label for="cover">Book Cover:</label> <input type="file" id="cover"
				name="cover" accept="image/jpeg,image/png,image/webp" required>
		</div>

		<br>

		<div>
			<label for="pdf">Book PDF:</label> <input type="file" id="pdf"
				name="pdf" accept="application/pdf" required>
		</div>

		<br> <a href="${pageContext.request.contextPath}/admin/books/add">
			<button type="submit">Add Book</button>
		</a>

	</form>

</body>
</html>