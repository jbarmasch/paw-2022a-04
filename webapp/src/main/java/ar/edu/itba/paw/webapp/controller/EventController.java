package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.exceptions.EventNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.EventForm;
import ar.edu.itba.paw.webapp.form.BookForm;
import ar.edu.itba.paw.webapp.form.FilterForm;
import ar.edu.itba.paw.webapp.helper.FilterUtils;
import ar.edu.itba.paw.webapp.validations.IntegerArray;
import ar.edu.itba.paw.webapp.validations.NumberFormat;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import ar.edu.itba.paw.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.*;

@Validated
@Controller
public class EventController {
    private final EventService eventService;
    private final LocationService locationService;
    private final TypeService typeService;
    private final TagService tagService;
    private final UserService userService;

    @Autowired
    public EventController(final EventService eventService, final LocationService locationService, final TypeService typeService, TagService tagService, UserService userService) {
        this.eventService = eventService;
        this.locationService = locationService;
        this.typeService = typeService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @RequestMapping(value = "/", method = { RequestMethod.GET })
    public ModelAndView home() {
        List<Event> fewTicketsEvents = eventService.getFewTicketsEvents();
        List<Event> upcomingEvents = eventService.getUpcomingEvents();

        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("fewTicketsEvents", fewTicketsEvents);
        mav.addObject("fewTicketsSize", fewTicketsEvents.size());
        mav.addObject("upcomingEvents", upcomingEvents);
        mav.addObject("upcomingSize", upcomingEvents.size());
        return mav;
    }

    @RequestMapping(value = "/profile", method = { RequestMethod.GET })
    public ModelAndView getUser() {
        return new ModelAndView("redirect:/profile/" + getUserId());
    }

    @RequestMapping(value = "/events", method = { RequestMethod.GET })
    public ModelAndView browseEvents(@ModelAttribute("filterForm") final FilterForm form, final BindingResult errors,
                                     @RequestParam(value = "locations", required = false) @IntegerArray final String[] locations,
                                     @RequestParam(value = "types", required = false) @IntegerArray final String[] types,
                                     @RequestParam(value = "minPrice", required = false) @NumberFormat(decimal = true) final String minPrice,
                                     @RequestParam(value = "maxPrice", required = false) @NumberFormat(decimal = true) final String maxPrice,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) final int page) {
        List<Event> events = eventService.filterBy(locations, types, minPrice, maxPrice, page);

        final ModelAndView mav = new ModelAndView("events");
        mav.addObject("page", page);
        mav.addObject("allLocations", locationService.getAll());
        mav.addObject("allTypes", typeService.getAll());
        mav.addObject("events", events);
        mav.addObject("size", events.size());
        return mav;
    }

