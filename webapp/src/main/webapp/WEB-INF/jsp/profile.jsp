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
        <spring:message var="starIconAlt" code="profile.starAlt"/>
        <c:if test="${user.votes > 0}">
            <div class="horizontal">
                <span class="center"><spring:message code="profile.rating"/>: <c:out value="${user.rating}"/> <img class="small-icon" src="<c:url value="/resources/svg/star.svg"/>"  alt="${starIconAlt}"/></span>
                <span>(<c:out value="${user.votes}"/>)</span>
            </div>
        </c:if>
        <c:if test="${stats != null}">
            <div class="vertical tickets sep-top-xl">
                <table class="stats-table tickets-table">
                    <tr>
                        <th><spring:message code="stats.stats"/></th>
                    </tr>
                    <tr>
                        <td><spring:message code="stats.bookedEvents"/></td>
                        <td class="table-number">${stats.eventsAttended}</td>
                    </tr>
                    <tr>
                        <td><spring:message code="stats.bookingsMade"/></td>
                        <td class="table-number">${stats.bookingsMade}</td>
                    </tr>
                    <tr>
                        <td><spring:message code="stats.favType"/></td>
                        <td class="table-number">${stats.favType.name}</td>
                    </tr>
                    <tr>
                        <td><spring:message code="stats.favLocation"/></td>
                        <td class="table-number">${stats.favLocation.name}</td>
                    </tr>
                </table>
            </div>
        </c:if>

    <c:if test="${size > 0}">
        <div class="left">
            <h3><spring:message code="profile.events"/></h3>
        </div>
        <div class="container multi-browse">
            <c:forEach var="event" items="${events}">
                <c:if test="${!event.deleted}">
                    <c:set var="event" value="${event}" scope="request"/>
                    <c:import url="eventCard.jsp"/>
                </c:if>
            </c:forEach>
            <c:url value="/events" var="moreUserEvents">
                <c:param name="searchUsername" value="${user.username}"/>
            </c:url>
            <a href="${moreUserEvents}"><spring:message code="profile.userEvents"/></a>
        </div>
    </c:if>
    </div>
</body>
</html>
