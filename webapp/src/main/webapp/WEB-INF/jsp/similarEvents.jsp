<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="similarEventsSize" value="${requestScope.similarEventsSize}"/>
<c:set var="similarEvents" value="${requestScope.similarEvents}"/>
<c:if test="${similarEventsSize > 0}">
    <c:choose>
        <c:when test="${similarEventsSize > 1}">
            <div>
                <h4 class="subtitle">Estos eventos también podrían interesarte</h4>
            </div>
        </c:when>
        <c:otherwise>
            <div>
                <h4 class="subtitle">Este evento también podría interesarte</h4>
            </div>
        </c:otherwise>
    </c:choose>
    <div class="container multi-browse">
        <c:forEach var="event" items="${similarEvents}">
            <c:set var="event" value="${event}" scope="request"/>
            <c:import url="eventCard.jsp"/>
        </c:forEach>
    </div>
</c:if>
