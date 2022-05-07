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
    <div class="container event">
        <div class="container">
            <div>
                <spring:message var="altEventMessage" code="event.imageAlt"/>
                <img class="event_img" alt="${altEventMessage}" src="<c:url value="/image/${event.imageId}"/>"/>
            </div>
            <div class="event_info">
                <c:if test="${isOwner}">
                    <div class="event_actions">
                        <spring:message var="altDeleteIcon" code="event.deleteAlt"/>
                        <spring:message var="altEditIcon" code="event.editAlt"/>
                        <input type="image" class="icon" src="<c:url value="/resources/svg/edit.svg"/>" alt="${altEditIcon}" onclick="location.href='<c:url value="/events/${event.id}/modify"/>'"/>
                        <input type="image" class="icon" src="<c:url value="/resources/svg/edit.svg"/>" alt="${altEditIcon}" onclick="location.href='<c:url value="/events/${event.id}/add-tickets"/>'"/>
                        <form class="transparent" action="<c:url value="/events/${event.id}/delete"/>" method="post">
                            <input class="icon" src="<c:url value="/resources/svg/trash.svg"/>" alt="${altDeleteIcon}" type="image" name="submit" value=""/>
                        </form>
                    </div>
                </c:if>
                <h3><c:out value="${event.name}"/></h3>
                <p><c:out value="${event.description}"/></p>
                <div class="container">
                    <spring:message var="altLocationIcon" code="event.locationAlt"/>
                    <img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="${altLocationIcon}"/><span><c:out value="${event.location.name}"/></span>
                </div>
                    <div class="container">
                        <spring:message var="altMinPriceIcon" code="event.priceAlt"/>
                    <img class="icon" src="<c:url value="/resources/svg/price-tag.svg"/>" alt="${altMinPriceIcon}"/>
                    <span>
                        <c:choose>
                            <c:when test="${event.minPrice == 0}"><spring:message code="event.free"/></c:when>
                            <c:otherwise>$<c:out value="${event.minPrice}"/></c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <div class="container">
                    <spring:message var="altDateIcon" code="event.dateAlt"/>
                    <img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="${altDateIcon}"/><span><c:out value="${event.dateFormatted}"/></span>
                </div>
                <div class="container">
                    <spring:message var="altTimeIcon" code="event.timeAlt"/>
                    <img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="${altTimeIcon}"/><span><c:out value="${event.timeFormatted}"/></span>
                </div>
                <div class="container">
                    <spring:message var="altTypeIcon" code="event.typeAlt"/>
                    <img class="icon" src="<c:url value="/resources/svg/party.svg"/>" alt="${altTypeIcon}"/><span><c:out value="${event.type.name}"/></span>
                </div>
                <div class="container tag-container">
                    <c:forEach var="tag" items="${event.tags}">
                        <span class="chip"><c:out value="${tag.name}"/></span>
                    </c:forEach>
                </div>
                <div class="horizontal">
                    <c:if test="${isOwner}">
                        <span><spring:message code="event.bookedTickets"/><c:out value="${event.attendance}"/></span>
                    </c:if>
                </div>
                <div class="container">
                    <spring:message var="altUserIcon" code="event.altUser"/>
                    <img class="icon" src="<c:url value="/resources/svg/user.svg"/>" alt="${altUserIcon}"/>
                    <a href="<c:url value="/profile/${event.user.id}"/>"><c:out value="${event.user.username}"/></a>
                </div>
                <div>
                    <c:if test="${isOwner}">
                        <c:choose>
                            <c:when test="${!event.soldOut && !event.deleted}">
                                <spring:message var="setSoldOut" code="event.setSoldout"/>
                                <form action="<c:url value="/events/${event.id}/soldout"/>" class="transparent" method="post">
                                    <input class="uk-button uk-button-text" type="submit" name="submit" value="${setSoldOut}"/>
                                </form>
                            </c:when>
                            <c:when test="${event.soldOut && !event.deleted}">
                                <spring:message var="setAvailable" code="event.setAvailable"/>
                                <form action="<c:url value="/events/${event.id}/active"/>" class="transparent" method="post">
                                    <input class="uk-button uk-button-text" type="submit" name="submit" value="${setAvailable}"/>
                                </form>
                            </c:when>
                        </c:choose>
                    </c:if>
                    <c:if test="${event.deleted}">
                        <b><spring:message code="event.deleted"/></b>
                    </c:if>
                </div>
            </div>
        </div>

        <c:if test="${!isOwner}">
            <c:url value="/events/${event.id}" var="postPath"/>
            <form:form novalidate="true" modelAttribute="bookForm" class="center bookForm" action="${postPath}" method="post" id="bookForm">
                <c:choose>
                    <c:when test="${event.soldOut}">
                        <c:set var="disabled" scope="session" value="true"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="disabled" scope="session" value="false"/>
                    </c:otherwise>
                </c:choose>

                <c:set var="i" scope="session" value="0"/>
                <c:forEach var="ticket" items="${event.tickets}">
                    <c:set var="ticketsLeft" scope="session" value="${ticket.qty}"/>
                    <div class="horizontal center">
                        <span><c:out value="${ticket.ticketName}"/></span>
                        <span>$<c:out value="${ticket.price}"/></span>
                        <form:input class="uk-input" type="number" disabled="${disabled}" path="bookings[${i}].qty" min="1" max="${ticket.qty}" required="true" id="qty${i}"/>
                        <form:errors path="bookings[${i}].qty" cssClass="error-message" element="span"/>
                        <spring:message code="Min.bookForm.qty" var="qtyMinSizeError"/>
                        <spring:message code="Max.bookForm.qtyStr" var="qtyMaxSizeError"/>
                        <spring:message code="NotNull.bookForm.qty" var="qtyNullError"/>

                        <form:input class="hidden" type="number" value="${ticket.id}" path="bookings[${i}].ticketId"/>
                        <span class="formError"></span>
                    </div>
                    <c:if test="${!disabled}">
                        <spring:message var="ticketsLeft" code="event.ticketsLeft"/>
                        <span>(${ticketsLeft}: ${ticket.qty})</span>
                    </c:if>
                    <c:set var="i" scope="session" value="${i + 1}"/>
                </c:forEach>

                <c:choose>
                    <c:when test="${event.soldOut}">
                        <div class="container event_buttons">
                            <spring:message var="soldOut" code="event.soldOut"/>
                            <input disabled class="uk-button" type="submit" name="submit" value="${soldOut}"/>
                        </div>
                    </c:when>
                    <c:when test="${!event.deleted}">
                        <div class="container event_buttons">
                            <spring:message var="book" code="event.book"/>
                            <input class="uk-button" type="submit" name="submit" value="${book}"/>
                        </div>
                    </c:when>
                </c:choose>
            </form:form>
        </c:if>
    </div>

    <c:set var="similarEvents" value="${similarEvents}" scope="request"/>
    <c:set var="similarEventsSize" value="${similarEventsSize}" scope="request"/>
    <c:import url="similarEvents.jsp"/>

    <c:set var="popularEvents" value="${popularEvents}" scope="request"/>
    <c:set var="popularEventsSize" value="${popularEventsSize}" scope="request"/>
    <c:import url="popularEvents.jsp"/>
</body>
</html>

<script type="text/javascript">
    (function() {
        var qty = document.getElementById('qty');
        var form = document.getElementById('bookForm');

        if (qty === null || form === null)
            return;

        var checkQtyValidity = function() {
            if (qty.validity.rangeUnderflow) {
                qty.setCustomValidity('${qtyMinSizeError}');
                updateQtyMessage();
            } else if (qty.validity.rangeOverflow) {
                qty.setCustomValidity('${qtyMaxSizeError} ' + ${ticketsLeft} + '.');
                updateQtyMessage();
            } else if (qty.validity.valueMissing) {
                qty.setCustomValidity('${qtyNullError}');
                updateQtyMessage();
            } else {
                qty.setCustomValidity('');
            }
        };

        var updateQtyMessage = function() {
            form.getElementsByClassName('formError')[0].innerHTML = qty.validationMessage;
        }

        qty.addEventListener('change', checkQtyValidity, false);
        qty.addEventListener('keyup', checkQtyValidity, false);

        form.addEventListener('submit', function(event) {
            if (form.classList) form.classList.add('submitted');
            checkQtyValidity();
            if (!this.checkValidity()) {
                event.preventDefault();
                updateQtyMessage();
            }
        }, false);
    }());
</script>
