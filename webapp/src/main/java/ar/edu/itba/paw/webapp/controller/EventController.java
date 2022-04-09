package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.service.MailService;
import ar.edu.itba.paw.webapp.form.EventForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.stereotype.Controller;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.service.EventService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.text.SimpleDateFormat;

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

    @RequestMapping("/events")
    public ModelAndView browseEvents(@RequestParam(value = "filterBy", required = false) final String[] filterBy,
                                     @RequestParam(value = "locations", required = false) final String[] locations,
                                     @RequestParam(value = "minPrice", required = false) final Double minPrice,
                                     @RequestParam(value = "maxPrice", required = false) final Double maxPrice) {
        final ModelAndView mav = new ModelAndView("index");
        if (filterBy != null)
            mav.addObject("events", eventService.filterBy(filterBy, locations, minPrice, maxPrice, 1));
        else
            mav.addObject("events", eventService.getAll(1));
        return mav;
    }

    @RequestMapping("/event/{eventId}")
    public ModelAndView eventDescription(@PathVariable("eventId") final long eventId) {
        final ModelAndView mav = new ModelAndView("event");
        mav.addObject("event", eventService.getEventById(eventId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @ModelAttribute("locations")
    public String[] populateLocations() {
        return Location.getNames();
    }


    @RequestMapping(value = "/createEvent", method = { RequestMethod.GET })
    public ModelAndView createForm(@ModelAttribute("eventForm") final EventForm form){
        return new ModelAndView("createEvent");
    }

    @RequestMapping("/createEvent")
    public ModelAndView createEvent(@Valid @ModelAttribute("eventForm") final EventForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return createForm(form);
        }
        final Event e = eventService.create(form.getName(), form.getDescription(), form.getLocation().getName(), form.getMaxCapacity(), form.getPrice(), form.getDate());
        return new ModelAndView("redirect:/event/" + e.getId());
    }

    @RequestMapping("/book")
    public ModelAndView bookEvent(@RequestParam("eventId") final long eventId,
                                 @RequestParam("mail") final String mail) {
        final Event e = eventService.getEventById(eventId).orElseThrow(UserNotFoundException::new);
        mailService.sendMail(mail, "Book", "Book event" + e.getName());
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
