package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.webapp.exceptions.EventNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.helper.FilterUtils;
import ar.edu.itba.paw.webapp.validations.IntegerArray;
import ar.edu.itba.paw.webapp.validations.NumberFormat;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.*;

//@Validated
@Controller
public class EventController {
    private final EventService eventService;
    private final LocationService locationService;
    private final TypeService typeService;
    private final TagService tagService;
    private final UserService userService;
    private final PawUserDetailsService userDetailsService;
    private final UserManager userManager;

    @Autowired
    public EventController(final EventService eventService, final LocationService locationService, final TypeService typeService,
                           final TagService tagService, final UserService userService, final UserManager userManager, final PawUserDetailsService userDetailsService) {
        this.eventService = eventService;
        this.locationService = locationService;
        this.typeService = typeService;
        this.tagService = tagService;
        this.userService = userService;
        this.userManager = userManager;
        this.userDetailsService = userDetailsService;
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


    @RequestMapping(value = "/events", method = { RequestMethod.GET })
    public ModelAndView browseEvents(@ModelAttribute("filterForm") final FilterForm form, final BindingResult errors,
                                     @ModelAttribute("searchForm") final SearchForm searchForm,
                                     @RequestParam(value = "locations", required = false) @IntegerArray final String[] locations,
                                     @RequestParam(value = "types", required = false) @IntegerArray final String[] types,
                                     @RequestParam(value = "minPrice", required = false) @NumberFormat(decimal = true) final String minPrice,
                                     @RequestParam(value = "maxPrice", required = false) @NumberFormat(decimal = true) final String maxPrice,
                                     @RequestParam(value = "search", required = false) final String search,
                                     @RequestParam(value = "order", required = false) @Pattern(regexp = "price|date") final String order,
                                     @RequestParam(value = "orderBy", required = false) @Pattern(regexp = "ASC|DESC") final String orderBy,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) final int page) {
        List<Event> events = eventService.filterBy(locations, types, minPrice, maxPrice, search, order, orderBy, page);

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
        filters.put("search", form.getSearchQuery());
        filters.put("order", form.getOrder());
        filters.put("orderBy", form.getOrderBy());
        String endURL = FilterUtils.createFilter(filters);

        if (endURL.isEmpty())
            return new ModelAndView("redirect:/events");
        return new ModelAndView("redirect:/events?" + endURL);
    }

    @RequestMapping(value = "/search", method = { RequestMethod.POST })
    public ModelAndView search(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, final BindingResult errors) {
        if (errors.hasErrors())
            return new ModelAndView("error");

        Map<String, Object> filters = new HashMap<>();
        filters.put("search", searchForm.getQuery());
        String endURL = FilterUtils.createFilter(filters);

        if (endURL.isEmpty())
            return new ModelAndView("redirect:/events");
        return new ModelAndView("redirect:/events?" + endURL);
    }

    @RequestMapping(value = "/events/{eventId}", method = RequestMethod.GET)
    public ModelAndView eventDescription(@ModelAttribute("bookForm") final BookForm form, @PathVariable("eventId") @Min(1) final int eventId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        boolean isLogged = false, isOwner = false;
        if (userManager.isAuthenticated()) {
            isLogged = true;
            isOwner = isEventOwner(event);
        }

        final ModelAndView mav = new ModelAndView("event");
        mav.addObject("event", event);
        mav.addObject("isOwner", isOwner);
        mav.addObject("isLogged", isLogged);
        List<Event> similarEvents = eventService.getSimilarEvents(eventId);
        List<Event> popularEvents = eventService.getPopularEvents(eventId);
        mav.addObject("similarEvents", similarEvents);
        mav.addObject("similarEventsSize", similarEvents.size());
        mav.addObject("popularEvents", popularEvents);
        mav.addObject("popularEventsSize", popularEvents.size());
        return mav;
    }

    @RequestMapping(value = "/events/{eventId}", method = { RequestMethod.POST }, params = "submit")
    public ModelAndView bookEvent(@Valid @ModelAttribute("bookForm") final BookForm form, final BindingResult errors,
                                  @PathVariable("eventId") @Min(1) final int eventId) {
        final Event e = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        final User user = userManager.getUser();
        final User eventUser = userService.getUserById(e.getUser().getId()).orElseThrow(RuntimeException::new);

//        if (errors.hasErrors() || form.getQty() > e.getMaxCapacity()) {
//            errors.rejectValue("qty", "Max.bookForm.qty", new Object[] {e.getMaxCapacity()}, "");
        if (errors.hasErrors()) {
            return eventDescription(form, eventId);
        }

        boolean booked = eventService.book(form.getBookings(), user.getId(), user.getUsername(), user.getMail(), eventId, e.getName(), eventUser.getMail());
        if (!booked)
            return new ModelAndView("redirect:/error");
        return new ModelAndView("redirect:/events/" + e.getId() + "/bookingSuccess");
    }

    @RequestMapping(value = "/events/{eventId}/booking-success", method = RequestMethod.GET)
    public ModelAndView eventDescription(@PathVariable("eventId") @Min(1) final int eventId) {
        if (!userManager.isAuthenticated())
            return new ModelAndView("redirect:/");

        final ModelAndView mav = new ModelAndView("bookingSuccess");
        List<Event> similarEvents = eventService.getSimilarEvents(eventId);
        List<Event> popularEvents = eventService.getPopularEvents(eventId);
        mav.addObject("similarEvents", similarEvents);
        mav.addObject("similarEventsSize", similarEvents.size());
        mav.addObject("popularEvents", popularEvents);
        mav.addObject("popularEventsSize", popularEvents.size());
        return mav;
    }

