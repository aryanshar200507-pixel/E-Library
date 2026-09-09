package com.project.elibrary.controller.bookmark;

import java.io.IOException;
import java.util.List;

import com.project.elibrary.bean.bookmark.Bookmark;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.bookmarkservice.BookmarkService;
import com.project.elibrary.service.bookmarkservice.BookmarkServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/books/bookmark")
public class BookmarkServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private BookmarkService bookmarkService;

    
    public BookmarkServlet() {
        bookmarkService = new BookmarkServiceImpl();
    }
    
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User is not logged in.");
            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User is not logged in.");
            return;
        }
        
        String action = request.getParameter("action");

        if ("list".equalsIgnoreCase(action)) {
            getBookmarks(request, response, loggedInUser);
            return;
        }

        String bookIdParam =
                request.getParameter("bookId");

        String pageNumberParam =
                request.getParameter("pageNumber");

        if (bookIdParam == null ||
            bookIdParam.isBlank() ||
            pageNumberParam == null ||
            pageNumberParam.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Book ID and page number are required.");
            return;
        }

        Long bookId;
        int pageNumber;

        try {

            bookId = Long.parseLong(bookIdParam);
            pageNumber = Integer.parseInt(pageNumberParam);

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid book ID or page number.");
            return;
        }

        if (bookId <= 0 || pageNumber < 1) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid book ID or page number.");
            return;
        }

        Bookmark bookmark =
                bookmarkService.getBookmark(
                        loggedInUser.getUserId(),
                        bookId,
                        pageNumber);

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        if (bookmark != null) {
            response.getWriter().write("true");
        } else {
            response.getWriter().write("false");
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Get logged-in user
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User is not logged in.");
            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User is not logged in.");
            return;
        }

        // Only USER can create/remove bookmarks
        if (loggedInUser.getRole() == null ||
            !"USER".equalsIgnoreCase(
                    loggedInUser.getRole().name())) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only users can manage bookmarks.");
            return;
        }

        // Get book ID
        String bookIdParam = request.getParameter("bookId");

        // Get PDF page number
        String pageNumberParam =
                request.getParameter("pageNumber");

        if (bookIdParam == null ||
            bookIdParam.isBlank() ||
            pageNumberParam == null ||
            pageNumberParam.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Book ID and page number are required.");
            return;
        }

        Long bookId;
        int pageNumber;

        try {

            bookId = Long.parseLong(bookIdParam);
            pageNumber = Integer.parseInt(pageNumberParam);

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid book ID or page number.");
            return;
        }

        if (bookId <= 0 || pageNumber < 1) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid book ID or page number.");
            return;
        }

        Long userId = loggedInUser.getUserId();

        // Check whether bookmark already exists
        Bookmark existingBookmark =
                bookmarkService.getBookmark(
                        userId,
                        bookId,
                        pageNumber);

        boolean success;
        String action;

        if (existingBookmark != null) {

            // Already bookmarked → remove it
            success = bookmarkService.removeBookmark(
                    userId,
                    bookId,
                    pageNumber);

            action = "removed";

        } else {

            // Not bookmarked → create it
            Bookmark bookmark =
                    new Bookmark(
                            userId,
                            bookId,
                            pageNumber);

            success = bookmarkService.addBookmark(bookmark);

            action = "added";
        }

        if (!success) {

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Failed to update bookmark.");
            return;
        }

        /*
         * Return a simple response for JavaScript.
         *
         * Later the reader will use this response
         * to change the bookmark icon.
         */
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(action);
    }
    
    private void getBookmarks(HttpServletRequest request,
            HttpServletResponse response,
            User loggedInUser)
throws IOException {

String bookIdParam = request.getParameter("bookId");

if (bookIdParam == null || bookIdParam.isBlank()) {
response.sendError(
HttpServletResponse.SC_BAD_REQUEST,
"Book ID is required."
);
return;
}

Long bookId;

try {
bookId = Long.parseLong(bookIdParam);
} catch (NumberFormatException e) {
response.sendError(
HttpServletResponse.SC_BAD_REQUEST,
"Invalid book ID."
);
return;
}

if (bookId <= 0) {
response.sendError(
HttpServletResponse.SC_BAD_REQUEST,
"Invalid book ID."
);
return;
}

List<Bookmark> bookmarks =
bookmarkService.getBookmarks(
loggedInUser.getUserId(),
bookId
);

response.setContentType("application/json");
response.setCharacterEncoding("UTF-8");

StringBuilder json = new StringBuilder();

json.append("[");

for (int i = 0; i < bookmarks.size(); i++) {

Bookmark bookmark = bookmarks.get(i);

if (i > 0) {
json.append(",");
}

json.append("{");
json.append("\"pageNumber\":");
json.append(bookmark.getPageNumber());
json.append("}");
}

json.append("]");

response.getWriter().write(json.toString());
}
}