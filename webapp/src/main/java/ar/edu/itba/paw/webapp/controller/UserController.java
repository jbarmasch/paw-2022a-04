package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.helper.AuthUtils;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
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

    @RequestMapping(value = "/profile", method = { RequestMethod.GET })
    public ModelAndView getUser() {
        return new ModelAndView("redirect:/profile/" + userManager.getUserId());
    }

    @RequestMapping(value = "/profile/{userId}", method = { RequestMethod.GET })
    public ModelAndView userProfile(@PathVariable("userId") final long userId) {
        User user = userService.getUserById(userId).orElse(null);
        if (user == null) {
            LOGGER.error("User not found");
            throw new UserNotFoundException();
        } else if (userManager.getUserId() != userId && !userManager.isCreator(user)) {
            return new ModelAndView("redirect:/403");
        }
        List<Event> events = user.getEvents();
        events = events.stream().limit(5).collect(Collectors.toList());

        final ModelAndView mav = new ModelAndView("profile");
        if (userManager.isAuthenticated() && userId == userManager.getUserId()) {
            UserStats stats = userService.getUserStats(userId).orElse(null);
            LOGGER.debug("User can see stats");
            mav.addObject("stats", stats);
        }
        mav.addObject("user", user);
        mav.addObject("events", events);
        mav.addObject("size", events.size());
        return mav;
    }

//    @ModelAttribute
//    public void addAttributes(Model model, final SearchForm searchForm) {
//        Locale locale = LocaleContextHolder.getLocale();
//        Tag.setLocale(locale);
//        Type.setLocale(locale);
//        model.addAttribute("username", userManager.getUsername());
//        model.addAttribute("isCreator", userManager.isCreator());
//        model.addAttribute("isBouncer", userManager.isBouncer());
//    }
}
