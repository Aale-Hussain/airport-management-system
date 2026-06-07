package com.example.hussain_progect_tp.SessionFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/protected_jsp/*")
//before any file in protected jsp get executed this servlet
// get exxecuted so users cant go directly from url to any
// of file in protected they are forced to went through login
// so session get created every request and response pass through this
//stop an HTTP request or response before it reaches a servlet/JSP
public class SessionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest  request = (HttpServletRequest) servletRequest;
        HttpServletResponse   response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("user") != null);
        if (loggedIn) {
            filterChain.doFilter(request,response);
        }
        else{
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }
}
