package ar.edu.itba.paw.webapp.controller;

//@Controller
//public class BookingController {
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private EventService eventService;
//    @Autowired
//    private EventBookingService eventBookingService;
//    @Autowired
//    private TicketService ticketService;
//    @Autowired
//    private CodeService codeService;
//    @Autowired
//    private UserManager userManager;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);
//
//    @RequestMapping(value = "/bookings", method = { RequestMethod.GET })
//    public ModelAndView bookings(@ModelAttribute("bookForm") final BookForm form,
//                                 @ModelAttribute("rateForm") final RateForm rateForm,
//                                 @RequestParam(value = "page", required = false, defaultValue = "1") final int page) {
//        final List<EventBooking> eventBookings = eventBookingService.getAllBookingsFromUser(userManager.getUserId(), page);
//
//        final ModelAndView mav = new ModelAndView("bookings");
//        mav.addObject("page", page);
//        mav.addObject("actualTime", LocalDateTime.now());
//        mav.addObject("oneMonthPrior", LocalDateTime.now().minusMonths(1));
//        mav.addObject("eventBookings", eventBookings);
//        mav.addObject("eventBookingsSize", eventBookings.size());
//        return mav;
//    }
//
//    @RequestMapping(value = "/bookings/rate/{eventId}", method = { RequestMethod.POST })
//    public ModelAndView rateEvent(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors,
//                                  @Valid @ModelAttribute("rateForm") final RateForm rateForm, final BindingResult rateErrors,
//                                  @PathVariable("eventId") final long eventId) {
//        Event e = eventService.getEventById(eventId).orElse(null);
//        if (e == null) {
//            LOGGER.error("Event not found");
//            throw new EventNotFoundException();
//        }
//
//        if (rateErrors.hasErrors()) {
//            LOGGER.error("BookForm has errors: {}", errors.getAllErrors().toArray());
//            return bookings(form, rateForm, form.getPage());
//        }
//
//        userService.rateUser(userManager.getUserId(), e.getOrganizer().getId(), rateForm.getRating());
//        return new ModelAndView("redirect:/bookings/");
//    }
//
//    @RequestMapping(value = "/bookings/cancel/{eventId}", method = { RequestMethod.GET })
//    public ModelAndView cancelBooking(@ModelAttribute("bookForm") final BookForm form,
//                                      @ModelAttribute("rateForm") final RateForm rateForm,
//                                      @PathVariable("eventId") final int eventId) {
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
//        final EventBooking eventBooking = eventBookingService.getBookingFromUser(user.getId(), eventId).orElse(null);
//        if (eventBooking == null) {
//            LOGGER.error("Booking not found");
//            throw new BookingNotFoundException();
//        }
//
//        ModelAndView mav = new ModelAndView("cancelBooking");
//        mav.addObject("eventBooking", eventBooking);
//        return mav;
//    }
//
//    @RequestMapping(value = "/bookings/cancel/{eventId}", method = { RequestMethod.POST })
//    public ModelAndView cancelBookingPost(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors,
//                                          @Valid @ModelAttribute("rateForm") final RateForm rateForm, final BindingResult rateErrors,
//                                          @PathVariable("eventId") final int eventId) {
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
//        final EventBooking eventBooking = eventBookingService.getBookingFromUser(user.getId(), eventId).orElse(null);
//        if (eventBooking == null) {
//            LOGGER.error("Booking not found");
//            throw new BookingNotFoundException();
//        }
//
//        EventBooking booking = new EventBooking(user, e, new ArrayList<>(), null);
//        for (BookingForm bookingForm : form.getBookings()) {
//            TicketBooking ticketBooking = new TicketBooking(ticketService.getTicketById(bookingForm.getTicketId()).orElse(null), bookingForm.getQty(), booking);
//            booking.addBooking(ticketBooking);
//        }
//
//        if (errors.hasErrors()) {
//            LOGGER.error("BookForm has errors: {}", errors.getAllErrors().toArray());
//            return cancelBooking(form, rateForm, eventId);
//        }
//
//        try {
//            eventBookingService.cancelBooking(booking, LocaleContextHolder.getLocale());
//        } catch (SurpassedMaxTicketsException ex) {
//            for (Map.Entry<Integer, Integer> error : ex.getErrorMap().entrySet()) {
//                errors.rejectValue("bookings[" + error.getKey() + "].qty", "Max.bookForm.qty", new Object[]{error.getValue()}, "");
//            }
//            LOGGER.error("BookForm has errors: {}", errors.getAllErrors().toArray());
//            return cancelBooking(form, rateForm, eventId);
//        }
//
//        return new ModelAndView("redirect:/bookings/");
//    }
//
//    @RequestMapping(value = "/bookings/{code}", method = { RequestMethod.GET })
//    public ModelAndView booking(HttpServletRequest request, @PathVariable("code") final String code) {
//        final EventBooking eventBooking = eventBookingService.getBooking(code).orElse(null);
//        final User user = userManager.getUser();
//        long userId = user.getId();
//        if (eventBooking == null || (userId != eventBooking.getUser().getId() && userId != eventBooking.getEvent().getOrganizer().getId() &&
//                userId != eventBooking.getEvent().getBouncer().getId())) {
//            LOGGER.error("Booking not found");
//            throw new BookingNotFoundException();
//        }
//
//        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
//        String bookUrl = baseUrl + "/bookings/" + code;
//        byte[] encodeBase64 = Base64.getEncoder().encode(codeService.createQr(bookUrl));
//        String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
//
//        final ModelAndView mav = new ModelAndView("booking");
//        mav.addObject("eventBooking", eventBooking);
//        mav.addObject("image", base64Encoded);
//        return mav;
//    }
//
//    @RequestMapping(value = "/bookings/{code}/confirm", method = { RequestMethod.POST })
//    public ModelAndView confirmBooking(@PathVariable("code") final String code) {
//        EventBooking eventBooking = eventBookingService.getBooking(code).orElse(null);
//        if (eventBooking == null) {
//            LOGGER.error("Booking not found");
//            throw new BookingNotFoundException();
//        }
//
//        eventBookingService.confirmBooking(eventBooking);
//        return new ModelAndView("redirect:/bookings/" + code);
//    }
//}

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.CodeService;
import ar.edu.itba.paw.service.EventBookingService;
import ar.edu.itba.paw.service.TicketService;
import ar.edu.itba.paw.webapp.dto.BookingDto;
import ar.edu.itba.paw.webapp.form.BookingForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("api/bookings")
@Component
public class BookingController {
    @Autowired
    private EventBookingService bs;
    @Autowired
    private TicketService ts;
    @Autowired
    private CodeService cs;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listBookings(@QueryParam("userId") final long userId,
                                 @QueryParam("page") @DefaultValue("1") final int page) {
        final EventBookingList res = bs.getAllBookingsFromUser(userId, page);
        final List<BookingDto> userList = res
                .getBookingList()
                .stream()
                .map(e -> BookingDto.fromBooking(uriInfo, e))
                .collect(Collectors.toList());

        int lastPage = res.getPages();

        if (userList.isEmpty()) {
            return Response.noContent().build();
        }

        Response.ResponseBuilder response = Response.ok(new GenericEntity<List<BookingDto>>(userList) {
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

//    @POST
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
//    public Response createBooking(@Valid final BookingForm form) {
////        final long userId = userManager.getUserId();
//
//        final Booking event = es.create(form.getName(), form.getDescription(), form.getLocation(), form.getType(), form.getTimestamp(),
//                null, form.getTags(), 1, form.isHasMinAge() ? form.getMinAge() : null, null, LocaleContextHolder.getLocale());
//
//        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(event.getId())).build();
//        return Response.created(uri).build();
//    }

    @GET
    @Path("/{code}")
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response getById(@PathParam("code") final String code) {
        Optional<BookingDto> bookingDto = bs.getBooking(code).map(e -> BookingDto.fromBooking(uriInfo, e));

        String bookUrl = "http://181.46.186.8:2557" + "/bookings/" + code;
        byte[] encodeBase64 = Base64.getEncoder().encode(cs.createQr(bookUrl));
        String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);


        if (bookingDto.isPresent()) {
            BookingDto aux = bookingDto.get();
            aux.setImage(base64Encoded);
            return Response.ok(aux).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


//    @RequestMapping(value = "/bookings/cancel/{eventId}", method = { RequestMethod.POST })
//    public ModelAndView cancelBookingPost(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors,
//                                          @Valid @ModelAttribute("rateForm") final RateForm rateForm, final BindingResult rateErrors,
//                                          @PathVariable("eventId") final int eventId) {
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
//        final EventBooking eventBooking = eventBookingService.getBookingFromUser(user.getId(), eventId).orElse(null);
//        if (eventBooking == null) {
//            LOGGER.error("Booking not found");
//            throw new BookingNotFoundException();
//        }
//
//        EventBooking booking = new EventBooking(user, e, new ArrayList<>(), null);
//        for (BookingForm bookingForm : form.getBookings()) {
//            TicketBooking ticketBooking = new TicketBooking(ticketService.getTicketById(bookingForm.getTicketId()).orElse(null), bookingForm.getQty(), booking);
//            booking.addBooking(ticketBooking);
//        }
//
//        if (errors.hasErrors()) {
//            LOGGER.error("BookForm has errors: {}", errors.getAllErrors().toArray());
//            return cancelBooking(form, rateForm, eventId);
//        }
//
//        try {
//            eventBookingService.cancelBooking(booking, LocaleContextHolder.getLocale());
//        } catch (SurpassedMaxTicketsException ex) {
//            for (Map.Entry<Integer, Integer> error : ex.getErrorMap().entrySet()) {
//                errors.rejectValue("bookings[" + error.getKey() + "].qty", "Max.bookForm.qty", new Object[]{error.getValue()}, "");
//            }
//            LOGGER.error("BookForm has errors: {}", errors.getAllErrors().toArray());
//            return cancelBooking(form, rateForm, eventId);
//        }
//
//        return new ModelAndView("redirect:/bookings/");
//    }
//
//    @RequestMapping(value = "/bookings/{code}", method = { RequestMethod.GET })
//    public ModelAndView booking(HttpServletRequest request, @PathVariable("code") final String code) {
//        final EventBooking eventBooking = eventBookingService.getBooking(code).orElse(null);
//        final User user = userManager.getUser();
//        long userId = user.getId();
//        if (eventBooking == null || (userId != eventBooking.getUser().getId() && userId != eventBooking.getEvent().getOrganizer().getId() &&
//                userId != eventBooking.getEvent().getBouncer().getId())) {
//            LOGGER.error("Booking not found");
//            throw new BookingNotFoundException();
//        }
//
//        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
//        String bookUrl = baseUrl + "/bookings/" + code;
//        byte[] encodeBase64 = Base64.getEncoder().encode(codeService.createQr(bookUrl));
//        String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);
//
//        final ModelAndView mav = new ModelAndView("booking");
//        mav.addObject("eventBooking", eventBooking);
//        mav.addObject("image", base64Encoded);
//        return mav;
//    }
//
//    @POST hacer put y delete
//    @Path("/{code}")
//    @Produces(value = {MediaType.APPLICATION_JSON,})
//    @RequestMapping(value = "/bookings/{code}/confirm", method = { RequestMethod.POST })
//    public ModelAndView confirmBooking(@PathVariable("code") final String code) {
//        EventBooking eventBooking = eventBookingService.getBooking(code).orElse(null);
//        if (eventBooking == null) {
//            LOGGER.error("Booking not found");
//            throw new BookingNotFoundException();
//        }
//
//        eventBookingService.confirmBooking(eventBooking);
//        return new ModelAndView("redirect:/bookings/" + code);
//    }
}
