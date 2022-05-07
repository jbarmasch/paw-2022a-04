<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<html>
<body>
    <%@ include file="appbar.jsp"%>
    <div class="container center vertical">
        <h2 class="sep-top-xl">@<c:out value="${user.username}"/></h2>
        <c:set value="${user.mail}" var="mail"/>
        <span>
            <spring:message code="profile.contact"/> <a href="mailto:${mail}">${mail}</a>
        </span>
        <span><spring:message code="profile.rating"/>: <c:out value="${user.rating}"/></span>
        <c:if test="${stats != null}">
            <div class="vertical tickets">
                <table class="tickets-table">
                    <tr>
                        <th><spring:message code="stats.stats"/></th>
                    </tr>
                    <tr>
                        <td><spring:message code="stats.bookedEvents"/>: </td>
                        <td class="table-number">${stats.eventsAttended}</td>
                    </tr>
                    <tr>
                        <td><spring:message code="stats.bookingsMade"/>: </td>
                        <td class="table-number">${stats.bookingsMade}</td>
                    </tr>
                    <tr>
                        <td><spring:message code="stats.favType"/>: </td>
                        <td class="table-number">${stats.favType.name}</td>
                    </tr>
                    <tr>
                        <td><spring:message code="stats.favLocation"/>: </td>
                        <td class="table-number">${stats.favLocation.name}</td>
                    </tr>
                </table>
            </div>
        </c:if>

        <div class="left">
            <h3><spring:message code="profile.events"/></h3>
        </div>
        <div class="container multi-browse">
            <c:choose>
                <c:when test="${size > 0}">
                    <c:forEach var="event" items="${events}">
                        <c:if test="${!event.deleted}">
                            <c:set var="event" value="${event}" scope="request"/>
                            <c:import url="eventCard.jsp"/>
                        </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <spring:message code="filter.noEvents"/>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
