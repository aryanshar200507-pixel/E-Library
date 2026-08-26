<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>E-Library Registration</title>
</head>

<body>

	<h2>Create Your E-Library Account</h2>

	<%
	String error = (String) request.getAttribute("error");
	String success = (String) request.getAttribute("success");
	%>

	<%
	if (error != null) {
	%>
	<p><%=error%></p>
	<%
	}
	%>

	<%
	if (success != null) {
	%>
	<p><%=success%></p>
	<%
	}
	%>

	<form action="register" method="post">

		<label>Name:</label> <input type="text" name="name" required>

		<br> <br> <label>Email:</label> <input type="email"
			name="email" required> <br> <br> <label>Password:</label>
		<input type="password" name="password" required> <br> <br>

		<button type="submit">Register</button>

	</form>

	<br>

	<a href="login.jsp"> Already have an account? Login </a>

</body>
</html>