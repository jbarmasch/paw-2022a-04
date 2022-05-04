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
    <h1>Su reserva se ha realizado con éxito</h1>
</div>
<c:if test="${similarEventsSize > 0}">
    <c:choose>
        <c:when test="${similarEventsSize > 1}">
            <div>
                <h4 class="subtitle">Estos eventos también podrían interesarte</h4>
            </div>
        </c:when>
        <c:otherwise>
            <div>
                <h4 class="subtitle">Este evento también podría interesarte</h4>
            </div>
        </c:otherwise>
    </c:choose>
    <div class="container multi-browse">

        <c:forEach var="event" items="${similarEvents}">
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
                                                    <c:when test="${event.minPrice == 0}">Gratis</c:when>
                                                    <c:otherwise>$<c:out value="${event.minPrice}"/></c:otherwise>
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
        </c:forEach>

    </div>
</c:if>

<c:if test="${popularEventsSize > 0}">
    <c:choose>
        <c:when test="${popularEventsSize > 1}">
            <div>
                <h4 class="subtitle">Otras personas también reservaron para estos eventos</h4>
            </div>
        </c:when>
        <c:otherwise>
            <div>
                <h4 class="subtitle">Otras personas también reservaron para este evento</h4>
            </div>
        </c:otherwise>
    </c:choose>
    <div class="container multi-browse">


        <c:forEach var="event" items="${popularEvents}">
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
                                                    <c:when test="${event.minPrice == 0}">Gratis</c:when>
                                                    <c:otherwise>$<c:out value="${event.minPrice}"/></c:otherwise>
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
        </c:forEach>
    </div>
</c:if>
</body>
</html>
