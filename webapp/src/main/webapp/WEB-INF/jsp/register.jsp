<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@ include file="include.jsp"%>
    <%@ include file="appbar.jsp"%>
    <title>Register</title>
</head>
<body>
    <c:url value="/register" var="postPath"/>
    <div class="only-element">
    <form:form novalidate="true" modelAttribute="registerForm" action="${postPath}" method="post">
        <h3>Register</h3>
        <div class="space-bet sep-top">
<%--            <form:input placeholder="Mail" type="email" path="mail"/>--%>
<%--            <form:errors path="mail" cssClass="error-message" element="p"/>--%>
            <span class="required">* </span>
            <form:label path="mail">Mail: </form:label>
            <form:input class="uk-input" type="email" path="mail" maxlength="100" required="true" id="mail"/>
            <form:errors path="mail" cssClass="error-message" element="span"/>
            <spring:message code="NotEmpty.bookForm.mail" var="mailEmptyError"/>
            <spring:message code="Email.bookForm.mail" var="mailTypeError"/>
            <spring:message code="Size.bookForm.mail" var="mailSizeError"/>
            <span class="formError"></span>
        </div>
        <div class="space-bet sep-top">
            <form:input placeholder="Username" type="text" path="username"/>
            <form:errors path="username" cssClass="error-message" element="p"/>
        </div>
        <div class="space-bet sep-top">
            <form:input placeholder="Password" type="password" path="password" />
            <form:errors path="password" cssClass="error-message" element="p"/>
        </div>
        <div class="space-bet sep-top">
            <form:input placeholder="Repeat password" type="password" path="repeatPassword"/>
            <form:errors path="repeatPassword" cssClass="error-message" element="p"/>
        </div>
        <hr/>
        <div class="center">
            <input type="submit" value="Register"/>
        </div>
    </form:form>
    </div>
</body>
</html>