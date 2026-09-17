
<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.project.elibrary.bean.user.User"%>

<%
    User loggedInUser =
            (User) request.getAttribute("loggedInUser");

    String error =
            (String) request.getAttribute("error");

    String success =
            (String) request.getAttribute("success");
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Suggest an App - E-Library</title>

    <!-- Dashboard CSS -->
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/dashboard.css">

    <!-- Lucide Icons CDN -->
    <script src="https://unpkg.com/lucide@latest"></script>

    <style>

        .suggestion-container {
            max-width: 800px;
            margin: 40px auto;
            padding: 0 20px;
        }

        .suggestion-card {
            background: #ffffff;
            border-radius: 12px;
            padding: 30px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
        }

        .suggestion-header {
            margin-bottom: 25px;
        }

        .suggestion-header h2 {
            margin: 0 0 8px;
        }

        .suggestion-header p {
            margin: 0;
            color: #666;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
        }

        .form-control {
            width: 100%;
            box-sizing: border-box;
            padding: 12px 14px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 15px;
            font-family: inherit;
        }

        .form-control:focus {
            outline: none;
            border-color: #555;
        }

        .readonly-field {
            background-color: #f5f5f5;
            cursor: not-allowed;
        }

        .suggestion-textarea {
            min-height: 180px;
            resize: vertical;
        }

        .character-info {
            display: flex;
            justify-content: space-between;
            margin-top: 6px;
            font-size: 13px;
            color: #777;
        }

        .submit-button {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 12px 20px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 15px;
            font-weight: 600;
        }

        .back-button {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            margin-left: 10px;
            padding: 12px 20px;
            border-radius: 8px;
            text-decoration: none;
            font-size: 15px;
            font-weight: 600;
        }

        .message {
            padding: 12px 15px;
            border-radius: 8px;
            margin-bottom: 20px;
        }

        .success-message {
            background-color: #e8f5e9;
            color: #2e7d32;
        }

        .error-message {
            background-color: #ffebee;
            color: #c62828;
        }

        .form-note {
            margin-top: 8px;
            font-size: 13px;
            color: #777;
        }

        @media (max-width: 600px) {

            .suggestion-container {
                margin: 20px auto;
                padding: 0 15px;
            }

            .suggestion-card {
                padding: 20px;
            }

            .back-button {
                margin-left: 0;
                margin-top: 10px;
            }

        }

    </style>

</head>

<body>

    <!-- HEADER -->

    <header class="header">

        <h2>E-Library</h2>

        <nav class="nav">

            <a href="${pageContext.request.contextPath}/user/dashboard">
                <i data-lucide="layout-dashboard"></i>
                Dashboard
            </a>

            <a href="${pageContext.request.contextPath}/books">
                <i data-lucide="library"></i>
                Categories
            </a>

            <a href="${pageContext.request.contextPath}/books/bookmark-book">
                <i data-lucide="bookmark"></i>
                Bookmarks
            </a>

            <a href="${pageContext.request.contextPath}/books/history"
               class="nav-link">
                <i data-lucide="history"></i>
                <span>History</span>
            </a>

            <a href="${pageContext.request.contextPath}/user/suggestion">
                <i data-lucide="message-square-plus"></i>
                Suggest an App
            </a>

            <a href="${pageContext.request.contextPath}/logout">
                <i data-lucide="log-out"></i>
                Logout
            </a>

        </nav>

    </header>


    <!-- MAIN CONTAINER -->

    <div class="suggestion-container">

        <div class="suggestion-card">

            <!-- PAGE HEADER -->

            <div class="suggestion-header">

                <h2>
                    <i data-lucide="message-square-plus"></i>
                    Suggest an App
                </h2>

                <p>
                    Have an idea that could improve the E-Library?
                    Share your suggestion with us.
                </p>

            </div>


            <!-- SUCCESS MESSAGE -->

            <%
                if (success != null && !success.isBlank()) {
            %>

                <div class="message success-message">
                    <%= success %>
                </div>

            <%
                }
            %>


            <!-- ERROR MESSAGE -->

            <%
                if (error != null && !error.isBlank()) {
            %>

                <div class="message error-message">
                    <%= error %>
                </div>

            <%
                }
            %>


            <!-- SUGGESTION FORM -->

            <form
                action="${pageContext.request.contextPath}/user/suggestion"
                method="post">


                <!-- NAME -->

                <div class="form-group">

                    <label for="name">
                        Name
                    </label>

                    <input
                        type="text"
                        id="name"
                        class="form-control readonly-field"
                        value="<%= loggedInUser != null
                                ? loggedInUser.getName()
                                : "" %>"
                        readonly>

                </div>


                <!-- EMAIL -->

                <div class="form-group">

                    <label for="email">
                        Email
                    </label>

                    <input
                        type="email"
                        id="email"
                        class="form-control readonly-field"
                        value="<%= loggedInUser != null
                                ? loggedInUser.getEmail()
                                : "" %>"
                        readonly>

                </div>


                <!-- SUGGESTION -->

                <div class="form-group">

                    <label for="description">
                        Your Suggestion
                    </label>

                    <textarea
                        id="description"
                        name="description"
                        class="form-control suggestion-textarea"
                        minlength="10"
                        maxlength="1000"
                        required
                        placeholder="Describe the app or feature you would like to see..."></textarea>

                    <div class="character-info">

                        <span>
                            Minimum 10 characters
                        </span>

                        <span>
                            Maximum 1000 characters
                        </span>

                    </div>

                    <div class="form-note">
                        Please provide a clear description of your idea.
                    </div>

                </div>


                <!-- BUTTONS -->

                <div>

                    <button
                        type="submit"
                        class="submit-button">

                        <i data-lucide="send"></i>

                        Submit Suggestion

                    </button>


                    <a
                        href="${pageContext.request.contextPath}/user/dashboard"
                        class="back-button">

                        <i data-lucide="arrow-left"></i>

                        Back to Dashboard

                    </a>

                </div>

            </form>

        </div>

    </div>


    <!-- LUCIDE ICONS -->

    <script>
        lucide.createIcons();
    </script>

</body>

</html>

