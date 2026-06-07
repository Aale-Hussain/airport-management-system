<%@ page import="com.example.hussain_progect_tp.classes.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Profilo — AeroPorto</title>
    <link rel="stylesheet" href="../css/Style.css">
</head>
<body class="page-body">
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
    String initial = (user.getUsername() != null && !user.getUsername().isEmpty())
            ? user.getUsername().substring(0,1).toUpperCase() : "?";
    boolean isGoogleUser = user.getGoogle_id() != null && !user.getGoogle_id().isEmpty();
%>

<div class="page-container" style="max-width:520px;">

    <a href="map.jsp" class="back-link">← MAPPA</a>

    <%-- Header --%>
    <div class="page-header">
        <div class="page-tag">✈ aeroporto sistema</div>
        <div class="page-title">Profilo</div>
        <div class="page-subtitle">Gestisci i tuoi dati — <%= user.getUsername().toUpperCase() %></div>
    </div>

    <%-- Profile card --%>
    <div class="profile-card">

        <%-- Avatar --%>
        <div class="profile-avatar"><%= initial %></div>

        <%-- Badges --%>
        <div style="text-align:center; margin-bottom:28px;">
            <span class="profile-email-badge"><%= user.getEmail() %></span>
            <% if (isGoogleUser) { %>
            <span class="google-badge">✓ Google</span>
            <% } %>
        </div>

        <%-- Messages --%>
        <%
            String msg = (String) session.getAttribute("message");
            if (msg != null) {
        %>
        <div class="msg msg-success" style="margin-bottom:20px;"><%= msg %></div>
        <% session.removeAttribute("message"); } %>

        <%
            String err = (String) session.getAttribute("error");
            if (err != null) {
        %>
        <div class="msg msg-error" style="margin-bottom:20px;"><%= err %></div>
        <% session.removeAttribute("error"); } %>

        <%-- Form --%>
        <form action="../ProfileServlet" method="post">

            <div class="form-group">
                <label>Username</label>
                <input type="text" name="username"
                       value="<%= user.getUsername() != null ? user.getUsername() : "" %>" required>
            </div>

            <div class="form-group">
                <label>Nome</label>
                <input type="text" name="nome"
                       value="<%= user.getNome() != null ? user.getNome() : "" %>">
            </div>

            <div class="form-group">
                <label>Cognome</label>
                <input type="text" name="cognome"
                       value="<%= user.getCognome() != null ? user.getCognome() : "" %>">
            </div>

            <div class="form-group">
                <label>Email <span style="color:var(--text-muted); font-size:9px; letter-spacing:1px;">— non modificabile</span></label>
                <div class="field-readonly"><%= user.getEmail() %></div>
            </div>

            <% if (isGoogleUser) { %>
            <div class="form-group">
                <label>Password <span style="color:var(--text-muted); font-size:9px;">— account Google</span></label>
                <div class="field-readonly">••••••••••••</div>
            </div>
            <% } %>

            <button type="submit" class="btn btn-primary" style="margin-top:8px;">
                Salva Modifiche →
            </button>

        </form>

    </div>

    <%-- Nav --%>
    <div class="nav-links">
        <a href="perferiti.jsp">♥ Preferiti</a> ·
        <a href="prenotazione.jsp">✈ Prenotazioni</a> ·
        <a href="../logoutServlet">Logout</a>
    </div>

</div>
</body>
</html>
