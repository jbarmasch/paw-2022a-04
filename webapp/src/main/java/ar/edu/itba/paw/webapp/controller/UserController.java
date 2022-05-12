package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.helper.AuthUtils;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.webapp.exceptions.EventNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.BookingNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.StatsNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.BookForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import ar.edu.itba.paw.webapp.form.RateForm;
import ar.edu.itba.paw.webapp.form.UserForm;
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
        List<Event> events = eventService.getUserEvents(userId, 1, locale).stream().limit(5).collect(Collectors.toList());

        final ModelAndView mav = new ModelAndView("profile");
        if (userManager.isAuthenticated() && userId == userManager.getUserId()) {
            UserStats stats = userService.getUserStats(userId, locale).orElse(null);
            LOGGER.debug("User can see stats");
            mav.addObject("stats", stats);
        }
        mav.addObject("user", user);
        mav.addObject("events", events);
        mav.addObject("size", events.size());
        return mav;
    }

    @RequestMapping(value = "/profile", method = { RequestMethod.GET })
    public ModelAndView getUser() {
        return new ModelAndView("redirect:/profile/" + userManager.getUserId());
    }

    @RequestMapping(value = "/bookings", method = { RequestMethod.GET })
    public ModelAndView bookings(@ModelAttribute("bookForm") final BookForm form,
                                 @ModelAttribute("rateForm") final RateForm rateForm,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") final int page,
                                 @RequestParam(required = false) final Integer eventId) {
        Locale locale = LocaleContextHolder.getLocale();
        List<EventBooking> previousBookings = userService.getAllPreviousBookingsFromUser(userManager.getUserId(), page, locale);
        List<EventBooking> futureBookings = userService.getAllFutureBookingsFromUser(userManager.getUserId(), page, locale);
        final ModelAndView mav = new ModelAndView("bookings");
        mav.addObject("page", page);
        mav.addObject("actualTime", LocalDateTime.now());
        mav.addObject("previousBookings", previousBookings);
        mav.addObject("futureBookings", futureBookings);
        mav.addObject("error", eventId);
        mav.addObject("previousSize", previousBookings.size());
        mav.addObject("futureSize", futureBookings.size());
        return mav;
    }

    @RequestMapping(value = "/bookings/rate/{eventId}", method = { RequestMethod.POST })
    public ModelAndView rateEvent(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors,
                                  @Valid @ModelAttribute("rateForm") final RateForm rateForm, final BindingResult rateErrors,
                                  @PathVariable("eventId") final int eventId) {
        Locale locale = LocaleContextHolder.getLocale();
        Event e = eventService.getEventById(eventId, locale).orElse(null);
        if (e == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }
        if (rateErrors.hasErrors()) {
            LOGGER.error("BookForm has errors: {}", errors.getAllErrors().toArray());
            return bookings(form, rateForm, form.getPage(), eventId);
        }
        userService.rateUser(userManager.getUserId(), e.getUser().getId(), rateForm.getRating());
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
        final User eventUser = userService.getUserById(e.getUser().getId()).orElse(null);
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
        List<TicketBooking> tickets = eventBooking.getBookings();
        Map<Integer, TicketBooking> ticketMap = new HashMap<>();
        for (TicketBooking ticket : tickets) {
            ticketMap.put(ticket.getTicket().getId(), ticket);
        }

        for (Booking booking : form.getBookings()) {
            TicketBooking ticket = ticketMap.get(booking.getTicketId());
            if (booking.getQty() != null && booking.getQty() > ticket.getQty())
                errors.rejectValue("bookings[" + i + "].qty", "Max.bookForm.qty", new Object[]{ticket.getQty()}, "");
            i++;
        }

        if (errors.hasErrors()) {
            LOGGER.error("BookForm has errors: {}", errors.getAllErrors().toArray());
            return cancelBooking(form, rateForm, eventId);
        }

        eventService.cancelBooking(form.getBookings(), user.getId(), user.getUsername(), user.getMail(), eventId, e.getUser().getUsername(), e.getName(), eventUser.getMail(), LocaleContextHolder.getLocale());
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
        final User eventUser = userService.getUserById(e.getUser().getId()).orElse(null);
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
