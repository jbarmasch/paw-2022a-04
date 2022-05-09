package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.helper.AuthUtils;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.webapp.exceptions.EventNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.BookForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import ar.edu.itba.paw.webapp.form.RateForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
                LOGGER.debug("Incorrect password or username");
                mav.addObject("error", true);
            }
            return mav;
        }
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.GET })
    public ModelAndView createForm(@ModelAttribute("userForm") final UserForm form) {
        if (!userManager.isAuthenticated())
            return new ModelAndView("register");
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("userForm") final UserForm form, final BindingResult errors, HttpServletRequest request) {
        if (errors.hasErrors())
            return createForm(form);

        userService.create(form.getUsername(), form.getPassword(), form.getMail());
        AuthUtils.requestAuthentication(request, form.getUsername(), form.getPassword());
        return new ModelAndView("redirect:" + AuthUtils.redirectionAuthenticationSuccess(request));
    }

    @RequestMapping(value = "/forgot-pass", method = RequestMethod.GET)
    public ModelAndView forgotPass() {
        if (!userManager.isAuthenticated())
            return new ModelAndView("forgotPass");
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/profile/{userId}", method = { RequestMethod.GET })
    public ModelAndView userProfile(@PathVariable("userId") final long userId) {
        UserStats stats = userService.getUserStats(userId).orElse(null);
        User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
        List<Event> events = eventService.getUserEvents(userId, 1).stream().limit(5).collect(Collectors.toList());

        final ModelAndView mav = new ModelAndView("profile");
        if (userManager.isAuthenticated() && userId == userManager.getUserId())
            mav.addObject("stats", stats);
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
        List<EventBooking> bookings = userService.getAllBookingsFromUser(userManager.getUserId(), page);
        final ModelAndView mav = new ModelAndView("bookings");
        mav.addObject("page", page);
        mav.addObject("actualTime", LocalDateTime.now());
        mav.addObject("bookings", bookings);
        mav.addObject("error", eventId);
        mav.addObject("size", bookings.size());
        return mav;
    }

    @RequestMapping(value = "/bookings/rate/{eventId}", method = { RequestMethod.POST })
    public ModelAndView rateEvent(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors,
                                  @Valid @ModelAttribute("rateForm") final RateForm rateForm, final BindingResult rateErrors,
                                  @PathVariable("eventId") final int eventId) {
        Event e = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        if (rateErrors.hasErrors())
            return bookings(form, rateForm, form.getPage(), eventId);
        userService.rateUser(userManager.getUserId(), e.getUser().getId(), rateForm.getRating());
        return new ModelAndView("redirect:/bookings/");
    }

    @RequestMapping(value = "/bookings/cancel/{eventId}", method = { RequestMethod.POST })
    public ModelAndView cancelBooking(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors,
                                      @Valid @ModelAttribute("rateForm") final RateForm rateForm, final BindingResult rateErrors,
                                      @PathVariable("eventId") final int eventId) {
        final Event e = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        final User user = userManager.getUser();
        final User eventUser = userService.getUserById(e.getUser().getId()).orElseThrow(RuntimeException::new);
        EventBooking eventBooking = userService.getBookingFromUser(user.getId(), eventId).orElseThrow(RuntimeException::new);

        int i = 0;
        List<TicketBooking> tickets = eventBooking.getBookings();
        for (Booking booking : form.getBookings()) {
            if (booking.getQty() != null && booking.getQty() > tickets.get(i).getQty()) {
                errors.rejectValue("bookings[" + i + "].qty", "Max.bookForm.qty", new Object[]{tickets.get(i).getQty()}, "");
            }
            i++;
        }

        if (errors.hasErrors()) {
            return bookings(form, rateForm, form.getPage(), eventId);
        }

        eventService.cancelBooking(form.getBookings(), user.getId(), user.getUsername(), user.getMail(), eventId, e.getName(), eventUser.getMail());
//            return new ModelAndView("redirect:/error");
        return new ModelAndView("redirect:/bookings/");
    }

    @ModelAttribute
    public void addAttributes(Model model, final SearchForm searchForm) {
        model.addAttribute("username", userManager.getUsername());
        model.addAttribute("isCreator", userManager.isCreator());
    }
}
