package com.example.hussain_progect_tp.Servlet;

import com.example.hussain_progect_tp.logica.RegisterLogica;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "registerServlet", value = "/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String username = request.getParameter("username");
        RegisterLogica registra = new RegisterLogica();
        boolean successo = registra.register(username,email,nome,cognome,password);
        if(successo){
            response.sendRedirect("login.jsp");
        }else{
            response.sendRedirect("register.jsp?error=1&email="+email);
        }
    }

}
