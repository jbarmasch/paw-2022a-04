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
            <c:choose>
                <c:when test="${futureSize + previousSize > 0}">
                    <div class="vertical full_width sep-top-xl">
                        <h4 class="subtitle"><spring:message code="booking.previous"/></h4>
                    <div class="container multi-browse booking-browse full_width">
                    <c:set var="i" scope="session" value="0"/>
                    <c:forEach var="booking" items="${previousBookings}">
                        <c:if test="${!booking.event.deleted}">
                        <div class="horizontal booking-card card uk-card uk-card-default" >
                            <div class="horizontal">
                                <div class="fill">
                                    <img src="<c:url value="/image/${booking.event.imageId}"/>" onclick="location.href='<c:url value="/events/${booking.event.id}"/>'" alt="Event image"/>
                                </div>
                                <div class="transparent booking-card-body">
                                    <h3 class="uk-card-title"><c:out value="${booking.event.name}"/></h3>
                                    <div class="booking-card-info">
                                        <div class="data full_width">
                                            <div><img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="Date icon"/><span><c:out value="${booking.event.dateFormatted}"/></span></div>
                                            <div><img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="Time icon"/><span><c:out value="${booking.event.timeFormatted}"/></span></div>
                                        </div>
                                        <div class="data full_width">
                                            <div><img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="Location icon"/><span><c:out value="${booking.event.location.name}"/></span></div>
                                            <div><img class="icon" src="<c:url value="/resources/svg/user.svg"/>" alt="User icon"/><span><c:out value="${booking.event.user.username}"/></span></div>
                                        </div>
                                        <hr/>
                                            <div class="container booking_button">
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
                                                </form:form>
                                            </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                        </c:if>
                        <c:set var="i" scope="session" value="${i + 1}"/>
                    </c:forEach>
                    </div>
                        <h4 class="subtitle"><spring:message code="booking.future" /></h4>
                    <div class="container multi-browse booking-browse full_width">
                    <c:set var="j" scope="session" value="0"/>
                    <c:forEach var="fbooking" items="${futureBookings}">
                        <c:if test="${!fbooking.event.deleted}">
                            <div class="horizontal booking-card card uk-card uk-card-default" >
                                <div class="horizontal">
                                    <div class="fill">
                                        <img src="<c:url value="/image/${fbooking.event.imageId}"/>" onclick="location.href='<c:url value="/events/${fbooking.event.id}"/>'" alt="Event image"/>
                                    </div>
                                    <div class="transparent booking-card-body">
                                        <h3 class="uk-card-title"><c:out value="${fbooking.event.name}"/></h3>
                                        <div class="booking-card-info">
                                            <div class="data full_width">
                                                <div><img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="Date icon"/><span><c:out value="${fbooking.event.dateFormatted}"/></span></div>
                                                <div><img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="Time icon"/><span><c:out value="${fbooking.event.timeFormatted}"/></span></div>
                                            </div>
                                            <div class="data full_width">
                                                <div><img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="Location icon"/><span><c:out value="${fbooking.event.location.name}"/></span></div>
                                                <div><img class="icon" src="<c:url value="/resources/svg/user.svg"/>" alt="User icon"/><span><c:out value="${fbooking.event.user.username}"/></span></div>
                                            </div>

                                            <hr/>

                                            <div class="container booking_button">
                                                        <spring:message code="booking.cancelTickets" var="cancelTickets"/>
                                                        <input class="cancel_button uk-button" type="button" value="${cancelTickets}" uk-toggle="target: #confirmation${j}"/>
                                                        <div id="confirmation${j}" uk-modal>
                                                            <div class="uk-modal-dialog">
                                                                <button class="uk-modal-close-default" type="button" uk-close></button>
                                                                <div class="uk-modal-header">
                                                                    <h2 class="uk-modal-title"><spring:message code="booking.cancel"/></h2>
                                                                </div>
                                                                <div class="uk-modal-body">
                                                                    <c:url value="/bookings/cancel/${fbooking.event.id}" var="postPath"/>
                                                                    <form:form novalidate="true" class="transparent" modelAttribute="bookForm" action="${postPath}" method="post" id="bookForm${j}">
                                                                        <div class="vertical center">
                                                                            <c:set var="j" value="0"/>
                                                                            <c:forEach var="tickets" items="${fbooking.bookings}">
                                                                                <div class="horizontal center">
                                                                                    <form:label class="sep-right" path="bookings[${j}].qty">*<spring:message code="booking.cancelTickets.qty"/>: </form:label>
                                                                                    <c:set var="qtyTickets" scope="session" value="${tickets.qty}"/>
                                                                                    <span><c:out value="${tickets.ticket.ticketName}"/></span>
                                                                                    <form:input class="uk-input" type="number" path="bookings[${j}].qty" min="1" max="${tickets.qty}" required="true" id="qty${j}"/>
                                                                                    <spring:message code="Min.bookForm.qty" var="minQtySizeError"/>
                                                                                    <spring:message code="Max.bookForm.qtyStr" var="maxQtySizeError"/>
                                                                                    <spring:message code="NotNull.bookForm.qty" var="qtyNullError"/>
                                                                                    <span class="formError"></span>

                                                                                    <form:input class="hidden" type="number" path="bookings[${j}].ticketId" value="${tickets.ticket.id}"/>
                                                                                    <c:set var="j" value="${j + 1}"/>
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

                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        <c:set var="j" scope="session" value="${j + 1}"/>
                    </c:forEach>
                    </div>
                    </div>
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
