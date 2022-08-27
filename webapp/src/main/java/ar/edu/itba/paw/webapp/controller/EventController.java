package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.webapp.dto.EventDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.exceptions.*;
import ar.edu.itba.paw.webapp.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//@Controller
//public class EventController {
//    @Autowired
//    private EventService eventService;
//    @Autowired
//    private EventBookingService eventBookingService;
//    @Autowired
//    private LocationService locationService;
//    @Autowired
//    private TypeService typeService;
//    @Autowired
//    private TicketService ticketService;
//    @Autowired
//    private TagService tagService;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private UserManager userManager;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
//
//    @RequestMapping(value = "/", method = { RequestMethod.GET })
//    public ModelAndView home() {
//        final List<Event> fewTicketsEvents = eventService.getFewTicketsEvents();
//        final List<Event> upcomingEvents = eventService.getUpcomingEvents();
//
//        final ModelAndView mav = new ModelAndView("index");
//        mav.addObject("fewTicketsEvents", fewTicketsEvents);
//        mav.addObject("fewTicketsSize", fewTicketsEvents.size());
//        mav.addObject("upcomingEvents", upcomingEvents);
//        mav.addObject("upcomingSize", upcomingEvents.size());
//        return mav;
//    }
//
//    @RequestMapping(value = "/events/{eventId}", method = RequestMethod.GET)
//    public ModelAndView eventDescription(@ModelAttribute("bookForm") final BookForm form,
//                                         @PathVariable("eventId") @Min(1) final long eventId) {
//        final Event event = eventService.getEventById(eventId).orElse(null);
//        if (event == null) {
//            LOGGER.error("Event not found");
//            throw new EventNotFoundException();
//        }
//        boolean isLogged = false, isOwner = false;
//        if (userManager.isAuthenticated()) {
//            isLogged = true;
//            isOwner = userManager.isEventOwner(event);
//        }
//
//        final ModelAndView mav = new ModelAndView("event");
//        mav.addObject("currentDate", LocalDateTime.now().toString().substring(0,16));
//        mav.addObject("event", event);
//        mav.addObject("ticketsSize", event.getTickets().size());
//        mav.addObject("isOwner", isOwner);
//        mav.addObject("isLogged", isLogged);
//        addSimilarAndPopularEvents(mav, eventId);
//        return mav;
//    }
//
//    private void addSimilarAndPopularEvents(ModelAndView mav, long eventId) {
//        List<Event> similarEvents = eventService.getSimilarEvents(eventId);
//        List<Event> popularEvents = eventService.getPopularEvents(eventId);
//        mav.addObject("similarEvents", similarEvents);
//        mav.addObject("similarEventsSize", similarEvents.size());
//        mav.addObject("popularEvents", popularEvents);
//        mav.addObject("popularEventsSize", popularEvents.size());
//    }
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
//    @RequestMapping(value = "/events/{eventId}/booking-success", method = RequestMethod.GET)
//    public ModelAndView bookSuccess(@PathVariable("eventId") @Min(1) final long eventId) {
//        if (!userManager.isAuthenticated())
//            return new ModelAndView("redirect:/");
//
//        final EventBooking eventBooking = eventBookingService.getBookingFromUser(userManager.getUserId(), eventId).orElse(null);
//        if (eventBooking == null) {
//            LOGGER.error("Booking not found");
//            throw new BookingNotFoundException();
//        }
//
//        final ModelAndView mav = new ModelAndView("bookingSuccess");
//        mav.addObject("code", eventBooking.getCode());
//        addSimilarAndPopularEvents(mav, eventId);
//        return mav;
//    }
//
//    @RequestMapping(value = "/events/{eventId}/stats", method = RequestMethod.GET)
//    public ModelAndView eventStats(@PathVariable("eventId") @Min(1) final long eventId) {
//        if (!userManager.isAuthenticated())
//            return new ModelAndView("redirect:/");
//
//        final Event event = eventService.getEventById(eventId).orElse(null);
//        if (event == null) {
//            LOGGER.error("Event not found");
//            throw new EventNotFoundException();
//        }
//        if (!userManager.isEventOwner(event)) {
//            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
//            return new ModelAndView("redirect:/events/" + eventId);
//        }
//
//        EventStats eventStats = eventService.getEventStats(eventId).orElse(null);
//        if (eventStats == null) {
//            LOGGER.error("Stats not found");
//            throw new StatsNotFoundException();
//        }
//        List<TicketStats> ticketsStats = ticketService.getTicketStats(eventId);
//
//        final ModelAndView mav = new ModelAndView("statsForEvent");
//        mav.addObject("currentDate", LocalDateTime.now().toString().substring(0,16));
//        mav.addObject("eventStats", eventStats);
//        mav.addObject("ticketsStats", ticketsStats);
//        mav.addObject("ticketsStatsSize", ticketsStats.size());
//        return mav;
//    }
//
//    @RequestMapping(value = "/create-event", method = { RequestMethod.GET })
//    public ModelAndView createForm(@ModelAttribute("eventForm") final EventForm form) {
//        final ModelAndView mav = new ModelAndView("createEvent");
//        mav.addObject("locations", locationService.getAll());
//        mav.addObject("currentDate", LocalDateTime.now().toString().substring(0,16));
//        mav.addObject("types", typeService.getAll());
//        mav.addObject("allTags", tagService.getAll());
//        return mav;
//    }
//
//    @RequestMapping(value = "/create-event", method = { RequestMethod.POST }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
//    public ModelAndView createEvent(HttpServletRequest request, @Valid @ModelAttribute("eventForm") final EventForm form, final BindingResult errors,
//                                    @RequestParam("image") MultipartFile imageFile) throws IOException {
//        if (errors.hasErrors()) {
//            LOGGER.error("EventForm has errors: {}", errors.getAllErrors().toArray());
//            return createForm(form);
//        }
//
//        final long userId = userManager.getUserId();
//        final Event e = eventService.create(form.getName(), form.getDescription(), form.getLocation(), form.getType(), form.getTimestamp(),
//                (imageFile == null || imageFile.isEmpty()) ? null : imageFile.getBytes(), form.getTags(), userId, form.isHasMinAge() ? form.getMinAge() : null,
//                request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath()), LocaleContextHolder.getLocale());
//
//        userManager.refreshAuthorities();
//        return new ModelAndView("redirect:/events/" + e.getId());
//    }
//
//    @RequestMapping(value = "/events/{eventId}/modify", method = { RequestMethod.GET })
//    public ModelAndView modifyForm(@ModelAttribute("eventForm") final EventForm form, @PathVariable("eventId") @Min(1) final long eventId) {
//        final Event event = eventService.getEventById(eventId).orElse(null);
//        if (event == null) {
//            LOGGER.error("Event not found");
//            throw new EventNotFoundException();
//        }
//        if (!userManager.isEventOwner(event)) {
//            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
//            return new ModelAndView("redirect:/events/" + eventId);
//        }
//
//        final ModelAndView mav = new ModelAndView("modifyEvent");
//        mav.addObject("locations", locationService.getAll());
//        mav.addObject("currentDate", LocalDateTime.now().toString().substring(0,16));
//        mav.addObject("allTags", tagService.getAll());
//        mav.addObject("types", typeService.getAll());
//        mav.addObject("event", event);
//        return mav;
//    }
//
//    @RequestMapping(value = "/events/{eventId}/modify", method = { RequestMethod.POST }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
//    public ModelAndView modifyEvent(@Valid @ModelAttribute("eventForm") final EventForm form, final BindingResult errors,
//                                    @PathVariable("eventId") @Min(1) final long eventId,
//                                    @RequestParam("image") CommonsMultipartFile imageFile) {
//        final Event event = eventService.getEventById(eventId).orElse(null);
//        if (event == null) {
//            LOGGER.error("Event not found");
//            throw new EventNotFoundException();
//        }
//        if (!userManager.isEventOwner(event))
//            return new ModelAndView("redirect:/events/" + eventId);
//
//        if (errors.hasErrors())
//            return modifyForm(form, eventId);
//
//        eventService.updateEvent(eventId, form.getName(), form.getDescription(), form.getLocation(), form.getType(), form.getTimestamp(),
//                (imageFile == null || imageFile.isEmpty()) ? null : imageFile.getBytes(), form.getTags(), form.isHasMinAge() ? form.getMinAge() : null);
//        return new ModelAndView("redirect:/events/" + eventId);
//    }
//
//    @RequestMapping(value = "/events/{eventId}/delete", method = { RequestMethod.POST })
//    public ModelAndView deleteEvent(@PathVariable("eventId") final long eventId) {
//        final Event event = eventService.getEventById(eventId).orElse(null);
//        if (event == null) {
//            LOGGER.error("Event not found");
//            throw new EventNotFoundException();
//        }
//        if (!userManager.isEventOwner(event)) {
//            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
//            return new ModelAndView("redirect:/events/" + eventId);
//        }
//
//        eventService.deleteEvent(eventId);
//        return new ModelAndView("redirect:/events");
//    }
//
//    @RequestMapping(value = "/events/{eventId}/soldout", method = { RequestMethod.POST })
//    public ModelAndView soldOutEvent(@PathVariable("eventId") @Min(1) final long eventId) {
//        final Event event = eventService.getEventById(eventId).orElse(null);
//        if (event == null) {
//            LOGGER.error("Event not found");
//            throw new EventNotFoundException();
//        }
//        if (!userManager.isEventOwner(event)) {
//            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
//            return new ModelAndView("redirect:/events/" + eventId);
//        }
//
//        eventService.soldOut(eventId);
//        return new ModelAndView("redirect:/events/" + eventId);
//    }
//
//    @RequestMapping(value = "/events/{eventId}/active", method = { RequestMethod.POST })
//    public ModelAndView activeEvent(@PathVariable("eventId") @Min(1) final long eventId) {
//        final Event event = eventService.getEventById(eventId).orElse(null);
//        if (event == null) {
//            LOGGER.error("Event not found");
//            throw new EventNotFoundException();
//        }
//        if (!userManager.isEventOwner(event)) {
//            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
//            return new ModelAndView("redirect:/events/" + eventId);
//        }
//
//        eventService.active(eventId);
//        return new ModelAndView("redirect:/events/" + eventId);
//    }
//
//    @RequestMapping(value = "/my-events", method = { RequestMethod.GET })
//    public ModelAndView myEvents(@RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) final int page) {
//        final User user = userManager.getUser();
//        List<Event> events = eventService.getUserEvents(user.getId(), page);
//
//        final ModelAndView mav = new ModelAndView("myEvents");
//        mav.addObject("page", page);
//        mav.addObject("myEvents", events);
//        mav.addObject("size", events.size());
//        return mav;
//    }
//
//    @RequestMapping(value = "/stats", method = { RequestMethod.GET })
//    public ModelAndView getStats() {
//        final long userId = userManager.getUserId();
//        final OrganizerStats stats = userService.getOrganizerStats(userId).orElse(null);
//        if (stats == null) {
//            LOGGER.error("Stats not found");
//            throw new StatsNotFoundException();
//        }
//        final EventStats eventStats = eventService.getEventStats(stats.getPopularEvent().getId()).orElse(null);
//
//        final ModelAndView mav = new ModelAndView("eventStats");
//        mav.addObject("stats", stats);
//        mav.addObject("eventStats", eventStats);
//        return mav;
//    }
//}

