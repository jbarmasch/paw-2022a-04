package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(final EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("events", eventService.getAll(1));
        return mav;
    }

    @RequestMapping("/event/{eventId}")
    public ModelAndView eventDescription(@PathVariable("eventId") final long eventId) {
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("event", eventService.getEventById(eventId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping("/createEvent")
    public ModelAndView createEvent(@RequestParam(value = "name") final String username,
                                    @RequestParam(value = "description") final String description,
                                    @RequestParam(value = "location") final String location,
                                    @RequestParam(value = "maxCapacity") final int maxCapacity) {
        final Event e = eventService.create(username, description, location, maxCapacity);
        return new ModelAndView("redirect:/?eventId=" + e.getId());
    }

    @RequestMapping("/event")
    public ModelAndView eventRedirect() {
        return new ModelAndView("event");
    }
}
