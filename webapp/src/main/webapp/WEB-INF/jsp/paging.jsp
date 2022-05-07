<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="page" value="${requestScope.page}"/>
<c:set var="size" value="${requestScope.size}"/>
<c:if test="${page > 1}">
    <c:url var="prevUrl" value="">
        <c:forEach items="${param}" var="entry">
            <c:if test="${entry.key != 'page'}">
                <c:param name="${entry.key}" value="${entry.value}" />
            </c:if>
        </c:forEach>
        <c:param name="page" value="${page - 1}" />
    </c:url>
</c:if>
<c:url var="nextUrl" value="">
    <c:forEach items="${param}" var="entry">
        <c:if test="${entry.key != 'page'}">
            <c:param name="${entry.key}" value="${entry.value}" />
        </c:if>
    </c:forEach>
    <c:param name="page" value="${page + 1}" />
</c:url>
<div class="pagination">
    <c:if test="${page > 1}">
        <a href="${prevUrl}"><spring:message code="previousPage"/></a>
    </c:if>
    <c:if test="${size == 10}">
        <a href="${nextUrl}"><spring:message code="nextPage"/></a>
    </c:if>
</div>
