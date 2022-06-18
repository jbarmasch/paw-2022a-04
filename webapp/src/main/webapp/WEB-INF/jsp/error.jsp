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
<body class="full_width full_height">
    <div class="container full_width partial_height horizontal center">
        <div class="vertical sep-right-xl limit-width">
            <spring:message var="errorCode" code="error.error" arguments="${code}"/>
            <h1 class="subtitle"><c:out value="${errorCode}"/></h1>
            <h5 class="subtitle_light event_desc"><c:out value="${message}"/></h5>
            <a class="sep-top-xl" href="<c:url value="/"/>"><spring:message code="goHome"/></a>
        </div>
        <img class="error_icon sep-left-xl" src="<c:url value="/resources/png/error.png"/>" alt="Error"/>
    </div>
</body>
</html>
