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

@WebServlet(name = "profileServlet",value ="/ProfileServlet")
public class ProfileServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute("user");
        if(user==null){
            response.sendRedirect("login.jsp");
            return;
        }
        String username = request.getParameter("username");
        String name = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        if(username == null || username.isEmpty()){
            session.setAttribute(
                    "error",
                    "Perfavore compilate tutti i campi. Nome e cognome non sono obbligatori"
            );
            response.sendRedirect(request.getContextPath() + "/protected_jsp/profile.jsp");
            return;
        }
        String q = "UPDATE users SET username=?, nome=?, cognome=? WHERE user_id=?";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(q)){
            ps.setString(1, username);
            ps.setString(2, (name == null || name.isEmpty()) ? "" :name);
            ps.setString(3, (cognome == null || cognome.isEmpty()) ? "" :cognome);
            ps.setInt(4,user.getUser_id());
            ps.executeUpdate();
            user.setUsername(username);
            user.setNome(name);
            user.setCognome(cognome);
            session.setAttribute("user",user);
            response.sendRedirect(request.getContextPath() + "/protected_jsp/map.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 1062) { //Dati duplicati error code
                session.setAttribute("error", "username gia in uso");
            } else {
                session.setAttribute("error", "errore: "+ e.getMessage());
            }

            response.sendRedirect(request.getContextPath()+"/protected_jsp/profile.jsp");
        }
    }

}
