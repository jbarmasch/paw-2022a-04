<%@ page contentType="text/html;charset=UTF-8" %>
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
    <c:import url="appbar.jsp"/>
    <div class="home">
        <div class="container full_width">
            <spring:message code="event.dateAlt" var="dateAlt"/>
            <spring:message code="event.timeAlt" var="timeAlt"/>
            <spring:message code="event.locationAlt" var="locationAlt"/>
            <spring:message code="event.altUser" var="userAlt"/>
            <spring:message code="event.imageAlt" var="imageAlt"/>
            <c:choose>
                <c:when test="${eventBookingsSize > 0}">
                    <div class="vertical full_width sep-top-xl">
                        <h4 class="title2"><spring:message code="booking.yours"/></h4>
                        <div class="container multi-browse booking-browse full_width">
                        <c:forEach var="booking" items="${eventBookings}">
                            <c:if test="${!booking.event.deleted}">
                                <c:choose>
                                    <c:when test="${booking.event.date >= actualTime}">
                                        <div class="horizontal booking-card card clickable uk-card uk-card-default" onclick="location.href='<c:url value="/bookings/${booking.code}"/>'">
                                    </c:when>
                                            <c:otherwise>
                                        <div class="horizontal booking-card card uk-card uk-card-default">
                                            </c:otherwise>
                                </c:choose>
                                <div class="horizontal">
                                    <div class="fill">
                                        <img src="<c:url value="/image/${booking.event.image.id}"/>" alt="${imageAlt}"/>
                                    </div>
                                    <div class="transparent booking-card-body">
                                        <h3 class="uk-card-title"><c:out value="${booking.event.name}"/></h3>
                                        <div class="booking-card-info">
                                            <div class="data full_width">
                                                <div><img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="${dateAlt}"/><span><c:out value="${booking.event.dateFormatted}"/></span></div>
                                                <div><img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="${timeAlt}"/><span><c:out value="${booking.event.timeFormatted}"/></span></div>
                                            </div>
                                            <div class="data full_width">
                                                <div><img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="${locationAlt}"/><span><c:out value="${booking.event.location.name}"/></span></div>
                                                <div><img class="icon" src="<c:url value="/resources/svg/user.svg"/>" alt="${userAlt}"/><span><c:out value="${booking.event.organizer.username}"/></span></div>
                                            </div>
                                            <hr/>
                                                <div class="container booking_button">
                                                    <c:choose>
                                                        <c:when test="${booking.event.date >= actualTime}">
                                                            <spring:message code="booking.cancelTickets" var="cancelTickets"/>
                                                            <input class="cancel_button uk-button booking_cancel" type="button" value="${cancelTickets}" onclick="event.stopImmediatePropagation(); location.href='<c:url value="/bookings/cancel/${booking.event.id}"/>'"/>
                                                        </c:when>
                                                        <c:when test="${booking.event.date >= oneMonthPrior}">
                                                            <c:url value="/bookings/rate/${booking.event.id}" var="postPath"/>
                                                            <form:form novalidate="true" class="transparent" modelAttribute="rateForm" action="${postPath}" method="post" id="rateForm${booking.event.id}">
                                                                <div class="horizontal">
                                                                    <spring:message code="booking.rateOrganizer"/>:
                                                                        <div class="rate">
                                                                            <input type="radio" id="star5" name="rate" value="5" onclick="rateOrganizer(${booking.event.id}, 5)" <c:if test="${booking.rating >= 4.5}">checked</c:if>/>
                                                                            <label for="star5"></label>
                                                                            <input type="radio" id="star4" name="rate" value="4" onclick="rateOrganizer(${booking.event.id}, 4)" <c:if test="${booking.rating >= 3.5 && booking.rating < 4.5}">checked</c:if>/>
                                                                            <label for="star4"></label>
                                                                            <input type="radio" id="star3" name="rate" value="3" onclick="rateOrganizer(${booking.event.id}, 3)" <c:if test="${booking.rating >= 2.5 && booking.rating < 3.5}">checked</c:if>/>
                                                                            <label for="star3" ></label>
                                                                            <input type="radio" id="star2" name="rate" value="2" onclick="rateOrganizer(${booking.event.id}, 2)" <c:if test="${booking.rating >= 1.5 && booking.rating < 2.5}">checked</c:if>/>
                                                                            <label for="star2"></label>
                                                                            <input type="radio" id="star1" name="rate" value="1" onclick="rateOrganizer(${booking.event.id}, 1)" <c:if test="${booking.rating < 1.5}">checked</c:if>/>
                                                                            <label for="star1"></label>
                                                                        </div>
                                                                </div>
                                                                <form:input class="hidden" id="rating${booking.event.id}" path="rating" type="number"/>
                                                                <form:input class="hidden" id="rating${booking.event.id}" path="eventId" type="number"/>
                                                            </form:form>
                                                        </c:when>
                                                    </c:choose>
                                                </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            </c:if>
                        </c:forEach>
                    </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="center sep-top-xl full_width">
                        <h2 class="title2">
                            <spring:message code="booking.noBookings"/>
                        </h2>
                    </div>
                </c:otherwise>
            </c:choose>
            <c:set var="page" value="${page}" scope="request"/>
            <c:import url="paging.jsp"/>
        </div>
    </div>
</body>
</html>

<script type="text/javascript">
    function rateOrganizer(eventId, i) {
        document.getElementById('rating' + eventId).value = i;
        document.getElementById('rateForm' + eventId).submit();
    }
</script>
