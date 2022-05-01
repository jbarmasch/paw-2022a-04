<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <%@ include file="include.jsp"%>
    <title>BotPass</title>
</head>
<body>
    <%@ include file="appbar.jsp"%>
    <div class="container event">
        <div class="container">
            <div>
                <img class="event_img" alt="Event image" src="<c:url value="/image/${event.imageId}"/>"/>
            </div>
            <div class="event_info">
                <c:if test="${isOwner}">
                    <div class="event_actions">
                        <input type="image" class="icon" src="<c:url value="/resources/svg/edit.svg"/>" alt="Trash icon" onclick="location.href='<c:url value="/events/${event.id}/modify"/>'"/>
                        <form class="transparent" action="<c:url value="/events/${event.id}/delete"/>" method="post">
                            <input class="icon" src="<c:url value="/resources/svg/trash.svg"/>" alt="Trash icon" type="image" name="submit" value=""/>
                        </form>
                    </div>
                </c:if>
                <h3><c:out value="${event.name}"/></h3>
                <p><c:out value="${event.description}"/></p>
                <div class="container">
                    <img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="Location icon"/><span><c:out value="${event.location.name}"/></span>
                </div>
                    <div class="container">
                    <img class="icon" src="<c:url value="/resources/svg/price-tag.svg"/>" alt="Price icon"/>
                    <span>
                        <c:choose>
                            <c:when test="${event.price == 0}">Gratis</c:when>
                            <c:otherwise>$<c:out value="${event.price}"/></c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <div class="container">
                    <img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="Date icon"/><span><c:out value="${event.dateFormatted}"/></span>
                </div>
                <div class="container">
                    <img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="Time icon"/><span><c:out value="${event.timeFormatted}"/></span>
                </div>
                <div class="container">
                    <img class="icon" src="<c:url value="/resources/svg/party.svg"/>" alt="Type icon"/><span><c:out value="${event.type.name}"/></span>
                </div>
                <div class="container tag-container">
                    <c:forEach var="tag" items="${event.tags}">
                        <span class="chip"><c:out value="${tag.name}"/></span>
                    </c:forEach>
                </div>
                <div class="horizontal">
                    <c:if test="${isOwner}">
                        <span>Entradas reservadas: <c:out value="${event.attendance}"/></span>
                    </c:if>
                </div>
                <div>
                    <c:if test="${isOwner}">
                        <c:choose>
                            <c:when test="${!event.soldOut && !event.deleted}">
                                <form action="<c:url value="/events/${event.id}/soldout"/>" class="transparent" method="post">
                                    <input class="uk-button uk-button-text" type="submit" name="submit" value="Marcar como agotado"/>
                                </form>
                            </c:when>
                            <c:when test="${event.soldOut && !event.deleted}">
                                <form action="<c:url value="/events/${event.id}/active"/>" class="transparent" method="post">
                                    <input class="uk-button uk-button-text" type="submit" name="submit" value="Marcar como disponible"/>
                                </form>
                            </c:when>
                        </c:choose>
                    </c:if>
                    <c:if test="${event.deleted}">
                        <b>Este evento ha sido borrado.</b>
                    </c:if>
                </div>
            </div>
        </div>

        <c:if test="${!isOwner}">
            <c:url value="/events/${event.id}" var="postPath"/>
            <form:form novalidate="true" modelAttribute="bookForm" class="center bookForm" action="${postPath}" method="post" id="bookForm">
                <c:choose>
                    <c:when test="${event.soldOut}">
                        <c:set var="disabled" scope="session" value="true"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="disabled" scope="session" value="false"/>
                    </c:otherwise>
                </c:choose>
                <c:set var="ticketsLeft" scope="session" value="${event.maxCapacity}"/>
                <div class="horizontal center">
                    <form:label path="qty">*Cantidad de entradas: </form:label>
                    <form:input class="uk-input" type="number" disabled="${disabled}" path="qty" min="1" max="${event.maxCapacity}" required="true" id="qty"/>
                    <form:errors path="qty" cssClass="error-message" element="span"/>
                    <spring:message code="Min.bookForm.qty" var="qtyMinSizeError"/>
                    <spring:message code="Max.bookForm.qtyStr" var="qtyMaxSizeError"/>
                    <spring:message code="NotNull.bookForm.qty" var="qtyNullError"/>
                    <span class="formError"></span>
                </div>

                <c:if test="${!disabled}">
                    <span>(Disponibles: ${event.maxCapacity})</span>
                </c:if>
                <c:choose>
                    <c:when test="${event.soldOut}">
                        <div class="container event_buttons">
                            <input disabled class="uk-button" type="submit" name="submit" value="Agotado"/>
                        </div>
                    </c:when>
                    <c:when test="${!event.deleted}">
                        <div class="container event_buttons">
                            <input class="uk-button" type="submit" name="submit" value="Reservar"/>
                        </div>
                    </c:when>
                </c:choose>
            </form:form>
        </c:if>
    </div>


    <c:if test="${similarEventsSize > 0}">
        <c:choose>
            <c:when test="${similarEventsSize > 1}">
                <div>
                    <h4 class="subtitle">Estos eventos también podrían interesarte</h4>
                </div>
            </c:when>
            <c:otherwise>
                <div>
                    <h4 class="subtitle">Este evento también podría interesarte</h4>
                </div>
            </c:otherwise>
        </c:choose>
    <div class="container multi-browse">

            <c:forEach var="event" items="${similarEvents}">
                <div class="card uk-card uk-card-default" onclick="location.href='<c:url value="/events/${event.id}"/>'">
                    <div class="uk-card-media-top">
                        <img class="card_img" src="<c:url value="/image/${event.imageId}"/>" alt="Party Image">
                    </div>
                    <div class="uk-card-body">
                        <h3 class="uk-card-title"><c:out value="${event.name}"/></h3>
                        <div class="container card_body">
                            <div class="card_info">
                                <img class="icon" src="<c:url value="/resources/svg/price-tag.svg"/>" alt="Price icon"/>
                                <span>
                                                <c:choose>
                                                    <c:when test="${event.price == 0}">Gratis</c:when>
                                                    <c:otherwise>$<c:out value="${event.price}"/></c:otherwise>
                                                </c:choose>
                                </span>
                                <img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="Location icon"/><span><c:out value="${event.location.name}"/></span>
                            </div>
                            <div class="card_info">
                                <img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="Date icon"/><span><c:out value="${event.dateFormatted}"/></span>
                                <img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="Time icon"/><span><c:out value="${event.timeFormatted}"/></span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>

    </div>
    </c:if>

    <c:if test="${popularEventsSize > 0}">
        <c:choose>
            <c:when test="${popularEventsSize > 1}">
                <div>
                    <h4 class="subtitle">Otras personas también reservaron para estos eventos</h4>
                </div>
            </c:when>
            <c:otherwise>
                <div>
                    <h4 class="subtitle">Otras personas también reservaron para este evento</h4>
                </div>
            </c:otherwise>
        </c:choose>
    <div class="container multi-browse">


            <c:forEach var="event" items="${popularEvents}">
                <div class="card uk-card uk-card-default" onclick="location.href='<c:url value="/events/${event.id}"/>'">
                    <div class="uk-card-media-top">
                        <img class="card_img" src="<c:url value="/image/${event.imageId}"/>" alt="Party Image">
                    </div>
                    <div class="uk-card-body">
                        <h3 class="uk-card-title"><c:out value="${event.name}"/></h3>
                        <div class="container card_body">
                            <div class="card_info">
                                <img class="icon" src="<c:url value="/resources/svg/price-tag.svg"/>" alt="Price icon"/>
                                <span>
                                                <c:choose>
                                                    <c:when test="${event.price == 0}">Gratis</c:when>
                                                    <c:otherwise>$<c:out value="${event.price}"/></c:otherwise>
                                                </c:choose>
                                            </span>
                                <img class="icon" src="<c:url value="/resources/svg/location-pin.svg"/>" alt="Location icon"/><span><c:out value="${event.location.name}"/></span>
                            </div>
                            <div class="card_info">
                                <img class="icon" src="<c:url value="/resources/svg/date.svg"/>" alt="Date icon"/><span><c:out value="${event.dateFormatted}"/></span>
                                <img class="icon" src="<c:url value="/resources/svg/time.svg"/>" alt="Time icon"/><span><c:out value="${event.timeFormatted}"/></span>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
    </div>
    </c:if>
</body>
</html>

<script type="text/javascript">
    (function() {
        var qty = document.getElementById('qty');
        var form = document.getElementById('bookForm');

        if (qty === null || form === null)
            return;

        var checkQtyValidity = function() {
            if (qty.validity.rangeUnderflow) {
                qty.setCustomValidity('${qtyMinSizeError}');
                updateQtyMessage();
            } else if (qty.validity.rangeOverflow) {
                qty.setCustomValidity('${qtyMaxSizeError} ' + ${ticketsLeft} + '.');
                updateQtyMessage();
            } else if (qty.validity.valueMissing) {
                qty.setCustomValidity('${qtyNullError}');
                updateQtyMessage();
            } else {
                qty.setCustomValidity('');
            }
        };

        var updateQtyMessage = function() {
            form.getElementsByClassName('formError')[0].innerHTML = qty.validationMessage;
        }

        qty.addEventListener('change', checkQtyValidity, false);
        qty.addEventListener('keyup', checkQtyValidity, false);

        form.addEventListener('submit', function(event) {
            if (form.classList) form.classList.add('submitted');
            checkQtyValidity();
            if (!this.checkValidity()) {
                event.preventDefault();
                updateQtyMessage();
            }
        }, false);
    }());
</script>
