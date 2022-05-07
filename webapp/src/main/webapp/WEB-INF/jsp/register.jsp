<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
    <%@ include file="appbar.jsp"%>
    <c:url value="/register" var="postPath"/>
    <div class="only-element">
    <form:form class="registerForm" novalidate="true" modelAttribute="userForm" action="${postPath}" method="post">
        <h3><spring:message code="register.register"/></h3>
        <div class="space-bet sep-top vertical">
            <spring:message code="register.mail" var="mailPlaceholder"/>
            <form:input placeholder="* ${mailPlaceholder}" type="email" path="mail" maxlength="100" required="true" id="mail"/>
            <form:errors path="mail" cssClass="error-message" element="span"/>
            <spring:message code="NotEmpty.userForm.mail" var="mailEmptyError"/>
            <spring:message code="Email.userForm.mail" var="mailTypeError"/>
            <spring:message code="Size.userForm.mail" var="mailSizeError"/>
            <span class="formError"></span>
        </div>
        <div class="space-bet sep-top vertical">
            <spring:message code="register.username" var="usernamePlaceholder"/>
            <form:input placeholder="* ${usernamePlaceholder}" type="text" path="username" pattern="[a-zA-Z0-9]+" minlength="6" maxlength="100" required="true" id="username"/>
            <form:errors path="username" cssClass="error-message" element="span"/>
            <spring:message code="NotEmpty.userForm.username" var="userEmptyError"/>
            <spring:message code="Size.userForm.username" var="userSizeError"/>
            <spring:message code="UniqueUsername.userForm.username" var="userUniqueError"/>
            <spring:message code="Pattern.userForm.username" var="userPatternError"/>
            <span class="formError"></span>
        </div>
        <div class="space-bet sep-top vertical">
            <spring:message code="register.password" var="passwordPlaceholder"/>
            <form:input placeholder="* ${passwordPlaceholder}" type="password" path="password" minlength="6" maxlength="100" required="true" id="password"/>
            <form:errors path="password" cssClass="error-message" element="span"/>
            <spring:message code="NotEmpty.userForm.password" var="passwordEmptyError"/>
            <spring:message code="Size.userForm.password" var="passwordSizeError"/>
            <spring:message code="userForm.passwordMatch" var="passwordMatchError"/>
            <span class="formError"></span>
        </div>
        <div class="space-bet sep-top vertical">
            <spring:message code="register.repeatPassword" var="repeatPasswordPlaceholder"/>
            <form:input placeholder="* ${repeatPasswordPlaceholder}" type="password" path="repeatPassword" minlength="6" maxlength="100" required="true" id="repeatPassword"/>
            <form:errors path="repeatPassword" cssClass="error-message" element="span"/>
            <span class="formError"></span>
        </div>
        <div class="center">
            <spring:message code="register.register" var="register"/>
            <input type="submit" value="${register}"/>
        </div>
    </form:form>
    </div>
</body>
</html>

