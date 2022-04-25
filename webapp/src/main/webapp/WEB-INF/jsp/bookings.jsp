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
    <div class="container browse booking-browse">
        <c:choose>
            <c:when test="${size > 0}">
                <c:forEach var="booking" items="${bookings}">
                    <div class="horizontal booking-card card uk-card uk-card-default" onclick="location.href='<c:url value="/events/${booking.event.id}"/>'">
                        <div class="fill">
                            <img src="<c:url value="data:image/jpeg;base64,${booking.event.img.formatted}"/>"/>
                        </div>
                        <div class="booking-card-body">
                            <h3 class="uk-card-title"><c:out value="${booking.event.name}"/></h3>
                            <div class="booking-card-info">
                                <div><img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="Date icon"/><span><c:out value="${booking.event.dateFormatted}"/></span></div>
                                <div><img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="Time icon"/><span><c:out value="${booking.event.timeFormatted}"/></span></div>
                                <div><img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="Location icon"/><span><c:out value="${booking.event.location.name}"/></span></div>
                                <div><img class="icon" src="<c:url value="/resources/svg/tickets2.svg"/>" alt="Tickets icon"/><span><c:out value="${booking.qty}"/></span></div>

                                <div>
                                <c:url value="/bookings/cancel/${booking.event.id}" var="postPath"/>
                                <form:form novalidate="true" class="booking-card-body transparent" modelAttribute="bookForm" action="${postPath}" method="post" id="bookForm">
                                    <div>
                                        <span class="required">* </span>
                                        <form:label path="qty">Cantidad de entradas: </form:label>
                                    </div>
                                    <form:input class="uk-input" type="number" path="qty" min="1" required="true" id="qty"/>
                                    <form:errors path="qty" cssClass="error-message" element="span"/>
                                    <spring:message code="Min.bookForm.qty" var="qtySizeError"/>
                                    <spring:message code="NotNull.bookForm.qty" var="qtyNullError"/>
                                    <span class="formError"></span>

                                    <div class="container event_buttons">
                                        <input class="cancel_button uk-button" type="submit" name="submit" value="Cancelar"/>
                                    </div>
                                </form:form>
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