@Path("events")
@Component
public class EventController {
    @Autowired
    private EventService es;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response listEvents(@QueryParam("locations") final List<Integer> locations,
                               @QueryParam("types") final List<Integer> types,
                               @QueryParam("minPrice") final Double minPrice,
                               @QueryParam("maxPrice") final Double maxPrice,
                               @QueryParam("search") final String search,
                               @QueryParam("tags") final List<Integer> tags,
                               @QueryParam("searchUsername") final String username,
                               @QueryParam("order") final Order order,
                               @QueryParam("soldOut") final Boolean showSoldOut,
                               @QueryParam("page") @DefaultValue("1") final int page) {
        final EventList res = es.filterBy(locations, types, minPrice, maxPrice, search, tags, username, order, showSoldOut, page);
        final List<EventDto> userList = res.getEventList().stream().map(e -> EventDto.fromEvent(uriInfo, e))
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

        response.link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", lastPage).build(), "last");

        return response.build();
    }

//    @POST
//    public Response createEvent(@QueryParam("username") final String username,
//                               @QueryParam("password") final String password) {
//        final User user = us.create(username, password, "izuku@gmail.com", Locale.ENGLISH);
//        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(user.getId())).build();
//        return Response.created(uri).build();
//    }

    @GET
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response getById(@PathParam("id") final long id) {
        Optional<EventDto> eventDto = es.getEventById(id).map(e -> EventDto.fromEvent(uriInfo, e));

        if (eventDto.isPresent()) {
            return Response.ok(eventDto.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

//    @DELETE
//    @Path("/{id}")
//    @Produces(value = { MediaType.APPLICATION_JSON, })
//    public Response deleteById(@PathParam("id") final long id) {
//        us.deleteById(id);
//        return Response.noContent().build();
//    }
}
