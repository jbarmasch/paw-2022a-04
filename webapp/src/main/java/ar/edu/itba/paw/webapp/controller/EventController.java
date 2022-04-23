package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.service.ImageService;
import ar.edu.itba.paw.service.LocationService;
import ar.edu.itba.paw.service.TypeService;
import ar.edu.itba.paw.service.TagService;
import ar.edu.itba.paw.service.MailService;
import ar.edu.itba.paw.webapp.exceptions.EventNotFoundException;
import ar.edu.itba.paw.webapp.form.EventForm;
import ar.edu.itba.paw.webapp.form.BookForm;
import ar.edu.itba.paw.webapp.form.FilterForm;
import ar.edu.itba.paw.webapp.helper.FilterUtils;
import ar.edu.itba.paw.webapp.validations.IntegerArray;
import ar.edu.itba.paw.webapp.validations.Price;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@Controller
public class EventController {
    private final EventService eventService;
    private final LocationService locationService;
    private final ImageService imageService;
    private final MailService mailService;
    private final TypeService typeService;
    private final TagService tagService;

    @Autowired
    public EventController(final EventService eventService, final LocationService locationService, final ImageService imageService, final MailService mailService, final TypeService typeService, TagService tagService) {
        this.eventService = eventService;
        this.locationService = locationService;
        this.imageService = imageService;
        this.mailService = mailService;
        this.typeService = typeService;
        this.tagService = tagService;
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView noSuchEvent() {
        return new ModelAndView("error");
    }

    @ExceptionHandler(MethodConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView constraintViolation(MethodConstraintViolationException e) {
        final ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", e.getMessage());
        return mav;
    }

    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/events", method = { RequestMethod.GET })
    public ModelAndView browseEvents(@ModelAttribute("filterForm") final FilterForm form,
                                     @RequestParam(value = "locations", required = false) @IntegerArray final String[] locations,
                                     @RequestParam(value = "types", required = false) @IntegerArray final String[] types,
                                     @RequestParam(value = "minPrice", required = false) @DecimalMin("0.00") final Double minPrice,
                                     @RequestParam(value = "maxPrice", required = false) @DecimalMin("0.00") final Double maxPrice) {
        final ModelAndView mav = new ModelAndView("events");
        mav.addObject("allLocations", locationService.getAll());
        mav.addObject("allTypes", typeService.getAll());
        List<Event> events = eventService.filterBy(locations, types, minPrice, maxPrice, 1);
        mav.addObject("events", events);
        mav.addObject("size", events.size());
        return mav;
    }

    @RequestMapping(value = "/events", method = { RequestMethod.POST })
    public ModelAndView browseByFilters(@Valid @ModelAttribute("filterForm") final FilterForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return new ModelAndView("error");
        }

        Map<String, Object> filters = new HashMap<>();
        filters.put("locations", form.getLocations());
        filters.put("types", form.getTypes());
        filters.put("minPrice", form.getMinPrice());
        filters.put("maxPrice", form.getMaxPrice());
        String endURL = FilterUtils.createFilter(filters);

        if (endURL.isEmpty())
            return new ModelAndView("redirect:/events");
        return new ModelAndView("redirect:/events?" + endURL);
    }

    @RequestMapping(value = "/events/{eventId}", method = RequestMethod.GET)
    public ModelAndView eventDescription(@ModelAttribute("bookForm") final BookForm form, @PathVariable("eventId") final int eventId) {
        final ModelAndView mav = new ModelAndView("event");
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        mav.addObject("event", event);
        return mav;
    }

    @RequestMapping(value = "/events/{eventId}", method = { RequestMethod.POST }, params = "submit")
    public ModelAndView bookEvent(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors, @PathVariable("eventId") final int eventId) {
        if (errors.hasErrors()) {
            return eventDescription(form, eventId);
        }
        final Event e = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        mailService.sendMail(form.getMail(), "Reserva recibida", "Se ha recibido una reserva de " + form.getQty() + " entradas a nombre de " + form.getName() + " " + form.getSurname() + " para " + e.getName() + ".");
        return new ModelAndView("redirect:/events/" + e.getId());
    }

    @RequestMapping(value = "/events/{eventId}", method = { RequestMethod.POST }, params = "cancel")
    public ModelAndView cancelBooking(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors, @PathVariable("eventId") final int eventId) {
        if (errors.hasErrors()) {
            return eventDescription(form, eventId);
        }
        final Event e = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        mailService.sendMail(form.getMail(), "Cancel", "Cancel event " + e.getName());
        return new ModelAndView("redirect:/events/" + e.getId());
    }

    @RequestMapping(value = "/createEvent", method = { RequestMethod.GET })
    public ModelAndView createForm(@ModelAttribute("eventForm") final EventForm form) {
        final ModelAndView mav = new ModelAndView("createEvent");
        mav.addObject("locations", locationService.getAll());
        mav.addObject("currentDate", LocalDateTime.now().toString().substring(0,16));
        mav.addObject("types", typeService.getAll());
        mav.addObject("allTags", tagService.getAll());
        return mav;
    }

    @RequestMapping(value = "/createEvent", method = { RequestMethod.POST }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ModelAndView createEvent(@Valid @ModelAttribute("eventForm") final EventForm form, final BindingResult errors, @RequestParam("image") CommonsMultipartFile imageFile) {
        if (errors.hasErrors()) {
            return createForm(form);
        }
        final int imageId = imageService.addEventImage(imageFile.getBytes());
        final Event e = eventService.create(form.getName(), form.getDescription(), form.getLocation(), form.getMaxCapacity(), form.getPrice(), form.getType(), form.getTimestamp(), imageId, form.getTags());
        return new ModelAndView("redirect:/events/" + e.getId());
    }

    @RequestMapping(value = "/events/{eventId}/modify", method = { RequestMethod.GET })
    public ModelAndView modifyForm(@ModelAttribute("eventForm") final EventForm form, @PathVariable("eventId") final int eventId) {
        final ModelAndView mav = new ModelAndView("modifyEvent");
        final Event e = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        mav.addObject("locations", locationService.getAll());
        mav.addObject("currentDate", LocalDateTime.now().toString().substring(0,16));
        mav.addObject("allTags", tagService.getAll());
        mav.addObject("types", typeService.getAll());
        mav.addObject("event", e);
        return mav;
    }

    @RequestMapping(value = "/events/{eventId}/modify", method = { RequestMethod.POST })
    public ModelAndView modifyEvent(@Valid @ModelAttribute("eventForm") final EventForm form, final BindingResult errors, @PathVariable("eventId") final int eventId) {
        if (errors.hasErrors()) {
            return modifyForm(form, eventId);
        }
        eventService.updateEvent(eventId, form.getName(), form.getDescription(), form.getLocation(), form.getMaxCapacity(), form.getPrice(), form.getType(), form.getTimestamp(), 1, form.getTags());
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @RequestMapping(value = "/events/{eventId}/delete", method = { RequestMethod.POST })
    public ModelAndView deleteEvent(@PathVariable("eventId") final int eventId) {
        eventService.deleteEvent(eventId);
        return new ModelAndView("redirect:/events");
    }
}
