<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
<%--    <title>BotPass</title> --%>
<%--    <script src="https://unpkg.com/react@latest/umd/react.development.js" crossorigin="anonymous"></script> --%>
<%--    <script src="https://unpkg.com/react-dom@latest/umd/react-dom.development.js"></script> --%>
<%--    <script src="https://unpkg.com/@mui/material@latest/umd/material-ui.development.js" crossorigin="anonymous"></script> --%>
<%--    <script src="https://unpkg.com/babel-standalone@latest/babel.min.js" crossorigin="anonymous"></script> --%>
<%--    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap"/> --%>
<%--    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/> --%>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/uikit@3.13.7/dist/css/uikit.min.css" />
    <script src="https://cdn.jsdelivr.net/npm/uikit@3.13.7/dist/js/uikit.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/uikit@3.13.7/dist/js/uikit-icons.min.js"></script>
</head>

<body>
<%@ include file="appbar.jsp"%>
<div>
    <div class="container browse">
        <c:forEach var="event" items="${events}">


                <div class="card uk-card uk-card-default" onclick="location.href='<c:url value="/event/${event.id}"/>'">
                    <div class="uk-card-media-top">
                        <img class="card_img" src="https://media.istockphoto.com/photos/dancing-friends-picture-id501387734?k=20&m=501387734&s=612x612&w=0&h=1mli5b7kpDg428fFZfsDPJ9dyVHsWsGK-EVYZUGWHpI=" alt="Party Image">
                    </div>
                    <div class="uk-card-body">
                        <h3 class="uk-card-title"><c:out value="${event.name}"/></h3>
                        <div class="container card_info">
                            <span>$<c:out value="${event.price}"/></span>
                            <span><c:out value="${event.location}"/></span>
                        </div>
                    </div>
                </div>


        </c:forEach>
    </div>
</div>
</body>
</html>
