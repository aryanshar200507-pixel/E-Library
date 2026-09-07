package com.project.elibrary.controller.comment;

import java.io.IOException;

import com.project.elibrary.bean.comment.Comment;
import com.project.elibrary.bean.enums.Role;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.commentservice.CommentService;
import com.project.elibrary.service.commentservice.CommentServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CommentService commentService;


    public CommentServlet() {

        commentService = new CommentServiceImpl();
    }


    /*
     * =====================================================
     * Handles all comment operations
     *
     * ADD
     * UPDATE
     * DELETE
     * =====================================================
     */
    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {


        // =================================================
        // 1. Check whether user is logged in
        // =================================================

        HttpSession session =
                request.getSession(false);


        if (session == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }


        User loggedInUser =
                (User) session.getAttribute("loggedInUser");


        if (loggedInUser == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login"
            );

            return;
        }


        // =================================================
        // 2. Get requested action
        // =================================================

        String action =
                request.getParameter("action");


        if (action == null
                || action.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Comment action is required."
            );

            return;
        }


        try {


            // =================================================
            // 3. Perform requested operation
            // =================================================

            switch (action) {


                case "add":

                    addComment(
                            request,
                            response,
                            loggedInUser
                    );

                    break;


                case "update":

                    updateComment(
                            request,
                            response,
                            loggedInUser
                    );

                    break;


                case "delete":

                    deleteComment(
                            request,
                            response,
                            loggedInUser
                    );

                    break;


                default:

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid comment action."
                    );
            }


        } catch (NumberFormatException e) {


            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid comment ID or book ID."
            );


        } catch (IllegalArgumentException e) {


            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage()
            );
        }
    }



    // =====================================================
    // ADD COMMENT
    // =====================================================

    private void addComment(HttpServletRequest request,
                            HttpServletResponse response,
                            User loggedInUser)
            throws IOException {


        // -------------------------------------------------
        // Only USER can add comments
        // -------------------------------------------------

        if (loggedInUser.getRole() != Role.USER) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only users can add comments."
            );

            return;
        }


        // -------------------------------------------------
        // Get book ID
        // -------------------------------------------------

        String bookIdParam =
                request.getParameter("bookId");


        // -------------------------------------------------
        // Get comment text
        // -------------------------------------------------

        String commentText =
                request.getParameter("comment");


        if (bookIdParam == null
                || commentText == null) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Book ID and comment are required."
            );

            return;
        }


        Long bookId =
                Long.parseLong(bookIdParam);


        // -------------------------------------------------
        // Create Comment object
        // -------------------------------------------------

        Comment comment =
                new Comment(
                        loggedInUser.getUserId(),
                        bookId,
                        commentText
                );


        // -------------------------------------------------
        // Save comment
        // -------------------------------------------------

        commentService.addComment(comment);


        // -------------------------------------------------
        // Return to SAME BOOK DETAILS PAGE
        // -------------------------------------------------

        response.sendRedirect(
                request.getContextPath()
                + "/books/details?id="
                + bookId
        );
    }



    // =====================================================
    // UPDATE COMMENT
    // =====================================================

    private void updateComment(HttpServletRequest request,
                               HttpServletResponse response,
                               User loggedInUser)
            throws IOException {


        // -------------------------------------------------
        // Only USER can edit comments
        // -------------------------------------------------

        if (loggedInUser.getRole() != Role.USER) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only users can edit comments."
            );

            return;
        }


        // -------------------------------------------------
        // Get comment ID
        // -------------------------------------------------

        String commentIdParam =
                request.getParameter("commentId");


        // -------------------------------------------------
        // Get book ID
        // -------------------------------------------------

        String bookIdParam =
                request.getParameter("bookId");


        // -------------------------------------------------
        // Get updated comment text
        // -------------------------------------------------

        String commentText =
                request.getParameter("comment");


        if (commentIdParam == null
                || bookIdParam == null
                || commentText == null) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Comment ID, book ID and comment are required."
            );

            return;
        }


        Long commentId =
                Long.parseLong(commentIdParam);


        Long bookId =
                Long.parseLong(bookIdParam);


        // -------------------------------------------------
        // Find existing comment
        // -------------------------------------------------

        Comment existingComment =
                commentService.getCommentById(commentId);


        if (existingComment == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Comment not found."
            );

            return;
        }


        // -------------------------------------------------
        // SECURITY CHECK
        //
        // User can edit ONLY their own comment
        // -------------------------------------------------

        if (!existingComment.getUserId()
                .equals(loggedInUser.getUserId())) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "You can edit only your own comments."
            );

            return;
        }


        // -------------------------------------------------
        // Update comment text
        // -------------------------------------------------

        existingComment.setComment(
                commentText
        );


        // -------------------------------------------------
        // Save updated comment
        // -------------------------------------------------

        commentService.updateComment(
                existingComment
        );


        // -------------------------------------------------
        // Return to SAME BOOK DETAILS PAGE
        // -------------------------------------------------

        response.sendRedirect(
                request.getContextPath()
                + "/books/details?id="
                + bookId
        );
    }



    // =====================================================
    // DELETE COMMENT
    // =====================================================

    private void deleteComment(HttpServletRequest request,
                               HttpServletResponse response,
                               User loggedInUser)
            throws IOException {


        // -------------------------------------------------
        // Get comment ID
        // -------------------------------------------------

        String commentIdParam =
                request.getParameter("commentId");


        // -------------------------------------------------
        // Get book ID
        // -------------------------------------------------

        String bookIdParam =
                request.getParameter("bookId");


        if (commentIdParam == null
                || bookIdParam == null) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Comment ID and book ID are required."
            );

            return;
        }


        Long commentId =
                Long.parseLong(commentIdParam);


        Long bookId =
                Long.parseLong(bookIdParam);


        // -------------------------------------------------
        // Find comment
        // -------------------------------------------------

        Comment existingComment =
                commentService.getCommentById(commentId);


        if (existingComment == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Comment not found."
            );

            return;
        }


        // =================================================
        // SECURITY CHECK
        // =================================================

        /*
         * USER:
         * Can delete ONLY their own comment.
         *
         * ADMIN:
         * Can delete ANY comment.
         */

        if (loggedInUser.getRole() == Role.USER) {


            if (!existingComment.getUserId()
                    .equals(loggedInUser.getUserId())) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "You can delete only your own comments."
                );

                return;
            }


        } else if (loggedInUser.getRole() == Role.ADMIN) {


            // ADMIN can delete any comment.


        } else {


            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "You are not allowed to delete comments."
            );

            return;
        }


        // -------------------------------------------------
        // Delete comment
        // -------------------------------------------------

        commentService.deleteComment(
                commentId
        );


        // -------------------------------------------------
        // Return to SAME BOOK DETAILS PAGE
        // -------------------------------------------------

        response.sendRedirect(
                request.getContextPath()
                + "/books/details?id="
                + bookId
        );
    }
}