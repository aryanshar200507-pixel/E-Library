<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.project.elibrary.bean.bookrequest.BookRequest"%>

<%@ page import="com.project.elibrary.bean.user.User"%>
<%@ page import="com.project.elibrary.bean.enums.Role"%>

<%
    List<BookRequest> requests =
            (List<BookRequest>) request.getAttribute("requests");

    Integer requestCount =
            (Integer) request.getAttribute("requestCount");

    String error =
            (String) request.getAttribute("error");

    String success =
            (String) request.getAttribute("success");
%>

<%
    String voteSuccess =
            (String) session.getAttribute("voteSuccess");

    String voteError =
            (String) session.getAttribute("voteError");

    String deleteSuccess =
            (String) session.getAttribute("deleteSuccess");

    String deleteError =
            (String) session.getAttribute("deleteError");

    // Remove messages after reading them
    session.removeAttribute("voteSuccess");
    session.removeAttribute("voteError");
    session.removeAttribute("deleteSuccess");
    session.removeAttribute("deleteError");
%>

<%
    User loggedInUser =
            (User) session.getAttribute("loggedInUser");

    boolean isAdmin =
            loggedInUser != null
            && loggedInUser.getRole() == Role.ADMIN;
%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Weekly Book Requests</title>

    <style>

        body {
            font-family: Arial, sans-serif;
            background: #f4efe6;
            margin: 0;
            padding: 30px;
        }

        .container {
            max-width: 900px;
            margin: auto;
        }

        h1 {
            color: #5c3b24;
        }

        .count {
            background: #e8dccb;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            color: #5c3b24;
            font-weight: bold;
        }

        .message {
            padding: 12px;
            margin-bottom: 15px;
            border-radius: 6px;
        }

        .error {
            background: #f8d7da;
            color: #842029;
        }

        .success {
            background: #d1e7dd;
            color: #0f5132;
        }

        .form-box {
            background: white;
            padding: 25px;
            border-radius: 10px;
            margin-bottom: 30px;
        }

        label {
            display: block;
            margin-top: 12px;
            margin-bottom: 5px;
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 10px;
            box-sizing: border-box;
        }

        button {
            margin-top: 20px;
            padding: 10px 20px;
            background: #6b4423;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .request {
            background: white;
            padding: 20px;
            margin-bottom: 15px;
            border-radius: 8px;
        }

        .request h3 {
            margin-top: 0;
            color: #5c3b24;
        }

    </style>

</head>

<body>

<div class="container">

    <h1>Weekly Book Requests</h1>

    <div class="count">
        Requests this week:
        <%= requestCount != null ? requestCount : 0 %> / 10
    </div>

    <% if (error != null) { %>

        <div class="message error">
            <%= error %>
        </div>

    <% } %>

    <% if (success != null) { %>

        <div class="message success">
            <%= success %>
        </div>

    <% } %>
    
    
    <% if (voteSuccess != null) { %>

    <div class="message success">
        <%= voteSuccess %>
    </div>

<% } %>

<% if (voteError != null) { %>

    <div class="message error">
        <%= voteError %>
    </div>

<% } %>

<% if (deleteSuccess != null) { %>

    <div class="message success">
        <%= deleteSuccess %>
    </div>

<% } %>

<% if (deleteError != null) { %>

    <div class="message error">
        <%= deleteError %>
    </div>

<% } %>


    <% if (!isAdmin && (requestCount == null || requestCount < 10)) { %>

    <div class="form-box">

        <h2>Request a Book</h2>

        <form action="<%= request.getContextPath() %>/book-request"
              method="post">

            <label>Book Title *</label>

            <input type="text"
                   name="title"
                   required
                   maxlength="255">

            <label>Author</label>

            <input type="text"
                   name="author"
                   maxlength="255">

            <button type="submit">
                Submit Request
            </button>

        </form>

    </div>

<% } %>


    <h2>Requested Books</h2>

    <%
        if (requests != null && !requests.isEmpty()) {

            for (BookRequest bookRequest : requests) {
    %>

        <div class="request">

            <h3>
                <%= bookRequest.getTitle() %>
            </h3>

            <% if (bookRequest.getAuthor() != null
                    && !bookRequest.getAuthor().isEmpty()) { %>

                <p>
                    Author:
                    <%= bookRequest.getAuthor() %>
                </p>

            <% } %>

            <%
    int voteCount =
            ((com.project.elibrary.service.bookrequestservice.BookRequestVoteService)
            new com.project.elibrary.service.bookrequestservice.BookRequestVoteService())
            .getVoteCount(bookRequest.getRequestId());
%>

<p>
    Votes: <%= voteCount %>
</p>

<% if (isAdmin) { %>

    <!-- ADMIN can delete the request -->
    <form action="<%= request.getContextPath() %>/book-request/delete"
          method="post">

        <input type="hidden"
               name="requestId"
               value="<%= bookRequest.getRequestId() %>">

        <button type="submit">
            Delete
        </button>

    </form>

<% } else { %>

    <!-- Normal USER can vote -->
    <form action="<%= request.getContextPath() %>/book-request/vote"
          method="post">

        <input type="hidden"
               name="requestId"
               value="<%= bookRequest.getRequestId() %>">

        <button type="submit">
            Vote
        </button>

    </form>

<% } %>

        </div>

    <%
            }

        } else {
    %>

        <p>No book requests this week.</p>

    <%
        }
    %>

</div>

</body>

</html>