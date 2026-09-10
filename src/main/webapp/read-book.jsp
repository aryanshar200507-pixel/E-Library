<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.project.elibrary.bean.book.Book"%>
<%@ page import="com.project.elibrary.bean.user.User"%>
<%@ page import="com.project.elibrary.bean.enums.Role"%>

<%

/* =========================================================
   GET DATA FROM ReadBookServlet
   ========================================================= */

Book book = (Book) request.getAttribute("book");

String pdfUrl = (String) request.getAttribute("pdfUrl");

Integer savedPageAttribute =
        (Integer) request.getAttribute("currentPage");

int savedPage =
        (savedPageAttribute != null && savedPageAttribute > 0)
        ? savedPageAttribute
        : 1;

String contextPath = request.getContextPath();

Long bookId =
        book != null && book.getBookId() != null
        ? book.getBookId()
        : 0L;


/* =========================================================
   LOGGED-IN USER
   ========================================================= */

User loggedInUser =
        (User) session.getAttribute("loggedInUser");


/* =========================================================
   ROLE CHECK
   ========================================================= */

boolean isNormalUser =
        loggedInUser != null
        && loggedInUser.getRole() == Role.USER;

boolean isAdmin =
        loggedInUser != null
        && loggedInUser.getRole() == Role.ADMIN;

%>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        <%= book != null
            ? book.getTitle()
            : "E-Library Reader" %>
    </title>


    <!-- =====================================================
         PDF.JS
         ===================================================== -->

    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.11.174/pdf.min.js">
    </script>


    <!-- =====================================================
         LUCIDE ICONS
         ===================================================== -->

    <script src="https://unpkg.com/lucide@latest">
    </script>


    <!-- =====================================================
         READER CSS
         ===================================================== -->

    <link rel="stylesheet"
          href="<%=contextPath%>/css/read-book.css">

</head>


<body>


