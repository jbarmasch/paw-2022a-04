<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="appbar container">
    <img onclick="location.href='<c:url value="/"/>'" class="logo" src="https://media-public.canva.com/uH36Y/MAELBWuH36Y/1/tl.png" alt="BotPass"/>
    <div class="appbar_buttons">
        <button class="uk-button uk-button-text" onclick="location.href='<c:url value="/events"/>'"><spring:message code="appbar.button1"/></button>
        <button class="uk-button uk-button-text" onclick="location.href='<c:url value="/create-event"/>'"><spring:message code="appbar.button2"/></button>
        <c:if test="${searchForm != null}" >
            <c:url value="/search" var="postPath"/>
            <form:form novalidate="true" modelAttribute="searchForm" action="${postPath}" method="post" class="horizontal align-center search-form" id="searchForm">
                <span uk-search-icon></span>
                <spring:message code="appbar.search" var="searchEvent"/>
                <form:input class="uk-search-input" type="search" placeholder="${searchEvent}" path="query" value="${param.search}"/>
            </form:form>
        </c:if>
    </div>
    <div>
        <div class="uk-inline">
            <c:choose>
                <c:when test="${username == null}">
                    <button class="uk-button uk-button-text" onclick="location.href='<c:url value="/login"/>'">Iniciar sesión</button>
                </c:when>
                <c:otherwise>
                <div class="uk-inline">
                    <button class="uk-button uk-button-text center"><c:out value="${username}"/><img src="<c:url value='/resources/svg/dropdown.svg'/>" alt="Dropdown"/></button>
                    <div uk-dropdown="mode: click; pos: bottom-center">
                        <ul class="uk-nav uk-dropdown-nav">
                            <li><a href="<c:url value="/bookings"/>"><spring:message code="appbar.dropdown1"/></a></li>
                            <li><a href="<c:url value="/my-events"/>"><spring:message code="appbar.dropdown2"/></a></li>
                            <li><a href="<c:url value="/stats"/>"><spring:message code="appbar.dropdown3"/></a></li>
                            <li class="uk-nav-divider"></li>
                            <li><a href="<c:url value="/logout"/>"><spring:message code="appbar.dropdown4"/></a></li>
                        </ul>
                    </div>
                </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>