<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <%@ include file="include.jsp"%>
    <%@ include file="appbar.jsp"%>
    <title>BotPass</title>
</head>
<body>
<c:url value="/login" var="loginUrl" />
<div class="only-element">
<form action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded">
    <h3>Login</h3>
    <div class="space-bet sep-top">
        <label for="username" class="sep-right">Username: </label>
        <input id="username" class="sep-left" name="j_username" type="text"/>
    </div>
    <div class="space-bet sep-top">
        <label for="password" class="sep-right">Password: </label>
        <input id="password" class="sep-left" name="j_password" type="password"/>
    </div>
    <div class="sep-top">
<%--        <label><input name="j_rememberme" type="checkbox"/><spring:message code="remember_me"/></label>--%>
        <label class="small-text align-center"><input name="j_rememberme" type="checkbox"/>Remember me</label>
    </div>
    <hr/>
    <div class="center">
        <input type="submit" value="Login"/>
    </div>
    <div class="center">
        <a href="<c:url value="/register"/>" class="small-text">Register</a>
    </div>
</form>
</div>
</body>
</html>