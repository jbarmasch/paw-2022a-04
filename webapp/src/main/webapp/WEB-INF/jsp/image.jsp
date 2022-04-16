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
    <c:url value="/addImage" var="postPath"/>
    <form:form novalidate="true" modelAttribute="imageForm" action="${postPath}" method="post" id="imageForm" enctype="multipart/form-data">
        <span class="required">* </span>
        <form:label path="image">Imagen: </form:label>
        <form:input type="file" path="image" accept="image/png, image/jpeg"/>
        <form:errors path="image" cssClass="error-message" element="span" required="true"/>
        <span class="formError"></span>

        <div class="container event">
            <input class="filter_button" type="submit" value="Crear"/>
        </div>
    </form:form>
</body>
</html>
