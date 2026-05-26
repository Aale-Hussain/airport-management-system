<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password — AeroPorto</title>
    <link rel="stylesheet" href="css/Style.css">
</head>
<body>

<div class="page-wrapper">
    <div class="airport-tag">✈ sistema aeroportuale</div>

    <div class="card">
        <div class="card-header">
            <span class="icon">🔑</span>
            <h1>Reset Password</h1>
            <p class="subtitle">Invio token via email</p>
        </div>

        <!-- POST goes to TokenGenerateServlet at /tokenGenerateServlet -->
        <form action="tokenGenerateServlet" method="post">

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email"
                       placeholder="gino@example.it" required>
            </div>

            <!-- Info message — tells user what will happen -->
            <div class="msg msg-info">
                Riceverai un link via email valido per 5 minuti.
            </div>

            <button type="submit" class="btn btn-primary">Invia Token →</button>

            <%-- Show message from servlet (e.g. "Email non trovato") --%>
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
