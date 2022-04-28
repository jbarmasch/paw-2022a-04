<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<div class="appbar container">
    <img onclick="location.href='<c:url value="/"/>'" class="logo" src="https://media-public.canva.com/uH36Y/MAELBWuH36Y/1/tl.png" alt="BotPass"/>
    <div class="appbar_buttons">
        <button class="uk-button uk-button-text" onclick="location.href='<c:url value="/events"/>'">Eventos</button>
        <button class="uk-button uk-button-text" onclick="location.href='<c:url value="/createEvent"/>'">Crear evento</button>
    </div>
    <div>
        <div class="uk-inline">
            <c:choose>
                <c:when test="${username == null}">
                    <button class="uk-button uk-button-text" onclick="location.href='<c:url value="/login"/>'">Iniciar sesión</button>
                </c:when>
                <c:otherwise>
                <div class="uk-inline">
                    <button class="uk-button uk-button-text"><c:out value="${username}"/><img src="<c:url value='/resources/svg/dropdown.svg'/>" alt="Dropdown"/></button>
                    <div uk-dropdown="mode: click; pos: bottom-center">
                        <ul class="uk-nav uk-dropdown-nav">
                            <li><a href="<c:url value="/bookings"/>">MIS RESERVAS</a></li>
                            <li><a href="<c:url value="/myEvents"/>">MIS EVENTOS</a></li>
                            <li class="uk-nav-divider"></li>
                            <li><a href="<c:url value="/logout"/>">CERRAR SESIÓN</a></li>
                        </ul>
                    </div>
                </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>