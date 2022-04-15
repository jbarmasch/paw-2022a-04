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
                    <img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="Location icon"/><span><c:out value="${event.location}"/></span>
                </div>
                    <div class="container">
                <img class="icon" src="<c:url value="/resources/svg/price-tag.svg"/>" alt="Price icon"/><span><c:out value="${event.price == 0 ? '$' + event.price : 'Gratis'}"/></span>
                </div>
                <div class="container">
                    <img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="Date icon"/><span><c:out value="${event.dateFormatted}"/></span>
                </div>
                <div class="container">
                    <img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="Time icon"/><span><c:out value="${event.timeFormatted}"/></span>
                </div>
                <div class="container">
                    <img class="icon" src="<c:url value="/resources/svg/party.svg"/>" alt="Type icon"/><span><c:out value="${event.type}"/></span>
                </div>
            </div>
        </div>

        <c:url value="/event/${event.id}" var="postPath"/>
        <form:form novalidate="true" modelAttribute="bookForm" action="${postPath}" method="post" id="bookForm">
            <span class="required">* </span>
            <form:label path="name">Nombre: </form:label>
            <form:input class="uk-input" type="text" path="name" maxlength="100" required="true"/>
            <form:errors path="name" cssClass="error-message" element="span" required="true"/>
            <spring:message code="NotEmpty.bookForm.name" var="nameEmptyError"/>
            <spring:message code="Size.bookForm.name" var="nameSizeError"/>
            <span class="formError"></span>

            <span class="required">* </span>
            <form:label path="surname">Apellido: </form:label>
            <form:input class="uk-input" type="text" path="surname" maxlength="100" required="true" id="surname"/>
            <form:errors path="surname" cssClass="error-message" element="span" required="true"/>
            <spring:message code="NotEmpty.bookForm.surname" var="surnameEmptyError"/>
            <spring:message code="Size.bookForm.surname" var="surnameSizeError"/>
            <span class="formError"></span>

            <span class="required">* </span>
            <form:label path="mail">Mail: </form:label>
            <form:input class="uk-input" type="email" path="mail" maxlength="100" required="true" id="mail"/>
            <form:errors path="mail" cssClass="error-message" element="span"/>
            <spring:message code="NotEmpty.bookForm.mail" var="mailEmptyError"/>
            <spring:message code="Email.bookForm.mail" var="mailTypeError"/>
            <spring:message code="Size.bookForm.mail" var="mailSizeError"/>
            <span class="formError"></span>

            <span class="required">* </span>
            <form:label path="qty">Cantidad de entradas: </form:label>
            <form:input class="uk-input" type="number" path="qty" min="1" required="true" id="qty"/>
            <form:errors path="qty" cssClass="error-message" element="span"/>
            <spring:message code="Min.bookForm.qty" var="qtySizeError"/>
            <spring:message code="NotNull.bookForm.qty" var="qtyNullError"/>
            <span class="formError"></span>

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
        var name = document.getElementById('name');
        var surname = document.getElementById('surname');
        var qty = document.getElementById('qty');
        var form = document.getElementById('bookForm');

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

        var checkSurnameValidity = function() {
            if (surname.validity.tooLong) {
                surname.setCustomValidity('${surnameSizeError}');
                updateSurnameMessage()
            } else if (surname.validity.valueMissing) {
                surname.setCustomValidity('${surnameEmptyError}');
                updateSurnameMessage()
            } else {
                surname.setCustomValidity('');
            }
        };

        var checkMailValidity = function() {
            if (mail.validity.typeMismatch) {
                mail.setCustomValidity('${mailTypeError}');
                updateMailMessage();
            } else if (mail.validity.valueMissing) {
                mail.setCustomValidity('${mailEmptyError}');
                updateMailMessage();
            } else if (mail.validity.tooLong) {
                mail.setCustomValidity('${mailSizeError}');
                updateMailMessage();
            } else {
                mail.setCustomValidity('');
            }
        };

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

        var updateNameMessage = function() {
            form.getElementsByClassName('formError')[0].innerHTML = name.validationMessage;
        }

        var updateSurnameMessage = function() {
            form.getElementsByClassName('formError')[1].innerHTML = surname.validationMessage;
        }

        var updateMailMessage = function() {
            form.getElementsByClassName('formError')[2].innerHTML = mail.validationMessage;
        }

        var updateQtyMessage = function() {
            form.getElementsByClassName('formError')[3].innerHTML = qty.validationMessage;
        }

        name.addEventListener('change', checkNameValidity, false);
        name.addEventListener('keyup', checkNameValidity, false);
        surname.addEventListener('change', checkSurnameValidity, false);
        surname.addEventListener('keyup', checkSurnameValidity, false);
        mail.addEventListener('change', checkMailValidity, false);
        mail.addEventListener('keyup', checkMailValidity, false);
        qty.addEventListener('change', checkQtyValidity, false);
        qty.addEventListener('keyup', checkQtyValidity, false);

        form.addEventListener('submit', function(event) {
            if (form.classList) form.classList.add('submitted');
            checkNameValidity();
            checkSurnameValidity();
            checkMailValidity();
            checkQtyValidity();
            if (!this.checkValidity()) {
                event.preventDefault();
                updateNameMessage();
                updateSurnameMessage();
                updateMailMessage();
                updateQtyMessage();
            }
        }, false);
    }());
</script>
