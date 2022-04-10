<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
    <%@ include file="appbar.jsp"%>
    <div class="container event">
        <div>
            <img class="event_img" src="https://d1csarkz8obe9u.cloudfront.net/posterpreviews/party-design-template-172a81aa153634421cabbdc2d4eeeb5b_screen.jpg?ts=1589238460" alt="Party img"/>
        </div>
        <div class="event_info">
            <h3><c:out value="${event.name}"/></h3>
            <p><c:out value="${event.description}"/></p>
            <p><c:out value="${event.location}"/></p>
            <p>$<c:out value="${event.price}"/></p>
            <p><c:out value="${event.date}"/></p>
            <p><c:out value="${event.type}"/></p>

            <c:url value="/book" var="postPath"/>
            <form:form modelAttribute="bookForm" action="${postPath}" method="post">

                <form:label path="qty">Cantidad de entradas: </form:label>
                <form:input class="uk-input" type="number" path="qty" />
                <form:errors path="qty" cssClass="formError" element="p"/>

                <form:label path="mail">Mail: </form:label>
                <form:input class="uk-input" type="text" path="mail" />
                <form:errors path="mail" cssClass="formError" element="p"/>

                <form:input class="uk-input hidden" type="text" path="eventId" value="${event.id}"/>

                <div class="container event_buttons">
                    <input class="uk-button" type="submit" name="submit" value="Book!"/>
                    <input class="uk-button cancel_button" type="submit" name="cancel" value="Cancel!"/>
                </div>
            </form:form>
        </div>
    </div>

</body>
</html>