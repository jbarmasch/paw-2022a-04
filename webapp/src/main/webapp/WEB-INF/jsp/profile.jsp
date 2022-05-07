<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<html>
<body>
    <%@ include file="appbar.jsp"%>
    <h2>User profile: <c:out value="${user.username}! ${user.id}"/></h2>
    <span><c:out value="${user.rating}"/></span>
    <c:choose>
        <c:when test="${stats == null}">
            NO hay stats
        </c:when>
        <c:otherwise>
            <div class="container center">
                <div class="vertical">
                    <span><spring:message code="stats.bookedEvents"/>: ${stats.eventsAttended}</span>
                    <span><spring:message code="stats.bookingsMade"/>: ${stats.bookingsMade}</span>
                    <span><spring:message code="stats.favType"/>: ${stats.favType.name}</span>
                    <span><spring:message code="stats.favLocation"/>: ${stats.favLocation.name}</span>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</body>
</html>
