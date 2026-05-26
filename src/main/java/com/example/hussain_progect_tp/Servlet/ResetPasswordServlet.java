package com.example.hussain_progect_tp.Servlet;

import com.example.hussain_progect_tp.servizi.DBConnection;
import com.example.hussain_progect_tp.servizi.PasswordHash;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "resetPassword", value = "/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        if(!password.equals(confirmPassword)){
            request.setAttribute("message", "Passwords do not match");
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
            return;
        }
        try(Connection con = DBConnection.getConnection()){

            String q = "SELECT * FROM token WHERE email = ? AND token = ? " +
                    "AND created_at > NOW() - INTERVAL 5 MINUTE AND used = false";
            try(PreparedStatement ps = con.prepareStatement(q)) {
                ps.setString(1, email);
                ps.setString(2, token);
                try(ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        request.setAttribute("message", "Token non valido");
                        request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
                        return;
                    }
                }
            }


            String insert_password = "UPDATE users SET password_hash = ? WHERE email = ?";
            String pass = PasswordHash.hash(password);
            try(PreparedStatement ps2 = con.prepareStatement(insert_password)) {
                ps2.setString(1, pass);
                ps2.setString(2, email);
                ps2.executeUpdate();
            }
            String delete_token = "UPDATE token SET used = true WHERE email = ? and token = ?";
            try(PreparedStatement ps3 = con.prepareStatement(delete_token)){
            ps3.setString(1,email);
            ps3.setString(2,token);
            ps3.executeUpdate();
            }
            request.setAttribute("message", "password reset");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
