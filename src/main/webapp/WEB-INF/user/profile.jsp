<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<%@ page import="com.project.elibrary.bean.user.User"%>

<%
User user = (User) request.getAttribute("user");
%>

<!DOCTYPE html>

<html lang="en">

<head>


<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>My Profile - E-Library</title>

<!-- Dashboard CSS -->
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/css/dashboard.css">

<!-- Lucide Icons -->
<script src="https://unpkg.com/lucide@latest"></script>

<style>

    .profile-page {
        min-height: calc(100vh - 70px);
        padding: 45px 20px;
        background: #f4f6f9;
    }

    .profile-wrapper {
        max-width: 950px;
        margin: auto;
    }

    .profile-header {
        margin-bottom: 28px;
    }

    .profile-header h1 {
        margin: 0;
        font-size: 30px;
        font-weight: 700;
        color: #252a34;
    }

    .profile-header p {
        margin-top: 7px;
        color: #777;
        font-size: 15px;
    }

    .profile-card {
        background: #ffffff;
        border-radius: 16px;
        box-shadow: 0 5px 25px rgba(0, 0, 0, 0.07);
        overflow: hidden;
    }

    .profile-top {
        padding: 32px;
        display: flex;
        align-items: center;
        gap: 20px;
        border-bottom: 1px solid #eeeeee;
    }

    .avatar {
        width: 82px;
        height: 82px;
        border-radius: 50%;
        background: #252a34;
        color: white;

        display: flex;
        align-items: center;
        justify-content: center;

        font-size: 30px;
        font-weight: 600;
        flex-shrink: 0;
    }

    .profile-name h2 {
        margin: 0 0 6px;
        font-size: 23px;
        color: #252a34;
    }

    .profile-name p {
        margin: 0;
        color: #777;
        font-size: 14px;
    }

    .status-badge {
        display: inline-block;
        margin-top: 10px;
        padding: 5px 12px;
        border-radius: 20px;
        background: #e8f7ee;
        color: #16834b;
        font-size: 12px;
        font-weight: 600;
    }

    .profile-content {
        padding: 32px;
    }

    .section-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 22px;
    }

    .section-title {
        margin: 0;
        font-size: 18px;
        font-weight: 650;
        color: #252a34;
    }

    .form-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 22px;
    }

    .form-group {
        display: flex;
        flex-direction: column;
    }

    .form-group label {
        font-size: 13px;
        font-weight: 600;
        color: #555;
        margin-bottom: 8px;
    }

    .form-group input {
        width: 100%;
        height: 45px;
        padding: 0 13px;

        border: 1px solid #dfe2e6;
        border-radius: 8px;

        font-size: 14px;
        color: #252a34;

        outline: none;
        transition: 0.2s;
    }

    .form-group input:focus {
        border-color: #252a34;
        box-shadow: 0 0 0 3px rgba(37, 42, 52, 0.08);
    }

    .form-group input[readonly] {
        background: #f7f7f8;
        color: #777;
    }

    .edit-btn {
        height: 40px;
        padding: 0 17px;

        border: 1px solid #d7d9dd;
        border-radius: 8px;

        background: white;
        color: #252a34;

        font-size: 13px;
        font-weight: 600;
        cursor: pointer;

        display: flex;
        align-items: center;
        gap: 7px;

        transition: 0.2s;
    }

    .edit-btn:hover {
        background: #f5f6f8;
    }

    .edit-btn i {
        width: 15px;
        height: 15px;
    }

    .account-info {
        margin-top: 35px;
        padding-top: 28px;
        border-top: 1px solid #eeeeee;
    }

    .info-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 15px;
    }

    .info-box {
        background: #f8f9fb;
        border: 1px solid #eeeeee;
        border-radius: 10px;
        padding: 16px;
    }

    .info-label {
        font-size: 12px;
        color: #888;
        margin-bottom: 5px;
    }

    .info-value {
        font-size: 14px;
        font-weight: 600;
        color: #252a34;
    }

    .actions {
        margin-top: 30px;

        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .left-actions,
    .right-actions {
        display: flex;
        gap: 12px;
    }

    .save-btn,
    .cancel-btn,
    .password-btn {
        height: 43px;
        padding: 0 20px;

        border-radius: 8px;

        font-size: 14px;
        font-weight: 600;
        cursor: pointer;

        transition: 0.2s;
    }

    .save-btn {
        border: none;
        background: #252a34;
        color: white;
    }

    .save-btn:hover {
        background: #11151c;
        transform: translateY(-1px);
    }

    .cancel-btn {
        border: 1px solid #d7d9dd;
        background: white;
        color: #555;
    }

    .cancel-btn:hover {
        background: #f5f6f8;
    }

    .password-btn {
        border: 1px solid #d7d9dd;
        background: white;
        color: #252a34;
    }

    .password-btn:hover {
        background: #f5f6f8;
    }

    .message {
        padding: 12px 15px;
        border-radius: 8px;
        margin-bottom: 25px;
        font-size: 14px;
    }

    .success {
        background: #eaf8ef;
        color: #167543;
        border: 1px solid #ccebd8;
    }

    .error {
        background: #fff0f0;
        color: #c62828;
        border: 1px solid #f2cccc;
    }

    .editing {
        background: #fafafa;
    }

    @media (max-width: 700px) {

        .profile-page {
            padding: 25px 15px;
        }

        .profile-top {
            padding: 25px;
        }

        .profile-content {
            padding: 25px;
        }

        .form-grid,
        .info-grid {
            grid-template-columns: 1fr;
        }

        .section-header {
            align-items: flex-start;
            gap: 15px;
        }

        .actions {
            flex-direction: column;
            align-items: stretch;
            gap: 15px;
        }

        .left-actions,
        .right-actions {
            width: 100%;
        }

        .left-actions button,
        .right-actions button {
            flex: 1;
        }

    }

</style>

</head>

<body>


<!-- HEADER / NAVBAR -->

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

        <a href="${pageContext.request.contextPath}/book-request">
            <i data-lucide="book-plus"></i>
            Book Requests
        </a>

        <a href="${pageContext.request.contextPath}/books/bookmark-book">
            <i data-lucide="bookmark"></i>
            Bookmarks
        </a>

        <a href="${pageContext.request.contextPath}/user/suggestion"
           class="nav-link">
            <i data-lucide="message-square-plus"></i>
            <span>Suggestions</span>
        </a>

        <a href="${pageContext.request.contextPath}/books/history"
           class="nav-link">
            <i data-lucide="history"></i>
            <span>History</span>
        </a>

        <a href="${pageContext.request.contextPath}/profile"
           class="active">
            <i data-lucide="user"></i>
            Profile
        </a>

        <a href="${pageContext.request.contextPath}/logout">
            <i data-lucide="log-out"></i>
            Logout
        </a>

    </nav>

</header>


<!-- PROFILE PAGE -->

<div class="profile-page">

    <div class="profile-wrapper">

        <div class="profile-header">

            <h1>My Profile</h1>

            <p>
                Manage your personal information and account settings.
            </p>

        </div>


        <div class="profile-card">


            <!-- PROFILE TOP -->

            <div class="profile-top">

                <div class="avatar">

                    <%= user.getName()
                            .substring(0, 1)
                            .toUpperCase() %>

                </div>

                <div class="profile-name">

                    <h2>
                        <%= user.getName() %>
                    </h2>

                    <p>
                        <%= user.getEmail() %>
                    </p>

                    <span class="status-badge">
                        <%= user.getStatus() %>
                    </span>

                </div>

            </div>


            <!-- PROFILE CONTENT -->

            <div class="profile-content">


                <!-- MESSAGES -->

                <%
                    String success =
                        request.getParameter("success");

                    String error =
                        (String) request.getAttribute("error");

                    if (success != null) {
                %>

                    <div class="message success">
                        <%= success %>
                    </div>

                <%
                    }

                    if (error != null) {
                %>

                    <div class="message error">
                        <%= error %>
                    </div>

                <%
                    }
                %>


                <!-- PERSONAL INFORMATION -->

                <div class="section-header">

                    <h3 class="section-title">
                        Personal Information
                    </h3>

                    <button
                        type="button"
                        class="edit-btn"
                        id="editButton"
                        onclick="enableEditing()">

                        <i data-lucide="pencil"></i>
                        Edit Profile

                    </button>

                </div>


                <form
                    id="profileForm"
                    action="${pageContext.request.contextPath}/profile"
                    method="post">


                    <div class="form-grid">


                        <!-- NAME -->

                        <div class="form-group">

                            <label for="name">
                                Full Name
                            </label>

                            <input
                                type="text"
                                id="name"
                                name="name"
                                value="<%= user.getName() %>"
                                readonly
                                required>

                        </div>


                        <!-- EMAIL -->

                        <div class="form-group">

                            <label for="email">
                                Email Address
                            </label>

                            <input
                                type="email"
                                id="email"
                                name="email"
                                value="<%= user.getEmail() %>"
                                readonly
                                required>

                        </div>


                    </div>


                    <!-- ACCOUNT INFORMATION -->

                    <div class="account-info">

                        <h3 class="section-title">
                            Account Information
                        </h3>

                        <div class="info-grid">

                            <div class="info-box">

                                <div class="info-label">
                                    Account Status
                                </div>

                                <div class="info-value">
                                    <%= user.getStatus() %>
                                </div>

                            </div>

                            <div class="info-box">

                                <div class="info-label">
                                    Account Type
                                </div>

                                <div class="info-value">
                                    <%= user.getRole() %>
                                </div>

                            </div>

                        </div>

                    </div>


                    <!-- ACTIONS -->

                    <div class="actions">


                        <div class="left-actions">

                            <button
                                type="submit"
                                class="save-btn"
                                id="saveButton"
                                style="display: none;">

                                Save Changes

                            </button>


                            <button
                                type="button"
                                class="cancel-btn"
                                id="cancelButton"
                                style="display: none;"
                                onclick="cancelEditing()">

                                Cancel

                            </button>

                        </div>


                        <div class="right-actions">

                            <button
                                type="button"
                                class="password-btn"
                                onclick="window.location.href='<%= request.getContextPath() %>/change-password'">

                                Change Password

                            </button>

                        </div>


                    </div>

                </form>

            </div>

        </div>

    </div>

</div>


<!-- EDIT PROFILE SCRIPT -->

<script>

    const nameInput = document.getElementById("name");
    const emailInput = document.getElementById("email");

    const editButton = document.getElementById("editButton");
    const saveButton = document.getElementById("saveButton");
    const cancelButton = document.getElementById("cancelButton");

    const originalName = nameInput.value;
    const originalEmail = emailInput.value;


    function enableEditing() {

        nameInput.removeAttribute("readonly");
        emailInput.removeAttribute("readonly");

        nameInput.classList.add("editing");
        emailInput.classList.add("editing");

        nameInput.focus();

        editButton.style.display = "none";

        saveButton.style.display = "inline-block";
        cancelButton.style.display = "inline-block";
    }


    function cancelEditing() {

        nameInput.value = originalName;
        emailInput.value = originalEmail;

        nameInput.setAttribute("readonly", true);
        emailInput.setAttribute("readonly", true);

        nameInput.classList.remove("editing");
        emailInput.classList.remove("editing");

        editButton.style.display = "flex";

        saveButton.style.display = "none";
        cancelButton.style.display = "none";
    }


    lucide.createIcons();

</script>

</body>

</html>
