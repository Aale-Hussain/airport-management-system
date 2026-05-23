<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
    <form action="login" method="post">
        Email:
        <input type="email" name="email" required>
        <br>
        Password:
        <input type="password" name="password"  required>
        <br>
        <button type="submit">Login</button>


        <% if (request.getParameter("error") != null) { %>
             <p>ce qualche problema con email o password</p>
        <% } %>
    </form>
    <form action="register.jsp" method="post">
        <button type="submit">Register</button>
    </form>
</body>
</html>