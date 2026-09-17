<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.project.elibrary.bean.suggestions.AppSuggestions" %>
<%@ page import="com.project.elibrary.bean.user.User" %>

<%
    List<AppSuggestions> suggestions =
            (List<AppSuggestions>) request.getAttribute("suggestions");

    Map<Long, User> suggestionUsers =
            (Map<Long, User>) request.getAttribute("suggestionUsers");

    String success = (String) request.getAttribute("success");
    String error = (String) request.getAttribute("error");

    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>App Suggestions - Admin</title>

    <style>

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f5f6fa;
            color: #222;
        }

        .header {
            background: #1f2937;
            color: white;
            padding: 18px 35px;

            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .header h2 {
            margin: 0;
            font-size: 22px;
        }

        .header-actions {
            display: flex;
            gap: 10px;
        }

        .header-actions a {
            text-decoration: none;
            color: white;
            background: #374151;

            padding: 9px 15px;
            border-radius: 6px;

            font-size: 14px;
        }

        .header-actions a:hover {
            background: #4b5563;
        }

        .container {
            width: 92%;
            max-width: 1200px;

            margin: 35px auto;
        }

        .page-title {
            margin-bottom: 25px;
        }

        .page-title h1 {
            margin: 0 0 8px;
            font-size: 28px;
        }

        .page-title p {
            margin: 0;
            color: #6b7280;
        }

        .message {
            padding: 13px 16px;
            border-radius: 7px;
            margin-bottom: 20px;
            font-size: 14px;
        }

        .success {
            background: #dcfce7;
            color: #166534;
            border: 1px solid #bbf7d0;
        }

        .error {
            background: #fee2e2;
            color: #991b1b;
            border: 1px solid #fecaca;
        }

        .suggestions-container {
            display: flex;
            flex-direction: column;
            gap: 18px;
        }

        .suggestion-card {
            background: white;

            border-radius: 10px;

            padding: 22px;

            box-shadow:
                0 2px 8px rgba(0, 0, 0, 0.08);

            border: 1px solid #e5e7eb;
        }

        .suggestion-top {
            display: flex;

            justify-content: space-between;
            align-items: flex-start;

            gap: 20px;

            margin-bottom: 18px;
        }

        .user-info h3 {
            margin: 0 0 7px;
            font-size: 18px;
        }

        .user-info p {
            margin: 4px 0;
            color: #6b7280;
            font-size: 14px;
        }

        .status {
            padding: 6px 12px;

            border-radius: 20px;

            font-size: 12px;
            font-weight: bold;

            white-space: nowrap;
        }

        .status-new {
            background: #dbeafe;
            color: #1d4ed8;
        }

        .status-accepted {
            background: #dcfce7;
            color: #15803d;
        }

        .suggestion-description {
            background: #f9fafb;

            border: 1px solid #e5e7eb;

            border-radius: 7px;

            padding: 16px;

            margin-bottom: 18px;

            line-height: 1.6;

            white-space: pre-wrap;
            overflow-wrap: anywhere;
        }

        .suggestion-meta {
            color: #6b7280;
            font-size: 13px;

            margin-bottom: 18px;
        }

        .actions {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
        }

        .actions form {
            margin: 0;
        }

        .btn {
            border: none;

            padding: 9px 16px;

            border-radius: 6px;

            cursor: pointer;

            font-size: 14px;
            font-weight: 600;
        }

        .btn-accept {
            background: #2563eb;
            color: white;
        }

        .btn-accept:hover {
            background: #1d4ed8;
        }

        .btn-delete {
            background: #dc2626;
            color: white;
        }

        .btn-delete:hover {
            background: #b91c1c;
        }

        .btn-implemented {
            background: #16a34a;
            color: white;
        }

        .btn-implemented:hover {
            background: #15803d;
        }

        .empty-state {
            background: white;

            border: 1px solid #e5e7eb;

            border-radius: 10px;

            padding: 50px 20px;

            text-align: center;

            color: #6b7280;
        }

        .empty-state h3 {
            margin-top: 0;
            color: #374151;
        }

        @media (max-width: 700px) {

            .header {
                padding: 15px 18px;
            }

            .container {
                width: 94%;
                margin: 25px auto;
            }

            .suggestion-top {
                flex-direction: column;
            }

        }

    </style>

</head>

<body>


<div class="header">

    <h2>E-Library Admin</h2>

    <div class="header-actions">

        <a href="<%= contextPath %>/admin/dashboard">
            Dashboard
        </a>

        <a href="<%= contextPath %>/logout">
            Logout
        </a>

    </div>

</div>


<div class="container">


    <div class="page-title">

        <h1>App Suggestions</h1>

        <p>
            Review and manage suggestions submitted by users.
        </p>

    </div>


    <% if (success != null) { %>

        <div class="message success">
            <%= success %>
        </div>

    <% } %>


    <% if (error != null) { %>

        <div class="message error">
            <%= error %>
        </div>

    <% } %>


    <% if (suggestions == null || suggestions.isEmpty()) { %>

        <div class="empty-state">

            <h3>No Active Suggestions</h3>

            <p>
                There are currently no new or accepted suggestions.
            </p>

        </div>

    <% } else { %>


        <div class="suggestions-container">


            <% for (AppSuggestions suggestion : suggestions) {

                User suggestionUser =
                        suggestionUsers.get(suggestion.getUserId());

            %>


                <div class="suggestion-card">


                    <div class="suggestion-top">


                        <div class="user-info">

                            <% if (suggestionUser != null) { %>

                                <h3>
                                    <%= suggestionUser.getName() %>
                                </h3>

                                <p>
                                    Email:
                                    <%= suggestionUser.getEmail() %>
                                </p>

                            <% } else { %>

                                <h3>
                                    User ID:
                                    <%= suggestion.getUserId() %>
                                </h3>

                                <p>
                                    User information unavailable
                                </p>

                            <% } %>


                            <p>
                                User ID:
                                <%= suggestion.getUserId() %>
                            </p>

                        </div>


                        <% if ("NEW".equals(suggestion.getStatus())) { %>

                            <span class="status status-new">
                                NEW
                            </span>

                        <% } else if
                            ("ACCEPTED".equals(suggestion.getStatus())) { %>

                            <span class="status status-accepted">
                                ACCEPTED
                            </span>

                        <% } %>


                    </div>


                    <div class="suggestion-description">

                        <%= suggestion.getDescription() %>

                    </div>


                    <div class="suggestion-meta">

                        Submitted:
                        <%= suggestion.getCreatedAt() %>

                    </div>


                    <div class="actions">


                        <% if ("NEW".equals(suggestion.getStatus())) { %>


                            <!-- ACCEPT -->

                            <form
                                action="<%= contextPath %>/admin/suggestions"
                                method="post">

                                <input
                                    type="hidden"
                                    name="suggestionId"
                                    value="<%= suggestion.getSuggestionId() %>">

                                <input
                                    type="hidden"
                                    name="action"
                                    value="accept">

                                <button
                                    type="submit"
                                    class="btn btn-accept">

                                    Accept

                                </button>

                            </form>


                            <!-- DELETE -->

                            <form
                                action="<%= contextPath %>/admin/suggestions"
                                method="post"
                                onsubmit="return confirm('Are you sure you want to delete this suggestion? This action cannot be undone.');">

                                <input
                                    type="hidden"
                                    name="suggestionId"
                                    value="<%= suggestion.getSuggestionId() %>">

                                <input
                                    type="hidden"
                                    name="action"
                                    value="delete">

                                <button
                                    type="submit"
                                    class="btn btn-delete">

                                    Delete

                                </button>

                            </form>


                        <% } else if
                            ("ACCEPTED".equals(suggestion.getStatus())) { %>


                            <!-- IMPLEMENTED -->

                            <form
                                action="<%= contextPath %>/admin/suggestions"
                                method="post"
                                onsubmit="return confirm('Are you sure this suggestion has been implemented? It will be permanently removed from the active suggestions list.');">

                                <input
                                    type="hidden"
                                    name="suggestionId"
                                    value="<%= suggestion.getSuggestionId() %>">

                                <input
                                    type="hidden"
                                    name="action"
                                    value="delete">

                                <button
                                    type="submit"
                                    class="btn btn-implemented">

                                    Implemented

                                </button>

                            </form>


                            <!-- DELETE -->

                            <form
                                action="<%= contextPath %>/admin/suggestions"
                                method="post"
                                onsubmit="return confirm('Are you sure you want to delete this suggestion? This action cannot be undone.');">

                                <input
                                    type="hidden"
                                    name="suggestionId"
                                    value="<%= suggestion.getSuggestionId() %>">

                                <input
                                    type="hidden"
                                    name="action"
                                    value="delete">

                                <button
                                    type="submit"
                                    class="btn btn-delete">

                                    Delete

                                </button>

                            </form>


                        <% } %>


                    </div>


                </div>


            <% } %>


        </div>


    <% } %>


</div>


</body>

</html>