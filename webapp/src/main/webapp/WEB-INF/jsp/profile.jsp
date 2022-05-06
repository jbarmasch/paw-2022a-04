<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<html>
<body>
    <%@ include file="appbar.jsp"%>
    <h2>User profile: <c:out value="${user.username}! ${user.id}"/></h2>
    <span><c:out value="${user.rating}"/></span>
</body>
</html>