    @RequestMapping(value = "/create-event", method = { RequestMethod.GET })
    public ModelAndView createForm(@ModelAttribute("eventForm") final EventForm form) {
        final ModelAndView mav = new ModelAndView("createEvent");
        mav.addObject("locations", locationService.getAll());
        mav.addObject("currentDate", LocalDateTime.now().toString().substring(0,16));
        mav.addObject("types", typeService.getAll());
        mav.addObject("allTags", tagService.getAll());
        return mav;
    }

    @RequestMapping(value = "/create-event", method = { RequestMethod.POST }, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ModelAndView createEvent(@Valid @ModelAttribute("eventForm") final EventForm form, final BindingResult errors,
                                    @RequestParam("image") CommonsMultipartFile imageFile) {
        if (errors.hasErrors())
            return createForm(form);

        final int userId = userManager.getUserId();
        final Event e = eventService.create(form.getName(), form.getDescription(), form.getLocation(), 0, 0,
                form.getType(), form.getTimestamp(), (imageFile == null || imageFile.isEmpty()) ? null : imageFile.getBytes(), form.getTags(), userId);

        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword());
        SecurityContextHolder.getContext().setAuthentication(token);

        return new ModelAndView("redirect:/events/" + e.getId());
    }

    @RequestMapping(value = "/events/{eventId}/modify", method = { RequestMethod.GET })
    public ModelAndView modifyForm(@ModelAttribute("eventForm") final EventForm form, @PathVariable("eventId") @Min(1) final int eventId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        if (!isEventOwner(event))
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
    public ModelAndView modifyEvent(@Valid @ModelAttribute("eventForm") final EventForm form, final BindingResult errors,
                                    @PathVariable("eventId") @Min(1) final int eventId,
                                    @RequestParam("image") CommonsMultipartFile imageFile) {
        if (errors.hasErrors())
            return modifyForm(form, eventId);

        eventService.updateEvent(eventId, form.getName(), form.getDescription(), form.getLocation(), 0, 0, form.getType(), form.getTimestamp(), (imageFile == null || imageFile.isEmpty()) ? null : imageFile.getBytes(), form.getTags());
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @RequestMapping(value = "/events/{eventId}/delete", method = { RequestMethod.POST })
    public ModelAndView deleteEvent(@PathVariable("eventId") final int eventId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        if (!isEventOwner(event))
            return new ModelAndView("redirect:/events/" + eventId);

        eventService.deleteEvent(eventId);
        return new ModelAndView("redirect:/events");
    }

    @RequestMapping(value = "/events/{eventId}/soldout", method = { RequestMethod.POST })
    public ModelAndView soldOutEvent(@PathVariable("eventId") @Min(1) final int eventId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        if (!isEventOwner(event))
            return new ModelAndView("redirect:/events/" + eventId);
        eventService.soldOut(eventId);
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @RequestMapping(value = "/events/{eventId}/active", method = { RequestMethod.POST })
    public ModelAndView activeEvent(@PathVariable("eventId") @Min(1) final int eventId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        if (!isEventOwner(event))
            return new ModelAndView("redirect:/events/" + eventId);

        eventService.active(eventId);
        return new ModelAndView("redirect:/events");
    }

    @RequestMapping(value = "/my-events", method = { RequestMethod.GET })
    public ModelAndView myEvents(@RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) final int page) {
        final int userId = userManager.getUserId();
        List<Event> events = eventService.getUserEvents(userId, page);
        final ModelAndView mav = new ModelAndView("myEvents");
        mav.addObject("page", page);
        mav.addObject("myEvents", events);
        mav.addObject("size", events.size());
        return mav;
    }

    @RequestMapping(value = "/stats", method = { RequestMethod.GET })
    public ModelAndView getStats() {
        final int userId = userManager.getUserId();
        Stats stats = userService.getUserStats(userId).orElseThrow(RuntimeException::new);
        final ModelAndView mav = new ModelAndView("stats");
        mav.addObject("stats", stats);
        return mav;
    }

    private boolean isEventOwner(Event event) {
        return event.getUser().getId() == userManager.getUserId();
    }

    @RequestMapping(value = "/events/{eventId}/add-tickets", method = { RequestMethod.GET })
    public ModelAndView createTicketsForm(@ModelAttribute("ticketsForm") TicketsForm ticketsForm, @PathVariable("eventId") @Min(1) final int eventId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        if (!isEventOwner(event))
            return new ModelAndView("redirect:/events/" + eventId);

        return new ModelAndView("tickets");
    }

    @RequestMapping(value = "/events/{eventId}/add-tickets", method = { RequestMethod.POST })
    public ModelAndView createTicketsForm(@Valid @ModelAttribute("ticketsForm") TicketsForm ticketsForm, final BindingResult errors,
                                          @PathVariable("eventId") @Min(1) final int eventId) {
        if (errors.hasErrors())
            return createTicketsForm(ticketsForm, eventId);

        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        Iterator<Ticket> it = ticketsForm.getTickets().iterator();
        while (it.hasNext()) {
            Ticket tick = it.next();
            if (tick == null)
                it.remove();
            else
                eventService.addTicket(event.getId(), tick);
        }

        return new ModelAndView("redirect:/events/" + eventId);
    }

    @ModelAttribute
    public void addAttributes(Model model, final SearchForm searchForm) {
        model.addAttribute("username", userManager.getUsername());
    }
}
