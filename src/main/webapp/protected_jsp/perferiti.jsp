<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.hussain_progect_tp.classes.User" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.example.hussain_progect_tp.servizi.DBConnection" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Preferiti — AeroPorto</title>
    <link rel="stylesheet" href="../css/Style.css">
</head>
<body class="page-body">
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("../login.jsp");
        return;
    }
%>

<div class="page-container">

    <a href="map.jsp" class="back-link">← MAPPA</a>

    <%-- Header --%>
    <div class="page-header">
        <div class="page-tag">✈ aeroporto sistema</div>
        <div class="page-title">Preferiti</div>
        <div class="page-subtitle">I TUOI AEROPORTI SALVATI — <%= user.getUsername().toUpperCase() %></div>
    </div>

    <%-- Success message --%>
    <%
        String removedMsg = request.getParameter("cancella");
        if ("ok".equals(removedMsg)) {
    %>
    <div class="msg msg-success prenotazioni-msg">
        ✓ Preferito rimosso con successo.
    </div>
    <% } %>

    <%-- Favourites list --%>
    <%
        try (Connection conn = DBConnection.getConnection()) {
            String q =
                    "SELECT p.osm_id, p.name, p.type, " +
                            "       up.is_liked, up.comment, up.poi_id " +
                            "FROM user_poi up " +
                            "JOIN poi p ON up.poi_id = p.poi_id " +
                            "WHERE up.user_id = ? " +
                            "ORDER BY up.created_at DESC";
            try (PreparedStatement ps = conn.prepareStatement(q)) {
                ps.setInt(1, user.getUser_id());
                try (ResultSet rs = ps.executeQuery()) {
                    boolean hasAny = false;
                    while (rs.next()) {
                        hasAny = true;
                        int     poiId   = rs.getInt("poi_id");
                        String  name    = rs.getString("name");
                        String  type    = rs.getString("type");
                        boolean liked   = rs.getBoolean("is_liked");
                        String  comment = rs.getString("comment");
    %>
    <div class="booking-card">
        <div class="booking-info">
            <div class="booking-route">
                <span class="liked-badge"><%= liked ? "♥" : "♡" %></span>
                <%= name != null ? name : "—" %>
            </div>
            <div class="booking-type"><%= type != null ? type.toUpperCase() : "" %></div>
            <% if (comment != null && !comment.isEmpty()) { %>
            <div class="booking-comment">💬 <%= comment %></div>
            <% } %>
        </div>
        <form action="../CancellaPerferitiServlet" method="post"
              onsubmit="return confirm('Rimuovere dai preferiti?');">
            <input type="hidden" name="poi_id" value="<%= poiId %>">
            <button type="submit" class="btn-cancel">Rimuovi</button>
        </form>
    </div>
    <%
        }
        if (!hasAny) {
    %>
    <div class="empty-msg">Nessun preferito salvato.</div>
    <%
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    %>

    <div class="nav-links">
        <a href="profile.jsp">👤 Profilo</a> ·
        <a href="prenotazione.jsp">✈ Prenotazioni</a> ·
        <a href="../logoutServlet">Logout</a>
    </div>

</div>
</body>
</html>
