package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.Type;
import ar.edu.itba.paw.service.MailService;
import ar.edu.itba.paw.webapp.form.EventForm;
import ar.edu.itba.paw.webapp.form.BookForm;
import ar.edu.itba.paw.webapp.form.FilterForm;
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
import java.util.Date;

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
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("index");
//        mav.addObject("events", eventService.getAll(1));
        return mav;
    }

    @RequestMapping(value = "/events", method = { RequestMethod.GET })
    public ModelAndView browseEvents(@ModelAttribute("filterForm") final FilterForm form,
                                     @RequestParam(value = "filterBy", required = false) final String[] filterBy,
                                     @RequestParam(value = "locations", required = false) final String[] locations,
                                     @RequestParam(value = "types", required = false) final String[] types,
                                     @RequestParam(value = "minPrice", required = false) final Double minPrice,
                                     @RequestParam(value = "maxPrice", required = false) final Double maxPrice) {
        final ModelAndView mav = new ModelAndView("events");
        mav.addObject("allLocations", Location.getNames());
        mav.addObject("allTypes", Type.getNames());
        if (filterBy != null)
            mav.addObject("events", eventService.filterBy(filterBy, locations, types, minPrice, maxPrice, 1));
        else
            mav.addObject("events", eventService.getAll(1));
        return mav;
    }

    @RequestMapping(value = "/events", method = { RequestMethod.POST })
    public ModelAndView browseByFilters(@Valid @ModelAttribute("filterForm") final FilterForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return browseEvents(form, null, null, null, null, null);
        }
        String filters = "";
        String endURL = "";
        if (form.getLocations() != null) {
            filters += "location";
            endURL += "&locations=" + form.getLocations();
        }
        if (form.getTypes() != null) {
            if (!filters.isEmpty())
                filters += ", ";
            filters += "type";
            endURL += "&types=" + form.getTypes();
        }
        if (form.getMaxPrice() != null || form.getMinPrice() != null) {
            if (!filters.isEmpty())
                filters += ", ";
            filters += "price";
            if (form.getMinPrice() != null)
                endURL += "&minPrice=" + form.getMinPrice();
            if (form.getMaxPrice() != null)
                endURL += "&maxPrice=" + form.getMaxPrice();
        }
        if (filters.isEmpty())
            return new ModelAndView("redirect:/events");
        return new ModelAndView("redirect:/events?filterBy=" + filters + endURL);
    }

    @RequestMapping(value = "/event/{eventId}", method = RequestMethod.GET)
    public ModelAndView eventDescription(@ModelAttribute("bookForm") final BookForm form, @PathVariable("eventId") final int eventId) {
        final ModelAndView mav = new ModelAndView("event");
        mav.addObject("event", eventService.getEventById(eventId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/createEvent", method = { RequestMethod.GET })
    public ModelAndView createForm(@ModelAttribute("eventForm") final EventForm form) {
        final ModelAndView mav = new ModelAndView("createEvent");
        mav.addObject("locations", Location.getNames());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = formatter.format(new Date());
        mav.addObject("currentDate", dateStr.substring(0, 10) + "T" + dateStr.substring(11, 16));
        mav.addObject("types", Type.getNames());
        return mav;
    }

    @RequestMapping(value = "/createEvent", method = { RequestMethod.POST })
    public ModelAndView createEvent(@Valid @ModelAttribute("eventForm") final EventForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return createForm(form);
        }
        final Event e = eventService.create(form.getName(), form.getDescription(), form.getLocation(), form.getMaxCapacity(), form.getPrice(), form.getType(), form.getDate());
        return new ModelAndView("redirect:/event/" + e.getId());
    }

    @RequestMapping(value = "/event/{eventId}", method = { RequestMethod.POST }, params = "submit")
    public ModelAndView bookEvent(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return eventDescription(form, form.getEventId());
        }
        final Event e = eventService.getEventById(form.getEventId()).orElseThrow(UserNotFoundException::new);
        mailService.sendMail(form.getMail(), "Book", "Book event " + e.getName());
        return new ModelAndView("redirect:/event/" + e.getId());
    }

    @RequestMapping(value = "/event/{eventId}", method = { RequestMethod.POST }, params = "cancel")
    public ModelAndView cancelBooking(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return eventDescription(form, form.getEventId());
        }
        final Event e = eventService.getEventById(form.getEventId()).orElseThrow(UserNotFoundException::new);
        mailService.sendMail(form.getMail(), "Cancel", "Cancel event " + e.getName());
        return new ModelAndView("redirect:/event/" + e.getId());
    }
}
