<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login — AeroPorto</title>
    <!-- Link to our shared CSS file -->
    <link rel="stylesheet" href="css/Style.css">
</head>
<body>

<div class="page-wrapper">
    <!-- Small label above card — like a departure board header -->
    <div class="airport-tag">✈ sistema aeroportuale</div>

    <div class="card">
        <div class="card-header">
            <span class="icon">🛫</span>
            <h1>Accesso</h1>
            <p class="subtitle">Inserire le credenziali</p>
        </div>


        <!-- Login form — POST goes to LoginServlet at /login -->
        <form action="login" method="post">

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email"
                       placeholder="gino@example.it" required>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>

            <button type="submit" class="btn btn-primary">Accedi →</button>

            <%-- Show error message if LoginServlet redirected with ?error=1 --%>
            <% if (request.getParameter("error") != null) { %>
            <div class="msg msg-error">Email o password non corretta.</div>
            <% } %>

            <%-- Show message set via request.setAttribute("message", ...) --%>
            <%
                String message = (String) request.getAttribute("message");
                if (message != null) {
            %>
            <div class="msg msg-success"><%= message %></div>
            <% } %>
        </form>

        <div class="divider">oppure</div>

        <!-- Register button — goes to signin.jsp -->
        <form action="signin.jsp" method="get">
            <button type="submit" class="btn btn-secondary">Registrati</button>
        </form>

        <!-- Password reset link -->
        <div class="card-footer" style="margin-top:16px;">
            <a href="tokenGenerate.jsp">Password dimenticata?</a>
        </div>
    </div>
</div>

</body>
</html>
