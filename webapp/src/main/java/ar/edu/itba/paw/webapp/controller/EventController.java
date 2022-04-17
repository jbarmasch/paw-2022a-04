package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.Type;
import ar.edu.itba.paw.service.ImageService;
import ar.edu.itba.paw.service.LocationService;
import ar.edu.itba.paw.service.TypeService;
import ar.edu.itba.paw.service.TagService;
import ar.edu.itba.paw.service.MailService;
import ar.edu.itba.paw.webapp.exceptions.EventNotFoundException;
import ar.edu.itba.paw.webapp.form.EventForm;
import ar.edu.itba.paw.webapp.form.BookForm;
import ar.edu.itba.paw.webapp.form.FilterForm;
import ar.edu.itba.paw.webapp.form.ImageForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

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

    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    @RequestMapping(value = "/events", method = { RequestMethod.GET })
    public ModelAndView browseEvents(@ModelAttribute("filterForm") final FilterForm form,
                                     @RequestParam(value = "locations", required = false) final Integer[] locations,
                                     @RequestParam(value = "types", required = false) final String[] types,
                                     @RequestParam(value = "minPrice", required = false) final Double minPrice,
                                     @RequestParam(value = "maxPrice", required = false) final Double maxPrice) {
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
        boolean hasFilter = false;
        String endURL = "";
        if (form.getLocations() != null) {
            hasFilter = true;
            endURL += "&locations=" + form.getLocations();
        }
        if (form.getTypes() != null) {
            hasFilter = true;
            endURL += "&types=" + form.getTypes();
        }
        if (form.getMinPrice() != null) {
            hasFilter = true;
            endURL += "&minPrice=" + form.getMinPrice();
        }
        if (form.getMaxPrice() != null) {
            hasFilter = true;
            endURL += "&maxPrice=" + form.getMaxPrice();
        }
        if (!hasFilter)
            return new ModelAndView("redirect:/events");
        return new ModelAndView("redirect:/events?" + endURL);
    }

    @RequestMapping(value = "/events/{eventId}", method = RequestMethod.GET)
    public ModelAndView eventDescription(@ModelAttribute("bookForm") final BookForm form, @PathVariable("eventId") final int eventId) {
        final ModelAndView mav = new ModelAndView("event");
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        mav.addObject("event", event);
        mav.addObject("location", locationService.getLocationById(event.getLocation()).orElseThrow(EventNotFoundException::new));
        mav.addObject("type", typeService.getTypeById(event.getType()).orElseThrow(EventNotFoundException::new));
//        final Image image = imageService.getImageById(event.getImg()).orElseThrow(EventNotFoundException::new);
        final Image image = imageService.getImageById(1).orElseThrow(EventNotFoundException::new);
        mav.addObject("image", imageService.getFormattedImage(image.getImage()));
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

    @RequestMapping(value = "/createEvent", method = { RequestMethod.POST })
    public ModelAndView createEvent(@Valid @ModelAttribute("eventForm") final EventForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return createForm(form);
        }
        final Event e = eventService.create(form.getName(), form.getDescription(), form.getLocation(), form.getMaxCapacity(), form.getPrice(), form.getType(), form.getTimestamp());
        return new ModelAndView("redirect:/events/" + e.getId());
    }

    @RequestMapping(value = "/events/{eventId}/modify", method = { RequestMethod.GET })
    public ModelAndView modifyForm(@ModelAttribute("eventForm") final EventForm form, @PathVariable("eventId") final int eventId) {
        final ModelAndView mav = new ModelAndView("modifyEvent");
        final Event e = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        mav.addObject("locations", locationService.getAll());
        mav.addObject("currentDate", LocalDateTime.now().toString().substring(0,16));
        mav.addObject("types", typeService.getAll());
//        mav.addObject("types", typeService.getAll().stream().map(Type::getName).toArray());
        mav.addObject("event", e);
        mav.addObject("location", locationService.getLocationById(e.getLocation()).orElseThrow(EventNotFoundException::new).getName());
        mav.addObject("type", typeService.getTypeById(e.getType()).orElseThrow(EventNotFoundException::new).getName());
        mav.addObject("date", e.getDate().toLocalDateTime());
        return mav;
    }

    @RequestMapping(value = "/events/{eventId}/modify", method = { RequestMethod.POST })
    public ModelAndView modifyEvent(@Valid @ModelAttribute("eventForm") final EventForm form, final BindingResult errors, @PathVariable("eventId") final int eventId) {
        if (errors.hasErrors()) {
            return modifyForm(form, eventId);
        }
        eventService.updateEvent(eventId, form.getName(), form.getDescription(), form.getLocation(), form.getMaxCapacity(), form.getPrice(), form.getType(), form.getTimestamp());
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @RequestMapping(value = "/events/{eventId}/delete", method = { RequestMethod.POST })
    public ModelAndView deleteEvent(@PathVariable("eventId") final int eventId) {
        eventService.deleteEvent(eventId);
        return new ModelAndView("redirect:/events");
    }

    @RequestMapping(value = "/addImage", method = { RequestMethod.GET })
    public ModelAndView getImageForm(@ModelAttribute("imageForm") final ImageForm form) {
        return new ModelAndView("image");
    }

    @RequestMapping(value = "/addImage", method = { RequestMethod.POST }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ModelAndView addImage(@RequestParam("image") CommonsMultipartFile imageFile) {
        imageService.addEventImage(imageFile.getBytes());
        return new ModelAndView("redirect:/events/1");
    }
}
