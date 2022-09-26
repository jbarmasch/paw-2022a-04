<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="event" value="${requestScope.event}"/>
<div class="card card_zoomed uk-card uk-card-default clickable"
     onclick="location.href='<c:url value="/events/${event.id}"/>'">
    <div class="uk-card-media-top">
        <spring:message code="event.imageAlt" var="imageAlt"/>
        <div class="card_img_container card_img_container_zoomed">
            <img class="card_img" src="<c:url value="/image/${event.image.id}"/>" alt="${imageAlt}">
            <c:if test="${event.soldOut}">
                <spring:message code="event.soldOut" var="soldOut"/>
                <img class="soldout_card soldout_card_zoomed" src="<c:url value="/resources/png/sold_out.png"/>"
                     alt="${soldOut}"/>
            </c:if>
        </div>
    </div>
    <div class="uk-card-body">
        <h3 class="uk-card-title uk-card-title_zoomed"><c:out value="${event.name}"/></h3>
        <div class="container card_body">
            <div class="card_info card_info_zoomed">
                <spring:message code="event.priceAlt" var="priceAlt"/>
                <img class="icon icon_zoomed" src="<c:url value="/resources/svg/price-tag.svg"/>" alt="${priceAlt}"/>
                <span>
                    <c:choose>
                        <c:when test="${event.soldOut}">
                            <spring:message code="event.soldOut"/>
                        </c:when>
                        <c:when test="${event.minPrice != null && event.minPrice == 0}">
                            <spring:message code="event.free"/>
                        </c:when>
                        <c:when test="${event.minPrice != null && event.minPrice > 0}">
                            <spring:message code="event.starting"/>&nbsp;$<c:out value="${event.minPrice}"/>
                        </c:when>
                        <c:otherwise>
                            <spring:message code="event.notApplies"/>
                        </c:otherwise>
                    </c:choose>
                </span>
            </div>
            <div class="card_info card_info_zoomed">
                <spring:message code="event.locationAlt" var="locationAlt"/>
                <img class="icon icon_zoomed" src="<c:url value="/resources/svg/location-pin.svg"/>"
                     alt="${locationAlt}"/><span><c:out value="${event.location.name}"/></span>
            </div>
            <div class="card_info card_info_zoomed">
                <div>
                    <spring:message code="event.dateAlt" var="dateAlt"/>
                    <img class="icon icon_zoomed" src="<c:url value="/resources/svg/date.svg"/>"
                         alt="${dateAlt}"/><span><c:out value="${event.dateFormatted}"/></span>
                </div>
                <div>
                    <spring:message code="event.timeAlt" var="timeAlt"/>
                    <img class="icon icon_zoomed" src="<c:url value="/resources/svg/time.svg"/>"
                         alt="${timeAlt}"/><span><c:out value="${event.timeFormatted}"/></span>
                </div>
            </div>
        </div>
    </div>
</div>
