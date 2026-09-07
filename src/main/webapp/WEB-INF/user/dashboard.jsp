<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%@ page import="com.project.elibrary.bean.book.Book" %>
<%@ page import="com.project.elibrary.bean.category.Category" %>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>User Dashboard - E-Library</title>

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
           HEADER
           ========================= */

        .header {
            background-color: white;
            padding: 18px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid #ddd;
        }

        .header h2 {
            margin: 0;
        }

        .nav {
            display: flex;
            gap: 25px;
        }

        .nav a {
            text-decoration: none;
            color: #333;
            font-weight: 500;
        }

        .nav a:hover {
            color: #007bff;
        }

        /* =========================
           MAIN CONTAINER
           ========================= */

        .dashboard-container {
            max-width: 1400px;
            margin: auto;
            padding: 30px 40px;
        }

        /* =========================
           SEARCH
           ========================= */

        .search-section {
            background-color: white;
            padding: 25px;
            border-radius: 10px;
            border: 1px solid #ddd;
            margin-bottom: 30px;
        }

        .search-section h2 {
            margin-top: 0;
        }

        .search-form {
            display: flex;
            gap: 10px;
        }

        .search-input {
            flex: 1;
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 15px;
        }

        .search-button {
            padding: 12px 22px;
            border: none;
            border-radius: 6px;
            background-color: #007bff;
            color: white;
            cursor: pointer;
        }

        .search-button:hover {
            background-color: #0056b3;
        }

        /* =========================
           CATEGORY SECTION
           ========================= */

        .category-section {
            background-color: white;
            padding: 25px;
            margin-bottom: 30px;
            border-radius: 10px;
            border: 1px solid #ddd;
        }

        .category-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }

        .category-header h2 {
            margin: 0;
        }

        .explore-button {
            text-decoration: none;
            color: #007bff;
            font-weight: 600;
        }

        .explore-button:hover {
            text-decoration: underline;
        }

        /* =========================
           BOOK ROW
           ========================= */

        .book-row {
            display: flex;
            gap: 18px;
            overflow-x: auto;
            padding-bottom: 10px;
        }

        /* =========================
           BOOK CARD
           ========================= */

        .book-card {
            min-width: 190px;
            width: 190px;
            background-color: #fafafa;
            border: 1px solid #ddd;
            border-radius: 8px;
            overflow: hidden;
            flex-shrink: 0;
        }

        .book-cover {
            width: 100%;
            height: 240px;
            object-fit: cover;
            display: block;
            background-color: #e9e9e9;
        }

        .no-cover {
            width: 100%;
            height: 240px;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: #e9e9e9;
            color: #777;
        }

        .book-info {
            padding: 14px;
        }

        .book-title {
            font-size: 17px;
            font-weight: bold;
            margin: 0 0 8px 0;
        }

        .book-author {
            font-size: 14px;
            color: #666;
            margin-bottom: 8px;
        }

        .book-rating {
            font-size: 14px;
            margin-bottom: 10px;
        }

        .star {
            color: #f5b301;
        }

        .book-description {
            font-size: 13px;
            color: #555;
            line-height: 1.4;
            height: 54px;
            overflow: hidden;
            margin-bottom: 12px;
        }

        .read-button {
            display: inline-block;
            text-decoration: none;
            padding: 8px 12px;
            background-color: #007bff;
            color: white;
            border-radius: 5px;
            font-size: 13px;
        }

        .read-button:hover {
            background-color: #0056b3;
        }

        /* =========================
           NO CATEGORY
           ========================= */

        .empty-message {
            color: #777;
            padding: 20px 0;
        }

        /* =========================
           RESPONSIVE
           ========================= */

        @media (max-width: 700px) {

            .header {
                padding: 15px 20px;
                flex-direction: column;
                align-items: flex-start;
                gap: 15px;
            }

            .dashboard-container {
                padding: 20px;
            }

            .search-form {
                flex-direction: column;
            }

        }

    </style>

</head>


<body>


<!-- =========================
     HEADER
     ========================= -->

<header class="header">

    <h2>E-Library</h2>

    <nav class="nav">

        <a href="${pageContext.request.contextPath}/user/dashboard">
            Dashboard
        </a>

        <a href="${pageContext.request.contextPath}/books">
            Categories
        </a>

    </nav>

