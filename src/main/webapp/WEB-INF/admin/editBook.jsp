<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="com.project.elibrary.bean.book.Book" %>
<%@ page import="com.project.elibrary.bean.category.Category" %>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Edit Book - E-Library</title>

</head>

<body>

<%
    Book book =
        (Book) request.getAttribute("book");

    List<Category> categories =
        (List<Category>) request.getAttribute("categories");

    String error =
        (String) request.getAttribute("error");
%>


<h1>Edit Book</h1>


<% if (error != null) { %>

    <p><%= error %></p>

<% } %>


<form
    action="${pageContext.request.contextPath}/admin/books/edit"
    method="post">


    <!-- Book ID -->

    <input
        type="hidden"
        name="bookId"
        value="<%= book.getBookId() %>">


    <!-- Title -->

    <label for="title">
        Title:
    </label>

    <br>

    <input
        type="text"
        id="title"
        name="title"
        value="<%= book.getTitle() %>"
        required>

    <br><br>


    <!-- Author -->

    <label for="author">
        Author:
    </label>

    <br>

    <input
        type="text"
        id="author"
        name="author"
        value="<%= book.getAuthor() %>"
        required>

    <br><br>


    <!-- Description -->

    <label for="description">
        Description:
    </label>

    <br>

    <textarea
        id="description"
        name="description"
        rows="6"
        cols="50"><%= book.getDescription() != null
                ? book.getDescription()
                : "" %></textarea>

    <br><br>


    <!-- Category -->

    <label for="categoryId">
        Category:
    </label>

    <br>

    <select
        id="categoryId"
        name="categoryId"
        required>

        <% for (Category category : categories) { %>

            <option          value="<%= category.getCategoryId() %>"
                <%= category.getCategoryId()
                        == book.getCategoryId()
                        ? "selected"
                        : "" %>>

                <%= category.getCategoryName() %>

            </option>

        <% } %>

    </select>

    <br><br>


    <!-- Published Date -->

    <label for="publishedAt">
        Published Date:
    </label>

    <br>

    <input
        type="date"
        id="publishedAt"
        name="publishedAt"
        value="<%= book.getPublishedAt() != null
                ? book.getPublishedAt()
                : "" %>">

    <br><br>


    <!-- Submit -->

    <button type="submit">
        Update Book
    </button>


    <a href="${pageContext.request.contextPath}/books">
        Cancel
    </a>

</form>

</body>

</html>