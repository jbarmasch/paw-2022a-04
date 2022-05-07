<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="popularEventsSize" value="${requestScope.popularEventsSize}"/>
<c:set var="popularEvents" value="${requestScope.popularEvents}"/>
<c:if test="${popularEventsSize > 0}">
    <c:choose>
        <c:when test="${popularEventsSize > 1}">
            <div>
                <h4 class="subtitle"><spring:message code="popular.pluralBooked"/></h4>
            </div>
        </c:when>
        <c:otherwise>
            <div>
                <h4 class="subtitle"><spring:message code="popular.singularBooked"/></h4>
            </div>
        </c:otherwise>
    </c:choose>
    <div class="container multi-browse">
        <c:forEach var="event" items="${popularEvents}">
            <c:set var="event" value="${event}" scope="request"/>
            <c:import url="eventCard.jsp"/>
        </c:forEach>
    </div>
</c:if>