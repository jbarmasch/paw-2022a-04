<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%@ include file="appbar.jsp" %>
    <h3><c:out value="${event.name}"/></h3>
    <p><c:out value="${event.description}"/></p>
</body>
</html>