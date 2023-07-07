package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.AlreadyMaxTicketsException;
import ar.edu.itba.paw.exceptions.SurpassedMaxTicketsException;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.EventBookingService;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.TicketService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.validations.PATCH;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Path("api/events")
@Component
public class EventController {
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

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

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
        final EventList res = es.filterBy(locations, types, minPrice, maxPrice, search, tags, username, userId, order, showSoldOut, showNoTickets, showPast, similarEvent, recommendedEvent, fewTickets, upcoming, page);
        final List<EventDto> eventList = res
                .getEventList()
                .stream()
                .map(e -> EventDto.fromEvent(uriInfo, e))
                .collect(Collectors.toList());

        int lastPage = res.getPages();

        if (eventList.isEmpty()) {
            return Response.noContent().build();
        }

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<EventDto>>(eventList) {});

        if (page != 1) {
            response.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev");
        }
        if (page != lastPage) {
            response.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next");
        }

        response
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", lastPage).build(), "last");

        return response.build();
    }

    @POST
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    public Response createEvent(@Valid @FormDataParam("form") final EventForm form,
                                @FormDataParam("image") InputStream inputStream,
                                @FormDataParam("image") FormDataContentDisposition contentDisposition) throws IOException {
        byte[] image = IOUtils.toByteArray(inputStream);

        final long userId = um.getUserId();
        
        final Event event = es.create(form.getName(), form.getDescription(), form.getLocation(), form.getType(), form.getTimestamp(),
                image, form.getTags(), userId, form.isHasMinAge() ? form.getMinAge() : null,
                uriInfo.getBaseUriBuilder().toString(), request.getLocale());

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(event.getId())).build();
        return Response.created(uri).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(value = {MediaType.MULTIPART_FORM_DATA})
    public Response updateEvent(@PathParam("id") final long id,
                                @Valid @FormDataParam("form") final EventForm form,
                                @FormDataParam("image") InputStream inputStream,
                                @FormDataParam("image") FormDataContentDisposition contentDisposition) throws IOException {
        byte[] data = IOUtils.toByteArray(inputStream);

        es.updateEvent(id, form.getName(), form.getDescription(), form.getLocation(), form.getType(), form.getTimestamp(),
                data, form.getTags(), form.isHasMinAge() ? form.getMinAge() : null);

        return Response.accepted().build();
    }

    @GET
    @Path("/{id}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("id") final long id) {
        Optional<EventDto> eventDto = es.getEventById(id).map(e -> EventDto.fromEvent(uriInfo, e));

        if (eventDto.isPresent()) {
            return Response.ok(eventDto.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/{id}/rating")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response rateUser(@PathParam("id") final long id,
                             @Valid final RateForm form) {
        final long userId = um.getUserId();
        final Event e = es.getEventById(id).orElse(null);

        if (e == null) {
            LOGGER.error("Event not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        us.rateUser(userId, e.getOrganizer().getId(), form.getRating());

        return Response.accepted().build();
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
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response deleteById(@PathParam("id") final long id) {
        es.deleteEvent(id);

        return Response.noContent().build();
    }

    @Path("/upcoming")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listUpcoming() {
        final List<Event> res = es.getUpcomingEvents();
        final List<EventDto> eventList = res
                .stream()
                .map(e -> EventDto.fromEvent(uriInfo, e))
                .collect(Collectors.toList());

        if (eventList.isEmpty()) {
            return Response.noContent().build();
        }

        return Response.ok(new GenericEntity<List<EventDto>>(eventList) {}).build();
    }

    @Path("/few-tickets")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listFewTickets() {
        final List<Event> res = es.getFewTicketsEvents();
        final List<EventDto> eventList = res
                .stream()
                .map(e -> EventDto.fromEvent(uriInfo, e))
                .collect(Collectors.toList());

        if (eventList.isEmpty()) {
            return Response.noContent().build();
        }

        return Response.ok(new GenericEntity<List<EventDto>>(eventList) {}).build();
    }

    @GET
    @Path("/{id}/similar")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listSimilarEvents(@PathParam("id") final long id) {
        final List<Event> res = es.getSimilarEvents(id);
        final List<EventDto> eventList = res
                .stream()
                .map(e -> EventDto.fromEvent(uriInfo, e))
                .collect(Collectors.toList());

        if (eventList.isEmpty()) {
            return Response.noContent().build();
        }

        return Response.ok(new GenericEntity<List<EventDto>>(eventList) {}).build();
    }

    @GET
    @Path("/{id}/recommended")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listRecommendedEvents(@PathParam("id") final long id) {
        final List<Event> res = es.getPopularEvents(id);
        final List<EventDto> eventList = res
                .stream()
                .map(e -> EventDto.fromEvent(uriInfo, e))
                .collect(Collectors.toList());

        if (eventList.isEmpty()) {
            return Response.noContent().build();
        }

        return Response.ok(new GenericEntity<List<EventDto>>(eventList) {}).build();
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
        Optional<Event> event = es.getEventById(id);

        if (!event.isPresent()) {
            LOGGER.error("Event not found");
            return Response.serverError().build();
        }

        ts.addTicket(event.get(), form.getTicketName(), form.getPrice(), form.getQty(),
                form.getLocalDate(form.getStarting()), form.getLocalDate(form.getUntil()), form.getMaxPerUser());

        return Response.accepted().build();
    }

    @Path("/{id}/bookings")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response bookEvent(@PathParam("id") final long id, final @Valid BookForm form) {
        final User user = um.getUser();
        Optional<Event> event = es.getEventById(id);

        if (!event.isPresent()) {
            return Response.serverError().build();
        }

        EventBooking booking = new EventBooking(user, event.get(), new ArrayList<>(), null);
        for (BookingForm bookingForm : form.getBookings()) {
            Ticket ticket = ts.getTicketById(bookingForm.getTicketId()).orElse(null);
            if (ticket == null) {
                LOGGER.error("Ticket not found");
                return Response.serverError().build();
            }
            TicketBooking ticketBooking = new TicketBooking(ticket, bookingForm.getQty(), booking);
            booking.addBooking(ticketBooking);
        }

        // TODO: arreglar
        EventBooking eventBooking;
        try {
            eventBooking = bs.book(booking, env.getProperty("baseUrl"), request.getLocale());
        } catch (AlreadyMaxTicketsException | SurpassedMaxTicketsException e) {
            for (Map.Entry<Integer, Integer> error : e.getErrorMap().entrySet()) {
                if (error.getValue() <= 0) {
                    System.out.println(error.getKey());
                    System.out.println("Max.bookForm.qtyReached");
//                    errors.rejectValue("bookings[" + error.getKey() + "].qty", "Max.bookForm.qtyReached", null, "");
                } else {
                    System.out.println(error.getKey());
                    System.out.println("Max.bookForm.qty");
//                    errors.rejectValue("bookings[" + error.getKey() + "].qty", "Max.bookForm.qty", new Object[]{error.getValue()}, "");
                }
            }
            LOGGER.error("Booking error");
            CustomErrorDto error = CustomErrorDto.fromException(e.getMessage());

            System.out.println(e.getErrorMap());

            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(new GenericEntity<CustomErrorDto>(error) {})
                    .build();
        }

        final URI uri = uriInfo.getBaseUriBuilder()
            .path("api/bookings").path(String.valueOf(eventBooking.getCode())).build();
        return Response.created(uri).build();
    }

    @Path("/{id}/stats")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getEventStats(@PathParam("id") final long id) {
        Optional<EventStatsDto> eventStatsDto = es.getEventStats(id).map(u -> EventStatsDto.fromEventStats(uriInfo, u));

        if (eventStatsDto.isPresent()) {
            return Response.ok(eventStatsDto.get()).build();
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
