<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrazione — AeroPorto</title>
    <link rel="stylesheet" href="css/Style.css">
</head>
<body style="display:flex; align-items:center; justify-content:center;">

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
                           value="<%= session.getAttribute("temp_username") != null ? session.getAttribute("temp_username") : "" %>"
                           placeholder="es. gino99" required>
                </div>

                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email"
                           value="<%= session.getAttribute("temp_email") != null ? session.getAttribute("temp_email") : "" %>"
                           placeholder="gino@example.it" required>
                </div>

                <div class="form-group">
                    <label for="nome">Nome</label>
                    <input type="text" id="nome" name="nome"
                           value="<%= session.getAttribute("temp_nome") != null ? session.getAttribute("temp_nome") : "" %>"
                            placeholder="Gino">
                </div>

                <div class="form-group">
                    <label for="cognome">Cognome</label>
                    <input type="text" id="cognome" name="cognome"
                           value="<%= session.getAttribute("temp_cognome") != null ? session.getAttribute("temp_cognome") : "" %>"
                            placeholder="Rossi">
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <button type="submit" class="btn btn-primary">Registrati →</button>

                <%-- Error: email already in use --%>
                <%
                    String error = request.getParameter("error");

                    if ("1".equals(error)) {
                %>
                <div class="msg msg-error">
                    Email non disponibile: <%= session.getAttribute("temp_email") %>
                </div>
                <%
                } else if ("2".equals(error)) {
                %>
                <div class="msg msg-error">
                    Password troppo corta (minimo 6 caratteri)
                </div>
                <%
                    }
                    session.removeAttribute("temp_username");
                    session.removeAttribute("temp_email");
                    session.removeAttribute("temp_nome");
                    session.removeAttribute("temp_cognome");
                %>
            </form>

            <div class="card-footer">
                Hai già un account? <a href="login.jsp">Accedi</a>
            </div>
        </div>
    </div>

</body>
</html>
