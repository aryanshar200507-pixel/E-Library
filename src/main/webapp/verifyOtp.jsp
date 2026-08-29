<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
<head>
    <meta charset="UTF-8">
    <title>Verify OTP</title>
</head>

<body>

    <h2>Verify OTP</h2>

    <p>
        Enter the 6-digit OTP sent to your email.
    </p>

    <%
        String error =
            (String) request.getAttribute("error");
    %>

    <% if (error != null) { %>

        <p><%= error %></p>

    <% } %>

    <form action="verify-otp" method="post">

        <label>OTP:</label>

        <input type="text"
               name="otp"
               maxlength="6"
               required>

        <br><br>

        <button type="submit">
            Verify OTP
        </button>

    </form>

</body>
</html>