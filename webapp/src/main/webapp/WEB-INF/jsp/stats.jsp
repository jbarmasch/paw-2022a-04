<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
<%@ include file="appbar.jsp"%>
<div class="container center">
    <div class="vertical">
        <span>Eventos reservados: ${stats.eventsAttended}</span>
        <span>Cantidad de entradas reservadas: ${stats.bookingsMade}</span>
        <span>Tipo de evento favorito: ${stats.favType.name}</span>
        <span>Ubicación favorita: ${stats.favLocation.name}</span>
    </div>
    <div class="vertical">
        <span>Eventos creados: ${stats.eventsCreated}</span>
        <span>Cantidad de entradas vendidas: ${stats.bookingsGotten}</span>
        <span>Evento más popular: ${stats.popularEvent.name}</span>
        <span>Dinero obtenido: </span>
    </div>
</div>
</body>
</html>
