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
    <div class="home">
        <div>
            <c:url value="/events" var="postPath"/>
            <form:form novalidate="true" modelAttribute="filterForm" action="${postPath}" method="post" class="filter" id="filterForm">
                <b>Filtros:</b>
                <div>
                    <form:label path="locations">Ubicación: </form:label>
                    <form:select class="uk-select" htmlEscape="true" multiple="true" path="locations">
                        <form:options items="${allLocations}" itemValue="id" itemLabel="name"/>
                    </form:select>
                    <form:errors path="locations" cssClass="error-message" element="span"/>
                </div>
                <div>
                    <form:label path="types">Tipo: </form:label>
                    <form:select class="uk-select" htmlEscape="true" multiple="true" path="types">
                        <form:options items="${allTypes}" itemValue="id" itemLabel="name"/>
                    </form:select>
                    <form:errors path="locations" cssClass="error-message" element="span"/>
                    <form:errors path="types" cssClass="error-message" element="span"/>
                </div>
                <div>
                    <form:label path="minPrice">Precio mínimo: </form:label>
                    <form:input class="uk-input" type="number" path="minPrice" min="0" step="0.01" id="minPrice"/>
                    <form:errors path="minPrice" cssClass="error-message" element="span"/>
                    <spring:message code="DecimalMin.filterForm.minPrice" var="minPriceMinError"/>
                    <spring:message code="Pattern.filterForm.minPrice" var="minPriceTypeError"/>
                    <span class="formError"></span>
                </div>
                <div>
                    <form:label path="maxPrice">Precio máximo: </form:label>
                    <form:input class="uk-input" type="number" path="maxPrice" min="0" step="0.01" id="maxPrice"/>
                    <form:errors path="maxPrice" cssClass="error-message" element="span"/>
                    <spring:message code="DecimalMin.filterForm.maxPrice" var="maxPriceMinError"/>
                    <spring:message code="Pattern.filterForm.maxPrice" var="maxPriceTypeError"/>
                    <span class="formError"></span>
                </div>
                <spring:message code="Price.filterForm" var="minMaxError"/>
                <form:errors cssClass="error-message" element="span"/>
                <span class="formError"></span>

                <div class="container event">
                    <input class="filter_button" type="submit" value="Aplicar">
                </div>
            </form:form>
        </div>
        <div class="container browse">
            <c:choose>
                <c:when test="${size > 0}">
                    <c:forEach var="event" items="${events}">
                        <c:if test="${!event.deleted}">
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
                        </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    No se encontraron eventos.
                </c:otherwise>
            </c:choose>
        </div>
        <c:if test="${page > 1}">
            <c:url var="prevUrl" value="">
                <c:forEach items="${param}" var="entry">
                    <c:if test="${entry.key != 'page'}">
                        <c:param name="${entry.key}" value="${entry.value}" />
                    </c:if>
                </c:forEach>
                <c:param name="page" value="${page - 1}" />
            </c:url>
        </c:if>
        <c:url var="nextUrl" value="">
            <c:forEach items="${param}" var="entry">
                <c:if test="${entry.key != 'page'}">
                    <c:param name="${entry.key}" value="${entry.value}" />
                </c:if>
            </c:forEach>
            <c:param name="page" value="${page + 1}" />
        </c:url>
        <div class="pagination">
            <c:if test="${page > 1}">
                <a href="${prevUrl}">Anterior</a>
            </c:if>
            <c:if test="${size == 10}">
                <a href="${nextUrl}">Siguiente</a>
            </c:if>
        </div>
    </div>
</body>
</html>

<script type="text/javascript">
    (function() {
        var minPrice = document.getElementById('minPrice');
        var maxPrice = document.getElementById('maxPrice');
        var form = document.getElementById('filterForm');

        var checkMinPriceValidity = function() {
            if (minPrice.validity.typeMismatch) {
                minPrice.setCustomValidity('${minPriceTypeError}');
                updateMinPriceMessage();
            } else if (minPrice.validity.rangeUnderflow) {
                minPrice.setCustomValidity('${minPriceMinError}');
                updateMinPriceMessage();
            } else {
                minPrice.setCustomValidity('');
            }
        };

        var checkMaxPriceValidity = function() {
            if (maxPrice.validity.typeMismatch) {
                maxPrice.setCustomValidity('${maxPriceTypeError}');
                updateMaxPriceMessage();
            } else if (maxPrice.validity.rangeUnderflow) {
                maxPrice.setCustomValidity('${maxPriceMinError}');
                updateMaxPriceMessage();
            } else {
                maxPrice.setCustomValidity('');
            }
        };

        var checkRangeValidity = function() {
            if (maxPrice.value.length !== 0 && minPrice.value.length !== 0) {
                if (maxPrice.value < minPrice.value) {
                    maxPrice.setCustomValidity('${minMaxError}');
                    updateMinMaxMessage();
                }
                else {
                    maxPrice.setCustomValidity('');
                }
            }
            else {
                maxPrice.setCustomValidity('');
            }
        }

        var updateMinPriceMessage = function() {
            form.getElementsByClassName('formError')[0].innerHTML = minPrice.validationMessage;
        }

        var updateMaxPriceMessage = function() {
            form.getElementsByClassName('formError')[1].innerHTML = maxPrice.validationMessage;
        }

        var updateMinMaxMessage = function() {
            form.getElementsByClassName('formError')[2].innerHTML = maxPrice.validationMessage;
        }

        minPrice.addEventListener('change', checkMinPriceValidity, false);
        minPrice.addEventListener('keyup', checkMinPriceValidity, false);
        maxPrice.addEventListener('change', checkMaxPriceValidity, false);
        maxPrice.addEventListener('keyup', checkMaxPriceValidity, false);
        maxPrice.addEventListener('change', checkRangeValidity, false);
        maxPrice.addEventListener('keyup', checkRangeValidity, false);

        form.addEventListener('submit', function(event) {
            if (form.classList) form.classList.add('submitted');
            checkMaxPriceValidity();
            checkMinPriceValidity();
            checkRangeValidity();
            if (!this.checkValidity()) {
                event.preventDefault();
                updateMaxPriceMessage();
                updateMinPriceMessage();
                updateMinMaxMessage();
            }
        }, false);
    }());
</script>
