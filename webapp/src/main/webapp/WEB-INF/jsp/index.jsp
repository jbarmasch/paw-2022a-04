<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
<%--    <%@ include file="appbar.jsp"%>--%>
    <jsp:include page="appbar.jsp">
        <jsp:param name="userId" value="${userId}"/>
    </jsp:include>
    <div class="container only-element">
        <h1>Bienvenido a BotPass.</h1>
    </div>
</body>
</html>
