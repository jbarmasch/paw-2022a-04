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
<div>
    <%@ include file="appbar.jsp"%>
    <c:url value="/events/${eventId}/add-tickets" var="postPath"/>
    <div class="only-element">
        <form:form novalidate="true" modelAttribute="ticketsForm" action="${postPath}" method="post" id="ticketsForm" enctype="multipart/form-data">
            <div>
                <form:label path="tickets[0].ticketName">Nombre de la entrada: </form:label>
                <form:input class="uk-input" type="text" path="tickets[0].ticketName" required="true"/>
                <form:errors path="tickets[0].ticketName" cssClass="error-message" element="span"/>
            </div>
            <div>
                <form:label path="tickets[0].price">Precio: </form:label>
                <form:input class="uk-input" type="number" path="tickets[0].price" min="0.00" step="0.01" value="0.00" required="true"/>
                <form:errors path="tickets[0].price" cssClass="error-message" element="span"/>
            </div>
            <div>
                <form:label path="tickets[0].qty">Cantidad: </form:label>
                <form:input class="uk-input" type="number" path="tickets[0].qty" min="0.00" step="0.01" value="0.00" required="true"/>
                <form:errors path="tickets[0].qty" cssClass="error-message" element="span"/>
            </div>
            </div>
            <div class="container event">
                <input class="filter_button" type="submit" value="Crear"/>
            </div>
        </form:form>
    </div>
</body>
</html>

<%--<script type="text/javascript">--%>
<%--    function addTicket(i) {--%>
<%--        document.getElementById('ticketsForm').insertAdjacentHTML("afterend", '<div>' +--%>
<%--                '<label for="tickets' + i + '.ticketName">Nombre de la entrada: </label>' +--%>
<%--                '<input id="tickets' + i + '.ticketName" name="tickets[' + i + '].ticketName" class="uk-input" type="text" required="true"/>--%>
<%--                <form:errors path="tickets[2].ticketName" cssClass="error-message" element="span"/>--%>
<%--            </div>--%>
<%--            <div>--%>
<%--                <form:label path="tickets[2].price">Precio: </form:label>--%>
<%--                <form:input class="uk-input" type="number" path="tickets[2].price" min="0.00" step="0.01" value="0.00" required="true"/>--%>
<%--                <form:errors path="tickets[2].price" cssClass="error-message" element="span"/>--%>
<%--            </div>--%>
<%--            <div>--%>
<%--                <form:label path="tickets[2].qty">Cantidad: </form:label>--%>
<%--                <form:input class="uk-input" type="number" path="tickets[2].qty" min="0.00" step="0.01" value="0.00" required="true"/>--%>
<%--                <form:errors path="tickets[2].qty" cssClass="error-message" element="span"/>--%>
<%--            </div>');--%>
<%--    }--%>
<%--</script>--%>