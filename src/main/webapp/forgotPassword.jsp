<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">
<title>Forgot Password</title>
</head>

<body>

	<h2>Forgot Password</h2>

	<p>Enter your registered email address. We will send you a
		verification OTP.</p>

	<%
	String error = (String) request.getAttribute("error");
	%>

	<%
	if (error != null) {
	%>

	<p><%=error%></p>

	<%
	}
	%>

	<form action="forgot-password" method="post">

		<label>Email:</label> <input type="email" name="email" required>

		<br>
		<br>

		<button type="submit">Send OTP</button>

	</form>

	<br>

	<a href="login.jsp"> Back to Login </a>

</body>
</html>