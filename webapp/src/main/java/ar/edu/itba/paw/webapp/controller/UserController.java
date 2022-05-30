package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.CodeService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.helper.AuthUtils;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.webapp.exceptions.EventNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.BookingNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CodeService codeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error, HttpServletRequest request) {
        if (!userManager.isAuthenticated()) {
            ModelAndView mav = new ModelAndView("login");
            if (error == null) {
                mav.addObject("error", false);
                AuthUtils.setReferrer(request, request.getHeader("Referer"));
            } else {
                LOGGER.error("Incorrect password or username");
                mav.addObject("error", true);
            }
            return mav;
        }

        LOGGER.debug("User is already authenticated");
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.GET })
    public ModelAndView createForm(@ModelAttribute("userForm") final UserForm form) {
        if (!userManager.isAuthenticated())
            return new ModelAndView("register");

        LOGGER.debug("User is already authenticated");
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("userForm") final UserForm form, final BindingResult errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            LOGGER.error("UserForm has errors: {}", errors.getAllErrors().toArray());
            return createForm(form);
        }

        LOGGER.debug("Create new user with username {}", form.getUsername());
        userService.create(form.getUsername(), form.getPassword(), form.getMail());
        AuthUtils.requestAuthentication(request, form.getUsername(), form.getPassword());
        return new ModelAndView("redirect:" + AuthUtils.redirectionAuthenticationSuccess(request));
    }

    @RequestMapping(value = "/forgot-pass", method = RequestMethod.GET)
    public ModelAndView forgotPass() {
        if (!userManager.isAuthenticated())
            return new ModelAndView("forgotPass");

        LOGGER.debug("User is already authenticated");
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/profile/{userId}", method = { RequestMethod.GET })
    public ModelAndView userProfile(@PathVariable("userId") final long userId) {
        Locale locale = LocaleContextHolder.getLocale();
        User user = userService.getUserById(userId).orElse(null);
        if (user == null) {
            LOGGER.error("User not found");
            throw new UserNotFoundException();
        } else if (!userManager.isCreator(user)) {
            return new ModelAndView("redirect:/403");
        }
        List<Event> events = user.getEvents();
        System.out.println("SIZE" + events.size());
        events = events.stream().limit(5).collect(Collectors.toList());

        final ModelAndView mav = new ModelAndView("profile");
        if (userManager.isAuthenticated() && userId == userManager.getUserId()) {
            UserStats stats = userService.getUserStats(userId, locale).orElse(null);
            LOGGER.debug("User can see stats");
            mav.addObject("stats", stats);
        }
        mav.addObject("user", user);
        mav.addObject("events", events);
        System.out.println("SIZE" + events.size());
        mav.addObject("size", events.size());
        return mav;
    }

    @RequestMapping(value = "/profile", method = { RequestMethod.GET })
    public ModelAndView getUser() {
        return new ModelAndView("redirect:/profile/" + userManager.getUserId());
    }


    @RequestMapping(value = "/bookings/{code}", method = { RequestMethod.GET })
    public ModelAndView booking(HttpServletRequest request, @PathVariable("code") final String code) {
        Locale locale = LocaleContextHolder.getLocale();
        EventBooking eventBooking = userService.getBooking(code, locale).orElse(null);
        User user = userManager.getUser();
        long userId = user.getId();
        if (eventBooking == null || (userId != eventBooking.getUser().getId() && userId != eventBooking.getEvent().getOrganizer().getId())) {
            LOGGER.error("Booking not found");
            throw new BookingNotFoundException();
        }

//        System.out.println(request.getScheme() + "://" + request.getServerName());
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        String bookUrl = baseUrl + "/bookings/" + code;
        byte[] encodeBase64 = Base64.getEncoder().encode(codeService.createQr(bookUrl));
        String base64Encoded = new String(encodeBase64, StandardCharsets.UTF_8);

        final ModelAndView mav = new ModelAndView("booking");
        mav.addObject("eventBooking", eventBooking);
        mav.addObject("image", base64Encoded);
        return mav;
    }

    @RequestMapping(value = "/bookings", method = { RequestMethod.GET })
    public ModelAndView bookings(@ModelAttribute("bookForm") final BookForm form,
                                 @ModelAttribute("rateForm") final RateForm rateForm,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") final int page) {
        Locale locale = LocaleContextHolder.getLocale();
        List<EventBooking> eventBookings = userService.getAllBookingsFromUser(userManager.getUserId(), page, locale);
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
        Locale locale = LocaleContextHolder.getLocale();
        Event e = eventService.getEventById(eventId, locale).orElse(null);
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

    @RequestMapping(value = "/bookings/cancel/{eventId}", method = { RequestMethod.POST })
    public ModelAndView cancelBookingPost(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors,
                                      @Valid @ModelAttribute("rateForm") final RateForm rateForm, final BindingResult rateErrors,
                                      @PathVariable("eventId") final int eventId) {
        Locale locale = LocaleContextHolder.getLocale();
        final Event e = eventService.getEventById(eventId, locale).orElse(null);
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

        EventBooking eventBooking = userService.getBookingFromUser(user.getId(), eventId, locale).orElse(null);
        if (eventBooking == null) {
            LOGGER.error("Booking not found");
            throw new BookingNotFoundException();
        }

        int i = 0;
        List<TicketBooking> ticketBookings = eventBooking.getTicketBookings();
        Map<Long, TicketBooking> ticketMap = new HashMap<>();
        for (TicketBooking ticketBooking : ticketBookings) {
            ticketMap.put(ticketBooking.getTicket().getId(), ticketBooking);
        }

        EventBooking booking = new EventBooking(user, e, new ArrayList<>(), null);
        for (BookingForm bookingForm : form.getBookings()) {
            TicketBooking ticketBooking = new TicketBooking(eventService.getTicketById(bookingForm.getTicketId()).orElse(null), bookingForm.getQty(), booking);
            booking.addBooking(ticketBooking);
        }

        for (TicketBooking ticketBooking : booking.getTicketBookings()) {
            if (ticketBooking.getQty() != null && ticketBooking.getQty() >= ticketMap.get(ticketBooking.getTicket().getId()).getQty())
                errors.rejectValue("bookings[" + i + "].qty", "Max.bookForm.qty", new Object[]{ticketMap.get(ticketBooking.getTicket().getId()).getQty()}, "");
            i++;
        }

        if (errors.hasErrors()) {
            LOGGER.error("BookForm has errors: {}", errors.getAllErrors().toArray());
            return cancelBooking(form, rateForm, eventId);
        }

        eventService.cancelBooking(booking, LocaleContextHolder.getLocale());
        return new ModelAndView("redirect:/bookings/");
    }


    @RequestMapping(value = "/bookings/cancel/{eventId}", method = { RequestMethod.GET })
    public ModelAndView cancelBooking(@ModelAttribute("bookForm") final BookForm form,
                                      @ModelAttribute("rateForm") final RateForm rateForm,
                                      @PathVariable("eventId") final int eventId) {
        Locale locale = LocaleContextHolder.getLocale();
        final Event e = eventService.getEventById(eventId, locale).orElse(null);
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
        EventBooking eventBooking = userService.getBookingFromUser(user.getId(), eventId, locale).orElse(null);
        if (eventBooking == null) {
            LOGGER.error("Booking not found");
            throw new BookingNotFoundException();
        }

        ModelAndView mav = new ModelAndView("cancelBooking");
        mav.addObject("eventBooking", eventBooking);
        return mav;
    }

    @ModelAttribute
    public void addAttributes(Model model, final SearchForm searchForm) {
        model.addAttribute("username", userManager.getUsername());
        model.addAttribute("isCreator", userManager.isCreator());
    }
}
