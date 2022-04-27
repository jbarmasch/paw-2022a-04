<%@ page import="org.springframework.security.core.userdetails.UserDetails" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<div class="appbar container">
    <img onclick="location.href='<c:url value="/"/>'" class="logo" src="https://media-public.canva.com/uH36Y/MAELBWuH36Y/1/tl.png" alt="BotPass"/>
    <div class="appbar_buttons">
        <button class="uk-button uk-button-text" onclick="location.href='<c:url value="/events"/>'">Eventos</button>
        <button class="uk-button uk-button-text" onclick="location.href='<c:url value="/createEvent"/>'">Crear evento</button>
        <button class="uk-button uk-button-text" onclick="location.href='<c:url value="/help"/>'">Ayuda</button>
    </div>
    <div>
        <div class="uk-inline">
            <% String[] strs = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().split(" "); request.setAttribute("strs", strs); request.setAttribute("strsSize", strs.length); %>
            <c:choose>
                <c:when test="${strsSize <= 1}">
                    <button class="uk-button uk-button-text" onclick="location.href='<c:url value="/login"/>'">Iniciar sesión</button>
                </c:when>
                <c:otherwise>
                    <button class="uk-button uk-button-text"><%= strs[2].replace(";", "") %><img src="<c:url value='/resources/svg/dropdown.svg'/>" alt="Dropdown"/></button>
                    <div uk-dropdown="pos: bottom-center">
                        <ul class="uk-nav uk-dropdown-nav">
                            <li><a href="<c:url value="/bookings"/>">MIS RESERVAS</a></li>
                            <li><a href="<c:url value="/myEvents"/>">MIS EVENTOS</a></li>
                            <li class="uk-nav-divider"></li>
                            <li><a href="<c:url value="/logout"/>">CERRAR SESIÓN</a></li>
                        </ul>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <%-- <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/98/OOjs_UI_icon_userAvatar.svg/2048px-OOjs_UI_icon_userAvatar.svg.png"
             alt="User profile pic" class="profile-pic" onclick="location.href='<c:url value="/profile"/>'"/> --%>
    </div>
</div>