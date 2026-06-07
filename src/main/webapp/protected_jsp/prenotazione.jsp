<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.hussain_progect_tp.classes.User" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.example.hussain_progect_tp.servizi.DBConnection" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Prenotazioni — AeroPorto</title>
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
        <div class="page-title">
            Prenotazioni
            <%-- Count badge — we'll show it inline --%>
        </div>
        <div class="page-subtitle">LE TUE ROTTE — <%= user.getUsername().toUpperCase() %></div>
    </div>

    <%-- Success message --%>
    <%
        String cancelMsg = request.getParameter("cancella");
        if ("ok".equals(cancelMsg)) {
    %>
    <div class="msg msg-success prenotazioni-msg">
        ✓ Prenotazione cancellata con successo.
    </div>
    <% } %>

    <%-- Bookings list --%>
    <%
        try (Connection conn = DBConnection.getConnection()) {
            String q =
                    "SELECT p.prenotazione_id, " +
                            "       par.name AS partenza, " +
                            "       arr.name AS arrivo, " +
                            "       p.data_prenotazione " +
                            "FROM prenotazione p " +
                            "JOIN poi par ON p.partenza_poi_id = par.poi_id " +
                            "JOIN poi arr ON p.arrival_poi_id  = arr.poi_id " +
                            "WHERE p.user_id = ? " +
                            "ORDER BY p.data_prenotazione DESC";
            try (PreparedStatement ps = conn.prepareStatement(q)) {
                ps.setInt(1, user.getUser_id());
                try (ResultSet rs = ps.executeQuery()) {
                    boolean hasBookings = false;
                    while (rs.next()) {
                        hasBookings = true;
                        int    bookingId = rs.getInt("prenotazione_id");
                        String partenza  = rs.getString("partenza");
                        String arrivo    = rs.getString("arrivo");
                        String date      = rs.getString("data_prenotazione");
    %>
    <div class="booking-card">
        <div class="booking-info">
            <div class="booking-route">
                <%= partenza != null ? partenza : "—" %>
                <span class="arrow">→</span>
                <%= arrivo != null ? arrivo : "—" %>
            </div>
            <div class="booking-date">📅 <%= date %></div>
            <div class="booking-id">BOOKING #<%= bookingId %></div>
        </div>
        <form action="../CancellaPrenotazioneServlet" method="post"
              onsubmit="return confirm('Cancellare questa prenotazione?');">
            <input type="hidden" name="prenotazione_id" value="<%= bookingId %>">
            <button type="submit" class="btn-cancel">Cancella</button>
        </form>
    </div>
    <%
        }
        if (!hasBookings) {
    %>
    <div class="empty-msg">Nessuna prenotazione trovata.</div>
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
        <a href="perferiti.jsp">♥ Preferiti</a> ·
        <a href="../logoutServlet">Logout</a>
    </div>

</div>
</body>
</html>
