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
    <%@ include file="appbar.jsp"%>
    <c:url value="/create-event" var="postPath"/>
    <div class="only-element">
        <form:form novalidate="true" modelAttribute="eventForm" action="${postPath}" method="post" id="eventForm" enctype="multipart/form-data">
            <div>
                <spring:message code="create.name" var="namePlaceholder"/>
                <form:input placeholder="* ${namePlaceholder}" class="uk-input" type="text" path="name" required="true"/>
                <form:errors path="name" cssClass="error-message" element="span"/>
                <spring:message code="NotEmpty.eventForm.name" var="nameEmptyError"/>
                <spring:message code="Size.eventForm.name" var="nameSizeError"/>
                <span class="formError"></span>
            </div>
            <div>
                <spring:message code="create.description" var="descPlaceholder"/>
                <form:input placeholder="${descPlaceholder}" class="uk-input" type="text" path="description"/>
                <form:errors path="description" cssClass="error-message" element="span"/>
                <spring:message code="Size.eventForm.description" var="descriptionSizeError"/>
                <span class="formError"></span>
            </div>
            <div>
                <spring:message code="create.location" var="locationPlaceholder"/>
                <form:select class="uk-select" htmlEscape="true" multiple="false" path="location" required="true">
                    <form:option value="" hidden="true" selected="true" label="* ${locationPlaceholder}"/>
                    <form:options items="${locations}"  itemValue="id" itemLabel="name"/>
                </form:select>
                <form:errors path="location" cssClass="error-message" element="span"/>
                <spring:message code="NotEmpty.eventForm.location" var="locationEmptyError"/>
                <span class="formError"></span>
            </div>
            <div>
                <spring:message code="create.type" var="typePlaceholder"/>
                <form:select id="type" class="uk-select" htmlEscape="true" multiple="false" path="type" required="true">
                    <form:option hidden="true" value="" selected="true" label="* ${typePlaceholder}"/>
                    <form:options items="${types}" itemValue="id" itemLabel="name"/>
                </form:select>
                <form:errors path="type" cssClass="error-message" element="span"/>
                <spring:message code="NotEmpty.eventForm.type" var="typeEmptyError"/>
                <span class="formError"></span>
            </div>
            <div>
                <span class="required">* </span>
                <form:label path="date"><spring:message code="create.date"/>: </form:label>
                <form:input class="uk-input" type="datetime-local" min="${currentDate}" path="date" required="true"/>
                <form:errors path="date" cssClass="error-message" element="span"/>
                <spring:message code="NotEmpty.eventForm.date" var="dateEmptyError"/>
                <spring:message code="Future.eventForm.date" var="dateMinError"/>
                <spring:message code="Pattern.eventForm.date" var="dateTypeError"/>
                <span class="formError"></span>
            </div>
            <div>
                <form:label path="tags" for="tags"><spring:message code="create.tags"/>: </form:label>
                <form:select id="type" class="uk-select" htmlEscape="true" multiple="true" path="tags" required="true">
                    <form:options items="${allTags}" itemValue="id" itemLabel="name"/>
                </form:select>
                <form:errors path="type" cssClass="error-message" element="span"/>
                <span class="formError"></span>
            </div>
            <div>
                <form:label path="image"><spring:message code="create.image"/>: </form:label>
                <form:input type="file" path="image" accept="image/png, image/jpeg"/>
                <form:errors path="image" cssClass="error-message" element="span"/>
                <span class="formError"></span>
            </div>
            <div class="container event">
                <spring:message code="create.button" var="buttonPlaceholder"/>
                <input class="filter_button" type="submit" value="${buttonPlaceholder}"/>
            </div>
        </form:form>
    </div>
</body>
</html>

