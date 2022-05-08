<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="event" value="${requestScope.event}"/>
<div class="card uk-card uk-card-default" onclick="location.href='<c:url value="/events/${event.id}"/>'">
    <div class="uk-card-media-top">
        <spring:message code="event.imageAlt" var="imageAlt"/>
        <div class="card_img_container">
            <img class="card_img" src="<c:url value="/image/${event.imageId}"/>" alt="${imageAlt}">
            <c:if test="${event.soldOut}">
                <spring:message code="event.soldOut" var="soldOut"/>
                <img class="soldout_card" src="<c:url value="/resources/png/sold_out.png"/>" alt="${soldOut}"/>
            </c:if>
        </div>
    </div>
    <div class="uk-card-body">
        <h3 class="uk-card-title"><c:out value="${event.name}"/></h3>
        <div class="container card_body">
            <div class="card_info">
                <spring:message code="event.priceAlt" var="priceAlt"/>
                <img class="icon" src="<c:url value="/resources/svg/price-tag.svg"/>" alt="${priceAlt}"/>
                <span>
                    <c:choose>
                        <c:when test="${event.minPrice == 0}"><spring:message code="event.free"/></c:when>
                        <c:otherwise>$<c:out value="${event.minPrice}"/></c:otherwise>
                    </c:choose>
                </span>
                <spring:message code="event.locationAlt" var="locationAlt"/>
                <img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="${locationAlt}"/><span><c:out value="${event.location.name}"/></span>
            </div>
            <div class="card_info">
                <spring:message code="event.dateAlt" var="dateAlt"/>
                <img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="${dateAlt}"/><span><c:out value="${event.dateFormatted}"/></span>
                <spring:message code="event.timeAlt" var="timeAlt"/>
                <img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="${timeAlt}"/><span><c:out value="${event.timeFormatted}"/></span>
            </div>
        </div>
    </div>
</div>
