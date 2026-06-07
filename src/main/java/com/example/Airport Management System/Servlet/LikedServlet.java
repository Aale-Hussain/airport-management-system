package com.example.hussain_progect_tp.Servlet;

import com.example.hussain_progect_tp.classes.User;
import com.example.hussain_progect_tp.servizi.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "likedServlet",  value = "/LikedServlet")
public class LikedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.getWriter().write("{\"is_liked\":false}");
            return;
        }
        Long osm_id = Long.parseLong(req.getParameter("osm_id"));
        String q = "Select up.is_liked from user_poi up " +
                "join poi p on up.poi_id = p.poi_id " +
                "where p.osm_id = ? and up.user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(q)) {
            ps.setLong(1, osm_id);
            ps.setLong(2, user.getUser_id());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean liked = rs.getBoolean("is_liked");
                    resp.getWriter().write("{\"is_liked\":" + liked + "}");
                } else {
                    resp.getWriter().write("{\"is_liked\":false}");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.getWriter().write("{\"is_liked\":false}");
        }
    }
}
