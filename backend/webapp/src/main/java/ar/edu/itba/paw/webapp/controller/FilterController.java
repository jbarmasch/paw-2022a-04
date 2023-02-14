package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.AlreadyMaxTicketsException;
import ar.edu.itba.paw.exceptions.DateRangeException;
import ar.edu.itba.paw.exceptions.SurpassedMaxTicketsException;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.EventBookingService;
import ar.edu.itba.paw.service.FilterService;
import ar.edu.itba.paw.service.TagService;
import ar.edu.itba.paw.service.TicketService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.webapp.dto.EventDto;
import ar.edu.itba.paw.webapp.dto.EventStatsDto;
import ar.edu.itba.paw.webapp.dto.FilterDto;
import ar.edu.itba.paw.webapp.form.*;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;


@Path("api/filters")
@Component
public class FilterController {
    @Autowired
    private TagService ts;
    @Autowired
    private FilterService fs;

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(TagController.class);


    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getFilterType(@QueryParam("locations") final List<Integer> locations,
                                  @QueryParam("types") final List<Integer> types,
                                  @QueryParam("minPrice") final Double minPrice,
                                  @QueryParam("maxPrice") final Double maxPrice,
                                  @QueryParam("search") final String search,
                                  @QueryParam("tags") final List<Integer> tags,
                                  @QueryParam("soldOut") final Boolean showSoldOut,
                                  @QueryParam("userId") final Integer userId,
                                  @QueryParam("noTickets") final Boolean showNoTickets) {
        final FilterType res = fs.getFilterType(locations, types, minPrice, maxPrice, search, tags, showSoldOut, showNoTickets, userId);
        final FilterDto filterDto = FilterDto.fromFilter(uriInfo, res);

        if (filterDto != null) {
            return Response.ok(filterDto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
