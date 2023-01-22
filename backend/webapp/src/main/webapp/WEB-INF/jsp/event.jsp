<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@ include file="include.jsp" %>
    <title>BotPass</title>
</head>
<body>
    <%@ include file="appbar.jsp" %>
    <div class="container event sep-bot-xl">
        <spring:message code="delete" var="delete"/>
        <div class="container align-center">
            <div class="event_img_container">
                <spring:message var="altEventMessage" code="event.imageAlt"/>
                <img class="event_img" alt="${altEventMessage}" src="<c:url value="/image/${event.image.id}"/>"/>
                <c:if test="${event.soldOut}">
                    <spring:message code="event.soldOut" var="soldOut"/>
                    <img class="soldout_event" src="<c:url value="/resources/png/sold_out.png"/>" alt="${soldOut}"/>
                </c:if>
            </div>
            <div class="event_info">
                <c:if test="${isOwner && event.date >= currentDate}">
                    <div class="event_actions_left">
                        <spring:message var="altStatsIcon" code="event.statsAlt"/>
                        <input type="image" class="stats_icon" src="<c:url value="/resources/svg/stats.svg"/>"
                               alt="${altStatsIcon}"
                               onclick="location.href='<c:url value="/events/${event.id}/stats"/>'"/>
                    </div>
                    <div class="event_actions">
                        <spring:message var="altDeleteIcon" code="event.deleteAlt"/>
                        <spring:message var="altEditIcon" code="event.editAlt"/>
                        <input type="image" class="icon" src="<c:url value="/resources/svg/edit.svg"/>"
                               alt="${altEditIcon}"
                               onclick="location.href='<c:url value="/events/${event.id}/modify"/>'"/>
                        <input class="icon" src="<c:url value="/resources/svg/trash.svg"/>" alt="${altDeleteIcon}"
                               type="image" uk-toggle="target: #confirmation"/>
                        <div id="confirmation" uk-modal>
                            <div class="uk-modal-dialog">
                                <button class="uk-modal-close-default" type="button" uk-close></button>
                                <div class="uk-modal-header">
                                    <h3 class="title2 uk-modal-title"><spring:message
                                            code="event.confirmationDelete"/></h3>
                                </div>
                                <div class="uk-modal-body">
                                    <form class="transparent form-marginless"
                                          action="<c:url value="/events/${event.id}/delete"/>" method="post">
                                        <input class="accept-button-modal cancel_button" type="submit"
                                               value="${delete}"/>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
                <h3 class="event_name"><c:out value="${event.name}"/></h3>
                <p class="event_desc"><c:out value="${event.description}"/></p>
                <div class="container">
                    <spring:message var="altLocationIcon" code="event.locationAlt"/>
                    <img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>"
                         alt="${altLocationIcon}"/><span><c:out value="${event.location.name}"/></span>
                </div>
                <div class="container">
                    <spring:message var="altMinPriceIcon" code="event.priceAlt"/>
                    <img class="icon" src="<c:url value="/resources/svg/price-tag.svg"/>" alt="${altMinPriceIcon}"/>
                    <span>
                        <c:choose>
                            <c:when test="${event.soldOut}">
                                <spring:message code="event.soldOut"/>
                            </c:when>
                            <c:when test="${event.minPrice != null && event.minPrice == 0}"><spring:message
                                    code="event.free"/></c:when>
                            <c:when test="${event.minPrice != null && event.minPrice > 0}"><spring:message
                                    code="event.starting"/>
                                &nbsp;$<c:out value="${event.minPrice}"/>
                            </c:when>
                            <c:otherwise>
                                <spring:message code="event.notApplies"/>
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <div class="container">
                    <spring:message var="altDateIcon" code="event.dateAlt"/>
                    <img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="${altDateIcon}"/><span><c:out
                        value="${event.dateFormatted}"/></span>
                </div>
                <div class="container">
                    <spring:message var="altTimeIcon" code="event.timeAlt"/>
                    <img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="${altTimeIcon}"/><span><c:out
                        value="${event.timeFormatted}"/></span>
                </div>
                <div class="container">
                    <spring:message var="altTypeIcon" code="event.typeAlt"/>
                    <img class="icon" src="<c:url value="/resources/svg/party.svg"/>" alt="${altTypeIcon}"/><span><c:out
                        value="${event.type.name}"/></span>
                </div>
                <div class="container tag-container">
                    <c:forEach var="tag" items="${event.tags}">
                        <span class="chip" onclick="location.href='<c:url value="/events?tags=${tag.id}"/>'"><c:out
                                value="${tag.name}"/></span>
                    </c:forEach>
                </div>
                <div class="container">
                    <spring:message var="altUserIcon" code="event.altUser"/>
                    <img class="icon" src="<c:url value="/resources/svg/user.svg"/>" alt="${altUserIcon}"/>
                    <a href="<c:url value="/profile/${event.organizer.id}"/>"><c:out
                            value="${event.organizer.username}"/></a>
                </div>
                <c:if test="${event.minAge != null}">
                    <div class="container min_age">
                        <spring:message code="event.minimumAge" arguments="${event.minAge}"/>
                    </div>
                </c:if>
                <div>
                    <c:if test="${isOwner && event.date >= currentDate}">
                        <c:choose>
                            <c:when test="${!event.soldOut && !event.deleted}">
                                <spring:message var="setSoldOut" code="event.setSoldout"/>
                                <form action="<c:url value="/events/${event.id}/soldout"/>" class="transparent"
                                      method="post">
                                    <input class="uk-button uk-button-text" type="submit" name="submit"
                                           value="${setSoldOut}"/>
                                </form>
                            </c:when>
                            <c:when test="${event.soldOut && !event.deleted}">
                                <spring:message var="setAvailable" code="event.setAvailable"/>
                                <form class="transparent" method="post">
                                    <input class="uk-button uk-button-text" type="submit" name="submit"
                                           value="${setAvailable}"/>
                                </form>
                            </c:when>
                        </c:choose>
                    </c:if>
                    <c:if test="${event.deleted}">
                        <b><spring:message code="event.deleted"/></b>
                    </c:if>
                </div>
            </div>
        </div>

        <c:choose>
            <c:when test="${!isOwner}">
                <c:url value="/events/${event.id}" var="postPath"/>
                <form:form novalidate="true" modelAttribute="bookForm" class="center bookForm" action="${postPath}"
                           method="post" id="bookForm">
                    <c:choose>
                        <c:when test="${event.soldOut}">
                            <c:set var="disabled" scope="session" value="true"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="disabled" scope="session" value="false"/>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${event.date >= currentDate}">
                        <c:choose>
                            <c:when test="${ticketsSize > 0}">
                                <table class="tickets-table booking-table">
                                    <tr>
                                        <th><spring:message code="tickets.ticketName"/></th>
                                        <th><spring:message code="tickets.ticketPrice"/></th>
                                        <th><spring:message code="tickets.ticketQty"/></th>
                                    </tr>
                                    <c:set var="i" scope="session" value="0"/>
                                    <c:set var="ticketsLeft" scope="session" value="${0}"/>
                                    <c:forEach var="ticket" items="${event.tickets}">
                                        <c:if test="${(ticket.starting == null || ticket.starting <= currentDate) && (ticket.until == null || ticket.until >= currentDate)}">
                                            <c:set var="ticketsLeft" scope="session"
                                                   value="${ticketsLeft + ticket.ticketsLeft}"/>
                                            <tr>
                                                <td>
                                                    <span><c:out value="${ticket.ticketName}"/></span>
                                                </td>
                                                <td class="table-number">
                                                    <c:choose>
                                                        <c:when test="${ticket.price > 0}">
                                                            <span>$<c:out value="${ticket.price}"/></span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <spring:message code="event.free"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="table-number">
                                                    <c:choose>
                                                        <c:when test="${ticket.ticketsLeft > 0}">
                                                            <input type="number" class="hidden" id="qtyMax${i}"
                                                                   value="${ticket.ticketsLeft < ticket.maxPerUser ? ticket.ticketsLeft : ticket.maxPerUser}"/>
                                                            <form:select id="qty${i}" class="uk-select"
                                                                         htmlEscape="true" multiple="false"
                                                                         path="bookings[${i}].qty" required="true">
                                                                <form:option value="0" selected="true"/>
                                                                <c:if test="${ticket.ticketsLeft > 0}">
                                                                    <c:forEach var="j" begin="${1}" step="1"
                                                                               end="${ticket.ticketsLeft < ticket.maxPerUser ? ticket.ticketsLeft : ticket.maxPerUser}">
                                                                        <form:option value="${j}"/>
                                                                    </c:forEach>
                                                                </c:if>
                                                            </form:select>
                                                            <span class="formError"></span>
                                                            <spring:message code="NotNull.bookForm.allQty"
                                                                            var="allQtyNullError"/>
                                                            <input type="text" class="hidden" id="errorQty"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <form:input id="qty${i}" value="0" class="hidden"
                                                                        path="bookings[${i}].qty" required="true"/>
                                                            <span class="soldout-text"><spring:message
                                                                    code="event.soldOut"/></span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <form:errors path="bookings[${i}].qty" cssClass="error-message"
                                                                 element="span"/>
                                                    <spring:message code="Min.bookForm.qty" var="qtyMinSizeError"/>
                                                    <spring:message code="Max.bookForm.qtyStr" var="qtyMaxSizeError"/>
                                                    <spring:message code="NotNull.bookForm.qty" var="qtyNullError"/>

                                                    <form:input class="hidden" type="number" value="${ticket.id}"
                                                                path="bookings[${i}].ticketId"/>
                                                </td>
                                                <c:if test="${!disabled && ticket.ticketsLeft > 0 && ticket.ticketsLeft <= ticket.qty * .20}">
                                                    <td>
                                                        <span><spring:message code="event.fewTickets"/></span>
                                                    </td>
                                                </c:if>
                                            </tr>
                                            <c:set var="i" scope="session" value="${i + 1}"/>
                                        </c:if>
                                    </c:forEach>
                                    <form:errors path="bookings" cssClass="error-message" element="span"/>
                                    <form:errors path="bookings[${i}]" cssClass="error-message" element="span"/>
                                    <form:errors path="bookings[${i}].ticketId" cssClass="error-message"
                                                 element="span"/>
                                </table>

                                <c:choose>
                                    <c:when test="${event.soldOut || ticketsLeft <= 0}">
                                        <div class="container event_buttons">
                                            <spring:message var="soldOut" code="event.soldOut"/>
                                            <input disabled class="uk-button book-button" type="submit" name="submit"
                                                   value="${soldOut}"/>
                                        </div>
                                    </c:when>
                                    <c:when test="${!event.deleted}">
                                        <div class="container event_buttons">
                                            <spring:message var="book" code="event.book"/>
                                            <input class="uk-button book-button" type="submit" name="submit"
                                                   value="${book}"/>
                                        </div>
                                    </c:when>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <spring:message code="event.noTickets"/>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </form:form>
            </c:when>
            <c:otherwise>
                <div class="tickets container vertical">
                    <table class="tickets-table">
                        <tr>
                            <th><spring:message code="tickets.ticketName"/></th>
                            <th><spring:message code="event.ticketsLeft"/></th>
                            <th><spring:message code="tickets.ticketPrice"/></th>
                            <th><spring:message code="tickets.ticketBooked"/></th>
                            <th><spring:message code="tickets.maxUser"/></th>
                            <th><spring:message code="tickets.starting"/></th>
                            <th><spring:message code="tickets.until"/></th>
                            <c:if test="${event.date >= currentDate}">
                                <th><spring:message code="tickets.actions"/></th>
                            </c:if>
                        </tr>

                        <c:set var="j" value="-1"/>
                        <c:forEach var="ticket" items="${event.tickets}">
                            <c:set var="j" value="${j + 1}"/>
                            <tr>
                                <td>
                                    <span><c:out value="${ticket.ticketName}"/></span>
                                </td>
                                <td class="table-number">
                                    <span><c:out value="${ticket.qty - ticket.booked}"/></span>
                                </td>
                                <td class="table-number">
                                    <span>$<c:out value="${ticket.price}"/></span>
                                </td>
                                <td class="table-number">
                                    <span><c:out value="${ticket.booked}"/></span>
                                </td>
                                <td class="table-number">
                                    <span><c:out value="${ticket.maxPerUser}"/></span>
                                </td>
                                <td class="table-number">
                                    <c:if test="${ticket.starting != null}">
                                        <span><c:out value="${ticket.startingDateFormatted}"/>&nbsp;<c:out
                                                value="${ticket.startingTimeFormatted}"/></span>
                                    </c:if>
                                </td>
                                <td class="table-number">
                                    <c:if test="${ticket.until != null}">
                                        <span><c:out value="${ticket.untilDateFormatted}"/>&nbsp;<c:out
                                                value="${ticket.untilTimeFormatted}"/></span>
                                    </c:if>
                                </td>
                                <c:if test="${event.date >= currentDate}">
                                    <td>
                                        <div class="horizontal">
                                            <input type="image" class="icon"
                                                   src="<c:url value="/resources/svg/edit.svg"/>" alt="${altEditIcon}"
                                                   onclick="location.href='<c:url
                                                           value="/events/${event.id}/modify-ticket/${ticket.id}"/>'"/>
                                            <input class="icon" src="<c:url value="/resources/svg/trash.svg"/>"
                                                   alt="${altDeleteIcon}" type="image"
                                                   uk-toggle="target: #confirmation${j}" value=""/>
                                            <div id="confirmation${j}" uk-modal>
                                                <div class="uk-modal-dialog">
                                                    <button class="uk-modal-close-default" type="button"
                                                            uk-close></button>
                                                    <div class="uk-modal-header">
                                                        <h3 class="uk-modal-title title2"><spring:message
                                                                code="ticket.confirmationDelete"/></h3>
                                                    </div>
                                                    <div class="uk-modal-body">
                                                        <form class="transparent form-marginless"
                                                              action="<c:url value="/events/${event.id}/delete-ticket/${ticket.id}"/>"
                                                              method="post">
                                                            <input class="accept-button-modal cancel_button"
                                                                   type="submit" value="${delete}">
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                            <form class="transparent form-marginless"
                                                  action="<c:url value="/events/${event.id}/delete-ticket/${ticket.id}"/>"
                                                  method="post">
                                            </form>
                                        </div>
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                    </table>
                    <spring:message code="event.addTicketAlt" var="altAddTicketIcon"/>
                    <c:if test="${event.date >= currentDate}">
                        <c:choose>
                            <c:when test="${ticketsSize >= 5}">
                                <input type="image" class="icon fab" src="<c:url value="/resources/svg/add.svg"/>"
                                       alt="${altAddTicketIcon}" onclick="maxTicketsReached()"/>
                            </c:when>
                            <c:otherwise>
                                <input type="image" class="icon fab" src="<c:url value="/resources/svg/add.svg"/>"
                                       alt="${altAddTicketIcon}"
                                       onclick="location.href='<c:url value="/events/${event.id}/add-ticket"/>'"/>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <spring:message code="event.maxTicketsReached" var="maxTicketsReached"/>

    <c:set var="similarEvents" value="${similarEvents}" scope="request"/>
    <c:set var="similarEventsSize" value="${similarEventsSize}" scope="request"/>
    <c:import url="similarEvents.jsp"/>

    <c:set var="popularEvents" value="${popularEvents}" scope="request"/>
    <c:set var="popularEventsSize" value="${popularEventsSize}" scope="request"/>
    <c:import url="popularEvents.jsp"/>
