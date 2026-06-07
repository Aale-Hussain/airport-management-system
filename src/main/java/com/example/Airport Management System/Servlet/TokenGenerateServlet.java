package com.example.hussain_progect_tp.Servlet;

import com.example.hussain_progect_tp.controlli.CheckEmail;
import com.example.hussain_progect_tp.controlli.SendEmail;
import com.example.hussain_progect_tp.servizi.DBConnection;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.UUID;



@WebServlet(name = "tokenGenerate", value = "/tokenGenerateServlet")
public class TokenGenerateServlet extends HttpServlet {
    private Connection conn;
    @Override
    public void init()  {
        try{
             conn = DBConnection.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        try{
            if(!CheckEmail.checkEmail(email)){
                request.setAttribute("message", "Email non trovato");
                request.getRequestDispatcher("tokenGenerate.jsp").forward(request, response);
                return;

            }

            String token = UUID.randomUUID().toString();
            String insertToken = "insert into token (token,email) values (?,?)";
            PreparedStatement ps = conn.prepareStatement(insertToken);
            ps.setString(1, token);
            ps.setString(2, email);
            ps.executeUpdate();
            String baseUrl = request.getScheme() + "://" +
                    request.getServerName() +
                    ":" + request.getServerPort() +
                    request.getContextPath();
            String linkToken = baseUrl + "/resetPassword.jsp?email="+email+"&token="+ token;
            new SendEmail().sendEmail(email,"Reset Password",linkToken);
            response.sendRedirect("resetPassword.jsp?email="+ email+"&token="+token);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
