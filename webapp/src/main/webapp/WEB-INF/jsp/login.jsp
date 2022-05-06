<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
    <%@ include file="appbar.jsp"%>
    <c:url value="/login" var="loginUrl" />
    <div class="only-element">
        <form action="${loginUrl}" class="loginForm" method="post" enctype="application/x-www-form-urlencoded">
            <h3>Inicio de sesión</h3>
            <div class="space-bet sep-top">
                <input id="username" placeholder="Nombre de usuario" name="j_username" type="text"/>
            </div>
            <div class="space-bet sep-top">
                <input id="password" placeholder="Contraseña" name="j_password" type="password"/>
            </div>
            <c:if test="${error}">
                <span class="formError visible">El usuario o la contraseña es incorrecto.</span>
            </c:if>
            <input class="hidden" name="j_rememberme" type="checkbox" checked/>
            <div class="center sep-top-xl">
                <input type="submit" value="Iniciar sesión"/>
            </div>
            <hr/>
            <div class="center">
                <input type="button" onclick="location.href='<c:url value="/register"/>'" class="uk-button-submit" value="Registrarse"/>
            </div>
        </form>
    </div>
</body>
</html>

<script type="text/javascript">
    (function() {
        var username = document.getElementById('username');
        var password = document.getElementById('password');
        var form = document.getElementById('registerForm');

        if (form == null)
            return;

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

        var updateUsernameMessage = function() {
            form.getElementsByClassName('formError')[0].innerHTML = username.validationMessage;
        }

        var updatePasswordMessage = function() {
            form.getElementsByClassName('formError')[1].innerHTML = password.validationMessage;
        }

        username.addEventListener('change', checkUsernameValidity, false);
        username.addEventListener('keyup', checkUsernameValidity, false);
        password.addEventListener('change', checkPasswordValidity, false);
        password.addEventListener('keyup', checkPasswordValidity, false);

        form.addEventListener('submit', function(event) {
            if (form.classList) form.classList.add('submitted');
            checkUsernameValidity();
            checkPasswordValidity();
            if (!this.checkValidity()) {
                event.preventDefault();
                updateUsernameMessage();
                updatePasswordMessage();
            }
        }, false);
    }());
</script>