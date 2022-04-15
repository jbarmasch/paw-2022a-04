<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<link href="/resources/css/style.css" rel="stylesheet" type="text/css">
<c:forEach var='event' items='${event}'>
    Print data
</c:forEach>
<div class="card" onclick="location.href='<c:url value="/event/${requestScope.event.id}"/>'">
    <img src="https://media.istockphoto.com/photos/dancing-friends-picture-id501387734?k=20&m=501387734&s=612x612&w=0&h=1mli5b7kpDg428fFZfsDPJ9dyVHsWsGK-EVYZUGWHpI=" alt="Fiesta"/>
    <div class="container card_info">
        <div class="container">
            <img class="icon" src="https://freepikpsd.com/file/2019/10/group-people-icon-png-Transparent-Images.png" alt="capacity"/>
            <p><c:out value="${requestScope.event.maxCapacity}"/></p>
        </div>
        <p><c:out value="${requestScope.event.location}"/></p>
        <p><c:out value="${requestScope.event.name}"/></p>
    </div>
</div>
