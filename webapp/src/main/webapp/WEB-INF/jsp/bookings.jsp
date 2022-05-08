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
        <div class="container browse booking-browse">
            <c:choose>
                <c:when test="${size > 0}">
                    <c:set var="i" scope="session" value="0"/>
                    <c:forEach var="booking" items="${bookings}">
                        <c:if test="${!booking.event.deleted}">
                        <div class="horizontal booking-card card uk-card uk-card-default" >
                            <div class="horizontal">
                                <div class="fill">
                                    <img src="<c:url value="/image/${booking.event.imageId}"/>" onclick="location.href='<c:url value="/events/${booking.event.id}"/>'" alt="Event image"/>
                                </div>
                                <div class="transparent booking-card-body">
                                    <h3 class="uk-card-title"><c:out value="${booking.event.name}"/></h3>
                                    <div class="booking-card-info">
                                        <div><img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="Date icon"/><span><c:out value="${booking.event.dateFormatted}"/></span></div>
                                        <div><img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="Time icon"/><span><c:out value="${booking.event.timeFormatted}"/></span></div>
                                        <div><img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="Location icon"/><span><c:out value="${booking.event.location.name}"/></span></div>
                                        <div><img class="icon" src="<c:url value="/resources/svg/user.svg"/>" alt="User icon"/><span><c:out value="${booking.event.user.username}"/></span></div>

                                        <hr/>

                                            <div class="container booking_button">
                                                <c:choose>
                                                    <c:when test="${booking.event.date < actualTime}">
                                                <c:url value="/bookings/rate/${booking.event.id}" var="postPath"/>
                                                <form:form novalidate="true" class="transparent" modelAttribute="rateForm" action="${postPath}" method="post" id="rateForm${booking.event.id}">
                                                    <div class="horizontal">
                                                        <spring:message code="booking.rateOrganizer"/>:
                                                        <div class="rate">
                                                            <input type="radio" id="star5" name="rate" value="5" onclick="rateOrganizer(${booking.event.id}, 5)"/>
                                                            <label for="star5" title="text"></label>
                                                            <input type="radio" id="star4" name="rate" value="4" onclick="rateOrganizer(${booking.event.id}, 4)"/>
                                                            <label for="star4" title="text"></label>
                                                            <input type="radio" id="star3" name="rate" value="3" onclick="rateOrganizer(${booking.event.id}, 3)"/>
                                                            <label for="star3" title="text"></label>
                                                            <input type="radio" id="star2" name="rate" value="2" onclick="rateOrganizer(${booking.event.id}, 2)"/>
                                                            <label for="star2" title="text"></label>
                                                            <input type="radio" id="star1" name="rate" value="1" onclick="rateOrganizer(${booking.event.id}, 1)"/>
                                                            <label for="star1" title="text"></label>
                                                        </div>
                                                    </div>
                                                    <form:input class="hidden" id="rating${booking.event.id}" path="rating" type="number"/>
                                                </form:form>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <spring:message code="booking.cancelTickets" var="cancelTickets"/>
                                                <input class="cancel_button uk-button" type="button" value="${cancelTickets}" uk-toggle="target: #confirmation${i}"/>
                                                <div id="confirmation${i}" uk-modal>
                                                    <div class="uk-modal-dialog">
                                                        <button class="uk-modal-close-default" type="button" uk-close></button>
                                                        <div class="uk-modal-header">
                                                            <h2 class="uk-modal-title"><spring:message code="booking.cancel"/></h2>
                                                        </div>
                                                        <div class="uk-modal-body">
                                                            <c:url value="/bookings/cancel/${booking.event.id}" var="postPath"/>
                                                            <form:form novalidate="true" class="transparent" modelAttribute="bookForm" action="${postPath}" method="post" id="bookForm${i}">
                                                                <div class="vertical center">
                                                                    <c:set var="i" value="0"/>
                                                                    <c:forEach var="tickets" items="${booking.bookings}">
                                                                        <div class="horizontal center">
                                                                            <form:label class="sep-right" path="bookings[${i}].qty">*<spring:message code="booking.cancelTickets.qty"/>: </form:label>
                                                                            <c:set var="qtyTickets" scope="session" value="${tickets.qty}"/>
                                                                            <span><c:out value="${tickets.ticket.ticketName}"/></span>
                                                                            <form:input class="uk-input" type="number" path="bookings[${i}].qty" min="1" max="${tickets.qty}" required="true" id="qty${i}"/>
                                                                            <spring:message code="Min.bookForm.qty" var="minQtySizeError"/>
                                                                            <spring:message code="Max.bookForm.qtyStr" var="maxQtySizeError"/>
                                                                            <spring:message code="NotNull.bookForm.qty" var="qtyNullError"/>
                                                                            <span class="formError"></span>

                                                                            <form:input class="hidden" type="number" path="bookings[${i}].ticketId" value="${tickets.ticket.id}"/>
                                                                            <c:set var="i" value="${i + 1}"/>
                                                                        </div>
                                                                    </c:forEach>
                                                                </div>

                                                                <form:input class="hidden" type="number" path="page" value="${page}"/>
                                                                <hr/>
                                                            <button class="accept-button-modal uk-button" type="submit" name="submit"><spring:message code="booking.cancelTickets"/></button>
                                                            </form:form>
                                                        </div>
                                                    </div>
                                                </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                        </c:if>
                        <c:set var="i" scope="session" value="${i + 1}"/>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <h5><spring:message code="booking.noBookings"/></h5>
                </c:otherwise>
            </c:choose>
            <c:set var="page" value="${page}" scope="request"/>
            <c:import url="paging.jsp"/>
        </div>
    </div>
