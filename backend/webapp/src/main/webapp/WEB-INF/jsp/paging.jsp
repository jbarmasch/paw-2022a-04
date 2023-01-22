<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="page" value="${requestScope.page}"/>
<c:set var="size" value="${requestScope.size}"/>
<c:if test="${page > 1}">
    <c:url var="prevUrl" value="">
        <c:forEach items="${param}" var="entry">
            <c:if test="${entry.key != 'page'}">
                <c:param name="${entry.key}" value="${entry.value}"/>
            </c:if>
        </c:forEach>
        <c:param name="page" value="${page - 1}"/>
    </c:url>
</c:if>
<c:url var="nextUrl" value="">
    <c:forEach items="${param}" var="entry">
        <c:if test="${entry.key != 'page'}">
            <c:param name="${entry.key}" value="${entry.value}"/>
        </c:if>
    </c:forEach>
    <c:param name="page" value="${page + 1}"/>
</c:url>
<div class="pagination vertical">
    <c:if test="${page > 1}">
        <button class="sep-top-xl pag-button" onclick="location.href='${prevUrl}'"><spring:message
                code="previousPage"/></button>
    </c:if>
    <c:if test="${size == 11}">
        <button class="sep-top-xl pag-button" onclick="location.href='${nextUrl}'"><spring:message
                code="nextPage"/></button>
    </c:if>
</div>
