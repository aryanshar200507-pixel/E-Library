package com.project.elibrary.controller.highlight;

import java.io.IOException;
import java.util.List;

import com.project.elibrary.bean.highlight.Highlight;
import com.project.elibrary.bean.user.User;
import com.project.elibrary.service.highlightservice.HighlightService;
import com.project.elibrary.service.highlightservice.HighlightServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/books/highlight")
public class HighlightServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private HighlightService highlightService;


    @Override
    public void init() throws ServletException {

        highlightService =
                new HighlightServiceImpl();
    }


    /*
     * =====================================================
     * GET
     * =====================================================
     *
     * Used to load all highlights belonging to
     * the currently logged-in user for a book.
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);


        if (session == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User is not logged in."
            );

            return;
        }


        User loggedInUser =
                (User) session.getAttribute(
                        "loggedInUser"
                );


        if (loggedInUser == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User is not logged in."
            );

            return;
        }


        String action =
                request.getParameter("action");


        if ("list".equalsIgnoreCase(action)) {

            getHighlights(
                    request,
                    response,
                    loggedInUser
            );

            return;
        }


        response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid action."
        );
    }


    /*
     * =====================================================
     * POST
     * =====================================================
     *
     * POST has two possible jobs:
     *
     * 1. Create a new highlight
     *
     * 2. Update the color of an existing highlight
     *
     * Example:
     *
     * /books/highlight
     *
     * -> create highlight
     *
     * /books/highlight?action=updatecolor
     *
     * -> change highlight color
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);


        if (session == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User is not logged in."
            );

            return;
        }


        User loggedInUser =
                (User) session.getAttribute(
                        "loggedInUser"
                );


        if (loggedInUser == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User is not logged in."
            );

            return;
        }


        /*
         * Only USER accounts can manage
         * personal highlights.
         */
        if (loggedInUser.getRole() == null ||
            !"USER".equalsIgnoreCase(
                    loggedInUser.getRole().name())) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Only users can manage highlights."
            );

            return;
        }


        /*
         * Check whether this POST request is
         * asking to change a highlight's color.
         */
        String action =
                request.getParameter("action");


        if ("updatecolor".equalsIgnoreCase(action)) {

            updateHighlightColor(
                    request,
                    response,
                    loggedInUser
            );

            return;
        }


        /*
         * If action is not updatecolor,
         * continue with normal highlight creation.
         */
        createHighlight(
                request,
                response,
                loggedInUser
        );
    }


    /*
     * =====================================================
     * CREATE HIGHLIGHT
     * =====================================================
     */
    private void createHighlight(
            HttpServletRequest request,
            HttpServletResponse response,
            User loggedInUser)
            throws IOException {


        String bookIdParam =
                request.getParameter("bookId");

        String pageNumberParam =
                request.getParameter("pageNumber");

        String selectedText =
                request.getParameter("selectedText");

        String startOffsetParam =
                request.getParameter("startOffset");

        String endOffsetParam =
                request.getParameter("endOffset");

        String rectanglesJson =
                request.getParameter(
                        "rectanglesJson"
                );

        String color =
                request.getParameter("color");


        if (bookIdParam == null ||
            bookIdParam.isBlank() ||

            pageNumberParam == null ||
            pageNumberParam.isBlank() ||

            selectedText == null ||
            selectedText.isBlank() ||

            startOffsetParam == null ||
            startOffsetParam.isBlank() ||

            endOffsetParam == null ||
            endOffsetParam.isBlank() ||

            rectanglesJson == null ||
            rectanglesJson.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Required highlight data is missing."
            );

            return;
        }


        Long bookId;

        int pageNumber;

        int startOffset;

        int endOffset;


        try {

            bookId =
                    Long.parseLong(bookIdParam);

            pageNumber =
                    Integer.parseInt(
                            pageNumberParam
                    );

            startOffset =
                    Integer.parseInt(
                            startOffsetParam
                    );

            endOffset =
                    Integer.parseInt(
                            endOffsetParam
                    );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid highlight data."
            );

            return;
        }


        if (bookId <= 0 ||
            pageNumber < 1 ||
            startOffset < 0 ||
            endOffset <= startOffset) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid highlight values."
            );

            return;
        }


        if (color == null ||
            color.isBlank()) {

            color = "yellow";
        }


        Highlight highlight =
                new Highlight(
                        loggedInUser.getUserId(),
                        bookId,
                        pageNumber,
                        selectedText,
                        startOffset,
                        endOffset,
                        rectanglesJson,
                        color
                );


        boolean success =
                highlightService.addHighlight(
                        highlight
                );


        if (!success) {

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Failed to save highlight."
            );

            return;
        }


        response.setContentType(
                "text/plain"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );


        response.getWriter().write(
                "saved"
        );
    }


    /*
     * =====================================================
     * UPDATE HIGHLIGHT COLOR
     * =====================================================
     *
     * Called when the reader sends:
     *
     * POST /books/highlight?action=updatecolor
     *
     * Required parameters:
     *
     * highlightId
     * color
     */
    private void updateHighlightColor(
            HttpServletRequest request,
            HttpServletResponse response,
            User loggedInUser)
            throws IOException {


        String highlightIdParam =
                request.getParameter(
                        "highlightId"
                );


        String color =
                request.getParameter("color");


        /*
         * Make sure required values were sent.
         */
        if (highlightIdParam == null ||
            highlightIdParam.isBlank() ||

            color == null ||
            color.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Highlight ID and color are required."
            );

            return;
        }


        Long highlightId;


        try {

            highlightId =
                    Long.parseLong(
                            highlightIdParam
                    );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid highlight ID."
            );

            return;
        }


        if (highlightId <= 0) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid highlight ID."
            );

            return;
        }


        /*
         * Ask the service to update the color.
         *
         * loggedInUser.getUserId() is important because
         * the highlight must belong to this user.
         */
        boolean success =
                highlightService.updateHighlightColor(
                        highlightId,
                        loggedInUser.getUserId(),
                        color
                );


        if (!success) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Highlight not found or color could not be updated."
            );

            return;
        }


        response.setContentType(
                "text/plain"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );


        response.getWriter().write(
                "updated"
        );
    }


    /*
     * =====================================================
     * DELETE
     * =====================================================
     *
     * Deletes only the highlight belonging to
     * the currently logged-in user.
     */
    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);


        if (session == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User is not logged in."
            );

            return;
        }


        User loggedInUser =
                (User) session.getAttribute(
                        "loggedInUser"
                );


        if (loggedInUser == null) {

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "User is not logged in."
            );

            return;
        }


        String highlightIdParam =
                request.getParameter(
                        "highlightId"
                );


        if (highlightIdParam == null ||
            highlightIdParam.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Highlight ID is required."
            );

            return;
        }


        Long highlightId;


        try {

            highlightId =
                    Long.parseLong(
                            highlightIdParam
                    );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid highlight ID."
            );

            return;
        }


        boolean success =
                highlightService.removeHighlight(
                        highlightId,
                        loggedInUser.getUserId()
                );


        if (!success) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Highlight not found."
            );

            return;
        }


        response.setContentType(
                "text/plain"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );


        response.getWriter().write(
                "deleted"
        );
    }


    /*
     * =====================================================
     * GET HIGHLIGHTS
     * =====================================================
     */
    private void getHighlights(
            HttpServletRequest request,
            HttpServletResponse response,
            User loggedInUser)
            throws IOException {

        String bookIdParam =
                request.getParameter("bookId");


        if (bookIdParam == null ||
            bookIdParam.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Book ID is required."
            );

            return;
        }


        Long bookId;


        try {

            bookId =
                    Long.parseLong(
                            bookIdParam
                    );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid book ID."
            );

            return;
        }


        List<Highlight> highlights =
                highlightService.getHighlights(
                        loggedInUser.getUserId(),
                        bookId
                );


        response.setContentType(
                "application/json"
        );

        response.setCharacterEncoding(
                "UTF-8"
        );


        StringBuilder json =
                new StringBuilder();


        json.append("[");


        for (int i = 0;
             i < highlights.size();
             i++) {

            Highlight highlight =
                    highlights.get(i);


            if (i > 0) {

                json.append(",");
            }


            json.append("{");


            json.append("\"highlightId\":");
            json.append(
                    highlight.getHighlightId()
            );


            json.append(",");


            json.append("\"pageNumber\":");
            json.append(
                    highlight.getPageNumber()
            );


            json.append(",");


            json.append("\"selectedText\":\"");
            json.append(
                    escapeJson(
                            highlight.getSelectedText()
                    )
            );
            json.append("\"");


            json.append(",");


            json.append("\"startOffset\":");
            json.append(
                    highlight.getStartOffset()
            );


            json.append(",");


            json.append("\"endOffset\":");
            json.append(
                    highlight.getEndOffset()
            );


            json.append(",");


            json.append("\"rectanglesJson\":");
            json.append(
                    highlight.getRectanglesJson()
            );


            json.append(",");


            json.append("\"color\":\"");
            json.append(
                    escapeJson(
                            highlight.getColor()
                    )
            );
            json.append("\"");


            json.append("}");
        }


        json.append("]");


        response.getWriter().write(
                json.toString()
        );
    }


    /*
     * =====================================================
     * JSON ESCAPE
     * =====================================================
     */
    private String escapeJson(String value) {

        if (value == null) {

            return "";
        }


        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}