<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login — AeroPorto</title>
    <!-- Link to our shared CSS file -->
    <link rel="stylesheet" href="css/Style.css">
    <!-- Load Google's Identity Services library -->
    <script src="https://accounts.google.com/gsi/client" async defer></script>
</head>
<body style="display:flex; align-items:center; justify-content:center;">

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
            <!-- ✅ GOOGLE SIGN-IN BUTTON -->
            <div class="google-wrapper">
                <div style="width:300px; margin:0 auto;">
                    <div id="g_id_onload"
                         data-client_id="YOUR_GOOGLE_CLIENT_ID"
                         data-callback="handleGoogleResponse">
                    </div>
                    <div class="g_id_signin"
                         data-type="standard"
                         data-size="large"
                         data-theme="outline"
                         data-text="sign_in_with"
                         data-shape="rectangular"
                         data-width="300">
                    </div>
                </div>
            </div>
            <br>
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
    <script src="google_auth.js"></script>

</body>
</html>
