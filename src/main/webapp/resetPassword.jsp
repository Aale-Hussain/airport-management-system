<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nuova Password — AeroPorto</title>
    <link rel="stylesheet" href="css/Style.css">
</head>
<body>

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
                // Read email and token from URL (e.g. ?email=x&token=y)
                // WHY: the email link from TokenGenerateServlet puts these in the URL
                // so we pre-fill the fields to make it easier for the user
                String email = request.getParameter("email");
                String token = request.getParameter("token");
                // Safety: if null, set to empty string so input is not broken
                if (email == null) email = "";
                if (token == null) token = "";
            %>

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email"
                       value="<%= email %>" required>
            </div>

            <div class="form-group">
                <label for="token">Token</label>
                <!-- type="text" not "password" so user can see/paste the token easily -->
                <input type="text" id="token" name="token"
                       value="<%= token %>" placeholder="xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx" required>
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
                if (message != null) {
            %>
            <div class="msg msg-error"><%= message %></div>
            <% } %>
        </form>

        <div class="card-footer">
            <a href="login.jsp">← Torna al login</a>
        </div>
    </div>
</div>

</body>
</html>
