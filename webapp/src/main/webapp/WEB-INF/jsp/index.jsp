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
        <c:when test="${upcomingEvents.size() > 0}">
            <div>
                <h2 class="subtitle">Próximos eventos</h2>
            </div>
            <div class="container multi-browse">
                <c:forEach var="event" items="${upcomingEvents}">
                    <c:if test="${!event.deleted}">
                        <div class="card uk-card uk-card-default" onclick="location.href='<c:url value="/events/${event.id}"/>'">
                            <div class="uk-card-media-top">
                                <img class="card_img" src="<c:url value="/image/${event.imageId}"/>" alt="Party Image">
                            </div>
                            <div class="uk-card-body">
                                <h3 class="uk-card-title"><c:out value="${event.name}"/></h3>
                                <div class="container card_body">
                                    <div class="card_info">
                                        <img class="icon" src="<c:url value="/resources/svg/price-tag.svg"/>" alt="Price icon"/>
                                        <span>
                                                <c:choose>
                                                    <c:when test="${event.price == 0}">Gratis</c:when>
                                                    <c:otherwise>$<c:out value="${event.price}"/></c:otherwise>
                                                </c:choose>
                                            </span>
                                        <img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="Location icon"/><span><c:out value="${event.location.name}"/></span>
                                    </div>
                                    <div class="card_info">
                                        <img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="Date icon"/><span><c:out value="${event.dateFormatted}"/></span>
                                        <img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="Time icon"/><span><c:out value="${event.timeFormatted}"/></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </c:when>
    </c:choose>
    <c:choose>
        <c:when test="${fewTicketsEvents.size() > 0}">
            <div>
                <h2 class="subtitle">No te pierdas estos eventos ¡Se están quedando sin entradas!</h2>
            </div>
            <div class="container multi-browse">
            <c:forEach var="event" items="${fewTicketsEvents}">
                <c:if test="${!event.deleted}">
                    <div class="card uk-card uk-card-default" onclick="location.href='<c:url value="/events/${event.id}"/>'">
                        <div class="uk-card-media-top">
                            <img class="card_img" src="<c:url value="/image/${event.imageId}"/>" alt="Party Image">
                        </div>
                        <div class="uk-card-body">
                            <h3 class="uk-card-title"><c:out value="${event.name}"/></h3>
                            <div class="container card_body">
                                <div class="card_info">
                                    <img class="icon" src="<c:url value="/resources/svg/price-tag.svg"/>" alt="Price icon"/>
                                    <span>
                                                <c:choose>
                                                    <c:when test="${event.price == 0}">Gratis</c:when>
                                                    <c:otherwise>$<c:out value="${event.price}"/></c:otherwise>
                                                </c:choose>
                                            </span>
                                    <img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="Location icon"/><span><c:out value="${event.location.name}"/></span>
                                </div>
                                <div class="card_info">
                                    <img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="Date icon"/><span><c:out value="${event.dateFormatted}"/></span>
                                    <img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="Time icon"/><span><c:out value="${event.timeFormatted}"/></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
            </div>
        </c:when>
    </c:choose>
</body>
</html>
