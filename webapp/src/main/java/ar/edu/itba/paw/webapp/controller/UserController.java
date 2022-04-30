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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "404");
        return mav;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            ModelAndView mav = new ModelAndView("login");
            if (error == null) {
                mav.addObject("error", false);
            } else {
                mav.addObject("error", true);
            }
            return mav;
        }
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
    public ModelAndView bookings(@ModelAttribute("bookForm") final BookForm form,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") final int page,
                                 @RequestParam(required = false) final Integer eventId) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        int userId = userService.findByUsername(username).orElseThrow(UserNotFoundException::new).getId();

        List<Booking> bookings = userService.getAllBookingsFromUser(userId, page);
        final ModelAndView mav = new ModelAndView("bookings");
        mav.addObject("page", page);
        mav.addObject("bookings", bookings);
        mav.addObject("error", eventId);
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
        int bookingQty = userService.getBookingFromUser(user.getId(), eventId).orElseThrow(RuntimeException::new).getQty();

        if (errors.hasErrors() || form.getQty() > bookingQty) {
            errors.rejectValue("qty", "Max.bookForm.qty", new Object[] {e.getMaxCapacity()}, "");
            return bookings(form, form.getPage(), bookingQty);
        }

        if (!userService.cancelBooking(form.getQty(), user.getId(), username, user.getMail(), eventId, e.getName(), eventUser.getMail()))
            return new ModelAndView("redirect:/error");
        return new ModelAndView("redirect:/bookings/");
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        String username = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken))
            username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        model.addAttribute("username", username);
    }
}
