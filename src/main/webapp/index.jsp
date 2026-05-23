<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Index</title>
</head>
<body>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    //senza index the tomcat non la sa dove andare questo manda al login
    response.sendRedirect("login.jsp");
%>
</body>
</html>
