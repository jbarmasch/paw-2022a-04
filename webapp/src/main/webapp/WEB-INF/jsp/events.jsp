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
    <c:import url="appbar.jsp"/>
    <c:import url="order.jsp"/>
    <div class="home">
        <div>
            <c:url value="/events" var="postPath"/>
            <form:form novalidate="true" modelAttribute="filterForm" action="${postPath}" method="post" class="filter" id="filterForm">
                <b><spring:message code="filter.filters"/>: </b>
                <div>
                    <form:label path="locations"><spring:message code="filter.location"/>: </form:label>
                    <form:select class="uk-select" htmlEscape="true" multiple="true" path="locations">
                        <c:forEach var="location" items="${allLocations}">
                            <c:set var="selectedLocation" value="${false}"/>
                            <c:forEach var="filterLocation" items="${param.locations}">
                                <c:choose>
                                    <c:when test="${location.id == filterLocation}">
                                        <c:set var="selectedLocation" value="${true}"/>
                                    </c:when>
                                </c:choose>
                            </c:forEach>
                            <c:choose>
                                <c:when test="${selectedLocation}">
                                    <form:option value="${location.id}" selected="selected">${location.name}</form:option>
                                </c:when>
                                <c:otherwise>
                                    <form:option value="${location.id}">${location.name}</form:option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </form:select>
                    <form:errors path="locations" cssClass="error-message" element="span"/>
                </div>
                <div>
                    <form:label path="types"><spring:message code="filter.type"/>: </form:label>
                    <form:select class="uk-select" htmlEscape="true" multiple="true" path="types">
                        <c:forEach var="type" items="${allTypes}">
                            <c:set var="selectedType" value="${false}"/>
                            <c:forEach var="filterType" items="${param.types}">
                                <c:choose>
                                    <c:when test="${type.id == filterType}">
                                        <c:set var="selectedType" value="${true}"/>
                                    </c:when>
                                </c:choose>
                            </c:forEach>
                            <c:choose>
                                <c:when test="${selectedType}">
                                    <form:option value="${type.id}" selected="selected">${type.name}</form:option>
                                </c:when>
                                <c:otherwise>
                                    <form:option value="${type.id}">${type.name}</form:option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </form:select>
                    <form:errors path="types" cssClass="error-message" element="span"/>
                </div>
                <div>
                    <form:label path="tags"><spring:message code="filter.tags"/>: </form:label>
                    <form:select class="uk-select" htmlEscape="true" multiple="true" path="tags">
                        <c:forEach var="tag" items="${allTags}">
                            <c:set var="selectedTag" value="${false}"/>
                            <c:forEach var="filterTag" items="${param.tags}">
                                <c:choose>
                                    <c:when test="${tag.id == filterTag}">
                                        <c:set var="selectedTag" value="${true}"/>
                                    </c:when>
                                </c:choose>
                            </c:forEach>
                            <c:choose>
                                <c:when test="${selectedTag}">
                                    <form:option value="${tag.id}" selected="selected">${tag.name}</form:option>
                                </c:when>
                                <c:otherwise>
                                    <form:option value="${tag.id}">${tag.name}</form:option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </form:select>
                    <form:errors path="tags" cssClass="error-message" element="span"/>
                </div>
                <div>
                    <form:label path="minPrice"><spring:message code="filter.minPrice"/>: </form:label>
                    <form:input class="uk-input" type="number" path="minPrice" min="0" step="0.01" id="minPrice" value="${param.minPrice}"/>
                    <form:errors path="minPrice" cssClass="error-message" element="span"/>
                    <spring:message code="DecimalMin.filterForm.minPrice" var="minPriceMinError"/>
                    <spring:message code="Pattern.filterForm.minPrice" var="minPriceTypeError"/>
                    <span class="formError"></span>
                </div>
                <div>
                    <form:label path="maxPrice"><spring:message code="filter.maxPrice"/>: </form:label>
                    <form:input class="uk-input" type="number" path="maxPrice" min="0" step="0.01" id="maxPrice" value="${param.maxPrice}"/>
                    <form:errors path="maxPrice" cssClass="error-message" element="span"/>
                    <spring:message code="DecimalMin.filterForm.maxPrice" var="maxPriceMinError"/>
                    <spring:message code="Pattern.filterForm.maxPrice" var="maxPriceTypeError"/>
                    <span class="formError"></span>
                </div>
                <spring:message code="Price.filterForm" var="minMaxError"/>
                <form:errors cssClass="error-message" element="span"/>
                <span class="formError"></span>

                <form:input class="hidden" type="text" path="searchQuery" value="${param.search}"/>
                <form:input class="hidden" type="text" path="orderBy" value="${param.orderBy}"/>
                <form:input class="hidden" type="text" path="order" value="${param.order}"/>
                <form:input class="hidden" type="text" path="username" value="${param.username}"/>

                <div class="container event">
                    <spring:message code="filter.apply" var="applyMessage"/>
                    <input class="filter_button" type="submit" value="${applyMessage}">
                </div>
            </form:form>
        </div>
        <div class="container browse">
            <c:choose>
                <c:when test="${size > 0}">
                    <c:forEach var="event" items="${events}">
                        <c:if test="${!event.deleted}">
                            <c:set var="event" value="${event}" scope="request"/>
                            <c:import url="eventCard.jsp"/>
                        </c:if>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <spring:message code="filter.noEvents"/>
                </c:otherwise>
            </c:choose>
        </div>
        <c:set var="page" value="${page}" scope="request"/>
        <c:import url="paging.jsp"/>
    </div>
</body>
</html>

<script type="text/javascript">
    // function orderBy(order, orderBy) {
    //     document.getElementsByName('order')[0].value = order;
    //     document.getElementsByName('orderBy')[0].value = orderBy;
    //     document.getElementById('filterForm').submit();
    // }

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
