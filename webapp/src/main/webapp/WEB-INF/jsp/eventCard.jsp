<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="event" value="${requestScope.event}"/>
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
