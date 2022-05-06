<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
    <c:import url="appbar.jsp"/>
    <div class="container only-element">
        <h1 class="title">¡Bienvenido a BotPass!</h1>
    </div>

    <c:choose>
        <c:when test="${upcomingSize > 0}">
            <div>
                <h2 class="subtitle">Próximos eventos</h2>
            </div>
            <div class="container multi-browse">
                <c:forEach var="event" items="${upcomingEvents}">
                    <c:set var="event" value="${event}" scope="request"/>
                    <c:import url="eventCard.jsp"/>
                </c:forEach>
            </div>
        </c:when>
    </c:choose>
    <c:choose>
        <c:when test="${fewTicketsSize > 0}">
            <div>
                <h2 class="subtitle">No te pierdas estos eventos ¡Se están quedando sin entradas!</h2>
            </div>
            <div class="container multi-browse">
            <c:forEach var="event" items="${fewTicketsEvents}">
                <c:set var="event" value="${event}" scope="request"/>
                <c:import url="eventCard.jsp"/>
            </c:forEach>
            </div>
        </c:when>
    </c:choose>
</body>
</html>
