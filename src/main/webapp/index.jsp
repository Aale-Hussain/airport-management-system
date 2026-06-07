<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Index</title>
</head>
<body>
    <%
        //controlla il session se esiste o no e non crea session perche ce false request.getSession(false)
        //session built in object given by jsp and can be used in different jsp file to
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("map.jsp");
        } else {
            response.sendRedirect("login.jsp");
        }
    %>
</body>
</html>
