<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Index</title>
</head>
<body>
<%
    HttpSession currentSession = request.getSession(false);
    if (currentSession != null && currentSession.getAttribute("user") != null) {
        response.sendRedirect("map.jsp");
    } else {
        response.sendRedirect("login.jsp");
    }
%>
</body>
</html>