<div class="reader">


    <!-- =====================================================
         TOP TOOLBAR

         Layout strategy:
         - Buttons that stay ALWAYS in the toolbar (mobile +
           desktop): back, title, sidebar toggle, search,
           prev, page number, next.
         - Everything else (bookmark, highlight, zoom, rotate,
           view mode, fullscreen) lives inside
           #toolbarOverflow. On desktop it renders inline
           (display:contents — visually identical to before).
           On mobile it becomes a dropdown opened by the
           "More" (⋮) button, so every feature stays reachable
           on a phone screen instead of being hidden.
         ===================================================== -->

    <div class="toolbar">


        <!-- BACK -->

        <button
            type="button"
            class="back-button"
            onclick="goBack()"
            title="Back">

            <i data-lucide="arrow-left"></i>

        </button>


        <!-- BOOK TITLE -->

        <div class="book-title">

            <%= book != null
                ? book.getTitle()
                : "E-Library Reader" %>

        </div>


        <!-- SIDEBAR (always visible — mobile users need this too) -->

        <button
            type="button"
            onclick="toggleSidebar()"
            title="Sidebar">

            <i data-lucide="panel-left"></i>

        </button>


        <!-- SEARCH -->

        <button
            type="button"
            onclick="toggleSearch()"
            title="Search inside book">

            <i data-lucide="search"></i>

        </button>


        <!-- PREVIOUS -->

        <button
            type="button"
            onclick="previousPage()"
            title="Previous Page">

            <i data-lucide="chevron-left"></i>

        </button>


        <!-- PAGE NUMBER -->

        <div class="page-controls">

            <input
                id="pageNumber"
                class="page-input"
                type="number"
                min="1"
                value="<%=savedPage%>"
                onchange="goToPageFromInput()">

            <span class="page-count">

                /

                <span id="totalPages">0</span>

            </span>

        </div>


        <!-- NEXT -->

        <button
            type="button"
            onclick="nextPage()"
            title="Next Page">

            <i data-lucide="chevron-right"></i>

        </button>


        <!-- =================================================
             OVERFLOW GROUP
             Desktop: renders inline, same as before.
             Mobile: becomes the dropdown opened by "More".
             ================================================= -->

        <div class="toolbar-overflow" id="toolbarOverflow">


            <!-- BOOKMARK -->

            <%
            if (isNormalUser) {
            %>

            <button
                type="button"
                id="bookmarkButton"
                class="bookmark-button"
                onclick="toggleBookmark()"
                title="Bookmark this page">

                <i data-lucide="bookmark"></i>
                <span class="label">Bookmark Page</span>

            </button>

            <%
            }
            %>


            <!-- HIGHLIGHT -->

            <%
            if (isNormalUser) {
            %>

            <button
                type="button"
                id="highlightHeaderButton"
                class="highlight-header-button active"
                onclick="activateHighlightMode()"
                title="Select text to highlight">

                <i data-lucide="highlighter"></i>
                <span class="label">Highlight Text</span>

            </button>

            <%
            }
            %>


            <!-- ZOOM OUT -->

            <button
                type="button"
                onclick="zoomOut()"
                title="Zoom Out">

                <i data-lucide="zoom-out"></i>
                <span class="label">Zoom Out</span>

            </button>


            <!-- ZOOM IN -->

            <button
                type="button"
                onclick="zoomIn()"
                title="Zoom In">

                <i data-lucide="zoom-in"></i>
                <span class="label">Zoom In</span>

            </button>


            <!-- FIT WIDTH -->

            <button
                type="button"
                onclick="fitWidth()"
                title="Fit Width">

                <i data-lucide="move-horizontal"></i>
                <span class="label">Fit Width</span>

            </button>


            <!-- ROTATE -->

            <button
                type="button"
                onclick="rotatePage()"
                title="Rotate">

                <i data-lucide="rotate-cw"></i>
                <span class="label">Rotate</span>

            </button>


            <!-- VIEW MODE -->

            <select
                id="viewMode"
                onchange="changeViewMode()"
                title="View Mode">

                <option value="single">
                    Single Page
                </option>

                <option value="spread">
                    Spread
                </option>

            </select>


            <!-- FULLSCREEN -->

            <button
                type="button"
                onclick="toggleFullscreen()"
                title="Fullscreen">

                <i data-lucide="maximize"></i>
                <span class="label">Fullscreen</span>

            </button>


        </div>


        <!-- MORE (mobile only — opens the overflow group above) -->

        <button
            type="button"
            id="moreOptionsButton"
            class="more-options-button mobile-only"
            onclick="toggleMoreMenu()"
            title="More options">

            <i data-lucide="more-vertical"></i>

        </button>


    </div>


    <!-- =====================================================
         READER CONTENT
         ===================================================== -->

    <div
        class="reader-content"
        id="readerContent">


        <!-- =================================================
             SIDEBAR
             ================================================= -->

        <div
            class="sidebar"
            id="sidebar">


            <!-- SIDEBAR TABS -->

            <div class="sidebar-tabs">


                <!-- PAGES -->

                <button
                    type="button"
                    id="pagesTab"
                    class="sidebar-tab active"
                    onclick="showSidebarTab('pages')">

                    <i data-lucide="file-text"></i>

                    <span>Pages</span>

                </button>


                <%
                if (isNormalUser) {
                %>


                <!-- BOOKMARKS -->

                <button
                    type="button"
                    id="bookmarksTab"
                    class="sidebar-tab"
                    onclick="showSidebarTab('bookmarks')">

                    <i data-lucide="bookmark"></i>

                    <span>Bookmarks</span>

                </button>


                <!-- HIGHLIGHTS -->

                <button
                    type="button"
                    id="highlightsTab"
                    class="sidebar-tab"
                    onclick="showSidebarTab('highlights')">

                    <i data-lucide="highlighter"></i>

                    <span>Highlights</span>

                </button>


                <%
                }
                %>


            </div>


            <!-- SIDEBAR CONTENT -->

            <div
                class="sidebar-content"
                id="pagesContainerSidebar">

                <!-- Thumbnails inserted here -->

            </div>


            <div
                class="sidebar-content"
                id="bookmarkContainer"
                style="display:none;">

                <!-- Bookmarks inserted here -->

            </div>


            <div
                class="sidebar-content"
                id="highlightContainer"
                style="display:none;">

                <!-- Highlights inserted here -->

            </div>


        </div>


        <!-- =================================================
             PDF VIEWER
             (tap/click on the left or right edge of this
             element turns the page — see read-book.js)
             ================================================= -->

        <div
            class="viewer"
            id="viewer">


            <!-- LOADING -->

            <div
                id="loading"
                class="loading">

                Loading PDF...

            </div>


            <!-- =================================================
                 SEARCH PANEL
                 ================================================= -->

            <div
                id="searchPanel"
                class="search-panel">


                <div class="search-row">


                    <!-- SEARCH INPUT -->

                    <input
                        type="text"
                        id="searchInput"
                        class="search-input"
                        placeholder="Search in book..."
                        onkeydown="handleSearchKey(event)">


                    <!-- SEARCH -->

                    <button
                        type="button"
                        onclick="performSearch()"
                        title="Search">

                        <i data-lucide="search"></i>

                    </button>


                    <!-- CLOSE -->

                    <button
                        type="button"
                        onclick="closeSearch()"
                        title="Close Search">

                        <i data-lucide="x"></i>

                    </button>


                </div>


                <!-- SEARCH INFO -->

                <div
                    id="searchInfo"
                    class="search-info">

                    Enter a word to search.

                </div>


                <!-- SEARCH NAVIGATION -->

                <div class="search-navigation">


                    <button
                        type="button"
                        onclick="previousSearchResult()"
                        title="Previous Result">

                        <i data-lucide="chevron-left"></i>

                    </button>


                    <button
                        type="button"
                        onclick="nextSearchResult()"
                        title="Next Result">

                        <i data-lucide="chevron-right"></i>

                    </button>


                </div>


            </div>


            <!-- PDF PAGES -->

            <div
                id="pagesContainer"
                class="pages-container">

            </div>


        </div>


    </div>


