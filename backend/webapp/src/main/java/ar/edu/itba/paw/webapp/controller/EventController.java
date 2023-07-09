package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.AlreadyMaxTicketsException;
import ar.edu.itba.paw.exceptions.EventNotFoundException;
import ar.edu.itba.paw.exceptions.SurpassedMaxTicketsException;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.EventBookingService;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.TicketService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.webapp.dto.EventDto;
import ar.edu.itba.paw.webapp.dto.EventStatsDto;
import ar.edu.itba.paw.webapp.dto.TicketDto;
import ar.edu.itba.paw.webapp.dto.TicketStatsDto;
import ar.edu.itba.paw.webapp.exceptions.TicketNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.helper.ImageUtils;
import ar.edu.itba.paw.webapp.helper.PaginationUtils;
import ar.edu.itba.paw.webapp.validations.PATCH;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("api/events")
@Component
public class EventController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
    @Autowired
    private EventService es;
    @Autowired
    private UserService us;
    @Autowired
    private TicketService ts;
    @Autowired
    private EventBookingService bs;
    @Autowired
    private UserManager um;
    @Autowired
    private Environment env;
    @Context
    private HttpServletRequest request;
    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listEvents(@QueryParam("locations") final List<Long> locations,
                               @QueryParam("types") final List<Long> types,
                               @QueryParam("minPrice") final Double minPrice,
                               @QueryParam("maxPrice") final Double maxPrice,
                               @QueryParam("search") final String search,
                               @QueryParam("tags") final List<Long> tags,
                               @QueryParam("searchUsername") final String username,
                               @QueryParam("userId") final Long userId,
                               @QueryParam("order") final Order order,
                               @QueryParam("soldOut") final Boolean showSoldOut,
                               @QueryParam("noTickets") final Boolean showNoTickets,
                               @QueryParam("showPast") final Boolean showPast,
                               @QueryParam("similar") final Long similarEvent,
                               @QueryParam("recommended") final Long recommendedEvent,
                               @QueryParam("fewTickets") final Boolean fewTickets,
                               @QueryParam("upcoming") final Boolean upcoming,
                               @QueryParam("page") @DefaultValue("1") final int page) {
        final EventList res = es.filterBy(locations, types, minPrice, maxPrice, search, tags, username, userId,
                order, showSoldOut, showNoTickets, showPast, similarEvent, recommendedEvent, fewTickets, upcoming, page);
        final List<EventDto> eventList = res
                .getEventList()
                .stream()
                .map(e -> EventDto.fromEvent(uriInfo, e))
                .collect(Collectors.toList());

        if (eventList.isEmpty()) {
            return Response.noContent().build();
        }

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<EventDto>>(eventList) {});
        PaginationUtils.setResponsePages(response, uriInfo, page, res.getPages());
        return response.build();
    }

    @POST
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    public Response createEvent(@Valid @FormDataParam("form") final EventForm form) {
        final long userId = um.getUserId();

        final Event event = es.create(form.getName(), form.getDescription(), form.getLocation(), form.getType(),
                form.getTimestamp(), null, form.getTags(), userId, form.isHasMinAge() ? form.getMinAge() : null,
                uriInfo.getBaseUriBuilder().toString(), request.getLocale());

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(event.getId())).build();
        return Response.created(uri).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    public Response updateEvent(@PathParam("id") final long id,
                                @Valid @FormDataParam("form") final EventForm form) {
        es.updateEvent(id, form.getName(), form.getDescription(), form.getLocation(), form.getType(), form.getTimestamp(),
                null, form.getTags(), form.isHasMinAge() ? form.getMinAge() : null);

        return Response.ok().build();
    }

    @Path("/{id}/image")
    @PUT
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    public Response updateEventImage(@PathParam("id") final long id,
                                     @FormDataParam("image") InputStream inputStream,
                                     @FormDataParam("image") FormDataContentDisposition contentDisposition) {
        final byte[] image = ImageUtils.getByteArray(inputStream);

        es.updateEventImage(id, image);

        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("id") final long id) {
        EventDto eventDto = es
                .getEventById(id)
                .map(e -> EventDto.fromEvent(uriInfo, e))
                .orElseThrow(EventNotFoundException::new);

        return Response.ok(eventDto).build();
    }

    @POST
    @Path("/{id}/rating")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response rateUser(@PathParam("id") final long id,
                             @Valid final RateForm form) {
        final Event e = es.getEventById(id).orElseThrow(EventNotFoundException::new);
        final long userId = um.getUserId();

        us.rateUser(userId, e.getOrganizer().getId(), form.getRating());

        return Response.ok().build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response soldOutEvent(@PathParam("id") final long id,
                                 @Valid final PatchForm patchForm) {
        if (patchForm.isActive()) {
            es.active(id);
        } else {
            es.soldOut(id);
        }

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response deleteById(@PathParam("id") final long id) {
        es.deleteEvent(id);

        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/tickets")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listTickets(@PathParam("id") final long id) {
        final List<Ticket> res = ts.getTickets(id);
        final List<TicketDto> ticketList = res
                .stream()
                .map(e -> TicketDto.fromTicket(uriInfo, e))
                .collect(Collectors.toList());

        if (ticketList.isEmpty()) {
            return Response.noContent().build();
        }

        return Response.ok(new GenericEntity<List<TicketDto>>(ticketList) {}).build();
    }

    @Path("/{id}/tickets")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response addTicket(@Valid TicketForm form, @PathParam("id") final long id) {
        Event event = es.getEventById(id).orElseThrow(EventNotFoundException::new);

        ts.addTicket(event, form.getTicketName(), form.getPrice(), form.getQty(),
                form.getLocalDate(form.getStarting()), form.getLocalDate(form.getUntil()), form.getMaxPerUser());

        return Response.ok().build();
    }

    @Path("/{id}/bookings")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response bookEvent(@PathParam("id") final long id, final @Valid BookForm form)
            throws SurpassedMaxTicketsException, AlreadyMaxTicketsException {
        final User user = um.getUser();
        Event event = es.getEventById(id).orElseThrow(EventNotFoundException::new);

        EventBooking booking = new EventBooking(user, event, new ArrayList<>(), null);
        for (BookingForm bookingForm : form.getBookings()) {
            Ticket ticket = ts.getTicketById(bookingForm.getTicketId()).orElseThrow(TicketNotFoundException::new);
            TicketBooking ticketBooking = new TicketBooking(ticket, bookingForm.getQty(), booking);
            booking.addBooking(ticketBooking);
        }

        EventBooking eventBooking = bs.book(booking, env.getProperty("baseUrl"), request.getLocale());

        final URI uri = uriInfo.getBaseUriBuilder()
                .path("api/bookings").path(String.valueOf(eventBooking.getCode())).build();
        return Response.created(uri).build();
    }

    @Path("/{id}/stats")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getEventStats(@PathParam("id") final long id) {
        EventStatsDto eventStatsDto = es
                .getEventStats(id)
                .map(u -> EventStatsDto.fromEventStats(uriInfo, u))
                .orElse(null);

        if (eventStatsDto != null) {
            return Response.ok(eventStatsDto).build();
        } else {
            return Response.noContent().build();
        }
    }

    @Path("/{id}/ticket-stats")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getTicketStats(@PathParam("id") final long id) {
        List<TicketStats> ticketsStats = ts.getTicketStats(id);
        final List<TicketStatsDto> ticketList = ticketsStats
                .stream()
                .map(e -> TicketStatsDto.fromTicketStats(uriInfo, e))
                .collect(Collectors.toList());

        if (!ticketList.isEmpty()) {
            return Response.ok(ticketList).build();
        } else {
            return Response.noContent().build();
        }
    }
}
