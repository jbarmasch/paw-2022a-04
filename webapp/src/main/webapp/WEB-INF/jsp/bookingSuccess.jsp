<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
    <%@ include file="appbar.jsp"%>
    <div class="container only-element">
        <h1>Su reserva se ha realizado con éxito.</h1>
    </div>

    <c:set var="similarEvents" value="${similarEvents}" scope="request"/>
    <c:set var="similarEventsSize" value="${similarEventsSize}" scope="request"/>
    <c:import url="similarEvents.jsp"/>

    <c:set var="popularEvents" value="${popularEvents}" scope="request"/>
    <c:set var="popularEventsSize" value="${popularEventsSize}" scope="request"/>
    <c:import url="popularEvents.jsp"/>
</body>
</html>