</div>


<!-- =====================================================
     MOBILE CONTROLS
     ===================================================== -->

<div class="mobile-controls">


    <button
        type="button"
        onclick="previousPage()"
        title="Previous Page">

        <i data-lucide="chevron-left"></i>

    </button>


    <button
        type="button"
        onclick="nextPage()"
        title="Next Page">

        <i data-lucide="chevron-right"></i>

    </button>


</div>


<!-- =====================================================
     HIGHLIGHT TOOLBAR
     ===================================================== -->

<%
if (isNormalUser) {
%>

<div
    id="highlightToolbar"
    class="highlight-toolbar">


    <!-- YELLOW -->

    <button
        type="button"
        class="color-yellow"
        onclick="applyHighlightColor('yellow')"
        title="Yellow">

        <span class="highlight-color-dot"></span>

    </button>


    <!-- GREEN -->

    <button
        type="button"
        class="color-green"
        onclick="applyHighlightColor('green')"
        title="Green">

        <span class="highlight-color-dot"></span>

    </button>


    <!-- BLUE -->

    <button
        type="button"
        class="color-blue"
        onclick="applyHighlightColor('blue')"
        title="Blue">

        <span class="highlight-color-dot"></span>

    </button>


    <!-- PINK -->

    <button
        type="button"
        class="color-pink"
        onclick="applyHighlightColor('pink')"
        title="Pink">

        <span class="highlight-color-dot"></span>

    </button>


    <!-- DELETE -->

    <button
        type="button"
        class="remove-button"
        onclick="removeSelectedHighlight()"
        title="Remove Highlight">

        <i data-lucide="trash-2"></i>

    </button>


</div>

<%
}
%>


<!-- =====================================================
     SERVER DATA FOR JAVASCRIPT
     ===================================================== -->

<script>

    window.pdfUrl =
        "<%=pdfUrl != null ? pdfUrl : ""%>";

    window.bookId =
        "<%=bookId%>";

    window.contextPath =
        "<%=contextPath%>";

    window.currentPage =
        <%=savedPage%>;

    window.isNormalUser =
        <%=isNormalUser%>;

</script>


<!-- =====================================================
     READER JAVASCRIPT
     ===================================================== -->

<script src="<%=contextPath%>/javascript/read-book.js">
</script>


<!-- =====================================================
     INITIALIZE LUCIDE ICONS
     ===================================================== -->

<script>

    if (typeof lucide !== "undefined") {
        lucide.createIcons();
    }

</script>


</body>

</html>
