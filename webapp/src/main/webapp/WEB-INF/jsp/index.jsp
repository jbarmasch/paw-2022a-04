<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <title>BotPass</title>
    <script src="https://unpkg.com/react@latest/umd/react.development.js" crossorigin="anonymous"></script>
    <script src="https://unpkg.com/react-dom@latest/umd/react-dom.development.js"></script>
    <script src="https://unpkg.com/@mui/material@latest/umd/material-ui.development.js" crossorigin="anonymous"></script>
    <script src="https://unpkg.com/babel-standalone@latest/babel.min.js" crossorigin="anonymous"></script>
<%--    <script src="/resources/js/index.js"/>--%>
<%--    <script src="/resources/js/card.js"/>--%>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
<%--    <link href="https://cdn.muicss.com/mui-0.10.3/css/mui.min.css" rel="stylesheet" type="text/css" />--%>
<%--    <script src="https://cdn.muicss.com/mui-0.10.3/js/mui.min.js"></script>--%>
</head>

<body>
<%@ include file="appbar.jsp"%>
<div>
    <div class="container browse">
        <div class="Button"></div>

        <c:forEach var="event" items="${events}">
            <div class="card" onclick="location.href='<c:url value="/event"><c:param name="id" value="${event.id}"/></c:url>'">
                <img src="https://media.istockphoto.com/photos/dancing-friends-picture-id501387734?k=20&m=501387734&s=612x612&w=0&h=1mli5b7kpDg428fFZfsDPJ9dyVHsWsGK-EVYZUGWHpI=" alt="Fiesta"/>
                <div class="container card_info">
                    <div class="container">
                        <img class="icon" src="https://freepikpsd.com/file/2019/10/group-people-icon-png-Transparent-Images.png" alt="capacity"/>
                        <p><c:out value="${event.maxCapacity}"/></p>
                    </div>
                    <p><c:out value="${event.location}"/></p>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
