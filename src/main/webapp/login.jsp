<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="login" method="post">

    <input type="email"
           name="email"
           placeholder="Email"
           required>

    <input type="password"
           name="password"
           placeholder="Password"
           required>

    <button type="submit">
        Login
    </button>
    
    <a href="forgotPassword.jsp">FORGOT PASSWORD ?</a>

</form>
</body>
</html>