<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
<head>
    <head>
        <%@ include file="include.jsp"%>
        <title>Create Event</title>
    </head>
</head>
<body>
    <%@ include file="appbar.jsp"%>
    <h2 class="">Create Event</h2>
    <c:url value="/createEvent" var="postPath"/>
    <div class="only-element">
    <form:form modelAttribute="eventForm" action="${postPath}" method="post">
        <div>
            <form:label path="name">Name: </form:label>
            <form:input class="uk-input" type="text" path="name"/>
            <form:errors path="name" cssClass="formError" element="p"/>
        </div>
        <div>
            <form:label path="description">Description: </form:label>
            <form:input class="uk-input" type="text" path="description" />
            <form:errors path="description" cssClass="formError" element="p"/>
        </div>
        <div>
            <form:label path="location">Location: </form:label>
            <form:select class="uk-select" htmlEscape="true"  multiple="false" path="location" items="${locations}"/>
            <form:errors path="location" cssClass="formError" element="p"/>
        </div>
        <div>
            <form:label path="maxCapacity">Max capacity: </form:label>
            <form:input class="uk-input" type="number" min="1" path="maxCapacity" />
            <form:errors path="maxCapacity" cssClass="formError" element="p"/>
        </div>
        <div>
            <form:label path="price">Price: </form:label>
            <form:input class="uk-input" type="number" path="price" />
            <form:errors path="price" cssClass="formError" element="p"/>
        </div>
        <div>
            <form:label path="date">Date: </form:label>
            <form:input class="uk-input" type="datetime-local" path="date" />
            <form:errors path="date" cssClass="formError" element="p"/>
        </div>
        <div>
            <input type="submit" value="Create event!"/>
        </div>
    </form:form>
    </div>

</body>
</html>
