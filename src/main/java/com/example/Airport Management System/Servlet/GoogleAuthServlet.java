package com.example.hussain_progect_tp.Servlet;

import com.example.hussain_progect_tp.classes.User;
import com.example.hussain_progect_tp.servizi.DBConnection;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.Collections;

@WebServlet(name = "GoogleAuth", value = "/GoogleAuthServlet")
public class GoogleAuthServlet extends HttpServlet {
    private static final String CLIENT_ID = "YOUR_GOOGLE_CLIENT_ID";
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JsonObject body  = JsonParser.parseString(sb.toString()).getAsJsonObject();
        String     token = body.get("token").getAsString();
        GoogleIdToken idToken;
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();
            idToken = verifier.verify(token);
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"error\":\"Errore verifica token\"}");
            return;
        }
        if (idToken == null) {
            out.print("{\"success\":false,\"error\":\"Token non valido\"}");
            return;
        }
        GoogleIdToken.Payload payload = idToken.getPayload();
        String googleId = payload.getSubject();
        String email    = payload.getEmail();
        String fullName = (String) payload.get("name");
        String nome     = "";
        String cognome  = "";
        if (fullName != null && fullName.contains(" ")) {
            String[] parts = fullName.split(" ", 2);
            nome    = parts[0];
            cognome = parts[1];
        } else if (fullName != null) {
            nome = fullName;
        }
        try (Connection conn = DBConnection.getConnection()) {
            int    userId       = -1;
            String sessionEmail = email;
            String sessionNome  = nome;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT user_id, email, nome FROM users WHERE google_id = ?")) {
                ps.setString(1, googleId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    userId       = rs.getInt("user_id");
                    sessionEmail = rs.getString("email");
                    sessionNome  = rs.getString("nome");
                }
            }
            if (userId == -1) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT user_id, email, nome FROM users WHERE email = ?")) {
                    ps.setString(1, email);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        userId       = rs.getInt("user_id");
                        sessionEmail = rs.getString("email");
                        sessionNome  = rs.getString("nome");
                        try (PreparedStatement link = conn.prepareStatement(
                                "UPDATE users SET google_id = ? WHERE user_id = ?")) {
                            link.setString(1, googleId);
                            link.setInt(2, userId);
                            link.executeUpdate();
                        }
                    }
                }
            }
            if (userId == -1) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO users (username, email, password_hash, nome, cognome, google_id) " +
                                "VALUES (?, ?, NULL, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, email.split("@")[0]);
                    ps.setString(2, email);
                    ps.setString(3, nome);
                    ps.setString(4, cognome);
                    ps.setString(5, googleId);
                    ps.executeUpdate();
                    ResultSet keys = ps.getGeneratedKeys();
                    if (keys.next()) userId = keys.getInt(1);
                }
            }
            if (userId == -1) {
                out.print("{\"success\":false,\"error\":\"Errore database\"}");
                return;
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT user_id, username, email, nome, cognome, google_id " +
                            "FROM users WHERE user_id = ?")) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("google_id")
                    );
                    HttpSession session = request.getSession(true);
                    session.setAttribute("user", user);
                    out.print("{\"success\":true,\"redirect\":\"protected_jsp/map.jsp\"}");
                } else {
                    out.print("{\"success\":false,\"error\":\"Utente non trovato\"}");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"error\":\"Errore database\"}");
        }
    }
}