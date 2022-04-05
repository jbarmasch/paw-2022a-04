package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.service.MailService;
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
    private final MailService mailService;

    @Autowired
    public EventController(final EventService eventService, final MailService mailService) {
        this.eventService = eventService;
        this.mailService = mailService;
    }

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("events", eventService.getAll(1));
        return mav;
    }

//    @RequestMapping("/event")
//    public ModelAndView eventDescription(@RequestParam("eventId") final long eventId) {
//        final ModelAndView mav = new ModelAndView("event");
//        mav.addObject("event", eventService.getEventById(eventId).orElseThrow(UserNotFoundException::new));
//        return mav;
//    }

    @RequestMapping("/event/{eventId}")
    public ModelAndView eventDescription(@PathVariable("eventId") final long eventId) {
        final ModelAndView mav = new ModelAndView("event");
        mav.addObject("event", eventService.getEventById(eventId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping("/createEvent")
    public ModelAndView createEvent(@RequestParam(value = "name") final String username,
                                    @RequestParam(value = "description") final String description,
                                    @RequestParam(value = "location") final String location,
                                    @RequestParam(value = "maxCapacity") final int maxCapacity,
                                    @RequestParam(value = "price") final double price) {
        final Event e = eventService.create(username, description, location, maxCapacity, price);
//        return new ModelAndView("redirect:/event?eventId=" + e.getId());
        return new ModelAndView("redirect:/event/" + e.getId());
    }

    @RequestMapping("/book")
    public ModelAndView bookEvent(@RequestParam("eventId") final long eventId,
                                 @RequestParam("mail") final String mail) {
        final Event e = eventService.getEventById(eventId).orElseThrow(UserNotFoundException::new);
        mailService.sendMail(mail, "Book", "Book event" + e.getName());
//        return new ModelAndView("redirect:/event?eventId=" + e.getId());
        return new ModelAndView("redirect:/event/" + e.getId());
    }

    @RequestMapping("/cancel")
    public ModelAndView sendMail(@RequestParam("eventId") final long eventId,
                                 @RequestParam("mail") final String mail) {
        final Event e = eventService.getEventById(eventId).orElseThrow(UserNotFoundException::new);
        mailService.sendMail(mail, "Cancel", "Cancel event" + e.getName());
        return new ModelAndView("redirect:/event/" + e.getId());
    }
}
