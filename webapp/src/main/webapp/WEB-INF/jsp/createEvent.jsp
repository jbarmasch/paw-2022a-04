<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
    <%@ include file="appbar.jsp"%>
    <c:url value="/createEvent" var="postPath"/>
    <div class="only-element">
        <form:form novalidate="true" modelAttribute="eventForm" action="${postPath}" method="post">
            <div>
                <form:label path="name">Nombre: </form:label>
                <form:input class="uk-input" type="text" path="name"/>
                <form:errors path="name" cssClass="error-message" element="span"/>
            </div>
            <div>
                <form:label path="description">Descripción: </form:label>
                <form:input class="uk-input" type="text" path="description" />
                <form:errors path="description" cssClass="error-message" element="span"/>
            </div>
            <div>
                <form:label path="location">Ubicación: </form:label>
                <form:select class="uk-select" htmlEscape="true" multiple="false" path="location">
                    <form:option value="" hidden="true" selected="true" label="Seleccione una ubicación"/>
                    <form:options items="${locations}"/>
                </form:select>
                <form:errors path="location" cssClass="error-message" element="span"/>
            </div>
            <div>
                <form:label path="type" for="type">Tipo: </form:label>
                <form:select id="type" class="uk-select" htmlEscape="true" multiple="false" path="type">
                    <form:option hidden="true" value="" selected="true" label="Seleccione un tipo"/>
                    <form:options items="${types}"/>
                </form:select>
                <form:errors path="type" cssClass="error-message" element="span"/>
            </div>
            <div>
                <form:label path="maxCapacity" for="maxCapacity">Aforo: </form:label>
                <form:input id="maxCapacity" class="uk-input" type="number" min="1" path="maxCapacity"/>
                <form:errors path="maxCapacity" cssClass="error-message" element="span"/>
            </div>
            <div>
                <form:label path="price">Precio: </form:label>
                <form:input class="uk-input" type="number" path="price" min="0.00" step="0.01" value="0.00" />
                <form:errors path="price" cssClass="error-message" element="span"/>
            </div>
            <div>
                <form:label path="date">Fecha: </form:label>
                <form:input class="uk-input" type="datetime-local" min="${currentDate}" path="date"/>
                <form:errors path="date" cssClass="error-message" element="span"/>
            </div>
            <div class="container event">
                <input class="filter_button" type="submit" value="Crear"/>
            </div>
        </form:form>
    </div>
</body>
</html>
