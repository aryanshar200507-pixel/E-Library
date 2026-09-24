<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.project.elibrary.bean.book.Book" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Stories E-Library | Read Beyond Walls</title>

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@500;600;700&family=Instrument+Sans:wght@400;500;600;700&display=swap"
          rel="stylesheet">

    <!-- Main stylesheet -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/home.css?v=3">
</head>

<body>

<%
    String ctx = request.getContextPath();

    List<Book> books =
        (List<Book>) request.getAttribute("books");

    Map<Long, String> bookCoverUrlMap =
        (Map<Long, String>) request.getAttribute("bookCoverUrlMap");
%>

<!-- =====================================================
     NAVBAR
===================================================== -->

<nav class="navbar" id="navbar">

    <a href="<%=ctx%>/home" class="logo">
        <i data-lucide="book-marked" class="logo-icon"></i>

        <span>
            Stories
            <small class="logo-subtitle">E-LIBRARY</small>
        </span>
    </a>

    <button class="nav-toggle"
            id="navToggle"
            type="button"
            aria-label="Toggle navigation"
            aria-expanded="false">
        <i data-lucide="menu"></i>
    </button>

    <div class="nav-links" id="navLinks">

        <a href="<%=ctx%>/home">Home</a>

        <a href="<%=ctx%>/books">Books</a>

        <a href="#categories">Explore</a>

        <a href="#about">About</a>

        <div class="nav-auth">

            <a href="<%=ctx%>/login.jsp"
               class="nav-login">
                Login
            </a>

            <a href="<%=ctx%>/Register.jsp"
               class="nav-register">
                Register
            </a>

        </div>

    </div>
</nav>


<!-- =====================================================
     HERO BANNER
     Background image is configured in home.css
===================================================== -->

<section class="hero" id="home">

    <div class="hero-content">

        <p class="hero-eyebrow">
            READ <span>·</span> LEARN <span>·</span> GROW
        </p>

        <h1>
            <span>A Library Without</span>
            <span class="hero-line-2">Walls.</span>
        </h1>

        <p class="hero-text">
            Read online, explore a world of knowledge and lose
            yourself in a growing shelf of stories — anytime,
            anywhere.
        </p>

        <div class="hero-actions">

            <a href="#latest-books" class="btn btn-primary">
                <i data-lucide="book-open"></i>
                Explore Books
                <i data-lucide="arrow-right"></i>
            </a>

            <a href="<%=ctx%>/Register.jsp"
               class="btn btn-secondary">
                <i data-lucide="user-round"></i>
                Join the Library
            </a>

        </div>

    </div>

    <!-- Feature strip -->
    <div class="hero-strip">

        <div class="hero-strip-inner">

            <div class="stat">
                <i data-lucide="book-open"></i>
                <div>
                    <strong>Read Online</strong>
                    <span>No downloads needed</span>
                </div>
            </div>

            <div class="stat">
                <i data-lucide="file-down"></i>
                <div>
                    <strong>PDF Access</strong>
                    <span>Take it offline</span>
                </div>
            </div>

            <div class="stat">
                <i data-lucide="clock-3"></i>
                <div>
                    <strong>Open 24/7</strong>
                    <span>Always on the shelf</span>
                </div>
            </div>

        </div>
    </div>

</section>


<!-- =====================================================
     CATEGORY SHOWCASE
     Decorative category cards; carousel controlled by home.js
===================================================== -->

<section class="category-section reveal" id="categories">

    <div class="category-heading">

        <p class="section-eyebrow">
            <span></span>
            BROWSE OUR COLLECTION
            <span></span>
        </p>

        <h2>Explore by Category</h2>

        <p>
            Find your next favorite book from a wide range of categories.
        </p>

    </div>

    <div class="category-carousel-wrap">

        <button type="button"
                class="category-arrow category-prev"
                aria-label="Previous categories">
            <i data-lucide="chevron-left"></i>
        </button>

        <div class="category-track" id="categoryTrack">

            <a href="<%=ctx%>/books" class="category-card">
                <i data-lucide="book-open"></i>
                <span>Fiction</span>
            </a>

            <a href="<%=ctx%>/books" class="category-card">
                <i data-lucide="scroll-text"></i>
                <span>Non-Fiction</span>
            </a>

            <a href="<%=ctx%>/books" class="category-card">
                <i data-lucide="flask-conical"></i>
                <span>Science</span>
            </a>

            <a href="<%=ctx%>/books" class="category-card">
                <i data-lucide="monitor"></i>
                <span>Technology</span>
            </a>

            <a href="<%=ctx%>/books" class="category-card">
                <i data-lucide="heart"></i>
                <span>Romance</span>
            </a>

            <a href="<%=ctx%>/books" class="category-card">
                <i data-lucide="search"></i>
                <span>Mystery</span>
            </a>

            <a href="<%=ctx%>/books" class="category-card">
                <i data-lucide="feather"></i>
                <span>Self-Help</span>
            </a>

            <a href="<%=ctx%>/books" class="category-card">
                <i data-lucide="landmark"></i>
                <span>History</span>
            </a>

        </div>

        <button type="button"
                class="category-arrow category-next"
                aria-label="Next categories">
            <i data-lucide="chevron-right"></i>
        </button>

    </div>

</section>


<!-- =====================================================
     LATEST BOOKS
===================================================== -->

