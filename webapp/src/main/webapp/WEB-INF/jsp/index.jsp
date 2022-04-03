<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<link href="/css/style.css" rel="stylesheet" type="text/css">
<head>
    <title>BotPass</title>
    <script src="https://unpkg.com/react@latest/umd/react.development.js" crossorigin="anonymous"></script>
    <script src="https://unpkg.com/react-dom@latest/umd/react-dom.development.js"></script>
    <script src="https://unpkg.com/@mui/material@latest/umd/material-ui.development.js" crossorigin="anonymous"></script>
    <script src="https://unpkg.com/babel-standalone@latest/babel.min.js" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
</head>

<html>
<body>
<%-- <h2>Hello <c:out value="${user.username}! ${user.id}"/></h2> --%>

<div>
    <%@ include file="appbar.jsp"%>
    <div class="container browse">
<%--        <c:forEach var="message" items="${event.events}">--%>
<%--            <li><c:out value="${message}" /></li>--%>
<%--        </c:forEach>--%>
<%--            <div class="card">--%>
<%--                <img src="https://media.istockphoto.com/photos/dancing-friends-picture-id501387734?k=20&m=501387734&s=612x612&w=0&h=1mli5b7kpDg428fFZfsDPJ9dyVHsWsGK-EVYZUGWHpI=" alt="Fiesta"/>--%>
<%--                <p>Alta joda en ${location} gor</p>--%>
<%--            </div>--%>
<%--         </c:forEach>--%>

        <div class="card">
            <img src="https://media.istockphoto.com/photos/dancing-friends-picture-id501387734?k=20&m=501387734&s=612x612&w=0&h=1mli5b7kpDg428fFZfsDPJ9dyVHsWsGK-EVYZUGWHpI=" alt="Fiesta"/>
            <p>Alta joda en reco gor</p>
        </div>
        <div class="card">
            <img src="https://media.istockphoto.com/photos/dancing-friends-picture-id501387734?k=20&m=501387734&s=612x612&w=0&h=1mli5b7kpDg428fFZfsDPJ9dyVHsWsGK-EVYZUGWHpI=" alt="Fiesta"/>
            <p>Alta joda en belgra gor</p>
        </div>
        <div class="card">
            <img src="https://media.istockphoto.com/photos/dancing-friends-picture-id501387734?k=20&m=501387734&s=612x612&w=0&h=1mli5b7kpDg428fFZfsDPJ9dyVHsWsGK-EVYZUGWHpI=" alt="Fiesta"/>
            <p>Alta joda en turde gor</p>
        </div>
        <div class="card">
            <img src="https://media.istockphoto.com/photos/dancing-friends-picture-id501387734?k=20&m=501387734&s=612x612&w=0&h=1mli5b7kpDg428fFZfsDPJ9dyVHsWsGK-EVYZUGWHpI=" alt="Fiesta"/>
            <p>Alta joda en adro gor</p>
        </div>
    </div>
</div>

</body>
</html>
