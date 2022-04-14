<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <div class="container event">
        <div class="container">
            <div>
                <img class="event_img" src="https://media.istockphoto.com/photos/dancing-friends-picture-id501387734?k=20&m=501387734&s=612x612&w=0&h=1mli5b7kpDg428fFZfsDPJ9dyVHsWsGK-EVYZUGWHpI=" alt="Party img"/>
            </div>
            <div class="event_info">
                <h3><c:out value="${event.name}"/></h3>
                <p><c:out value="${event.description}"/></p>
                <div class="container">
                    <img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="Location emoji"/><span><c:out value="${event.location}"/></span>
                </div>
                    <div class="container">
                <img class="icon" src="<c:url value="/resources/svg/price-tag.svg"/>" alt="Price"/><span>$<c:out value="${event.price}"/></span>
                </div>
                <div class="container">
                    <img class="icon" src="<c:url value="/resources/svg/date.svg"/>"/><span><c:out value="${event.dateFormatted}"/></span>
                </div>
                <div class="container">
                    <img class="icon" src="<c:url value="/resources/svg/time.svg"/>"/><span><c:out value="${event.timeFormatted}"/></span>
                </div>
                <div class="container">
                    <img class="icon" src="<c:url value="/resources/svg/party.svg"/>"/><span><c:out value="${event.type}"/></span>
                </div>
            </div>
        </div>

        <c:url value="/event/${event.id}" var="postPath"/>
        <form:form novalidate="true" modelAttribute="bookForm" action="${postPath}" method="post" id="bookForm">

            <form:label path="name">Nombre: </form:label>
            <form:input class="uk-input" type="text" path="name" />
            <form:errors path="name" cssClass="error-message" element="span"/>

            <form:label path="surname">Apellido: </form:label>
            <form:input class="uk-input" type="text" path="surname" />
            <form:errors path="surname" cssClass="error-message" element="span"/>

            <form:label path="dni">DNI: </form:label>
            <form:input class="uk-input dni" type="number" path="dni" min="0"/>
            <form:errors path="dni" cssClass="error-message" element="span"/>

            <form:label path="mail">Mail: </form:label>
            <form:input class="uk-input" type="email" path="mail" required="true"/>
            <form:errors path="mail" cssClass="error-message" element="span"/>
            <spring:message code="NotEmpty.bookForm.mail" var="mailEmptyError"/>
            <spring:message code="Email.bookForm.mail" var="mailTypeError"/>
            <span class="error2"></span>

            <form:label path="qty">Cantidad de entradas: </form:label>
            <form:input class="uk-input" type="number" path="qty" min="1"/>
            <form:errors path="qty" cssClass="error-message" element="span"/>

            <form:input class="uk-input hidden" type="text" path="eventId" value="${event.id}"/>

            <div class="container event_buttons">
                <input class="uk-button" type="submit" name="submit" value="Reservar"/>
                <input class="uk-button cancel_button" type="submit" name="cancel" value="Cancelar"/>
            </div>
        </form:form>
    </div>

</body>
</html>

<script type="text/javascript">
    (function() {
        var mail = document.getElementById('mail');
        var form = document.getElementById('bookForm');

        var checkMailValidity = function() {
            if (mail.validity.typeMismatch) {
                mail.setCustomValidity('${mailTypeError}');
                updateMailMessage();
            } else if (mail.validity.valueMissing) {
                mail.setCustomValidity('${mailEmptyError}');
                updateMailMessage();
            } else {
                mail.setCustomValidity('');
            }
        };

        var updateMailMessage = function() {
            form.getElementsByClassName('error2')[0].innerHTML = mail.validationMessage;
        }

        mail.addEventListener('change', checkMailValidity, false);

        form.addEventListener('submit', function(event) {
            if (form.classList) form.classList.add('submitted');
            checkMailValidity();
            if (!this.checkValidity()) {
                event.preventDefault();
                updateMailMessage();
            }
        }, false);
    }());
</script>
