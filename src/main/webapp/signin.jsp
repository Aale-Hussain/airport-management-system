<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrazione — AeroPorto</title>
    <link rel="stylesheet" href="css/Style.css">
</head>
<body>

<div class="page-wrapper">
    <div class="airport-tag">✈ sistema aeroportuale</div>

    <div class="card">
        <div class="card-header">
            <span class="icon">📋</span>
            <h1>Registrazione</h1>
            <p class="subtitle">Nuovo passeggero</p>
        </div>

        <!-- Registration form — POST goes to SigninServlet at /signin -->
        <form action="signin" method="post">

            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username"
                       placeholder="es. gino99" required>
            </div>

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email"
                       placeholder="gino@example.it" required>
            </div>

            <div class="form-group">
                <label for="nome">Nome</label>
                <input type="text" id="nome" name="nome" placeholder="Gino">
            </div>

            <div class="form-group">
                <label for="cognome">Cognome</label>
                <input type="text" id="cognome" name="cognome" placeholder="Rossi">
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn btn-primary">Registrati →</button>

            <%-- Error: email already in use --%>
            <%
                if (request.getParameter("error") != null) {
                    String badEmail = request.getParameter("email");
            %>
            <div class="msg msg-error">
                Email non disponibile: <%= badEmail %>
            </div>
            <% } %>
        </form>

        <div class="card-footer">
            Hai già un account? <a href="login.jsp">Accedi</a>
        </div>
    </div>
</div>

</body>
</html>
