package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Booking;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.MailService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.exceptions.EventNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.BookForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final EventService eventService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(final UserService userService, final EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("error");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        LOGGER.debug("Debug");
        LOGGER.warn("Warning");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken)
            return new ModelAndView("login");
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView redirectLogin() {
        return new ModelAndView("redirect:/events");
    }

    @RequestMapping("/profile/{userId}")
    public ModelAndView userProfile(@PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user", userService.getUserById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/register", method = { RequestMethod.GET })
    public ModelAndView createForm(@ModelAttribute("registerForm") final UserForm form) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken)
            return new ModelAndView("register");
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm form, final BindingResult errors) {
        if (errors.hasErrors())
            return createForm(form);

        final User u = userService.create(form.getUsername(), form.getPassword(), form.getMail());
        return new ModelAndView("redirect:/login/");
    }

    @RequestMapping(value = "/bookings", method = { RequestMethod.GET })
    public ModelAndView bookings(@ModelAttribute("bookForm") final BookForm form) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        int userId = userService.findByUsername(username).orElseThrow(UserNotFoundException::new).getId();

        List<Booking> bookings = userService.getAllBookingsFromUser(userId);
        final ModelAndView mav = new ModelAndView("bookings");
        mav.addObject("bookings", bookings);
        mav.addObject("size", bookings.size());
        return mav;
    }

    @RequestMapping(value = "/forgotPass", method = RequestMethod.GET)
    public ModelAndView forgotPass() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken)
            return new ModelAndView("forgotPass");
        return new ModelAndView("redirect:/");
    }


    @RequestMapping(value = "/bookings/cancel/{eventId}", method = { RequestMethod.POST })
    public ModelAndView cancelBooking(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors, @PathVariable("eventId") final int eventId) {
        final Event e = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        final String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        final User user = userService.findByUsername(username).orElseThrow(UserNotFoundException::new);
        final User eventUser = userService.getUserById(e.getUserId()).orElseThrow(RuntimeException::new);

        if (errors.hasErrors())
            return bookings(form);

        if (!userService.cancelBooking(form.getQty(), user.getId(), username, user.getMail(), eventId, e.getName(), eventUser.getMail()))
            System.out.println("error");

        return new ModelAndView("redirect:/bookings/");
    }
}