<main class="container reveal" id="latest-books">

    <div class="section-title">

        <div>
            <p class="section-eyebrow">FRESH ON THE SHELF</p>

            <h2>Latest Books</h2>

            <p>
                Discover new stories and explore our growing collection.
            </p>
        </div>

        <a href="<%=ctx%>/books" class="section-link">
            View All
            <i data-lucide="arrow-right"></i>
        </a>

    </div>


    <%
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

        <article class="book-card reveal">

            <!-- Book cover -->
            <div class="book-cover-container">

                <%
                    if (coverUrl != null && !coverUrl.isBlank()) {
                %>

                <img src="<%=coverUrl%>"
                     alt="<%=book.getTitle()%> cover"
                     class="book-cover"
                     loading="lazy">

                <%
                    } else {
                %>

                <div class="no-cover">
                    <i data-lucide="book-open"></i>
                    <span>No cover available</span>
                </div>

                <%
                    }
                %>

                <span class="cover-ribbon">
                    <i data-lucide="bookmark"></i>
                </span>

            </div>

            <!-- Book details -->
            <div class="book-info">

                <h3 class="book-title">
                    <%=book.getTitle()%>
                </h3>

                <p class="book-author">
                    <i data-lucide="feather"></i>
                    By <%=book.getAuthor()%>
                </p>

                <p class="book-description">
                    <%=book.getDescription() != null
                        ? book.getDescription()
                        : "Discover this book in Stories E-Library."%>
                </p>

                <a href="<%=ctx%>/books/details?id=<%=book.getBookId()%>"
                   class="view-button">
                    <i data-lucide="book-open"></i>
                    View Details
                    <i data-lucide="arrow-up-right"></i>
                </a>

            </div>

        </article>

        <%
            }
        %>

    </div>

    <%
        } else {
    %>

    <div class="no-books reveal">

        <i data-lucide="library"></i>

        <h3>No Books Yet</h3>

        <p>
            The shelves are empty right now. Check back soon for new stories.
        </p>

    </div>

    <%
        }
    %>

</main>


<!-- =====================================================
     ABOUT / CTA SECTION
===================================================== -->

<section class="about-section reveal" id="about">

    <div class="about-content">

        <p class="section-eyebrow">MORE THAN BOOKS</p>

        <h2>
            A Better You,<br>
            One Book at a Time
        </h2>

        <p>
            Stories E-Library is your space to read, learn and grow.
            From timeless classics to modern stories, knowledge is
            just a click away.
        </p>

        <a href="<%=ctx%>/Register.jsp" class="btn btn-primary">
            Get Started
            <i data-lucide="arrow-right"></i>
        </a>

    </div>

    <div class="about-stats">

        <div class="about-stat">
            <i data-lucide="book-open"></i>
            <div>
                <strong>Explore</strong>
                <span>Books across categories</span>
            </div>
        </div>

        <div class="about-stat">
            <i data-lucide="library"></i>
            <div>
                <strong>Discover</strong>
                <span>Stories for every reader</span>
            </div>
        </div>

        <div class="about-stat">
            <i data-lucide="clock-3"></i>
            <div>
                <strong>Anytime</strong>
                <span>Read at your own pace</span>
            </div>
        </div>

        <div class="about-stat">
            <i data-lucide="heart"></i>
            <div>
                <strong>Keep Growing</strong>
                <span>A new chapter every day</span>
            </div>
        </div>

    </div>

</section>


<!-- =====================================================
     FOOTER
===================================================== -->

<footer class="footer">

    <div class="footer-content">

        <div class="footer-brand">

            <div class="footer-brand-name">
                <i data-lucide="book-marked"></i>
                <span>Stories E-Library</span>
            </div>

            <p>Stories Beyond Walls.</p>

            <div class="social-links">

                <a href="#"
                   aria-label="Social media"
                   title="Social media">
                    <i data-lucide="message-circle"></i>
                </a>

                <a href="#"
                   aria-label="Photo sharing"
                   title="Photo sharing">
                    <i data-lucide="camera"></i>
                </a>

                <a href="#"
                   aria-label="Video channel"
                   title="Video channel">
                    <i data-lucide="circle-play"></i>
                </a>

            </div>

        </div>


        <div class="footer-column">
            <h3>Quick Links</h3>
            <a href="<%=ctx%>/home">Home</a>
            <a href="<%=ctx%>/books">Books</a>
            <a href="#categories">Explore</a>
            <a href="#about">About</a>
        </div>


        <div class="footer-column">
            <h3>Categories</h3>
            <a href="<%=ctx%>/books">Fiction</a>
            <a href="<%=ctx%>/books">Non-Fiction</a>
            <a href="<%=ctx%>/books">Science</a>
            <a href="<%=ctx%>/books">Technology</a>
            <a href="<%=ctx%>/books">Romance</a>
        </div>


        <div class="footer-newsletter">

            <h3>Stay Updated</h3>

            <p>
                Get updates about new books and stories.
            </p>

        </div>

    </div>


    <div class="footer-bottom">

        <p>
            &copy; 2026 Stories E-Library. All rights reserved.
        </p>

        <div>
            <span>Read</span>
            <span>Learn</span>
            <span>Grow</span>
            <span>Together</span>
        </div>

    </div>

</footer>


<!-- =====================================================
     SCRIPTS
===================================================== -->

<script src="https://unpkg.com/lucide@latest"></script>

<script src="<%=ctx%>/javascript/home.js?v=3"></script>

</body>
</html>