package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.CodeService;
import ar.edu.itba.paw.service.EventBookingService;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.TicketService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.webapp.exceptions.BookingNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.EventNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.BookForm;
import ar.edu.itba.paw.webapp.form.BookingForm;
import ar.edu.itba.paw.webapp.form.RateForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class BookingController {
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private EventBookingService eventBookingService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private UserManager userManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);

    @RequestMapping(value = "/bookings", method = { RequestMethod.GET })
    public ModelAndView bookings(@ModelAttribute("bookForm") final BookForm form,
                                 @ModelAttribute("rateForm") final RateForm rateForm,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") final int page) {
        final List<EventBooking> eventBookings = eventBookingService.getAllBookingsFromUser(userManager.getUserId(), page);

        final ModelAndView mav = new ModelAndView("bookings");
        mav.addObject("page", page);
        mav.addObject("actualTime", LocalDateTime.now());
        mav.addObject("oneMonthPrior", LocalDateTime.now().minusMonths(1));
        mav.addObject("eventBookings", eventBookings);
        mav.addObject("eventBookingsSize", eventBookings.size());
        return mav;
    }

    @RequestMapping(value = "/bookings/rate/{eventId}", method = { RequestMethod.POST })
    public ModelAndView rateEvent(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors,
                                  @Valid @ModelAttribute("rateForm") final RateForm rateForm, final BindingResult rateErrors,
                                  @PathVariable("eventId") final long eventId) {
        Event e = eventService.getEventById(eventId).orElse(null);
        if (e == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }

        if (rateErrors.hasErrors()) {
            LOGGER.error("BookForm has errors: {}", errors.getAllErrors().toArray());
            return bookings(form, rateForm, form.getPage());
        }

        userService.rateUser(userManager.getUserId(), e.getOrganizer().getId(), rateForm.getRating());
        return new ModelAndView("redirect:/bookings/");
    }

    @RequestMapping(value = "/bookings/cancel/{eventId}", method = { RequestMethod.GET })
    public ModelAndView cancelBooking(@ModelAttribute("bookForm") final BookForm form,
                                      @ModelAttribute("rateForm") final RateForm rateForm,
                                      @PathVariable("eventId") final int eventId) {
        final Event e = eventService.getEventById(eventId).orElse(null);
        if (e == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }
        final User user = userManager.getUser();
        final User eventUser = userService.getUserById(e.getOrganizer().getId()).orElse(null);
        if (eventUser == null) {
            LOGGER.error("Organizer not found");
            throw new UserNotFoundException();
        }
        final EventBooking eventBooking = eventBookingService.getBookingFromUser(user.getId(), eventId).orElse(null);
        if (eventBooking == null) {
            LOGGER.error("Booking not found");
            throw new BookingNotFoundException();
        }

        ModelAndView mav = new ModelAndView("cancelBooking");
        mav.addObject("eventBooking", eventBooking);
        return mav;
    }

    @RequestMapping(value = "/bookings/cancel/{eventId}", method = { RequestMethod.POST })
    public ModelAndView cancelBookingPost(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors,
                                          @Valid @ModelAttribute("rateForm") final RateForm rateForm, final BindingResult rateErrors,
                                          @PathVariable("eventId") final int eventId) {
        final Event e = eventService.getEventById(eventId).orElse(null);
        if (e == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }
        final User user = userManager.getUser();
        final User eventUser = userService.getUserById(e.getOrganizer().getId()).orElse(null);
        if (eventUser == null) {
            LOGGER.error("Organizer not found");
            throw new UserNotFoundException();
        }
        final EventBooking eventBooking = eventBookingService.getBookingFromUser(user.getId(), eventId).orElse(null);
        if (eventBooking == null) {
            LOGGER.error("Booking not found");
            throw new BookingNotFoundException();
        }

        EventBooking booking = new EventBooking(user, e, new ArrayList<>(), null);
        for (BookingForm bookingForm : form.getBookings()) {
            TicketBooking ticketBooking = new TicketBooking(ticketService.getTicketById(bookingForm.getTicketId()).orElse(null), bookingForm.getQty(), booking);
            booking.addBooking(ticketBooking);
        }

        if (errors.hasErrors()) {
            LOGGER.error("BookForm has errors: {}", errors.getAllErrors().toArray());
            return cancelBooking(form, rateForm, eventId);
        }

        try {
            eventBookingService.cancelBooking(booking, LocaleContextHolder.getLocale());
        } catch (SurpassedMaxTicketsException ex) {
            for (Map.Entry<Integer, Integer> error : ex.getErrorMap().entrySet()) {
                errors.rejectValue("bookings[" + error.getKey() + "].qty", "Max.bookForm.qty", new Object[]{error.getValue()}, "");
            }
            LOGGER.error("BookForm has errors: {}", errors.getAllErrors().toArray());
            return cancelBooking(form, rateForm, eventId);
        }

        return new ModelAndView("redirect:/bookings/");
    }

    @RequestMapping(value = "/bookings/{code}", method = { RequestMethod.GET })
    public ModelAndView booking(HttpServletRequest request, @PathVariable("code") final String code) {
        final EventBooking eventBooking = eventBookingService.getBooking(code).orElse(null);
        final User user = userManager.getUser();
        long userId = user.getId();
        if (eventBooking == null || (userId != eventBooking.getUser().getId() && userId != eventBooking.getEvent().getOrganizer().getId() &&
                userId != eventBooking.getEvent().getBouncer().getId())) {
            LOGGER.error("Booking not found");
            throw new BookingNotFoundException();
        }

        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        String bookUrl = baseUrl + "/bookings/" + code;
        byte[] encodeBase64 = Base64.getEncoder().encode(codeService.createQr(bookUrl));
        String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);

        final ModelAndView mav = new ModelAndView("booking");
        mav.addObject("eventBooking", eventBooking);
        mav.addObject("image", base64Encoded);
        return mav;
    }

    @RequestMapping(value = "/bookings/{code}/confirm", method = { RequestMethod.POST })
    public ModelAndView confirmBooking(@PathVariable("code") final String code) {
        EventBooking eventBooking = eventBookingService.getBooking(code).orElse(null);
        if (eventBooking == null) {
            LOGGER.error("Booking not found");
            throw new BookingNotFoundException();
        }

        eventBookingService.confirmBooking(eventBooking);
        return new ModelAndView("redirect:/bookings/" + code);
    }
}
