<%@ page import="com.example.hussain_progect_tp.classes.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>mappa</title>
    <!-- this is the css file to show the open layer -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/ol@v9.1.0/ol.css">
    <link rel="stylesheet" href="../css/map.css">
</head>
<body>
    <%
        User user = (User) session.getAttribute("user");
    %>
    <div id="map"></div>
    <div id="search-bar">
        <input type="text" id="city"    placeholder="Citta" />
        <button id="search-btn">Cerca</button>
    </div>
    <div id="result-count"></div>
    <div id="poi-overlay"></div>
    <div id="poi-panel" style="position:fixed; top:50%; left:50%; transform:translate(-50%,-50%); z-index:201;">
        <button id="poi-close">✕</button>
        <h3 id="poi-name"></h3>
        <p id="poi-address"></p>

        <div style="margin-top:12px;">
            <label class="poi-label">Commento</label>
            <input type="text" id="poi-comment" placeholder="Scrivi un commento..." />
        </div>

        <div style="margin-top:14px; display:flex; align-items:center; gap:12px;">
            <label class="poi-label">Like</label>
            <button id="poi-heart-btn" title="Like">♡</button>
            <input type="checkbox" id="poi-liked" style="display:none;" />
        </div>

        <div style="margin-top:12px; display:flex; gap:8px;">
            <button id="poi-save-btn" style="flex:1;">♥ Salva</button>
            <button id="poi-partenza-btn" style="flex:1;">✈ Partenza</button>
            <button id="poi-arrivo-btn" style="flex:1;">🛬 Arrivo</button>
        </div>
        <p id="poi-save-msg" style="display:none;"></p>
    </div>

    <%-- booking bar — fixed below search bar --%>
    <div id="booking-bar">
        <div class="booking-slot" id="slot-partenza">
            <span class="booking-label">✈ PARTENZA</span>
            <span class="booking-value" id="val-partenza">—</span>
        </div>
        <div class="booking-slot" id="slot-arrivo">
            <span class="booking-label">🛬 ARRIVO</span>
            <span class="booking-value" id="val-arrivo">—</span>
        </div>
        <div class="booking-slot">
            <span class="booking-label">📅 DATA</span>
            <input type="date" id="booking-date" />
        </div>
        <button id="booking-confirm-btn" disabled>Prenota</button>
        <button id="booking-reset-btn">↺ Reset</button>
    </div>


    <%-- booking result message --%>
    <div id="booking-msg" style="display:none;"></div>
    <div class="profile-menu">
        <button class="profile-btn">
            <%= user.getUsername() != null ? user.getUsername().substring(0,1).toUpperCase() : "?" %>
        </button>
        <div class="profile-dropdown">
            <div class="profile-name">
                <%= user.getUsername() != null ? user.getUsername() : user.getEmail() %>
            </div>
            <a href="profile.jsp">Profilo</a>
            <a href="perferiti.jsp">Preferiti</a>
            <a href="prenotazione.jsp">Prenotazioni</a>
            <a href="../logoutServlet">Logout</a>
        </div>
    </div>

    <!-- so CDN has the openlayer code that jsDelivr transport
    when user login and npm is the library that store the
     whole js open source code  ol.js is file name of openlayer
     code that jsdelivr fetch and it is in folder dist -->
    <script src="https://cdn.jsdelivr.net/npm/ol@v9.1.0/dist/ol.js"></script>
    <script src="JS/map_js.js"></script>
</body>
</html>