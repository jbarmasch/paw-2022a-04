<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
    <%@ include file="appbar.jsp"%>
    <div class="container center">
        <div class="vertical">
            <span><spring:message code="stats.eventsCreated"/>: ${stats.eventsCreated}</span>
            <span><spring:message code="stats.bookingsGotten"/>: ${stats.bookingsGotten}</span>
            <span><spring:message code="stats.popoularEvent"/>: ${stats.popularEvent.name}</span>
            <span><spring:message code="stats.moneyEarned"/>: </span>
        </div>
    </div>
</body>
</html>