    @RequestMapping(value = "/events", method = { RequestMethod.POST })
    public ModelAndView browseByFilters(@Valid @ModelAttribute("filterForm") final FilterForm form, final BindingResult errors) {
        if (errors.hasErrors())
            return new ModelAndView("error");

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

    private Integer getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User user = userService.findByUsername(username).orElseThrow(UserNotFoundException::new);
            return user.getId();
        }
        return null;
    }

    @RequestMapping(value = "/events/{eventId}", method = RequestMethod.GET)
    public ModelAndView eventDescription(@ModelAttribute("bookForm") final BookForm form, @PathVariable("eventId") @Min(1) final int eventId) {
        final Event e = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        boolean isLogged = false, isOwner = false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            isLogged = true;
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            User user = userService.findByUsername(username).orElseThrow(UserNotFoundException::new);
            if (e.getUserId() == user.getId())
                isOwner = true;
        }

        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        final ModelAndView mav = new ModelAndView("event");
        mav.addObject("event", event);
        mav.addObject("isOwner", isOwner);
        mav.addObject("isLogged", isLogged);
        return mav;
    }

    @RequestMapping(value = "/events/{eventId}", method = { RequestMethod.POST }, params = "submit")
    public ModelAndView bookEvent(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors, @PathVariable("eventId") @Min(1) final int eventId) {
        final Event e = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        final String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        final User user = userService.findByUsername(username).orElseThrow(UserNotFoundException::new);
        final User eventUser = userService.getUserById(e.getUserId()).orElseThrow(RuntimeException::new);

        if (errors.hasErrors() || form.getQty() > e.getMaxCapacity()) {
            errors.rejectValue("qty", "Max.bookForm.qty", new Object[] {e.getMaxCapacity()}, "");
            return eventDescription(form, eventId);
        }

        boolean booked = eventService.book(form.getQty(), user.getId(), username, user.getMail(), eventId, e.getName(), eventUser.getMail());
        if (!booked)
            return new ModelAndView("redirect:/error");
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
        if (errors.hasErrors())
            return createForm(form);

        final String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        final int userId = userService.findByUsername(username).orElseThrow(UserNotFoundException::new).getId();
        final Event e = eventService.create(form.getName(), form.getDescription(), form.getLocation(), form.getMaxCapacity(), form.getPrice(), form.getType(), form.getTimestamp(), (imageFile == null || imageFile.isEmpty()) ? null : imageFile.getBytes(), form.getTags(), userId);
        return new ModelAndView("redirect:/events/" + e.getId());
    }

    @RequestMapping(value = "/events/{eventId}/modify", method = { RequestMethod.GET })
    public ModelAndView modifyForm(@ModelAttribute("eventForm") final EventForm form, @PathVariable("eventId") @Min(1) final int eventId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        if (!canUpdateEvent(event))
            return new ModelAndView("redirect:/events/" + eventId);

        final ModelAndView mav = new ModelAndView("modifyEvent");
        mav.addObject("locations", locationService.getAll());
        mav.addObject("currentDate", LocalDateTime.now().toString().substring(0,16));
        mav.addObject("allTags", tagService.getAll());
        mav.addObject("types", typeService.getAll());
        mav.addObject("event", event);
        return mav;
    }

    @RequestMapping(value = "/events/{eventId}/modify", method = { RequestMethod.POST }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ModelAndView modifyEvent(@Valid @ModelAttribute("eventForm") final EventForm form, final BindingResult errors, @PathVariable("eventId") @Min(1) final int eventId, @RequestParam("image") CommonsMultipartFile imageFile) {
        if (errors.hasErrors())
            return modifyForm(form, eventId);

        eventService.updateEvent(eventId, form.getName(), form.getDescription(), form.getLocation(), form.getMaxCapacity(), form.getPrice(), form.getType(), form.getTimestamp(), (imageFile == null || imageFile.isEmpty()) ? null : imageFile.getBytes(), form.getTags());
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @RequestMapping(value = "/events/{eventId}/delete", method = { RequestMethod.POST })
    public ModelAndView deleteEvent(@PathVariable("eventId") final int eventId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        if (!canUpdateEvent(event))
            return new ModelAndView("redirect:/events/" + eventId);

        eventService.deleteEvent(eventId);
        return new ModelAndView("redirect:/events");
    }

    @RequestMapping(value = "/events/{eventId}/soldout", method = { RequestMethod.POST })
    public ModelAndView soldOutEvent(@PathVariable("eventId") @Min(1) final int eventId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        if (!canUpdateEvent(event))
            return new ModelAndView("redirect:/events/" + eventId);

        eventService.soldOut(eventId);
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @RequestMapping(value = "/events/{eventId}/active", method = { RequestMethod.POST })
    public ModelAndView activeEvent(@PathVariable("eventId") @Min(1) final int eventId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        if (!canUpdateEvent(event))
            return new ModelAndView("redirect:/events/" + eventId);

        eventService.active(eventId);
        return new ModelAndView("redirect:/events");
    }

    @RequestMapping(value = "/myEvents", method = { RequestMethod.GET })
    public ModelAndView myEvents(@RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) final int page) {
        final Integer userId = getUserId();
        if (userId == null)
            throw new UserNotFoundException();

        List<Event> events = eventService.getUserEvents(userId, page);
        final ModelAndView mav = new ModelAndView("myEvents");
        mav.addObject("page", page);
        mav.addObject("myEvents", events);
        mav.addObject("size", events.size());
        return mav;
    }

    private boolean canUpdateEvent(Event event) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        int userId = userService.findByUsername(username).orElseThrow(UserNotFoundException::new).getId();
        return event.getUserId() == userId;
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