<script type="text/javascript">
    (function() {
        var name = document.getElementById('name');
        var description = document.getElementById('description');
        // var maxCapacity = document.getElementById('maxCapacity');
        // var price = document.getElementById('price');
        var date = document.getElementById('date');
        var type = document.getElementById('type');
        var location = document.getElementById('location');
        var form = document.getElementById('eventForm');
        var uploadField = document.getElementById("image");

        uploadField.onchange = function() {
            if (this.files[0].size > 1048576) {
                UIkit.notification("MUY GRANDE", {timeout: 4000}, {status: 'danger'});
                // alert("MUY GRANDE");
                this.value = "";
            }
        };

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

        <%--var checkMaxCapacityValidity = function() {--%>
        <%--    if (maxCapacity.validity.typeMismatch) {--%>
        <%--        maxCapacity.setCustomValidity('${maxCapacityTypeError}');--%>
        <%--        updateMaxCapacityMessage()--%>
        <%--    } else if (maxCapacity.validity.rangeUnderflow) {--%>
        <%--        maxCapacity.setCustomValidity('${maxCapacitySizeError}');--%>
        <%--        updateMaxCapacityMessage()--%>
        <%--    } else if (maxCapacity.validity.valueMissing) {--%>
        <%--        maxCapacity.setCustomValidity('${maxCapacityNullError}');--%>
        <%--        updateMaxCapacityMessage()--%>
        <%--    } else {--%>
        <%--        maxCapacity.setCustomValidity('');--%>
        <%--    }--%>
        <%--};--%>

        var checkTypeValidity = function() {
            if (type.validity.valueMissing) {
                type.setCustomValidity('${typeEmptyError}');
                updateTypeMessage()
            } else {
                type.setCustomValidity('');
            }
        };

        var checkLocationValidity = function() {
            if (location.validity.valueMissing) {
                location.setCustomValidity('${locationEmptyError}');
                updateLocationMessage()
            } else {
                location.setCustomValidity('');
            }
        };

        var checkDescriptionValidity = function() {
            if (description.validity.tooLong) {
                description.setCustomValidity('${descriptionSizeError}');
                updateDescriptionMessage();
            } else {
                description.setCustomValidity('');
            }
        };

        <%--var checkPriceValidity = function() {--%>
        <%--    if (price.validity.typeMismatch) {--%>
        <%--        price.setCustomValidity('${priceTypeError}');--%>
        <%--        updatePriceMessage();--%>
        <%--    } else if (price.validity.valueMissing) {--%>
        <%--        price.setCustomValidity('${priceNullError}');--%>
        <%--        updatePriceMessage();--%>
        <%--    } else if (price.validity.rangeUnderflow) {--%>
        <%--        price.setCustomValidity('${priceMinError}');--%>
        <%--        updatePriceMessage();--%>
        <%--    } else {--%>
        <%--        price.setCustomValidity('');--%>
        <%--    }--%>
        <%--};--%>

        var checkDateValidity = function() {
            if (date.validity.valueMissing) {
                date.setCustomValidity('${dateEmptyError}');
                updateDateMessage();
            } else if (date.validity.typeMismatch) {
                date.setCustomValidity('${dateTypeError}');
                updateDateMessage();
            } else if (date.validity.rangeUnderflow) {
                date.setCustomValidity('${dateMinError}');
                updateDateMessage();
            } else {
                date.setCustomValidity('');
            }
        };

        var updateNameMessage = function() {
            form.getElementsByClassName('formError')[0].innerHTML = name.validationMessage;
        }

        var updateDescriptionMessage = function() {
            form.getElementsByClassName('formError')[1].innerHTML = description.validationMessage;
        }

        var updateLocationMessage = function() {
            form.getElementsByClassName('formError')[2].innerHTML = location.validationMessage;
        }

        var updateTypeMessage = function() {
            form.getElementsByClassName('formError')[3].innerHTML = type.validationMessage;
        }

        // var updateMaxCapacityMessage = function() {
        //     form.getElementsByClassName('formError')[4].innerHTML = maxCapacity.validationMessage;
        // }
        //
        // var updatePriceMessage = function() {
        //     form.getElementsByClassName('formError')[5].innerHTML = price.validationMessage;
        // }

        var updateDateMessage = function() {
            form.getElementsByClassName('formError')[6].innerHTML = date.validationMessage;
        }

        name.addEventListener('change', checkNameValidity, false);
        name.addEventListener('keyup', checkNameValidity, false);
        // maxCapacity.addEventListener('change', checkMaxCapacityValidity, false);
        // maxCapacity.addEventListener('keyup', checkMaxCapacityValidity, false);
        description.addEventListener('change', checkDescriptionValidity, false);
        description.addEventListener('keyup', checkDescriptionValidity, false);
        location.addEventListener('change', checkLocationValidity, false);
        location.addEventListener('keyup', checkLocationValidity, false);
        type.addEventListener('change', checkTypeValidity, false);
        type.addEventListener('keyup', checkTypeValidity, false);
        // price.addEventListener('change', checkPriceValidity, false);
        // price.addEventListener('keyup', checkPriceValidity, false);
        date.addEventListener('change', checkDateValidity, false);
        date.addEventListener('keyup', checkDateValidity, false);


        form.addEventListener('submit', function(event) {
            if (form.classList) form.classList.add('submitted');
            checkNameValidity()
            // checkMaxCapacityValidity()
            checkDescriptionValidity();
            checkLocationValidity();
            checkTypeValidity();
            // checkPriceValidity();
            checkDateValidity();
            if (!this.checkValidity()) {
                event.preventDefault();
                updateNameMessage();
                // updateMaxCapacityMessage();
                updateDescriptionMessage();
                updateLocationMessage();
                updateTypeMessage();
                // updatePriceMessage();
                updateDateMessage();
            }
        }, false);
    }());
</script>
