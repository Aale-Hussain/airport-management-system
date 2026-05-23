<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register</title>
</head>
<body>
    <form action="register" method="post">
        Username:
        <input type="text" name="username" required>
        <br>
        Email:
        <input type="email" name="email" required>
        <br>
        Nome:
        <input type="text" name="nome">
        <br>
        Cognome:
        <input type="text" name="cognome">
        <br>
        Password:
        <input type="password" name="password" required>
        <br>
        <button type="submit">Register</button>
        <% if (request.getParameter("error") != null) {
            String email = request.getParameter("email");
        %>
        <p>Impossibile utilizzare questo indirizzo email: <%= email%></p>
        <% } %>
    </form>

</body>
</html>
