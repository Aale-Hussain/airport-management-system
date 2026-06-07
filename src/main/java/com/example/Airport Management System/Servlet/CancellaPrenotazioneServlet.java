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
import java.sql.SQLException;

@WebServlet(name = "prenotazioneupdate", value = "/CancellaPrenotazioneServlet")
public class CancellaPrenotazioneServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");
        if(user==null){
            response.sendRedirect("login.jsp");
            return;
        }
        int prenotazione_id = Integer.parseInt(request.getParameter("prenotazione_id"));
        String q = "DELETE FROM prenotazione WHERE prenotazione_id =? and user_id = ?";
        try(Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(q)){
            ps.setInt(1, prenotazione_id);
            ps.setInt(2, user.getUser_id());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        response.sendRedirect("protected_jsp/prenotazione.jsp?cancella=ok");
    }
}
