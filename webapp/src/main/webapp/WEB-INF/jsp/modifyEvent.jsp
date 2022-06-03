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
    <c:url value="/events/${eventId}/modify" var="postPath"/>
    <div class="only-element">
        <form:form class="horizontal" novalidate="true" modelAttribute="eventForm" action="${postPath}" method="post" id="eventForm" enctype="multipart/form-data">
            <div class="interline">
            <h4 class="title2"><spring:message code="modify.event"/></h4>
            <div>
                <span class="required">* </span>
                <form:label path="name"><spring:message code="create.name"/>: </form:label>
                <form:input class="uk-input" type="text" path="name" required="true" value="${event.name}"/>
                <form:errors path="name" cssClass="error-message" element="span"/>
                <spring:message code="NotEmpty.eventForm.name" var="nameEmptyError"/>
                <spring:message code="Size.eventForm.name" var="nameSizeError"/>
                <span class="formError"></span>
            </div>
            <div>
                <form:label path="description"><spring:message code="create.description"/>: </form:label>
                <form:input class="uk-input" type="text" path="description" value="${event.description}"/>
                <form:errors path="description" cssClass="error-message" element="span"/>
                <spring:message code="Size.eventForm.description" var="descriptionSizeError"/>
                <span class="formError"></span>
            </div>
            <div>
                <span class="required">* </span>
                <form:label path="location"><spring:message code="create.location"/>:: </form:label>
                <form:select class="uk-select" htmlEscape="true" multiple="false" path="location" required="true">
                    <c:forEach var="item" items="${locations}">
                        <c:choose>
                            <c:when test="${item.id == event.location.id}">
                                <form:option value="${item.id}" selected="selected" >${item.name}</form:option>
                            </c:when>
                            <c:otherwise>
                                <form:option value="${item.id}">${item.name}</form:option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </form:select>
                <form:errors path="location" cssClass="error-message" element="span"/>
                <spring:message code="NotEmpty.eventForm.location" var="locationEmptyError"/>
                <span class="formError"></span>
            </div>
            <div>
                <span class="required">* </span>
                <form:label path="type" for="type"><spring:message code="create.type"/>:: </form:label>
                <form:select id="type" class="uk-select" htmlEscape="true" multiple="false" path="type" required="true">
                    <c:forEach var="item" items="${types}">
                        <c:choose>
                            <c:when test="${item.id == event.type.id}">
                                <form:option value="${item.id}" selected="selected" >${item.name}</form:option>
                            </c:when>
                            <c:otherwise>
                                <form:option value="${item.id}">${item.name}</form:option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </form:select>
                <form:errors path="type" cssClass="error-message" element="span"/>
                <spring:message code="NotEmpty.eventForm.type" var="typeEmptyError"/>
                <span class="formError"></span>
            </div>
            <div>
                <span class="required">* </span>
                <form:label path="date"><spring:message code="create.date"/>:: </form:label>
                <form:input class="uk-input" type="datetime-local" min="${currentDate}" path="date" required="true" value="${event.date}"/>
                <form:errors path="date" cssClass="error-message" element="span"/>
                <spring:message code="NotEmpty.eventForm.date" var="dateEmptyError"/>
                <spring:message code="Future.eventForm.date" var="dateMinError"/>
                <spring:message code="Pattern.eventForm.date" var="dateTypeError"/>
                <span class="formError"></span>
            </div>
            </div>
            <div class="interline">
            <div>
                <form:label path="tags" for="tags"><spring:message code="create.tags"/>:: </form:label>
                <form:select id="type" class="uk-select" htmlEscape="true" multiple="true" path="tags" required="true">
                    <c:forEach var="tag" items="${allTags}">
                        <c:set var="selectedTag" value="${false}"/>
                        <c:forEach var="eventTag" items="${event.tags}">
                            <c:choose>
                                <c:when test="${tag.id == eventTag.id}">
                                    <c:set var="selectedTag" value="${true}"/>
                                </c:when>
                            </c:choose>
                        </c:forEach>
                        <c:choose>
                            <c:when test="${selectedTag}">
                                <form:option value="${tag.id}" selected="selected" >${tag.name}</form:option>
                            </c:when>
                            <c:otherwise>
                                <form:option value="${tag.id}">${tag.name}</form:option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </form:select>
                <form:errors path="type" cssClass="error-message" element="span"/>
                <span class="formError"></span>
            </div>
            <div>
                <form:label path="image"><spring:message code="create.image"/>:: </form:label>
                <form:input type="file" path="image" accept="image/png, image/jpeg"/>
                <form:errors path="image" cssClass="error-message" element="span"/>
                <span class="formError"></span>
            </div>

            <div class="horizontal align-center">
                <div class="horizontal center">
                    <c:choose>
                        <c:when test="${event.minAge != null}">
                            <c:set value="true" var="checked"/>
                            <form:checkbox path="hasMinAge" id="hasMinAge" checked="checked"/>
                        </c:when>
                        <c:otherwise>
                            <c:set value="false" var="checked"/>
                            <form:checkbox path="hasMinAge" id="hasMinAge"/>
                        </c:otherwise>
                    </c:choose>

                    <form:label path="hasMinAge" class="sep-left"><spring:message code="create.minAge"/></form:label>
                </div>

                <form:select class="uk-select input_select sep-left-l" htmlEscape="true" multiple="false" path="minAge" id="minAgeInput">
                    <c:forEach var="item" step="1" begin="14" end="27">
                        <c:choose>
                            <c:when test="${item == event.minAge}">
                                <form:option value="${item}" selected="selected" >${item}</form:option>
                            </c:when>
                            <c:otherwise>
                                <form:option value="${item}">${item}</form:option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </form:select>
                <form:errors path="minAge" cssClass="error-message" element="span"/>
                <spring:message code="Range.eventForm.minAge" var="rangeAgeMinError"/>
<%--                <spring:message code="Max.eventForm.minAge" var="minAgeMaxError"/>--%>
                <spring:message code="NotNull.eventForm.minAge" var="minAgeNullError"/>
                <span class="formError"></span>
            </div>

            <div class="container event">
                <spring:message code="modify.update" var="modifyMessage"/>
                <input class="filter_button" type="submit" value="${modifyMessage}"/>
            </div>
            </div>
        </form:form>
    </div>
</body>
</html>

<script type="text/javascript">
    function hasMinimumAge() {
        var checkBox = document.getElementById("hasMinAge");
        var minAgeInput = document.getElementById("minAgeInput");

        if (checkBox == null || minAgeInput == null)
            return;

        minAgeInput.disabled = !checkBox.checked;
    }

    (function() {
        hasMinimumAge();

        var name = document.getElementById('name');
        var description = document.getElementById('description');
        var date = document.getElementById('date');
        var age = document.getElementById('minAgeInput');
        var type = document.getElementById('type');
        var location = document.getElementById('location');
        var form = document.getElementById('eventForm');
        var uploadField = document.getElementById("image");

        uploadField.onchange = function() {
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