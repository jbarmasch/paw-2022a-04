<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
    <%@ include file="appbar.jsp"%>
    <div class="container event sep-bot-xl">
        <div class="tickets container vertical">
            <table class="stats-table tickets-table">
                <tr>
                    <th><spring:message code="stats.forEvent" arguments="${eventStats.eventName}"/></th>
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

                <div class="tickets container vertical">
                    <table class="tickets-table">
                        <tr>
                            <th><spring:message code="tickets.ticketName"/></th>
                            <th><spring:message code="event.ticketsLeft"/></th>
                            <th><spring:message code="tickets.ticketPrice"/></th>
                            <th><spring:message code="tickets.ticketBooked"/></th>
                            <th><spring:message code="stats.attendance"/></th>
                            <th><spring:message code="stats.saleRatio"/></th>
                            <th><spring:message code="stats.attendanceRatio"/></th>
                            <th><spring:message code="stats.moneyEarned"/></th>
                        </tr>

                        <c:set var="j" value="-1"/>
                        <c:if test="${ticketsStatsSize > 0}">
                            <c:forEach var="ticketStat" items="${ticketsStats}">
                                <c:set var="j" value="${j + 1}"/>
                                <tr>
                                    <td>
                                        <span><c:out value="${ticketStat.ticketName}"/></span>
                                    </td>
                                    <td class="table-number">
                                        <span><c:out value="${ticketStat.qty - ticketStat.booked}"/></span>
                                    </td>
                                    <td class="table-number">
                                        <span>$<c:out value="${ticketStat.price}"/></span>
                                    </td>
                                    <td class="table-number">
                                        <span><c:out value="${ticketStat.booked}"/></span>
                                    </td>
                                    <td class="table-number">
                                        <span><c:out value="${ticketStat.realQty}"/></span>
                                    </td>
                                    <td class="table-number">
                                        <span><fmt:formatNumber type="number" maxFractionDigits="2" value="${ticketStat.saleRatio * 100}"/>%</span>
                                    </td>
                                    <td class="table-number">
                                        <span><fmt:formatNumber type="number" maxFractionDigits="2" value="${ticketStat.attendance * 100}"/>%</span>
                                    </td>
                                    <td class="table-number">
                                        <span><c:out value="$${ticketStat.income}"/></span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                    </table>
                </div>
    </div>
</body>
</html>
