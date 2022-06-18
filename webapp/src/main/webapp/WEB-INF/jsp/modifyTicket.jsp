<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
    <div>
        <%@ include file="appbar.jsp"%>
        <c:url value="/events/${eventId}/modify-ticket/${ticketId}" var="postPath"/>
        <div class="only-element">
            <form:form novalidate="true" modelAttribute="ticketForm" action="${postPath}" method="post" id="ticketForm">
                <div>
                    <spring:message code="tickets.ticketName" var="ticketNameMessage"/>
                    <form:input placeholder="${ticketNameMessage}" class="uk-input" type="text" path="ticketName" required="true" value="${ticket.ticketName}"/>
                    <form:errors path="ticketName" cssClass="error-message" element="span"/>
                    <spring:message code="NotEmpty.eventForm.name" var="nameEmptyError"/>
                    <spring:message code="Size.eventForm.name" var="nameSizeError"/>
                    <span class="formError"></span>
                </div>
                <div>
                    <form:label path="price"><spring:message code="tickets.ticketPrice"/>: </form:label>
                    <form:input class="uk-input" type="number" path="price" min="0.00" step="0.01" required="true" value="${ticket.price}"/>
                    <form:errors path="price" cssClass="error-message" element="span"/>
                    <spring:message code="NotNull.eventForm.price" var="priceNullError"/>
                    <spring:message code="typeMismatch.eventForm.price" var="priceTypeError"/>
                    <spring:message code="DecimalMin.eventForm.price" var="priceMinError"/>
                    <span class="formError"></span>
                </div>
                <div>
                    <form:label path="qty"><spring:message code="tickets.ticketQty"/>: </form:label>
                    <form:input class="uk-input" type="number" path="qty" min="${ticket.booked > 0 ? ticket.booked : 1}" step="1" value="${ticket.qty}" required="true"/>
                    <form:errors path="qty" cssClass="error-message" element="span"/>
                    <spring:message code="NotNull.bookForm.qty" var="qtyNullError"/>
                    <%--<spring:message code="Max.bookForm.qty" var="maxQtySizeError"/>--%>
                    <spring:message code="Min.bookForm.qtyAnotherStr" var="minQtySizeError"/>
                    <span class="formError"></span>
                </div>
                <div>
                    <form:label path="starting"><spring:message code="tickets.starting"/>: </form:label>
                    <form:input class="uk-input" type="datetime-local" max="${event.date}" path="starting" value="${ticket.starting}"/>
                    <form:errors path="starting" cssClass="error-message" element="span"/>
                    <spring:message code="NotEmpty.eventForm.date" var="dateEmptyError"/>
                    <spring:message code="Future.eventForm.date" var="dateMinError"/>
                    <spring:message code="Pattern.eventForm.date" var="dateTypeError"/>
                    <span class="formError"></span>
                </div>
                <div>
                    <form:label path="until"><spring:message code="tickets.until"/>: </form:label>
                    <form:input class="uk-input" type="datetime-local" min="${currentDate}" max="${event.date}" path="until" value="${ticket.until}"/>
                    <form:errors path="until" cssClass="error-message" element="span"/>
                    <spring:message code="NotEmpty.eventForm.date" var="dateEmptyError"/>
                    <spring:message code="Future.eventForm.date" var="dateMinError"/>
                    <spring:message code="Pattern.eventForm.date" var="dateTypeError"/>
                    <span class="formError"></span>
                </div>
                <div class="horizontal align-center">
                    <spring:message code="tickets.maxPerUser"/>:
                    <form:select class="uk-select input_select sep-left-l" htmlEscape="true" multiple="false" path="maxPerUser" id="maxPerUserInput">
                        <c:forEach var="val" step="1" begin="1" end="10">
                            <c:choose>
                                <c:when test="${ticket.maxPerUser == val}">
                                    <form:option value="${val}" selected="selected" />
                                </c:when>
                                <c:otherwise>
                                    <form:option value="${val}"/>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </form:select>
                    <form:errors path="maxPerUser" cssClass="error-message" element="span"/>
                    <spring:message code="Range.eventForm.minAge" var="rangeAgeMinError"/>
                    <spring:message code="NotNull.eventForm.minAge" var="minAgeNullError"/>
                    <span class="sep-left formError"></span>
                </div>
                <spring:message code="tickets.maxTicketsReached" var="maxTicketsReached"/>
                <div class="container event">
                    <spring:message code="modify.update" var="modify"/>
                    <input class="filter_button upper" type="submit" value="${modify}"/>
                </div>
            </form:form>
        </div>
</body>
</html>

<script type="text/javascript">
    (function() {
        var form = document.getElementById('ticketForm');
        var qty = document.getElementById('qty');
        var price = document.getElementById('price');
        var name = document.getElementById('ticketName');

        if (form === null)
            return;

        var checkNameValidity = function() {
            if (name.validity.tooLong) {
                name.setCustomValidity('${nameSizeError}');
                updateNameMessage()
            } else if (name.validity.valueMissing) {
                name.setCustomValidity('${nameEmptyError}');
                updateNameMessage()
            } else {
                name.setCustomValidity('');
            }
        };

        var checkQtyValidity = function() {
            if (qty.validity.rangeUnderflow) {
                qty.setCustomValidity('${minQtySizeError} ' + qty.min);
                updateQtyMessage();
            } else if (qty.validity.valueMissing) {
                qty.setCustomValidity('${qtyNullError}');
                updateQtyMessage();
            } else {
                qty.setCustomValidity('');
            }
        };

        var checkPriceValidity = function() {
            if (price.validity.typeMismatch) {
                price.setCustomValidity('${priceTypeError}');
                updatePriceMessage();
            } else if (price.validity.valueMissing) {
                price.setCustomValidity('${priceNullError}');
                updatePriceMessage();
            } else if (price.validity.rangeUnderflow) {
                price.setCustomValidity('${priceMinError}');
                updatePriceMessage();
            } else {
                price.setCustomValidity('');
            }
        };

        var updateNameMessage = function() {
            form.getElementsByClassName('formError')[0].innerHTML = name.validationMessage;
        }

        var updatePriceMessage = function() {
            form.getElementsByClassName('formError')[1].innerHTML = price.validationMessage;
        }

        var updateQtyMessage = function() {
            form.getElementsByClassName('formError')[2].innerHTML = qty.validationMessage;
        }

        qty.addEventListener('change', checkQtyValidity, false);
        qty.addEventListener('keyup', checkQtyValidity, false);
        price.addEventListener('change', checkPriceValidity, false);
        price.addEventListener('keyup', checkPriceValidity, false);
        name.addEventListener('change', checkNameValidity, false);
        name.addEventListener('keyup', checkNameValidity, false);

        form.addEventListener('submit', function (event) {
            if (form.classList) form.classList.add('submitted');
            checkNameValidity();
            checkPriceValidity();
            checkQtyValidity();
            if (!this.checkValidity()) {
                event.preventDefault();
                updateNameMessage();
                updatePriceMessage();
                updateQtyMessage();
            }
        }, false);
    }());
</script>
