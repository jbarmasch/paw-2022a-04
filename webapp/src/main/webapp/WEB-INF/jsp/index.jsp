<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@ include file="include.jsp" %>
    <link href="https://fonts.googleapis.com/css?family=Teko:700&display=swap" rel="stylesheet">
    <title>BotPass</title>
</head>
<body>
    <c:import url="appbar.jsp"/>
    <div class="container only-element">
        <h1 class="title neon-text"><spring:message code="index.welcome"/></h1>
    </div>

    <c:choose>
        <c:when test="${upcomingSize > 0}">
            <div class="event-box">
                <div class="vertical center">
                    <div>
                        <h2 class="title"><spring:message code="index.nextEvents"/></h2>
                    </div>
                    <div class="container multi-browse">
                        <c:forEach var="event" items="${upcomingEvents}">
                            <c:set var="event" value="${event}" scope="request"/>
                            <c:import url="eventCard.jsp"/>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:when>
    </c:choose>
    <c:choose>
        <c:when test="${fewTicketsSize > 0}">
            <div class="event-box">
                <div class="vertical center">
                    <div>
                        <h2 class="title"><spring:message code="index.popularEvents"/></h2>
                    </div>
                    <div class="container multi-browse">
                        <c:forEach var="event" items="${fewTicketsEvents}">
                            <c:set var="event" value="${event}" scope="request"/>
                            <c:import url="eventCard.jsp"/>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:when>
    </c:choose>
</body>
</html>
