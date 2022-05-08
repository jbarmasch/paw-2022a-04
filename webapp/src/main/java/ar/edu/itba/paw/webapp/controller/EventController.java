package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.webapp.exceptions.EventNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.helper.FilterUtils;
import ar.edu.itba.paw.webapp.validations.IntegerArray;
import ar.edu.itba.paw.webapp.validations.NumberFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class EventController {
    @Autowired
    private EventService eventService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserManager userManager;

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
        mav.addObject("ticketsSize", event.getTickets().size());
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
        final User eventUser = userService.getUserById(e.getUser().getId()).orElseThrow(UserNotFoundException::new);

        int i = 0;
        List<Ticket> tickets = e.getTickets();
        for (Booking booking : form.getBookings()) {
            if (booking.getQty() != null && booking.getQty() > tickets.get(i).getTicketsLeft()) {
                errors.rejectValue("bookings[" + i + "].qty", "Max.bookForm.qty", new Object[]{tickets.get(i).getTicketsLeft()}, "");
            }
            i++;
        }
        if (errors.hasErrors()) {
            return eventDescription(form, eventId);
        }

        boolean booked = eventService.book(form.getBookings(), user.getId(), user.getUsername(), user.getMail(), eventId, e.getName(), eventUser.getMail());
        if (!booked)
            return new ModelAndView("redirect:/error");
        return new ModelAndView("redirect:/events/" + e.getId() + "/booking-success");
    }

    @RequestMapping(value = "/events/{eventId}/booking-success", method = RequestMethod.GET)
    public ModelAndView bookSuccess(@PathVariable("eventId") @Min(1) final int eventId) {
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

        userManager.refreshAuthorities();
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

        eventService.updateEvent(eventId, form.getName(), form.getDescription(), form.getLocation(), 0, 0,
                form.getType(), form.getTimestamp(), (imageFile == null || imageFile.isEmpty()) ? null : imageFile.getBytes(), form.getTags());
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
        EventStats stats = userService.getEventStats(userId).orElseThrow(RuntimeException::new);
        final ModelAndView mav = new ModelAndView("eventStats");
        mav.addObject("stats", stats);
        return mav;
    }

    private boolean isEventOwner(Event event) {
        return event.getUser().getId() == userManager.getUserId();
    }

    @RequestMapping(value = "/events/{eventId}/add-ticket", method = { RequestMethod.GET })
    public ModelAndView createTicketsForm(@ModelAttribute("ticketForm") TicketForm form, @PathVariable("eventId") @Min(1) final int eventId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        if (!isEventOwner(event))
            return new ModelAndView("redirect:/events/" + eventId);
        if (event.getTickets().size() >= 5)
            return new ModelAndView("redirect:/events/" + eventId);

        return new ModelAndView("ticket");
    }

    @RequestMapping(value = "/events/{eventId}/add-ticket", method = { RequestMethod.POST })
    public ModelAndView createTicketsForm(@Valid @ModelAttribute("ticketForm") TicketForm form, final BindingResult errors,
                                          @PathVariable("eventId") @Min(1) final int eventId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        if (!isEventOwner(event))
            return new ModelAndView("redirect:/events/" + eventId);
        if (errors.hasErrors())
            return createTicketsForm(form, eventId);

        eventService.addTicket(event.getId(), form.getTicketName(), form.getPrice(), form.getQty());
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @RequestMapping(value = "/events/{eventId}/modify-ticket/{ticketId}", method = { RequestMethod.GET })
    public ModelAndView modifyTicketForm(@ModelAttribute("ticketForm") TicketForm form,
                                          @PathVariable("eventId") @Min(1) final int eventId,
                                          @PathVariable("ticketId") @Min(1) final int ticketId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        if (!isEventOwner(event))
            return new ModelAndView("redirect:/events/" + eventId);

        final Ticket ticket = eventService.getTicketById(ticketId).orElseThrow(EventNotFoundException::new);
        ModelAndView mav = new ModelAndView("modifyTicket");
        mav.addObject("ticket", ticket);
        return mav;
    }

    @RequestMapping(value = "/events/{eventId}/modify-ticket/{ticketId}", method = { RequestMethod.POST })
    public ModelAndView modifyTicket(@Valid @ModelAttribute("ticketForm") TicketForm form, final BindingResult errors,
                                      @PathVariable("eventId") @Min(1) final int eventId,
                                      @PathVariable("ticketId") @Min(1) final int ticketId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        if (!isEventOwner(event))
            return new ModelAndView("redirect:/events/" + eventId);
        if (errors.hasErrors())
            return modifyTicketForm(form, eventId, ticketId);

        eventService.updateTicket(event.getId(), form.getTicketName(), form.getPrice(), form.getQty());
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @RequestMapping(value = "/events/{eventId}/delete-ticket/{ticketId}", method = { RequestMethod.POST })
    public ModelAndView deleteTicket(@PathVariable("eventId") @Min(1) final int eventId, @PathVariable("ticketId") @Min(1) final int ticketId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        if (!isEventOwner(event))
            return new ModelAndView("redirect:/events/" + eventId);

        eventService.deleteTicket(ticketId);
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @ModelAttribute
    public void addAttributes(Model model, final SearchForm searchForm) {
        model.addAttribute("username", userManager.getUsername());
        model.addAttribute("isCreator", userManager.isCreator());
    }
}
