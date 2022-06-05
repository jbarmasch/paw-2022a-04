<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<html>
<body>
    <%@ include file="appbar.jsp"%>
    <div class="container center vertical">
        <div class="horizontal">
            <div class="vertical center sep-right-xl">
        <h2 class="sep-top-xl">@<c:out value="${user.username}"/></h2>
        <c:set value="${user.mail}" var="mail"/>
        <span>
            <a href="mailto:${mail}">${mail}</a>
        </span>
        <spring:message var="starIconAlt" code="profile.starAlt"/>
        <c:if test="${user.votes > 0}">
            <div class="horizontal sep-top">
                <h4 class="center subtitle"><c:out value="${user.rating}"/> <img class="small-icon" src="<c:url value="/resources/svg/star.svg"/>"  alt="${starIconAlt}"/></h4>
                <h4 class="subtitle_light">(<c:out value="${user.votes}"/>)</h4>
            </div>
        </c:if>
            </div>
        <c:if test="${stats != null}">
            <div class="vertical tickets sep-top-xl sep-left-xl">
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
        </div>

    <c:if test="${size > 0}">
        <div class="event-box sep-top-xxl">
            <div class="vertical center">
                <h3 class="subtitle"><spring:message code="profile.events"/></h3>
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
            </div>
        </div>
    </c:if>
    </div>
</body>
</html>
