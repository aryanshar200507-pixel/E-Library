
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.project.elibrary.bean.book.Book" %>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>E-Library | Home</title>

    <style>

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
            color: #222;
        }

        /* ================= NAVBAR ================= */

        .navbar {
            background-color: #1f2937;
            height: 65px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 60px;
        }

        .logo {
            color: white;
            font-size: 25px;
            font-weight: bold;
            text-decoration: none;
        }

        .nav-links {
            display: flex;
            gap: 25px;
        }

        .nav-links a {
            color: white;
            text-decoration: none;
            font-size: 15px;
        }

        .nav-links a:hover {
            color: #d1d5db;
        }

        /* ================= HERO ================= */

        .hero {
            background: linear-gradient(135deg, #1f2937, #374151);
            color: white;
            text-align: center;
            padding: 70px 20px;
        }

        .hero h1 {
            font-size: 42px;
            margin-bottom: 15px;
        }

        .hero p {
            font-size: 18px;
            color: #d1d5db;
            margin-bottom: 25px;
        }

        .hero-button {
            display: inline-block;
            background-color: white;
            color: #1f2937;
            padding: 12px 25px;
            border-radius: 6px;
            text-decoration: none;
            font-weight: bold;
        }

        .hero-button:hover {
            background-color: #e5e7eb;
        }

        /* ================= MAIN ================= */

        .container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 25px;
        }

        .section-title {
            text-align: center;
            margin-bottom: 30px;
        }

        .section-title h2 {
            font-size: 30px;
            margin-bottom: 8px;
        }

        .section-title p {
            color: #6b7280;
        }

        /* ================= BOOK GRID ================= */

        .book-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 25px;
        }

        /* ================= BOOK CARD ================= */

        .book-card {
            background-color: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 3px 12px rgba(0, 0, 0, 0.08);
            transition: transform 0.2s ease,
                        box-shadow 0.2s ease;
        }

        .book-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
        }

        .book-cover-container {
            height: 280px;
            background-color: #e5e7eb;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .book-cover {
            max-width: 180px;
            height: 240px;
            object-fit: cover;
            border-radius: 5px;
            box-shadow: 0 3px 8px rgba(0, 0, 0, 0.2);
        }

        .no-cover {
            color: #6b7280;
            font-size: 14px;
        }

        /* ================= BOOK INFO ================= */

        .book-info {
            padding: 20px;
        }

        .book-title {
            font-size: 20px;
            margin-bottom: 8px;
            color: #111827;
        }

        .book-author {
            color: #6b7280;
            font-size: 14px;
            margin-bottom: 12px;
        }

        .book-description {
            color: #4b5563;
            font-size: 14px;
            line-height: 1.5;
            height: 65px;
            overflow: hidden;
            margin-bottom: 18px;
        }

        /* ================= BUTTON ================= */

        .view-button {
            display: inline-block;
            width: 100%;
            text-align: center;
            padding: 11px;
            background-color: #1f2937;
            color: white;
            text-decoration: none;
            border-radius: 6px;
            font-weight: bold;
            transition: background-color 0.2s ease;
        }

        .view-button:hover {
            background-color: #374151;
        }

        /* ================= NO BOOKS ================= */

        .no-books {
            text-align: center;
            background-color: white;
            padding: 40px;
            border-radius: 10px;
            color: #6b7280;
        }

        /* ================= FOOTER ================= */

        .footer {
            margin-top: 60px;
            background-color: #1f2937;
            color: #d1d5db;
            text-align: center;
            padding: 25px;
        }

        /* ================= RESPONSIVE ================= */

        @media (max-width: 900px) {

            .book-grid {
                grid-template-columns: repeat(2, 1fr);
            }

            .navbar {
                padding: 0 25px;
            }
        }

        @media (max-width: 600px) {

            .book-grid {
                grid-template-columns: 1fr;
            }

            .navbar {
                padding: 0 15px;
            }

            .nav-links {
                gap: 10px;
            }

            .hero h1 {
                font-size: 32px;
            }
        }

    </style>

</head>


<body>


    <!-- ================= NAVBAR ================= -->

    <nav class="navbar">

        <a href="<%= request.getContextPath() %>/home"
           class="logo">
            E-Library
        </a>

        <div class="nav-links">

            <a href="<%= request.getContextPath() %>/home">
                Home
            </a>

            <a href="<%= request.getContextPath() %>/books">
                Books
            </a>

            <a href="<%= request.getContextPath() %>/login.jsp">
                Login
            </a>

        </div>

    </nav>


    <!-- ================= HERO SECTION ================= -->

    <section class="hero">

        <h1>Welcome to E-Library</h1>

        <p>
            Discover, explore and read your favourite books online.
        </p>

        <a href="#latest-books" class="hero-button">
            Explore Books
        </a>

    </section>


    <!-- ================= BOOK SECTION ================= -->

    <main class="container" id="latest-books">

        <div class="section-title">

            <h2>Latest Books</h2>

            <p>
                Explore the latest books available in our library.
            </p>

        </div>


        <%

            List<Book> books =
                    (List<Book>) request.getAttribute("books");

            Map<Long, String> bookCoverUrlMap =
                    (Map<Long, String>)
                    request.getAttribute("bookCoverUrlMap");


            if (books != null && !books.isEmpty()) {

        %>


        <div class="book-grid">


            <%

                for (Book book : books) {

                    String coverUrl = null;

                    if (bookCoverUrlMap != null) {

                        coverUrl =
                                bookCoverUrlMap.get(book.getBookId());

                    }

            %>


            <!-- ================= BOOK CARD ================= -->

            <div class="book-card">


                <div class="book-cover-container">

                    <% if (coverUrl != null) { %>

                        <img
                            src="<%= coverUrl %>"
                            alt="<%= book.getTitle() %>"
                            class="book-cover"
                        >

                    <% } else { %>

                        <span class="no-cover">
                            No Cover Available
                        </span>

                    <% } %>


                </div>


                <div class="book-info">


                    <h3 class="book-title">
                        <%= book.getTitle() %>
                    </h3>


                    <p class="book-author">
                        By <%= book.getAuthor() %>
                    </p>


                    <p class="book-description">
                        <%= book.getDescription() %>
                    </p>


                    <a
                        href="<%= request.getContextPath() %>/books/details?id=<%= book.getBookId() %>"
                        class="view-button"
                    >
                        View Details
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

                <h3>No books available</h3>

                <p>
                    There are currently no books available in the library.
                </p>

            </div>


        <%

            }

        %>


    </main>


    <!-- ================= FOOTER ================= -->

    <footer class="footer">

        <p>
            © 2026 E-Library. All rights reserved.
        </p>

    </footer>


</body>

</html>
