<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<button class="uk-button uk-button-default" type="button"><spring:message code="order"/></button>
<div uk-dropdown="mode: click; pos: bottom-center">
    <ul class="uk-nav uk-dropdown-nav">
    <%--<li><a href="javascript:" onclick="orderBy('price', 'ASC');">Menor precio</a></li>--%>
    <%--<li><a href="javascript:" onclick="orderBy('price', 'DESC');">Mayor precio</a></li>--%>
        <c:url var="lowPrice" value="">
            <c:forEach items="${param}" var="entry">
                <c:if test="${entry.key != 'order' && entry.key != 'orderBy' && entry.key != 'page'}">
                    <c:param name="${entry.key}" value="${entry.value}" />
                </c:if>
            </c:forEach>
            <c:param name="order" value="price" />
            <c:param name="orderBy" value="ASC" />
        </c:url>
        <a href="${lowPrice}"><spring:message code="order.minPrice"/></a>
        <c:url var="highPrice" value="">
            <c:forEach items="${param}" var="entry">
                <c:if test="${entry.key != 'order' && entry.key != 'orderBy' && entry.key != 'page'}">
                    <c:param name="${entry.key}" value="${entry.value}" />
                </c:if>
            </c:forEach>
            <c:param name="order" value="price" />
            <c:param name="orderBy" value="DESC" />
        </c:url>
        <a href="${highPrice}"><spring:message code="order.maxPrice"/></a>
        <c:url var="lowDate" value="">
            <c:forEach items="${param}" var="entry">
                <c:if test="${entry.key != 'order' && entry.key != 'orderBy' && entry.key != 'page'}">
                    <c:param name="${entry.key}" value="${entry.value}" />
                </c:if>
            </c:forEach>
            <c:param name="order" value="date" />
            <c:param name="orderBy" value="ASC" />
        </c:url>
        <a href="${lowDate}"><spring:message code="order.minDate"/></a>
        <c:url var="highDate" value="">
            <c:forEach items="${param}" var="entry">
                <c:if test="${entry.key != 'order' && entry.key != 'orderBy' && entry.key != 'page'}">
                    <c:param name="${entry.key}" value="${entry.value}" />
                </c:if>
            </c:forEach>
            <c:param name="order" value="date" />
            <c:param name="orderBy" value="DESC" />
        </c:url>
        <a href="${highDate}"><spring:message code="order.maxDate"/></a>
    </ul>
</div>
