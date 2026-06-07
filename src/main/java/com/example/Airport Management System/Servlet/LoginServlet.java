package com.example.hussain_progect_tp.Servlet;

import com.example.hussain_progect_tp.classes.User;
import com.example.hussain_progect_tp.logica.LoginLogica;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet(name = "Login", value = "/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (password == null) {
            response.sendRedirect("login.jsp?error=google");
            return;
        }
        LoginLogica loginLogica = new LoginLogica();
        User user = loginLogica.login(email, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect("protected_jsp/map.jsp");
        } else {
            response.sendRedirect("login.jsp?error=1");
        }
    }
}