</body>
</html>
<c:choose>
    <c:when test="${error != null}">
        <c:set var="errorVar" scope="session" value="${error}"/>
    </c:when>
    <c:otherwise>
        <c:set var="errorVar" scope="session" value=""/>
    </c:otherwise>
</c:choose>

<script type="text/javascript">
    function rateOrganizer(eventId, i) {
        console.log("SUSAN")
        document.getElementById('rating' + eventId).value = i;
        document.getElementById('rateForm' + eventId).submit();
    }

    (function() {
        var qty = document.getElementById('qty');
        var form = document.getElementsByClassName('transparent');

        if (qty === null || form === null)
            return;

        if ("${errorVar}" !== '')
            UIkit.notification("${maxQtySizeError} ${errorVar}", {status: 'danger'}, {pos: 'bottom-center'})

        var checkQtyValidity = function() {
            for (var i = 0; i < ${i}; i++) {
                var qtyTickets = document.getElementsByClassName('uk-open').item(0).getElementsByClassName('uk-input').item(i).getAttribute('max');
                qty = document.getElementsByClassName('uk-open').item(0).getElementsByClassName('uk-input').item(i);
                if (qty.validity.rangeUnderflow) {
                    qty.setCustomValidity('${minQtySizeError}');
                    updateQtyMessage();
                } else if (qty.validity.rangeOverflow) {
                    qty.setCustomValidity('${maxQtySizeError} ' + qtyTickets + '.');
                    updateQtyMessage();
                } else if (qty.validity.valueMissing) {
                    qty.setCustomValidity('${qtyNullError}');
                    updateQtyMessage();
                } else {
                    qty.setCustomValidity('');
                }
            }
        };

        var updateQtyMessage = function() {
            form = document.getElementsByClassName('uk-open').item(0).getElementsByClassName('transparent').item(0);
            for (var i = 0; i < ${i}; i++) {
                qty = document.getElementsByClassName('uk-open').item(0).getElementsByClassName('uk-input').item(i);
                form.getElementsByClassName('formError')[i].innerHTML = qty.validationMessage;
            }
        }

        qty.addEventListener('change', checkQtyValidity, false);
        qty.addEventListener('keyup', checkQtyValidity, false);

        for (var i = 0; i < ${i}; i++) {
            qty = document.getElementById('qty' + i);
            qty.addEventListener('change', checkQtyValidity, false);
            qty.addEventListener('keyup', checkQtyValidity, false);
        }

        for (let i = 0; i < form.length; i++) {
            form.item(i).addEventListener('submit', function (event) {
                var form = document.getElementsByClassName('uk-open').item(0).getElementsByClassName('transparent').item(0);
                if (form.classList) form.classList.add('submitted');
                checkQtyValidity();
                if (!this.checkValidity()) {
                    event.preventDefault();
                    updateQtyMessage();
                }
            }, false);
        }
    }());
</script>
