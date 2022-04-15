<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>Register</title>
</head>
<body>
    <h2>Register</h2>
    <c:url value="/register" var="postPath"/>
    <form:form novalidate="true" modelAttribute="registerForm" action="${postPath}" method="post">
        <div>
            <form:label path="username">Username: </form:label>
            <form:input type="text" path="username"/>
            <form:errors path="username" cssClass="error-message" element="p"/>
        </div>
        <div>
            <form:label path="password">Password: </form:label>
            <form:input type="password" path="password" />
            <form:errors path="password" cssClass="error-message" element="p"/>
        </div>
        <div>
            <form:label path="repeatPassword">Repeat password: </form:label>
            <form:input type="password" path="repeatPassword"/>
            <form:errors path="repeatPassword" cssClass="error-message" element="p"/>
        </div>
        <div>
            <input type="submit" value="Register"/>
        </div>
    </form:form>
</body>
</html>