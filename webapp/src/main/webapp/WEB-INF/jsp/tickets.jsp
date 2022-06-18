<%--<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>--%>
<%--<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>--%>
<%--<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>--%>

<%--<html>--%>
<%--<head>--%>
<%--    <%@ include file="include.jsp"%>--%>
<%--    <title>BotPass</title>--%>
<%--</head>--%>
<%--<body>--%>
<%--<div>--%>
<%--    <%@ include file="appbar.jsp"%>--%>
<%--    <c:url value="/events/${eventId}/add-tickets" var="postPath"/>--%>
<%--    <div class="only-element">--%>
<%--        <form:form novalidate="true" modelAttribute="ticketsForm" action="${postPath}" method="post" id="ticketsForm" enctype="multipart/form-data">--%>
<%--            <form:input type="number" class="hidden" value="1" path="ticketQty"/>--%>
<%--            <c:forEach var="i" begin="${0}" end="${4}" step="${1}">--%>
<%--                <c:choose>--%>
<%--                    <c:when test="${i == 0}">--%>
<%--                        <div id="tickets.${i}">--%>
<%--                    </c:when>--%>
<%--                    <c:otherwise>--%>
<%--                        <div class="hidden" id="tickets.${i}">--%>
<%--                    </c:otherwise>--%>
<%--                </c:choose>--%>
<%--                    <div>--%>
<%--                        <spring:message code="tickets.ticketName" var="ticketName"/>--%>
<%--                        <form:input placeholder="${ticketName}" class="uk-input" type="text" path="tickets[${i}].ticketName" required="true"/>--%>
<%--&lt;%&ndash;                        <form:errors path="tickets[${i}].ticketName" cssClass="error-message" element="span"/>&ndash;%&gt;--%>
<%--                    </div>--%>
<%--                    <div>--%>
<%--                        <form:label path="tickets[0].price"><spring:message code="tickets.ticketPrice"/>: </form:label>--%>
<%--                        <form:input class="uk-input" type="number" path="tickets[${i}].price" min="0.00" step="0.01" value="0.00" required="true"/>--%>
<%--&lt;%&ndash;                        <form:errors path="tickets[${i}].price" cssClass="error-message" element="span"/>&ndash;%&gt;--%>
<%--                    </div>--%>
<%--                    <div>--%>
<%--                        <form:label path="tickets[0].qty"><spring:message code="tickets.ticketQty"/>: </form:label>--%>
<%--                        <form:input class="uk-input" type="number" path="tickets[${i}].qty" min="0" step="1" value="0" required="true"/>--%>
<%--&lt;%&ndash;                        <form:errors path="tickets[${i}].qty" cssClass="error-message" element="span"/>&ndash;%&gt;--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </c:forEach>--%>
<%--            <form:errors path="tickets[0].ticketName" cssClass="error-message" element="span"/>--%>
<%--            <form:errors path="tickets[0].price" cssClass="error-message" element="span"/>--%>
<%--            <form:errors path="tickets[0].qty" cssClass="error-message" element="span"/>--%>
<%--            <spring:message code="tickets.maxTicketsReached" var="maxTicketsReached"/>--%>
<%--            <button type="button" onclick="addTicket()">Agregar tickets</button>--%>
<%--            </div>--%>
<%--            <div class="container event">--%>
<%--                <spring:message code="create.button" var="create"/>--%>
<%--                <input class="filter_button" type="submit" value="${create}"/>--%>
<%--            </div>--%>
<%--        </form:form>--%>
<%--    </div>--%>
<%--</body>--%>
<%--</html>--%>

<%--<script type="text/javascript">--%>
<%--    var i = 1;--%>
<%--    function addTicket() {--%>
<%--        if (i === 5)--%>
<%--            UIkit.notification("${maxTicketsReached}", {status: 'warning'}, {timeout: 4000}, {pos: 'bottom-center'})--%>
<%--        else {--%>
<%--            var ticketForm = document.getElementById('tickets.' + i);--%>
<%--            ticketForm.style.display = 'block';--%>
<%--            var ticketQty = document.getElementById('ticketQty');--%>
<%--            ticketQty.value = ++i;--%>
<%--        }--%>
<%--    }--%>

<%--    (function() {--%>
<%--        var qty = document.getElementById('qty');--%>
<%--        var form = document.getElementById('ticketsForm');--%>

<%--        if (qty === null || form === null)--%>
<%--            return;--%>

<%--        if ("${errorVar}" !== '')--%>
<%--            UIkit.notification("${maxQtySizeError} ${errorVar}", {status: 'danger'}, {pos: 'bottom-center'})--%>

<%--        var checkQtyValidity = function() {--%>
<%--            var qtyTickets = document.getElementsByClassName('uk-open').item(0).getElementsByClassName('uk-input').item(0).getAttribute('max');--%>
<%--            qty = document.getElementsByClassName('uk-open').item(0).getElementsByClassName('uk-input').item(0);--%>
<%--            if (qty.validity.rangeUnderflow) {--%>
<%--                qty.setCustomValidity('${minQtySizeError}');--%>
<%--                updateQtyMessage();--%>
<%--            } else if (qty.validity.rangeOverflow) {--%>
<%--                qty.setCustomValidity('${maxQtySizeError} ' + qtyTickets + '.');--%>
<%--                updateQtyMessage();--%>
<%--            } else if (qty.validity.valueMissing) {--%>
<%--                qty.setCustomValidity('${qtyNullError}');--%>
<%--                updateQtyMessage();--%>
<%--            } else {--%>
<%--                qty.setCustomValidity('');--%>
<%--            }--%>
<%--        };--%>

<%--        var updateQtyMessage = function() {--%>
<%--            qty = document.getElementsByClassName('uk-open').item(0).getElementsByClassName('uk-input').item(0);--%>
<%--            form = document.getElementsByClassName('uk-open').item(0).getElementsByClassName('transparent').item(0);--%>
<%--            form.getElementsByClassName('formError')[0].innerHTML = qty.validationMessage;--%>
<%--        }--%>

<%--        qty.addEventListener('change', checkQtyValidity, false);--%>
<%--        qty.addEventListener('keyup', checkQtyValidity, false);--%>

<%--        for (let i = 0; i < form.length; i++) {--%>
<%--            form.item(i).addEventListener('submit', function (event) {--%>
<%--                var form = document.getElementsByClassName('uk-open').item(0).getElementsByClassName('transparent').item(0);--%>
<%--                if (form.classList) form.classList.add('submitted');--%>
<%--                checkQtyValidity();--%>
<%--                if (!this.checkValidity()) {--%>
<%--                    event.preventDefault();--%>
<%--                    updateQtyMessage();--%>
<%--                }--%>
<%--            }, false);--%>
<%--        }--%>
<%--    }());--%>
<%--</script>--%>