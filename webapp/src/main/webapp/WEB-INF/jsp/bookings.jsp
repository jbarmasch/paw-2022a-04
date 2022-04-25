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
<%@ include file="appbar.jsp"%>
<div class="home">
    <div class="container browse booking-browse">
        <c:choose>
            <c:when test="${size > 0}">
                <c:set var="i" scope="session" value="0"/>
                <c:forEach var="booking" items="${bookings}">
                    <c:if test="${booking.qty > 0 && !booking.event.deleted}">
                    <div class="horizontal booking-card card uk-card uk-card-default" >
                        <div class="horizontal">
                            <div class="fill">
                                <img src="<c:url value="data:image/jpeg;base64,${booking.event.img.formatted}"/>" onclick="location.href='<c:url value="/events/${booking.event.id}"/>'" alt="Event picture"/>
                            </div>
                            <div class="booking-card-body">
                                <h3 class="uk-card-title"><c:out value="${booking.event.name}"/></h3>
                                <div class="booking-card-info">
                                    <div><img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="Date icon"/><span><c:out value="${booking.event.dateFormatted}"/></span></div>
                                    <div><img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="Time icon"/><span><c:out value="${booking.event.timeFormatted}"/></span></div>
                                    <div><img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="Location icon"/><span><c:out value="${booking.event.location.name}"/></span></div>
                                    <div><img class="icon" src="<c:url value="/resources/svg/tickets2.svg"/>" alt="Tickets icon"/><span class="booking_qty"><c:out value="${booking.qty}"/></span></div>
                            <div>


                                    <div class="container booking_button">
                                        <input class="cancel_button uk-button" type="button" value="Cancelar entradas" uk-toggle="target: #confirmation${i}"/>
                                        <div id="confirmation${i}" uk-modal>
                                            <div class="uk-modal-dialog">
                                                <button class="uk-modal-close-default" type="button" uk-close></button>
                                                <div class="uk-modal-header">
                                                    <h2 class="uk-modal-title">Cancelar reserva</h2>
                                                </div>
                                                <div class="uk-modal-body">
                                                    <c:url value="/bookings/cancel/${booking.event.id}" var="postPath"/>
                                                    <form:form novalidate="true" class="booking-card-body transparent" modelAttribute="bookForm" action="${postPath}" method="post" id="bookForm">
                                                    <div class="horizontal">
                                                        <span class="required">* </span>
                                                        <form:label path="qty">Cantidad de entradas a cancelar: </form:label>
                                                    </div>
                                                    <form:input class="uk-input" type="number" path="qty" min="1" required="true" id="qty"/>
                                                    <form:errors path="qty" cssClass="error-message" element="span"/>
                                                    <spring:message code="Min.bookForm.qty" var="qtySizeError"/>
                                                    <spring:message code="NotNull.bookForm.qty" var="qtyNullError"/>
                                                        <span class="formError"></span>

                                                        <hr/>
                                                    <button class="accept-button-modal uk-button" type="submit" name="submit">Cancelar entradas</button>
                                                    </form:form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
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
                No se encontraron eventos.
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>

<script type="text/javascript">
    (function() {
        var qty = document.getElementById('qty');
        var form = document.getElementById('bookForm');

        if (qty === null || form === null)
            return;

        var checkQtyValidity = function() {
            if (qty.validity.rangeOverflow) {
                qty.setCustomValidity('${qtySizeError}');
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
