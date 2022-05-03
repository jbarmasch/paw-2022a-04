package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Booking;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.auth.AuthenticationManager;
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
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final EventService eventService;
    private final AuthenticationManager authenticationManager;
    private final UserManager userManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(final UserService userService, final EventService eventService, final AuthenticationManager authenticationManager,
                          final UserManager userManager) {
        this.userService = userService;
        this.eventService = eventService;
        this.authenticationManager = authenticationManager;
        this.userManager = userManager;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error, HttpServletRequest request) {
        if (!userManager.isAuthenticated()) {
            ModelAndView mav = new ModelAndView("login");
            if (error == null) {
                mav.addObject("error", false);
                authenticationManager.setReferrer(request, request.getHeader("Referer"));
            } else
                mav.addObject("error", true);
            return mav;
        }
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.GET })
    public ModelAndView createForm(@ModelAttribute("registerForm") final UserForm form) {
        if (!userManager.isAuthenticated()) {
            return new ModelAndView("register");
        }
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm form, final BindingResult errors, HttpServletRequest request) {
        if (errors.hasErrors())
            return createForm(form);

        userService.create(form.getUsername(), form.getPassword(), form.getMail());
        authenticationManager.requestAuthentication(request, form.getUsername(), form.getPassword());
        return new ModelAndView("redirect:" + authenticationManager.redirectionAuthenticationSuccess(request));
    }

    @RequestMapping(value = "/forgotPass", method = RequestMethod.GET)
    public ModelAndView forgotPass() {
        if (!userManager.isAuthenticated())
            return new ModelAndView("forgotPass");
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/profile/{userId}", method = { RequestMethod.GET })
    public ModelAndView userProfile(@PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user", userService.getUserById(userId).orElseThrow(UserNotFoundException::new));
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
        List<Booking> bookings = userService.getAllBookingsFromUser(userManager.getUserId(), page);
        final ModelAndView mav = new ModelAndView("bookings");
        mav.addObject("page", page);
        mav.addObject("bookings", bookings);
        mav.addObject("error", eventId);
        mav.addObject("size", bookings.size());
        return mav;
    }

    @RequestMapping(value = "/bookings/rate/{eventId}", method = { RequestMethod.POST })
    public ModelAndView rateEvent(@Valid @ModelAttribute("rateForm") final RateForm form, final BindingResult errors,
                                  @PathVariable("eventId") final int eventId) {
        eventService.rateEvent(userManager.getUserId(), eventId, form.getRating());
        return new ModelAndView("redirect:/bookings/");
    }


    @RequestMapping(value = "/bookings/cancel/{eventId}", method = { RequestMethod.POST })
    public ModelAndView cancelBooking(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors,
                                      @Valid @ModelAttribute("rateForm") final RateForm rateForm, final BindingResult rateErrors,
                                      @PathVariable("eventId") final int eventId) {
        final Event e = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        final User user = userManager.getUser();
        final User eventUser = userService.getUserById(e.getUser().getId()).orElseThrow(RuntimeException::new);
        int bookingQty = userService.getBookingFromUser(user.getId(), eventId).orElseThrow(RuntimeException::new).getQty();

        if (errors.hasErrors() || form.getQty() > bookingQty) {
            errors.rejectValue("qty", "Max.bookForm.qty", new Object[] {e.getMaxCapacity()}, "");
            return bookings(form, rateForm, form.getPage(), bookingQty);
        }

        if (!eventService.cancelBooking(form.getQty(), user.getId(), user.getUsername(), user.getMail(), eventId, e.getName(), eventUser.getMail()))
            return new ModelAndView("redirect:/error");
        return new ModelAndView("redirect:/bookings/");
    }

    @ModelAttribute
    public void addAttributes(Model model, final SearchForm searchForm) {
        model.addAttribute("username", userManager.getUsername());
    }
}
