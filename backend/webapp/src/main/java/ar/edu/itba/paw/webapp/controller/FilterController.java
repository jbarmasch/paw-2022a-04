package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.FilterService;
import ar.edu.itba.paw.webapp.dto.FilterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;


@Path("api/filters")
@Component
public class FilterController {
    @Autowired
    private FilterService fs;
    @Context
    private HttpServletRequest request;
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getFilterType(@QueryParam("locations") final List<Long> locations,
                                  @QueryParam("types") final List<Long> types,
                                  @QueryParam("minPrice") final Double minPrice,
                                  @QueryParam("maxPrice") final Double maxPrice,
                                  @QueryParam("search") final String search,
                                  @QueryParam("tags") final List<Long> tags,
                                  @QueryParam("soldOut") final Boolean showSoldOut,
                                  @QueryParam("userId") final Long userId,
                                  @QueryParam("noTickets") final Boolean showNoTickets,
                                  @QueryParam("locale") final String locale) {
        final FilterType res = fs
                .getFilterType(locations, types, minPrice, maxPrice, search, tags, showSoldOut, showNoTickets, userId);
        final FilterDto filterDto = FilterDto.fromFilter(uriInfo, res, request.getLocale().getLanguage());

        return Response.ok(filterDto).build();
    }
}
