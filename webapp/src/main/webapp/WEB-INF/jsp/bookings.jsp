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
                    <div class="booking-card uk-card uk-card-default" onclick="location.href='<c:url value="/events/${booking.event.id}"/>'">
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
                                <span><c:out value="${booking.qty}"/></span>
                                <div class="card_info">
                                    <img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="Date icon"/><span><c:out value="${booking.event.dateFormatted}"/></span>
                                    <img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="Time icon"/><span><c:out value="${booking.event.timeFormatted}"/></span>
                                </div>
                            </div>
                            <div>

<%--                                <form action="/bookings/${booking.event.id}" method="post"> --%>
<%--                                    <input class="icon" src="<c:url value="/resources/svg/trash.svg"/>" alt="Trash icon" type="image" name="submit" value=""/>--%>
<%--                                </form>--%>
                            </div>
                        </div>
                    </div>
                    <c:url value="/bookings/cancel/${booking.event.id}" var="postPath"/>
                    <form:form novalidate="true" modelAttribute="bookForm" action="${postPath}" method="post" id="bookForm">
                        <span class="required">* </span>
                        <form:label path="qty">Cantidad de entradas: </form:label>
                        <form:input class="uk-input" type="number" path="qty" min="1" required="true" id="qty"/>
                        <form:errors path="qty" cssClass="error-message" element="span"/>
                        <spring:message code="Min.bookForm.qty" var="qtySizeError"/>
                        <spring:message code="NotNull.bookForm.qty" var="qtyNullError"/>
                        <span class="formError"></span>

                        <div class="container event_buttons">
                            <input class="uk-button" type="submit" name="submit" value="Reservar"/>
                                <%-- <input class="uk-button cancel_button" type="submit" name="cancel" value="Cancelar"/> --%>
                        </div>
                    </form:form>

<%--                    <button onclick="location.href='<c:url value="/bookings/cancel/${booking.event.id}"/>'" class="cancel_button uk-button" formmethod="post">Cancelar</button>--%>
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

