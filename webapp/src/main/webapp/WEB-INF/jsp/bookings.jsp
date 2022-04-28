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
                        <c:if test="${booking.qty > 0 && !booking.event.deleted}">
                        <div class="horizontal booking-card card uk-card uk-card-default" >
                            <div class="horizontal">
                                <div class="fill">
                                    <img src="<c:url value="/image/${booking.event.imageId}"/>" onclick="location.href='<c:url value="/events/${booking.event.id}"/>'" alt="Event image"/>
                                </div>
                                <div class="transparent">
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
                                                            <form:form novalidate="true" class="transparent" modelAttribute="bookForm" action="${postPath}" method="post" id="bookForm${i}">
                                                                <div class="horizontal center">
                                                                    <form:label class="sep-right" path="qty">*Cantidad de entradas a cancelar: </form:label>

                                                                <c:set var="qtyTickets" scope="session" value="${booking.qty}"/>
                                                                <form:input class="uk-input" type="number" path="qty" min="1" max="${booking.qty}" required="true" id="qty"/>
<%--                                                                <form:errors path="qty" cssClass="error-message" element="span"/>--%>
                                                                <spring:message code="Min.bookForm.qty" var="minQtySizeError"/>
                                                                <spring:message code="Max.bookForm.qtyStr" var="maxQtySizeError"/>
                                                                <spring:message code="NotNull.bookForm.qty" var="qtyNullError"/>
                                                                <span class="formError"></span>
                                                                </div>
                                                                <form:input class="hidden" type="number" path="page" value="${page}"/>
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
                    <h5>No has hecho reservas todavía.</h5>
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
    (function() {
        var qty = document.getElementById('qty');
        var form = document.getElementsByClassName('transparent');

        if (qty === null || form === null)
            return;

        if ("${errorVar}" !== '')
            UIkit.notification("${maxQtySizeError} ${errorVar}", {status: 'danger'}, {pos: 'bottom-center'})

        var checkQtyValidity = function() {
            var qtyTickets = document.getElementsByClassName('uk-open').item(0).getElementsByClassName('uk-input').item(0).getAttribute('max');
            qty = document.getElementsByClassName('uk-open').item(0).getElementsByClassName('uk-input').item(0);
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
        };

        var updateQtyMessage = function() {
            qty = document.getElementsByClassName('uk-open').item(0).getElementsByClassName('uk-input').item(0);
            form = document.getElementsByClassName('uk-open').item(0).getElementsByClassName('transparent').item(0);
            form.getElementsByClassName('formError')[0].innerHTML = qty.validationMessage;
        }

        qty.addEventListener('change', checkQtyValidity, false);
        qty.addEventListener('keyup', checkQtyValidity, false);

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
