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
        <form:form class="horizontal" novalidate="true" modelAttribute="eventForm" action="${postPath}" method="post" id="eventForm" enctype="multipart/form-data">
            <div class="interline">
            <h4 class="title2"><spring:message code="create.event"/></h4>
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
            </div>
            <div class="interline">
            <div>
                <form:label path="tags" for="tags"><spring:message code="create.tags"/>: </form:label>
                <form:select id="tags" class="uk-select" htmlEscape="true" multiple="true" path="tags">
                    <form:options items="${allTags}" itemValue="id" itemLabel="name"/>
                </form:select>
                <form:errors path="tags" cssClass="error-message" element="span"/>
                <span class="formError"></span>
            </div>
            <div>
                <form:label path="image"><spring:message code="create.image"/>: </form:label>
                <form:input type="file" path="image" accept="image/png, image/jpeg"/>
                <form:errors path="image" cssClass="error-message" element="span"/>
                <span class="formError"></span>
            </div>
            <div class="horizontal align-center">
                <div class="horizontal center">
                    <form:checkbox path="hasMinAge" id="hasMinAge" onclick="hasMinimumAge()"/>
                    <form:label path="hasMinAge" class="sep-left"><spring:message code="create.minAge"/></form:label>
                </div>

                <form:select class="uk-select input_select sep-left-l" htmlEscape="true" multiple="false" path="minAge" id="minAgeInput" disabled="true">
                    <form:option value="" hidden="true" selected="true" label=""/>
                    <c:forEach var="age" step="1" begin="14" end="27">
                        <form:option value="${age}" label="${age}"/>
                    </c:forEach>
                </form:select>
                <form:errors path="minAge" cssClass="error-message" element="span"/>
                <spring:message code="Range.eventForm.minAge" var="rangeAgeMinError"/>
                <spring:message code="NotNull.eventForm.minAge" var="minAgeNullError"/>
                <span class="sep-left formError"></span>
            </div>
            <div class="container event">
                <spring:message code="create.button" var="buttonPlaceholder"/>
                <input class="filter_button" type="submit" value="${buttonPlaceholder}"/>
            </div>
            </div>
        </form:form>
    </div>
</body>
</html>

<script type="text/javascript">
    function hasMinimumAge() {
        var checkBox = document.getElementById("hasMinAge");
        var minAgeInput = document.getElementById("minAgeInput")

        minAgeInput.disabled = !checkBox.checked;
    }

    (function() {
        var name = document.getElementById('name');
        var description = document.getElementById('description');
        var date = document.getElementById('date');
        var age = document.getElementById('minAgeInput');
        var type = document.getElementById('type');
        var location = document.getElementById('location');
        var form = document.getElementById('eventForm');
        var uploadField = document.getElementById("image");

        uploadField.onchange = function() {
            // TODO: HACER UN SPRING MESSAGE!!!!
            if (this.files[0].size > 1048576) {
                UIkit.notification("MUY GRANDE", {timeout: 4000}, {status: 'danger'});
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

        var checkAgeValidity = function() {
            if (age.validity.valueMissing) {
                age.setCustomValidity('${minAgeNullError}');
                updateAgeMessage()
            } else if (parseInt(age.value) < 14 || parseInt(age.value) > 27) {
                age.setCustomValidity('${rangeAgeMinError}');
                updateAgeMessage();
            } else {
                age.setCustomValidity('');
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

        var updateDateMessage = function() {
            form.getElementsByClassName('formError')[4].innerHTML = date.validationMessage;
        }

        var updateAgeMessage = function() {
            form.getElementsByClassName('formError')[7].innerHTML = age.validationMessage;
        }

        name.addEventListener('change', checkNameValidity, false);
        name.addEventListener('keyup', checkNameValidity, false);
        age.addEventListener('change', checkAgeValidity, false);
        age.addEventListener('keyup', checkAgeValidity, false);
        description.addEventListener('change', checkDescriptionValidity, false);
        description.addEventListener('keyup', checkDescriptionValidity, false);
        location.addEventListener('change', checkLocationValidity, false);
        location.addEventListener('keyup', checkLocationValidity, false);
        type.addEventListener('change', checkTypeValidity, false);
        type.addEventListener('keyup', checkTypeValidity, false);
        date.addEventListener('change', checkDateValidity, false);
        date.addEventListener('keyup', checkDateValidity, false);


        form.addEventListener('submit', function(event) {
            if (form.classList) form.classList.add('submitted');
            checkNameValidity()
            checkDescriptionValidity();
            checkLocationValidity();
            checkTypeValidity();
            checkDateValidity();
            checkAgeValidity();
            if (!this.checkValidity()) {
                event.preventDefault();
                updateNameMessage();
                updateDescriptionMessage();
                updateLocationMessage();
                updateTypeMessage();
                updateDateMessage();
                updateAgeMessage();
            }
        }, false);
    }());
</script>
