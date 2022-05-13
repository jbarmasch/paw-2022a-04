<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
    <c:import url="appbar.jsp"/>
    <div class="vertical center">
        <c:url value="/bookings/cancel/${eventBooking.event.id}" var="postPath"/>
        <form:form novalidate="true" class="transparent" modelAttribute="bookForm" action="${postPath}" method="post" id="bookForm">
        <div>
            <h2 class="subtitle"><spring:message code="booking.cancel"/></h2>
        </div>

                <table class="tickets tickets-table">
                    <tr>
                        <th><spring:message code="tickets.ticketName"/></th>
                        <th><spring:message code="tickets.ticketPrice"/></th>
                        <th><spring:message code="tickets.ticketQty"/></th>
                        <th><spring:message code="booking.cancel"/></th>
                    </tr>
                    <c:set var="j" scope="session" value="0"/>
                    <c:forEach var="booking" items="${eventBooking.bookings}">
                        <tr>
                            <td>
                                <span><c:out value="${booking.ticket.ticketName}"/></span>
                            </td>
                            <td class="table-number">
                                <c:choose>
                                    <c:when test="${booking.ticket.price > 0}">
                                        <span>$<c:out value="${booking.ticket.price}"/></span>
                                    </c:when>
                                    <c:otherwise>
                                        <spring:message code="event.free"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="table-number">
                                <span><c:out value="${booking.qty}"/></span>
                            </td>
                            <td class="table-number">
                                <form:input path="bookings[${j}].qty" id="qty${j}" type="number" min="0" max="${booking.qty}" step="1" value="0"/>
                                <span class="formError"></span>

                                <form:input class="hidden" type="number" path="bookings[${j}].ticketId" value="${booking.ticket.id}"/>
                                <form:input class="hidden" type="text" value="${booking.ticket.ticketName}" path="bookings[${j}].ticketName"/>
                                <form:input class="hidden" type="number" value="${booking.ticket.price}" path="bookings[${j}].price"/>

                                <form:errors path="bookings[${j}].qty" cssClass="error-message" element="span"/>
                                <spring:message code="Min.bookForm.qty" var="qtyMinSizeError"/>
                                <spring:message code="Max.bookForm.qtyStr" var="qtyMaxSizeError"/>
                                <spring:message code="NotNull.bookForm.qty" var="qtyNullError"/>
                            </td>
                            <form:errors path="bookings" cssClass="error-message" element="span"/>
                        </tr>
                        <c:set var="j" scope="session" value="${j + 1}"/>
                    </c:forEach>
                    <spring:message code="NotNull.bookForm.allQty" var="allQtyNullError"/>
                    <input type="text" class="hidden" id="errorQty"/>
                </table>
            <spring:message code="booking.cancel" var="cancel"/>
            <input class="cancel_button sep-top-xl" type="submit" value="${cancel}"/>
            <form:input class="hidden" type="number" path="page" value="${1}"/>
            </form:form>
    </div>
</body>
</html>

<script type="text/javascript">
    (function() {
        var errorQty = document.getElementById('errorQty');
        var form = document.getElementById('bookForm');

        if (form === null)
            return;

        var checkQtyValidityWithNumber = function(i) {
            var qty = document.getElementById('qty' + i);
            if (qty.validity.rangeUnderflow) {
                qty.setCustomValidity('${qtyMinSizeError}');
                updateQtyMessage(i);
            } else if (qty.validity.rangeOverflow) {
                qty.setCustomValidity('${qtyMaxSizeError} ' + qty.max + '.');
                updateQtyMessage(i);
            } else {
                qty.setCustomValidity('');
            }
        };

        var checkQtyValidityAll = function() {
            var aux = 0;
            for (var j = 0; j < parseInt("${j}"); j++) {
                var qty = document.getElementById('qty' + j);
                if (qty.value === '' || parseInt(qty.value) === 0)
                    aux++;
            }
            if (aux === parseInt("${j}")) {
                errorQty.setCustomValidity('${allQtyNullError}');
                UIkit.notification("${allQtyNullError}", {timeout: 4000}, {status: 'danger'});
            } else {
                errorQty.setCustomValidity('');
            }
        };

        var checkQtyValidity = function(i) {
            i = i.currentTarget.i;
            var qty = document.getElementById('qty' + i);
            if (qty.validity.rangeUnderflow) {
                qty.setCustomValidity('${qtyMinSizeError}');
                updateQtyMessage(i);
            } else if (qty.validity.rangeOverflow) {
                qty.setCustomValidity('${qtyMaxSizeError} ' + qty.max + '.');
                updateQtyMessage(i);
            } else {
                qty.setCustomValidity('');
            }
        };

        var updateQtyMessage = function(i) {
            var qty = document.getElementById('qty' + i);
            form.getElementsByClassName('formError')[i].innerHTML = qty.validationMessage;
        }

        for (var j = 0; j < parseInt("${j}"); j++) {
            var qty = document.getElementById('qty' + j);
            qty.addEventListener('change', checkQtyValidity, false);
            qty.i = j;
            qty.addEventListener('keyup', checkQtyValidity, false);
            qty.i = j;
        }

        form.addEventListener('submit', function(event) {
            if (form.classList) form.classList.add('submitted');
            for (var j = 0; j < parseInt("${j}"); j++)
                checkQtyValidityWithNumber(j);
            checkQtyValidityAll();
            if (!this.checkValidity()) {
                event.preventDefault();
            }
        }, false);
    }());
</script>
