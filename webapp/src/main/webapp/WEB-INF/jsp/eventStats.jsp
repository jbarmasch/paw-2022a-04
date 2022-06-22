<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
    <%@ include file="appbar.jsp"%>
    <div class="container event">
        <div class="vertical tickets sep-top-xl">
            <table class="stats-table tickets-table">
                <tr>
                    <th><spring:message code="stats.statsEvents"/></th>
                </tr>
                <tr>
                    <td><spring:message code="stats.eventsCreated"/></td>
                    <td class="table-number">${stats.eventsCreated}</td>
                </tr>
                <tr>
                    <td><spring:message code="stats.bookingsGotten"/></td>
                    <td class="table-number">${stats.bookingsGotten}</td>
                </tr>
                <tr>
                    <td><spring:message code="stats.attendanceRatio"/></td>
                    <td class="table-number"><fmt:formatNumber type="number" maxFractionDigits="2" value="${stats.attendance * 100}"/>%</td>
                </tr>
                <tr>
                    <td><spring:message code="stats.popularEvent"/></td>
                    <td class="table-number"><a href="<c:url value="/events/${stats.popularEvent.id}"/>">${stats.popularEvent.name}</a></td>
                </tr>
                <tr>
                    <td><spring:message code="stats.moneyEarned"/></td>
                    <td class="table-number">$${stats.income}</td>
                </tr>
            </table>
        </div>
        <c:if test="${eventStats != null}">
        <div class="tickets container vertical">
            <table class="stats-table tickets-table">
                <tr>
                    <th><spring:message code="stats.forPopularEvent"/></th>
                </tr>
                <tr>
                    <td><spring:message code="stats.bookingsGotten"/></td>
                    <td class="table-number">${eventStats.booked}</td>
                </tr>
                <tr>
                    <td><spring:message code="stats.saleRatio"/></td>
                    <td class="table-number"><fmt:formatNumber type="number" maxFractionDigits="2" value="${eventStats.saleRatio * 100}"/>%</td>
                </tr>
                <tr>
                    <td><spring:message code="stats.attendance"/></td>
                    <td class="table-number">${eventStats.attended}</td>
                </tr>
                <tr>
                    <td><spring:message code="stats.attendanceRatio"/></td>
                    <td class="table-number"><fmt:formatNumber type="number" maxFractionDigits="2" value="${eventStats.attendance * 100}"/>%</td>
                </tr>
                <tr>
                    <td><spring:message code="stats.expectedMoneyEarned"/></td>
                    <td class="table-number">$${eventStats.expectedIncome}</td>
                </tr>
                <tr>
                    <td><spring:message code="stats.moneyEarned"/></td>
                    <td class="table-number">$${eventStats.income}</td>
                </tr>
            </table>
        </div>
        </c:if>
    </div>
</body>
</html>
