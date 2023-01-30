package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.AlreadyMaxTicketsException;
import ar.edu.itba.paw.exceptions.DateRangeException;
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

    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listEvents(@QueryParam("locations") final List<Integer> locations,
                               @QueryParam("types") final List<Integer> types,
                               @QueryParam("minPrice") final Double minPrice,
                               @QueryParam("maxPrice") final Double maxPrice,
                               @QueryParam("search") final String search,
                               @QueryParam("tags") final List<Integer> tags,
                               @QueryParam("searchUsername") final String username,
                               @QueryParam("userId") final Long userId,
                               @QueryParam("order") final Order order,
                               @QueryParam("soldOut") final Boolean showSoldOut,
                               @QueryParam("page") @DefaultValue("1") final int page) {
        final EventList res = es.filterBy(locations, types, minPrice, maxPrice, search, tags, username, userId, order, showSoldOut, page);
        final List<EventDto> userList = res
                .getEventList()
                .stream()
                .map(e -> EventDto.fromEvent(uriInfo, e))
                .collect(Collectors.toList());

        int lastPage = res.getPages();

        if (userList.isEmpty()) {
            return Response.noContent().build();
        }

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<EventDto>>(userList) {});

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
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response createEvent(@Valid final EventForm form) throws IOException {
        final long userId = um.getUserId();
        
//        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        final Event event = es.create(form.getName(), form.getDescription(), form.getLocation(), form.getType(), form.getTimestamp(),
                null, form.getTags(), userId, form.isHasMinAge() ? form.getMinAge() : null,
                "http://181.46.186.8:2557", LocaleContextHolder.getLocale());

        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(event.getId())).build();
        return Response.created(uri).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response updateEvent(@PathParam("id") final long id, @Valid final EventForm form) {
        es.updateEvent(id, form.getName(), form.getDescription(), form.getLocation(), form.getType(), form.getTimestamp(),
                null, form.getTags(), form.isHasMinAge() ? form.getMinAge() : null);

        return Response.accepted().build();
    }

    @Path("/{id}/image")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addImage(@PathParam("id") final long id, 
                             @FormDataParam("image") InputStream inputStream,
                             @FormDataParam("image") FormDataContentDisposition contentDisposition) throws IOException {
        System.out.println("hola");
        byte[] data = IOUtils.toByteArray(inputStream);
        es.updateEventImage(id, data);

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

    @PUT
    @Path("/{id}/active")
    public Response activeEvent(@PathParam("id") final long id) {
        es.active(id);

        return Response.accepted().build();
    }

    @PUT
    @Path("/{id}/soldout")
    public Response soldOutEvent(@PathParam("id") final long id) {
        es.soldOut(id);

        return Response.accepted().build();
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

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<EventDto>>(eventList) {});

        return response.build();
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

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<EventDto>>(eventList) {});

        return response.build();
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

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<TicketDto>>(ticketList) {});

        return response.build();
    }

    @Path("/{id}/tickets")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response addTicket(final TicketsForm form, @PathParam("id") final long id) {
        Optional<Event> event = es.getEventById(id);

        if (!event.isPresent()) {
            return Response.serverError().build();
        }

        try {
            for (TicketForm ticket : form.getTickets()) {
                ts.addTicket(event.get(), ticket.getTicketName(), ticket.getPrice(), ticket.getQty(),
                        ticket.getLocalDate(ticket.getStarting()), ticket.getLocalDate(ticket.getUntil()), ticket.getMaxPerUser());
            }
        } catch (DateRangeException e) {
            return Response.serverError().build();
        }

        return Response.accepted().build();
    }

    @Path("/{id}/bookings")
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response bookEvent(@PathParam("id") final long id,
                              @Valid final BookForm form) {
//        , final BindingResult errors
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

        try {
            bs.book(booking, "http://181.46.186.8:2557", LocaleContextHolder.getLocale());
        } catch (AlreadyMaxTicketsException | SurpassedMaxTicketsException ex) {
            return Response.serverError().build();
//            for (Map.Entry<Integer, Integer> error : ex.getErrorMap().entrySet()) {
//                if (error.getValue() <= 0) {
//                    errors.rejectValue("bookings[" + error.getKey() + "].qty", "Max.bookForm.qtyReached", null, "");
//                    return Response.serverError().build();
//                } else {
//                    errors.rejectValue("bookings[" + error.getKey() + "].qty", "Max.bookForm.qty", new Object[]{error.getValue()}, "");
//                    return Response.serverError().build();
//                }
//                LOGGER.error("BookForm has errors: {}", errors.getAllErrors().toArray());
        }

        return Response.accepted().build();
    }

    @Path("/{id}/bookings")
    @DELETE
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response cancelBooking(@PathParam("id") final long id,
                                  @Valid final BookForm form) {
        final User user = um.getUser();
        Optional<Event> event = es.getEventById(id);

        if (!event.isPresent()) {
            return Response.serverError().build();
        }

        final EventBooking eventBooking = bs.getBookingFromUser(user.getId(), id).orElse(null);
        if (eventBooking == null) {
            LOGGER.error("Booking not found");
            return Response.serverError().build();
        }

        EventBooking booking = new EventBooking(user, event.get(), new ArrayList<>(), null);
        for (BookingForm bookingForm : form.getBookings()) {
            TicketBooking ticketBooking = new TicketBooking(ts.getTicketById(bookingForm.getTicketId()).orElse(null), bookingForm.getQty(), booking);
            booking.addBooking(ticketBooking);
        }

        try {
            bs.cancelBooking(booking, LocaleContextHolder.getLocale());
        } catch (SurpassedMaxTicketsException ex) {
//            for (Map.Entry<Integer, Integer> error : ex.getErrorMap().entrySet()) {
//                errors.rejectValue("bookings[" + error.getKey() + "].qty", "Max.bookForm.qty", new Object[]{error.getValue()}, "");
            LOGGER.error("BookForm has errors");
            return Response.serverError().build();
//            }
//            return cancelBooking(form, rateForm, eventId);
        }

        return Response.noContent().build();
    }

    @Path("/{id}/stats")
    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getEventStats(@PathParam("id") final long id) {
        Optional<EventStatsDto> eventStatsDto = es.getEventStats(id).map(u -> EventStatsDto.fromEventStats(uriInfo, u));

        if (eventStatsDto.isPresent()) {
            return Response.ok(eventStatsDto.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