</header>


<!-- =========================
     MAIN DASHBOARD
     ========================= -->

<div class="dashboard-container">


    <!-- =========================
         SEARCH SECTION
         ========================= -->

    <section class="search-section">

        <h2>Search Books</h2>

        <form
            class="search-form"
            action="${pageContext.request.contextPath}/books"
            method="get">

            <input
                class="search-input"
                type="text"
                name="keyword"
                placeholder="Search by title or author">

            <button
                class="search-button"
                type="submit">

                Search

            </button>

        </form>

    </section>


    <!-- =========================
         TOP RATED CATEGORIES
         ========================= -->

    <%

        List<Category> topCategories =
            (List<Category>) request.getAttribute("topCategories");

        Map<Long, List<Book>> categoryBooksMap =
            (Map<Long, List<Book>>) request.getAttribute("categoryBooksMap");

        Map<Long, Double> bookAverageRatingMap =
            (Map<Long, Double>) request.getAttribute("bookAverageRatingMap");

        Map<Long, Integer> bookRatingCountMap =
            (Map<Long, Integer>) request.getAttribute("bookRatingCountMap");

        Map<Long, String> bookCoverUrlMap =
            (Map<Long, String>) request.getAttribute("bookCoverUrlMap");


        if (topCategories != null && !topCategories.isEmpty()) {

            for (Category category : topCategories) {

                Long categoryId =
                    category.getCategoryId();

                List<Book> books =
                    categoryBooksMap.get(categoryId);

    %>


    <!-- =========================
         CATEGORY
         ========================= -->

    <section class="category-section">


        <div class="category-header">

            <h2>
                <%= category.getCategoryName() %>
            </h2>

            <a
                class="explore-button"
                href="${pageContext.request.contextPath}/books/category?id=<%= categoryId %>">

                Explore More →

            </a>

        </div>


        <%

            if (books != null && !books.isEmpty()) {

        %>


        <div class="book-row">


        <%

            for (Book book : books) {

                Long bookId =
                    book.getBookId();

                Double averageRating =
                    bookAverageRatingMap.get(bookId);

                Integer ratingCount =
                    bookRatingCountMap.get(bookId);

                String coverUrl =
                    bookCoverUrlMap.get(bookId);

        %>


            <!-- =========================
                 BOOK CARD
                 ========================= -->

            <div class="book-card">


                <%

                    if (coverUrl != null && !coverUrl.isBlank()) {

                %>

                    <img
                        class="book-cover"
                        src="<%= coverUrl %>"
                        alt="Book Cover">

                <%

                    } else {

                %>

                    <div class="no-cover">
                        No Cover
                    </div>

                <%

                    }

                %>


                <div class="book-info">


                    <h3 class="book-title">

                        <%= book.getTitle() %>

                    </h3>


                    <div class="book-author">

                        By <%= book.getAuthor() %>

                    </div>


                    <div class="book-rating">

                        <span class="star">★</span>

                        <%

                            if (averageRating != null) {

                        %>

                            <%= String.format("%.1f", averageRating) %>

                        <%

                            } else {

                        %>

                            No rating

                        <%

                            }

                        %>


                        <%

                            if (ratingCount != null && ratingCount > 0) {

                        %>

                            (<%= ratingCount %>)

                        <%

                            }

                        %>

                    </div>


                    <div class="book-description">

                        <%

                            String description =
                                book.getDescription();

                            if (description != null
                                    && !description.isBlank()) {

                                if (description.length() > 100) {

                                    out.print(
                                        description.substring(0, 100)
                                        + "..."
                                    );

                                } else {

                                    out.print(description);

                                }

                            } else {

                                out.print("No description available.");

                            }

                        %>

                    </div>


                    <a
                        class="read-button"
                        href="${pageContext.request.contextPath}/books/details?id=<%= bookId %>">

                        Read More

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

            <p class="empty-message">
                No books available in this category.
            </p>

        <%

            }

        %>


    </section>


    <%

            }

        } else {

    %>


        <p class="empty-message">
            No highly rated categories are available yet.
        </p>


    <%

        }

    %>


</div>


</body>

</html>