<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<link href="/css/style.css" rel="stylesheet" type="text/css">

<title>BotPass</title>
<html>
<body>
<!-- <h2>Hello <c:out value="${user.username}! ${user.id}"/></h2> -->

<div>
    <div class="appbar container">
        <img class="logo" src="https://media-public.canva.com/uH36Y/MAELBWuH36Y/1/tl.png" alt="BotPass"/>
        <input type="button" value="Home"/>
    </div>
    <div class="container browse">
        <c:forEach var="message" items="${event.events}">
            <li><c:out value="${message}" /></li>
        </c:forEach>
        <div class="card">
            <img src="https://media.istockphoto.com/photos/dancing-friends-picture-id501387734?k=20&m=501387734&s=612x612&w=0&h=1mli5b7kpDg428fFZfsDPJ9dyVHsWsGK-EVYZUGWHpI=" alt="Fiesta"/>
            <p>Alta joda en sani gor</p>
        </div>
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
