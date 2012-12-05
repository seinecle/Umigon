<%-- 
    Document   : index
    Created on : Dec 3, 2012, 7:47:28 AM
    Author     : C. Levallois
--%>

<%@page import="Twitter.TweetLoader"%>
<%@page import="Twitter.Controller"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <%
            String[] args = new String[0];
//            Controller.main(args);
            TweetLoader.main(args);
        %>

    </body>
</html>
