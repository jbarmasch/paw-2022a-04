package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.service.MailService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {
    private final UserService us;
    private final EventService evs;
    private final MailService ms;

    @Autowired
    public HelloWorldController(final UserService us, final EventService evs, final MailService ms) {
        this.us = us;
        this.evs = evs;
        this.ms = ms;
    }

    @RequestMapping("/")
    public ModelAndView helloWorld(@RequestParam(name = "eventId", defaultValue = "1") final long eventId) {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("events", evs.getAll(1));
        return mav;
    }

    @RequestMapping("/profile/{userId}")
    public ModelAndView userProfile(@PathVariable("userId") final long userId) {
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user", us.getUserById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping("/event/{eventId}")
    public ModelAndView eventDescription(@PathVariable("eventId") final long eventId) {
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("event", evs.getEventById(eventId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping("/create")
    public ModelAndView create(@RequestParam(value = "username") final String username,
                               @RequestParam(value = "password") final String password) {
            final User u = us.create(username, password);
            return new ModelAndView("redirect:/?userId=" + u.getId());
    }

    @RequestMapping("/createEvent")
    public ModelAndView createEvent(@RequestParam(value = "name") final String username,
                                    @RequestParam(value = "description") final String description,
                                    @RequestParam(value = "location") final String location,
                                    @RequestParam(value = "maxCapacity") final int maxCapacity) {
        final Event e = evs.create(username, description, location, maxCapacity);
        return new ModelAndView("redirect:/?eventId=" + e.getId());
    }

    @RequestMapping("/event")
    public ModelAndView eventRedirect() {
        return new ModelAndView("event");
    }

    @RequestMapping("/mail")
    public ModelAndView sendMail() {
        ms.sendMail("santilococo.01@gmail.com", "Test", "Probando mandar un mail");
        return new ModelAndView("redirect:/");
    }
}
