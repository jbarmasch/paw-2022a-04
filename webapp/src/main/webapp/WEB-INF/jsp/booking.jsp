<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
    <c:import url="appbar.jsp"/>
    <div class="container event">
        <div class="vertical">
                <div>
                    <h2 class="title2"><spring:message code="mail.booking"/></h2>
                </div>

                <table class="tickets tickets-table">
                    <tr>
                        <th><spring:message code="tickets.ticketName"/></th>
                        <th><spring:message code="tickets.ticketQty"/></th>
                        <th><spring:message code="tickets.ticketPrice"/></th>
                    </tr>
                    <c:set var="total" value="0"/>
                    <c:forEach var="ticketBooking" items="${eventBooking.ticketBookings}">
                        <c:set var="total" value="${total + ticketBooking.qty * ticketBooking.ticket.price}"/>
                        <tr>
                            <td>
                                <span><c:out value="${ticketBooking.ticket.ticketName}"/></span>
                            </td>
                            <td class="table-number">
                                <span><c:out value="${ticketBooking.qty}"/></span>
                            </td>
                            <td class="table-number">
                                <c:choose>
                                    <c:when test="${ticketBooking.ticket.price > 0}">
                                        <span>$<c:out value="${ticketBooking.ticket.price}"/></span>
                                    </c:when>
                                    <c:otherwise>
                                        <spring:message code="event.free"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <th><spring:message code="tickets.total"/></th>
                        <td></td>
                        <td class="table-number">$<c:out value="${total}"/></td>
                    </tr>
                </table>
        </div>
        <div>
            <img src="data:image/png;base64,${image}" alt="QR"/>
        </div>
    </div>
</body>
</html>

