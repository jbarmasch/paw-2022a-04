<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
<%@ include file="appbar.jsp"%>
<div class="home">
    <div class="container browse">
        <c:choose>
            <c:when test="${size > 0}">
                <c:forEach var="booking" items="${bookings}">
                    <div class="card uk-card uk-card-default" onclick="location.href='<c:url value="/events/${booking.event.id}"/>'">
                        <div class="uk-card-media-top">
                            <img class="card_img" src="data:image/jpeg;base64,${booking.event.img.formatted}" alt="Party Image">
                        </div>
                        <div class="uk-card-body">
                            <h3 class="uk-card-title"><c:out value="${booking.event.name}"/></h3>
                            <div class="container card_body">
                                <div class="card_info">
                                    <img class="icon" src="<c:url value="/resources/svg/price-tag.svg"/>" alt="Price icon"/>
                                    <span>
                                            <c:choose>
                                                <c:when test="${booking.event.price == 0}">Gratis</c:when>
                                                <c:otherwise>$<c:out value="${booking.event.price}"/></c:otherwise>
                                            </c:choose>
                                        </span>
                                    <img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="Location icon"/><span><c:out value="${booking.event.location.name}"/></span>
                                </div>
                                <div class="card_info">
                                    <img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="Date icon"/><span><c:out value="${booking.event.dateFormatted}"/></span>
                                    <img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="Time icon"/><span><c:out value="${booking.event.timeFormatted}"/></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                No se encontraron eventos.
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>

