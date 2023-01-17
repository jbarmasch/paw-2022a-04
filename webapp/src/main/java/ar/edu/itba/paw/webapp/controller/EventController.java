package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.TicketService;
import ar.edu.itba.paw.webapp.dto.EventDto;
import ar.edu.itba.paw.webapp.form.EventForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.Media;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

//
//    @RequestMapping(value = "/events/{eventId}", method = { RequestMethod.POST }, params = "submit")
//    public ModelAndView bookEvent(HttpServletRequest request,
//                                  @Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors,
//                                  @PathVariable("eventId") @Min(1) final long eventId) {
//        final Event e = eventService.getEventById(eventId).orElse(null);
//        if (e == null) {
//            LOGGER.error("Event not found");
//            throw new EventNotFoundException();
//        }
//        final User user = userManager.getUser();
//        final User eventUser = userService.getUserById(e.getOrganizer().getId()).orElse(null);
//        if (eventUser == null) {
//            LOGGER.error("Organizer not found");
//            throw new UserNotFoundException();
//        }
//
//        EventBooking booking = new EventBooking(user, e, new ArrayList<>(), null);
//        for (BookingForm bookingForm : form.getBookings()) {
//            Ticket ticket = ticketService.getTicketById(bookingForm.getTicketId()).orElse(null);
//            if (ticket == null) {
//                LOGGER.error("Ticket not found");
//                throw new TicketNotFoundException();
//            }
//            TicketBooking ticketBooking = new TicketBooking(ticket, bookingForm.getQty(), booking);
//            booking.addBooking(ticketBooking);
//        }
//
//        if (errors.hasErrors()) {
//            LOGGER.error("BookForm has errors: {}", errors.getAllErrors().toArray());
//            return eventDescription(form, eventId);
//        }
//
//        try {
//            eventBookingService.book(booking, request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath()), LocaleContextHolder.getLocale());
//        } catch (AlreadyMaxTicketsException | SurpassedMaxTicketsException ex) {
//            for (Map.Entry<Integer, Integer> error : ex.getErrorMap().entrySet()) {
//                if (error.getValue() <= 0) {
//                    errors.rejectValue("bookings[" + error.getKey() + "].qty", "Max.bookForm.qtyReached", null, "");
//                } else {
//                    errors.rejectValue("bookings[" + error.getKey() + "].qty", "Max.bookForm.qty", new Object[]{error.getValue()}, "");
//                }
//            }
//            LOGGER.error("BookForm has errors: {}", errors.getAllErrors().toArray());
//            return eventDescription(form, eventId);
//        }
//
//        return new ModelAndView("redirect:/events/" + e.getId() + "/booking-success");
//    }
//
//       final EventBooking eventBooking = eventBookingService.getBookingFromUser(userManager.getUserId(), eventId).orElse(null);

@Path("api/events")
@Component
public class EventController {
    @Autowired
    private EventService es;
    @Autowired
    private TicketService ts;

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

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<EventDto>>(userList) {
        });

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

    @Autowired
    private Validator validator;

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    public Response createEvent(@Valid final EventForm form) {
//        final long userId = userManager.getUserId();

        Set<ConstraintViolation<EventForm>> violations = validator.validate(form);
        System.out.println(violations);

        final Event event = es.create(form.getName(), form.getDescription(), form.getLocation(), form.getType(), form.getTimestamp(),
                null, form.getTags(), 1, form.isHasMinAge() ? form.getMinAge() : null, null, LocaleContextHolder.getLocale());

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

}