</body>
</html>

<script type="text/javascript">
    function maxTicketsReached() {
        UIkit.notification("${maxTicketsReached}", {timeout: 4000}, {status: 'danger'});
    }

    (function () {
        var errorQty = document.getElementById('errorQty');
        var form = document.getElementById('bookForm');

        if (form === null)
            return;

        var checkQtyValidityWithNumber = function (i) {
            var qty = document.getElementById('qty' + i);
            var qtyMax = document.getElementById('qtyMax' + i);
            if (parseInt(qty.value) < 0) {
                qty.setCustomValidity('${qtyMinSizeError}');
                updateQtyMessage(i);
            } else if (parseInt(qty.value) > parseInt(qtyMax.value)) {
                qty.setCustomValidity('${qtyMaxSizeError} ' + qtyMax.value + '.');
                updateQtyMessage(i);
            } else {
                qty.setCustomValidity('');
            }
        };

        var checkQtyValidityAll = function () {
            var aux = 0;
            for (var j = 0; j < parseInt("${i}"); j++) {
                var qty = document.getElementById('qty' + j);
                if (qty.value === '' || parseInt(qty.value) === 0)
                    aux++;
            }
            if (aux === parseInt("${i}")) {
                errorQty.setCustomValidity('${allQtyNullError}');
                UIkit.notification("${allQtyNullError}", {timeout: 4000}, {status: 'danger'});
            } else {
                errorQty.setCustomValidity('');
            }
        };


        var checkQtyValidity = function (i) {
            i = i.currentTarget.i;
            var qty = document.getElementById('qty' + i);
            var qtyMax = document.getElementById('qtyMax' + i);
            if (parseInt(qty.value) < 0) {
                qty.setCustomValidity('${qtyMinSizeError}');
                updateQtyMessage(i);
            } else if (parseInt(qty.value) > parseInt(qtyMax.value)) {
                qty.setCustomValidity('${qtyMaxSizeError} ' + qtyMax.value + '.');
                updateQtyMessage(i);
            } else {
                qty.setCustomValidity('');
            }
        };

        var updateQtyMessage = function (i) {
            var qty = document.getElementById('qty' + i);
            form.getElementsByClassName('formError')[i].innerHTML = qty.validationMessage;
        }

        for (var j = 0; j < parseInt("${i}"); j++) {
            var qty = document.getElementById('qty' + j);
            qty.addEventListener('change', checkQtyValidity, false);
            qty.i = j;
            qty.addEventListener('keyup', checkQtyValidity, false);
            qty.i = j;
        }

        form.addEventListener('submit', function (event) {
            if (form.classList) form.classList.add('submitted');
            for (var j = 0; j < parseInt("${i}"); j++)
                checkQtyValidityWithNumber(j);
            checkQtyValidityAll();
            if (!this.checkValidity()) {
                event.preventDefault();
            }
        }, false);
    }());
</script>