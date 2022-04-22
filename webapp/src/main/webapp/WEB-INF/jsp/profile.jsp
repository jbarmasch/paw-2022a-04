<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<head>
    <%@ include file="include.jsp"%>
    <%@ include file="appbar.jsp"%>
    <title>BotPass</title>
</head>
<html>
<body>
<h2>User profile: <c:out value="${user.username}! ${user.id}"/></h2>
</body>
</html>
