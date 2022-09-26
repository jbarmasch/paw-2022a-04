<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@ include file="include.jsp" %>
    <title>BotPass</title>
</head>
<body>
    <c:import url="appbar.jsp"/>
    <div class="home">
        <div class="container browse">
            <c:choose>
                <c:when test="${size > 0}">
                    <c:forEach var="event" items="${myEvents}">
                        <c:set var="event" value="${event}" scope="request"/>
                        <c:import url="eventCard.jsp"/>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="center sep-top-xl full_width">
                        <h2>
                            <spring:message code="filter.noEvents"/>
                        </h2>
                    </div>
                </c:otherwise>
            </c:choose>
            <c:set var="page" value="${page}" scope="request"/>
            <c:import url="paging.jsp"/>
        </div>
    </div>
</body>
</html>
