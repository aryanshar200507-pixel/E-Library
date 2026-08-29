<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="com.project.elibrary.bean.user.User" %>
    <%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
</head>
<body>

    <h1>Admin Dashboard</h1>
    <h2>Total Users: ${totalUsers}</h2>
   
    <form action="${pageContext.request.contextPath}/admin/dashboard" method="get">

    <input type="text" name="keyword" placeholder="Search user">

    <button type="submit">Search</button>

</form>
    <h2>View Users:</h2>

<%
    List<User> users = (List<User>) request.getAttribute("users");

    if (users != null && !users.isEmpty()) {
%>

<table border="1" cellpadding="10" cellspacing="0">

    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Email</th>
        <th>Role</th>
        <th>Status</th>
        <th>Deactivate</th>
        <th>Recover</th>
    </tr>

<%
        for (User usr : users) {
%>

    <tr>
        <td><%= usr.getUserId() %></td>
        <td><%= usr.getName() %></td>
        <td><%= usr.getEmail() %></td>
        <td><%= usr.getRole() %></td>
        <td><%= usr.getStatus() %></td>
        <td><form action="${pageContext.request.contextPath}/admin/dashboard" method="post">

		    <input type="hidden" name="userId" value="<%= usr.getUserId() %>">
		    <input type="hidden" name="action" value="block">
		
		    <button type="submit">Block</button>
		
		</form></td>
		<td><form action="${pageContext.request.contextPath}/admin/dashboard" method="post">
		<input type="hidden" name="action" value="recover">

    <input type="hidden" name="userId" value="<%= usr.getUserId() %>">

    <button type="submit">Recover</button>

</form></td>

    </tr>

<%
        }
%>

</table>

<%
    } else {
%>

<p>No users found.</p>

<%
    }
%>


</body>
</html>