<script type="text/javascript">
    (function() {
        var mail = document.getElementById('mail');
        var username = document.getElementById('username');
        var password = document.getElementById('password');
        var repeatPassword = document.getElementById('repeatPassword');
        var form = document.getElementById('userForm');

        var checkMailValidity = function() {
            if (mail.validity.typeMismatch) {
                mail.setCustomValidity('${mailTypeError}');
                updateMailMessage();
            } else if (mail.validity.tooShort || mail.validity.tooLong) {
                mail.setCustomValidity('${mailSizeError}');
                updateMailMessage();
            } else if (mail.validity.valueMissing) {
                mail.setCustomValidity('${mailEmptyError}');
                updateMailMessage();
            } else {
                mail.setCustomValidity('');
            }
        };

        var checkUsernameValidity = function() {
            if (username.validity.typeMismatch) {
                username.setCustomValidity('${mailTypeError}');
                updateUsernameMessage();
            } else if (username.validity.valueMissing) {
                username.setCustomValidity('${userEmptyError}');
                updateUsernameMessage();
            } else if (username.validity.patternMismatch) {
                username.setCustomValidity('${userPatternError}');
                updateUsernameMessage();
            } else if (username.validity.tooShort || username.validity.tooLong) {
                username.setCustomValidity('${userSizeError}');
                updateUsernameMessage();
            } else {
                username.setCustomValidity('');
            }
        };

        var checkPasswordValidity = function() {
            if (password.validity.typeMismatch) {
                password.setCustomValidity('${mailTypeError}');
                updatePasswordMessage();
            } else if (password.validity.valueMissing) {
                password.setCustomValidity('${passwordEmptyError}');
                updatePasswordMessage();
            } else if (password.validity.tooShort || password.validity.tooLong) {
                password.setCustomValidity('${passwordSizeError}');
                updatePasswordMessage();
            } else {
                password.setCustomValidity('');
            }
        };

        var checkRepeatPasswordValidity = function() {
            if (repeatPassword.validity.typeMismatch) {
                repeatPassword.setCustomValidity('${mailTypeError}');
                updateRepeatPasswordMessage();
            } else if (repeatPassword.validity.valueMissing) {
                repeatPassword.setCustomValidity('${passwordEmptyError}');
                updateRepeatPasswordMessage();
            } else if (repeatPassword.validity.tooShort || repeatPassword.validity.tooLong) {
                repeatPassword.setCustomValidity('${passwordSizeError}');
                updateRepeatPasswordMessage();
            } else {
                repeatPassword.setCustomValidity('');
            }
        };

        var checkPasswordMatchValidity = function() {
            if (!password.validity.valid && form.getElementsByClassName('formError')[2].innerHTML !== '${passwordMatchError}')
                return;

            if (password.value.length !== 0 && repeatPassword.value.length !== 0) {
                if (password.value !== repeatPassword.value) {
                    password.setCustomValidity('${passwordMatchError}');
                    updatePasswordMessage();
                } else {
                    password.setCustomValidity('');
                }
            } else {
                password.setCustomValidity('');
            }
        }

        var updateMailMessage = function() {
            form.getElementsByClassName('formError')[0].innerHTML = mail.validationMessage;
        }

        var updateUsernameMessage = function() {
            form.getElementsByClassName('formError')[1].innerHTML = username.validationMessage;
        }

        var updatePasswordMessage = function() {
            form.getElementsByClassName('formError')[2].innerHTML = password.validationMessage;
        }

        var updateRepeatPasswordMessage = function() {
            form.getElementsByClassName('formError')[3].innerHTML = repeatPassword.validationMessage;
        }

        mail.addEventListener('change', checkMailValidity, false);
        mail.addEventListener('keyup', checkMailValidity, false);
        username.addEventListener('change', checkUsernameValidity, false);
        username.addEventListener('keyup', checkUsernameValidity, false);
        password.addEventListener('change', checkPasswordValidity, false);
        password.addEventListener('keyup', checkPasswordValidity, false);
        password.addEventListener('change', checkPasswordMatchValidity, false);
        password.addEventListener('keyup', checkPasswordMatchValidity, false);
        repeatPassword.addEventListener('change', checkPasswordMatchValidity, false);
        repeatPassword.addEventListener('keyup', checkPasswordMatchValidity, false);
        repeatPassword.addEventListener('change', checkRepeatPasswordValidity, false);
        repeatPassword.addEventListener('keyup', checkRepeatPasswordValidity, false);

        form.addEventListener('submit', function(event) {
            if (form.classList) form.classList.add('submitted');
            checkMailValidity();
            checkUsernameValidity();
            checkPasswordValidity();
            checkRepeatPasswordValidity();
            checkPasswordMatchValidity();
            if (!this.checkValidity()) {
                event.preventDefault();
                updateMailMessage();
                updateUsernameMessage();
                updatePasswordMessage();
                updateRepeatPasswordMessage();
            }
        }, false);
    }());
</script>
