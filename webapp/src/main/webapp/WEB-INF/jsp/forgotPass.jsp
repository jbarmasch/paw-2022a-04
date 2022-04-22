<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <%@ include file="include.jsp"%>
    <%@ include file="appbar.jsp"%>
    <title>BotPass</title>
</head>
<body>
<div class="only-element">
    <form>
        <h3>Forgot password</h3>
        <div class="space-bet sep-top">
            <input id="email" placeholder="Email" name="email" type="text"/>
        </div>
        <div class="center sep-top-xl">
            <input type="submit" value="Send email"/>
        </div>
    </form>
</div>
</body>
</html>