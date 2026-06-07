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
import java.sql.*;

@WebServlet(name = "poiServlet", value = "/PoiServlet")
public class PoiServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User)session.getAttribute("user");
        if(user==null){
            resp.sendRedirect(req.getContextPath()+"/login.jsp");
            return;
        }
        Long osmID = new Long(req.getParameter("osm_id"));
        String name  = req.getParameter("name");
        double lat  = Double.parseDouble(req.getParameter("lat"));
        double lon = Double.parseDouble(req.getParameter("lon"));
        String type = req.getParameter("type");
        String comment = req.getParameter("comment");
        boolean is_liked = ("true".equals(req.getParameter("is_liked")));
        String q = "insert ignore into poi (osm_id, name, lat, lon, type)" +
                " values(?,?,?,?,?)";
        try(Connection conn = DBConnection.getConnection();
        PreparedStatement ps =  conn.prepareStatement(q);) {
            ps.setLong(1, osmID.longValue());
            ps.setString(2, name);
            ps.setDouble(3, lat);
            ps.setDouble(4, lon);
            ps.setString(5, type);
            ps.executeUpdate();

            int poi_id = -1;
            String q_poiID = "Select poi_id from poi where osm_id = ?";
            try (PreparedStatement ps1 = conn.prepareStatement(q_poiID)) {
                ps1.setLong(1, osmID);
                try(ResultSet rs = ps1.executeQuery()) {
                    if (rs.next()) {
                        poi_id = rs.getInt(1);//column index da quale ccolumn voi prendere i dati
                    }
                }
            }

            if(poi_id==-1){
                    resp.getWriter().write("errore poi non trovato"); //it will be shown on user browser the error message
            }
            String q_userpoi =
                    "INSERT INTO user_poi (user_id, poi_id, is_liked, comment) VALUES (?,?,?,?) " +
                            "ON DUPLICATE KEY UPDATE is_liked = VALUES(is_liked), comment = VALUES(comment)";
            try (PreparedStatement ps1 = conn.prepareStatement(q_userpoi)) {
                ps1.setInt(1, user.getUser_id());
                ps1.setInt(2, poi_id);
                ps1.setBoolean(3, is_liked);
                ps1.setString(4, comment);
                ps1.executeUpdate();
            }
            resp.getWriter().write("salvato");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
