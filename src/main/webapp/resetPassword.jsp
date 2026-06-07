<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nuova Password — AeroPorto</title>
    <link rel="stylesheet" href="css/Style.css">
</head>
<body style="display:flex; align-items:center; justify-content:center;">

    <div class="page-wrapper">
        <div class="airport-tag">✈ sistema aeroportuale</div>

        <div class="card">
            <div class="card-header">
                <span class="icon">🔒</span>
                <h1>Nuova Password</h1>
                <p class="subtitle">Inserire il token ricevuto</p>
            </div>

            <!-- POST goes to ResetPasswordServlet at /ResetPasswordServlet -->
            <!-- email and token come pre-filled from the URL parameters -->
            <form action="ResetPasswordServlet" method="post">

                <%
                    String email = (String) request.getAttribute("email");
                    String token = (String) request.getAttribute("token");
                    if (email == null) email = request.getParameter("email");
                    if (token == null) token = request.getParameter("token");
                    if (email == null) email = "";
                    if (token == null) token = "";
                %>

                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email"
                           value="<%= email %>" readonly>
                </div>

                <div class="form-group">
                    <label for="token">Token</label>
                    <!-- type="text" not "password" so user can see/paste the token easily -->
                    <input type="text" id="token" name="token"
                           value="<%= token %>"  readonly>
                </div>

                <div class="form-group">
                    <label for="password">Nuova Password</label>
                    <input type="password" id="password" name="password" required>
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Conferma Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" required>
                </div>

                <button type="submit" class="btn btn-primary">Reimposta Password →</button>

                <%-- Message from servlet (success or error) --%>
                <%
                    String message = (String) request.getAttribute("message");
                    String error = (String) request.getAttribute("error");

                    if (message != null) {
                %>
                <div class="msg msg-error"><%= message %></div>
                <%
                } else if (error != null) {
                %>
                <div class="msg msg-error"><%= error %></div>
                <%
                    }
                %>

            </form>

            <div class="card-footer">
                <a href="login.jsp">← Torna al login</a>
            </div>
        </div>
    </div>

</body>
</html>
