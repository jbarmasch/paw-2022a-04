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
    <div class="home">
        <div>
            <c:url value="/events" var="postPath"/>
            <form:form modelAttribute="filterForm" action="${postPath}" method="post"  class="filter">
                <b>Filters:</b>
                <div>
                    <form:label path="locations">Location: </form:label>
                    <form:select class="uk-select" htmlEscape="true" multiple="true" path="locations" items="${allLocations}"/>
                    <form:errors path="locations" cssClass="formError" element="p"/>
                </div>
                <div>
                    <form:label path="types">Name: </form:label>
                    <form:select class="uk-select" htmlEscape="true" multiple="true" path="types" items="${allTypes}"/>
                    <form:errors path="types" cssClass="formError" element="p"/>
                </div>
                <div>
                    <form:label path="minPrice">Min. price: </form:label>
                    <form:input class="uk-input" type="number" path="minPrice" />
                    <form:errors path="minPrice" cssClass="formError" element="p"/>
                </div>
                <div>
                    <form:label path="maxPrice">Max. price: </form:label>
                    <form:input class="uk-input" type="number" path="maxPrice"/>
                    <form:errors path="maxPrice" cssClass="formError" element="p"/>
                </div>
                <div class="container event">
                    <input class="filter_button" type="submit" value="Apply!">
                </div>
            </form:form>
        </div>
        <div class="container browse">
            <c:forEach var="event" items="${events}">
                    <div class="card uk-card uk-card-default" onclick="location.href='<c:url value="/event/${event.id}"/>'">
                        <div class="uk-card-media-top">
                            <img class="card_img" src="https://media.istockphoto.com/photos/dancing-friends-picture-id501387734?k=20&m=501387734&s=612x612&w=0&h=1mli5b7kpDg428fFZfsDPJ9dyVHsWsGK-EVYZUGWHpI=" alt="Party Image">
                        </div>
                        <div class="uk-card-body">
                            <h3 class="uk-card-title"><c:out value="${event.name}"/></h3>
                            <div class="container card_body">
                                <div class="card_info">
                                    <span>$<c:out value="${event.price}"/></span>
                                    <span><c:out value="${event.location}"/></span>
                                </div>
                                <%-- <span><c:out value="${event.type}"/></span> --%>
                                <div class="card_info">
                                    <span><c:out value="${event.dateFormatted}"/></span>
                                    <span><c:out value="${event.timeFormatted}"/></span>
                                </div>
                            </div>
                        </div>
                    </div>
            </c:forEach>
        </div>
    </div>
</body>
</html>

