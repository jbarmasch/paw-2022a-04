<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
    <%@ include file="appbar.jsp"%>
    <div class="container browse">
        <div>
            <img height="200" src="https://d1csarkz8obe9u.cloudfront.net/posterpreviews/party-design-template-172a81aa153634421cabbdc2d4eeeb5b_screen.jpg?ts=1589238460" alt="Party img"/>
        </div>
        <div class="event_info">
            <h3><c:out value="${event.name}"/></h3>
            <p><c:out value="${event.description}"/></p>
            <p><c:out value="${event.location}"/></p>
            <p>$<c:out value="${event.price}"/></p>
            <p><c:out value="${event.date}"/></p>
            <p><c:out value="${event.type}"/></p>
            Cantidad de entradas: <input type="number"/>
            <p>Mail: <input id="mail" type="text" name="mail"/>
                <span class="error"></span>
            </p>

            <button onclick="sendMail()">Reserva!</button>
            <button onclick="sendCancelMail()">Cancela!</button>

            <%-- <button onclick="location.href='<c:url value="/book"><c:param name="mail" value="slococo@itba.edu.ar"/><c:param name="eventId" value="${event.id}"/> </c:url>'">Reserva!</button> --%>
        </div>
    </div>

</body>
<script type="text/javascript">
    function sendMail() {
        const val = document.getElementById("mail").value;
        if (isMail(val)){
            window.location.href = "<c:url value="/book"/>" + "?mail=" + val + "&eventId=" + <c:out value="${event.id}"/>;
        }
        else {
            document.querySelector("error").textContent = "Invalid email";
        }
    }

    function isMail(mail) {
        return String(mail).toLowerCase().match(/^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
    }

    function sendCancelMail() {
        const val = document.getElementById("mail").value;
        if (isMail(val)){
            window.location.href = "<c:url value="/cancel"/>" + "?mail=" + val + "&eventId=" + <c:out value="${event.id}"/>;
        }
        else {
            document.querySelector(".error").textContent = "Invalid email";
        }
    }
</script>
</html>