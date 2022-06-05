package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.webapp.exceptions.*;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.helper.AESUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

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
    public ModelAndView eventDescription(@ModelAttribute("bookForm") final BookForm form, @PathVariable("eventId") @Min(1) final long eventId) {
        final Event event = eventService.getEventById(eventId).orElse(null);
        if (event == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }
        boolean isLogged = false, isOwner = false;
        if (userManager.isAuthenticated()) {
            isLogged = true;
            isOwner = isEventOwner(event);
        }

        final ModelAndView mav = new ModelAndView("event");
        mav.addObject("currentDate", LocalDateTime.now().toString().substring(0,16));
        mav.addObject("event", event);
        mav.addObject("ticketsSize", event.getTickets().size());
        mav.addObject("isOwner", isOwner);
        mav.addObject("isLogged", isLogged);
        addSimilarAndPopularEvents(mav, eventId);
        return mav;
    }

    private void addSimilarAndPopularEvents(ModelAndView mav, long eventId) {
        List<Event> similarEvents = eventService.getSimilarEvents(eventId);
        List<Event> popularEvents = eventService.getPopularEvents(eventId);
        mav.addObject("similarEvents", similarEvents);
        mav.addObject("similarEventsSize", similarEvents.size());
        mav.addObject("popularEvents", popularEvents);
        mav.addObject("popularEventsSize", popularEvents.size());
    }

    @RequestMapping(value = "/events/{eventId}", method = { RequestMethod.POST }, params = "submit")
    public ModelAndView bookEvent(HttpServletRequest request, @Valid@ModelAttribute("bookForm") final BookForm form, final BindingResult errors,
                                  @PathVariable("eventId") @Min(1) final long eventId) {
        final Event e = eventService.getEventById(eventId).orElse(null);
        if (e == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }

        final User user = userManager.getUser();
        final User eventUser = userService.getUserById(e.getOrganizer().getId()).orElse(null);
        if (eventUser == null) {
            LOGGER.error("Organizer not found");
            throw new UserNotFoundException();
        }

        int i = 0;
        List<Ticket> tickets = e.getTickets();
        Map<Long, Ticket> ticketMap = new HashMap<>();
        for (Ticket ticket : tickets) {
            ticketMap.put(ticket.getId(), ticket);
        }

        EventBooking booking = new EventBooking(user, e, new ArrayList<>(), null);
        for (BookingForm bookingForm : form.getBookings()) {
            TicketBooking ticketBooking = new TicketBooking(eventService.getTicketById(bookingForm.getTicketId()).orElse(null), bookingForm.getQty(), booking);
            booking.addBooking(ticketBooking);
        }

        for (TicketBooking ticketBooking : booking.getTicketBookings()) {
            Ticket ticket = ticketMap.get(ticketBooking.getTicket().getId());
            if (ticketBooking.getQty() != null && ticketBooking.getQty() > Math.min(6, ticket.getTicketsLeft()))
                errors.rejectValue("bookings[" + i + "].qty", "Max.bookForm.qty", new Object[]{Math.min(6, ticket.getTicketsLeft())}, "");
            i++;
        }

        if (errors.hasErrors()) {
            LOGGER.error("BookForm has errors: {}", errors.getAllErrors().toArray());
            return eventDescription(form, eventId);
        }

        eventService.book(booking, request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath()), LocaleContextHolder.getLocale());
        return new ModelAndView("redirect:/events/" + e.getId() + "/booking-success");
    }

    @RequestMapping(value = "/events/{eventId}/booking-success", method = RequestMethod.GET)
    public ModelAndView bookSuccess(@PathVariable("eventId") @Min(1) final long eventId) {
        if (!userManager.isAuthenticated())
            return new ModelAndView("redirect:/");

        EventBooking eventBooking = userService.getBookingFromUser(userManager.getUserId(), eventId).orElse(null);
        if (eventBooking == null) {
            LOGGER.error("Booking not found");
            throw new BookingNotFoundException();
        }

        final ModelAndView mav = new ModelAndView("bookingSuccess");
        mav.addObject("code", eventBooking.getCode());
        addSimilarAndPopularEvents(mav, eventId);
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
                                    @RequestParam("image") MultipartFile imageFile) throws IOException {
        if (errors.hasErrors()) {
            LOGGER.error("EventForm has errors: {}", errors.getAllErrors().toArray());
            return createForm(form);
        }

        final long userId = userManager.getUserId();
        final Event e = eventService.create(form.getName(), form.getDescription(), form.getLocation(), form.getType(), form.getTimestamp(),
                (imageFile == null || imageFile.isEmpty()) ? null : imageFile.getBytes(), form.getTags(), userId, form.isHasMinAge() ? form.getMinAge() : null);

        userManager.refreshAuthorities();
        return new ModelAndView("redirect:/events/" + e.getId());
    }

    @RequestMapping(value = "/events/{eventId}/modify", method = { RequestMethod.GET })
    public ModelAndView modifyForm(@ModelAttribute("eventForm") final EventForm form, @PathVariable("eventId") @Min(1) final long eventId) {
        final Event event = eventService.getEventById(eventId).orElse(null);
        if (event == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }

        if (!isEventOwner(event)) {
            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
            return new ModelAndView("redirect:/events/" + eventId);
        }

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
                                    @PathVariable("eventId") @Min(1) final long eventId,
                                    @RequestParam("image") CommonsMultipartFile imageFile) {
        final Event event = eventService.getEventById(eventId).orElse(null);
        if (event == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }
        if (!isEventOwner(event))
            return new ModelAndView("redirect:/events/" + eventId);
        if (errors.hasErrors())
            return modifyForm(form, eventId);

        eventService.updateEvent(eventId, form.getName(), form.getDescription(), form.getLocation(),
                form.getType(), form.getTimestamp(), (imageFile == null || imageFile.isEmpty()) ? null : imageFile.getBytes(), form.getTags(), form.isHasMinAge() ? form.getMinAge() : null);
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @RequestMapping(value = "/events/{eventId}/delete", method = { RequestMethod.POST })
    public ModelAndView deleteEvent(@PathVariable("eventId") final long eventId) {
        final Event event = eventService.getEventById(eventId).orElse(null);
        if (event == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }
        if (!isEventOwner(event)) {
            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
            return new ModelAndView("redirect:/events/" + eventId);
        }

        eventService.deleteEvent(eventId);
        return new ModelAndView("redirect:/events");
    }

    @RequestMapping(value = "/events/{eventId}/soldout", method = { RequestMethod.POST })
    public ModelAndView soldOutEvent(@PathVariable("eventId") @Min(1) final long eventId) {
        final Event event = eventService.getEventById(eventId).orElse(null);
        if (event == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }
        if (!isEventOwner(event)) {
            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
            return new ModelAndView("redirect:/events/" + eventId);
        }

        eventService.soldOut(eventId);
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @RequestMapping(value = "/events/{eventId}/active", method = { RequestMethod.POST })
    public ModelAndView activeEvent(@PathVariable("eventId") @Min(1) final long eventId) {
        final Event event = eventService.getEventById(eventId).orElse(null);
        if (event == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }
        if (!isEventOwner(event)) {
            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
            return new ModelAndView("redirect:/events/" + eventId);
        }

        eventService.active(eventId);
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @RequestMapping(value = "/events/{eventId}/add-ticket", method = { RequestMethod.GET })
    public ModelAndView createTicketsForm(@ModelAttribute("ticketForm") TicketForm form, @PathVariable("eventId") @Min(1) final long eventId) {
        final Event event = eventService.getEventById(eventId).orElse(null);
        if (event == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }
        if (!isEventOwner(event)) {
            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
            return new ModelAndView("redirect:/events/" + eventId);
        }
        if (event.getTickets().size() >= 5) {
            LOGGER.debug("Event already has maximum amount of tickets");
            return new ModelAndView("redirect:/events/" + eventId);
        }

        ModelAndView mav = new ModelAndView("ticket");
        mav.addObject("event", event);
        mav.addObject("currentDate", LocalDateTime.now().toString().substring(0,16));
        return mav;
    }

    @RequestMapping(value = "/events/{eventId}/add-ticket", method = { RequestMethod.POST })
    public ModelAndView createTicketsForm(@Valid @ModelAttribute("ticketForm") TicketForm form, final BindingResult errors,
                                          @PathVariable("eventId") @Min(1) final long eventId) {
        final Event event = eventService.getEventById(eventId).orElse(null);
        if (event == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }
        if (!isEventOwner(event)) {
            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
            return new ModelAndView("redirect:/events/" + eventId);
        }
        if (form.getLocalDate(form.getStarting()) != null && form.getLocalDate(form.getStarting()).isAfter(event.getDate()))
            errors.rejectValue("starting", "Min.bookForm.qtyAnother", null, "");
        if (form.getLocalDate(form.getUntil()) != null && form.getLocalDate(form.getUntil()).isAfter(event.getDate()))
            errors.rejectValue("until", "Min.bookForm.qtyAnother", null, "");
        if (form.getLocalDate(form.getStarting()) != null && form.getLocalDate(form.getUntil()) != null &&
                form.getLocalDate(form.getStarting()).isAfter(form.getLocalDate(form.getUntil())))
            errors.rejectValue("until", "Min.bookForm.qtyAnother", null, "");

        if (errors.hasErrors()) {
            LOGGER.error("TicketForm has errors: {}", errors.getAllErrors().toArray());
            return createTicketsForm(form, eventId);
        }

        eventService.addTicket(event.getId(), form.getTicketName(), form.getPrice(), form.getQty(),
                form.getLocalDate(form.getStarting()), form.getLocalDate(form.getUntil()));
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @RequestMapping(value = "/events/{eventId}/modify-ticket/{ticketId}", method = { RequestMethod.GET })
    public ModelAndView modifyTicketForm(@ModelAttribute("ticketForm") TicketForm form,
                                          @PathVariable("eventId") @Min(1) final long eventId,
                                          @PathVariable("ticketId") @Min(1) final long ticketId) {
        final Event event = eventService.getEventById(eventId).orElse(null);
        if (event == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }
        if (!isEventOwner(event)) {
            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
            return new ModelAndView("redirect:/events/" + eventId);
        }

        final Ticket ticket = eventService.getTicketById(ticketId).orElse(null);
        if (ticket == null) {
            LOGGER.error("Ticket not found");
            throw new TicketNotFoundException();
        }

        ModelAndView mav = new ModelAndView("modifyTicket");
        mav.addObject("ticket", ticket);
        mav.addObject("event", event);
        mav.addObject("currentDate", LocalDateTime.now().toString().substring(0,16));
        return mav;
    }

    @RequestMapping(value = "/events/{eventId}/modify-ticket/{ticketId}", method = { RequestMethod.POST })
    public ModelAndView modifyTicket(@Valid @ModelAttribute("ticketForm") TicketForm form, final BindingResult errors,
                                      @PathVariable("eventId") @Min(1) final long eventId,
                                      @PathVariable("ticketId") @Min(1) final long ticketId) {
        final Event event = eventService.getEventById(eventId).orElse(null);
        if (event == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }

        if (!isEventOwner(event)) {
            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
            return new ModelAndView("redirect:/events/" + eventId);
        }
        final Ticket ticket = eventService.getTicketById(ticketId).orElse(null);
        if (ticket == null) {
            LOGGER.error("Ticket not found");
            throw new TicketNotFoundException();
        }
        if (form.getQty() < ticket.getBooked())
            errors.rejectValue("qty", "Min.bookForm.qtyAnother", new Object[]{ticket.getBooked()}, "");
        if (errors.hasErrors()) {
            LOGGER.error("TicketForm has errors: {}", errors.getAllErrors().toArray());
            return modifyTicketForm(form, eventId, ticketId);
        }

        eventService.updateTicket(ticketId, form.getTicketName(), form.getPrice(), form.getQty(), form.getLocalDate(form.getStarting()), form.getLocalDate(form.getUntil()));
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @RequestMapping(value = "/events/{eventId}/delete-ticket/{ticketId}", method = { RequestMethod.POST })
    public ModelAndView deleteTicket(@PathVariable("eventId") @Min(1) final long eventId, @PathVariable("ticketId") @Min(1) final long ticketId) {
        final Event event = eventService.getEventById(eventId).orElse(null);
        if (event == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }

        if (!isEventOwner(event)) {
            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
            return new ModelAndView("redirect:/events/" + eventId);
        }

        eventService.deleteTicket(ticketId, LocaleContextHolder.getLocale());
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @RequestMapping(value = "/my-events", method = { RequestMethod.GET })
    public ModelAndView myEvents(@RequestParam(value = "page", required = false, defaultValue = "1") @Min(1) final int page) {
        final User user = userManager.getUser();
        List<Event> events = user.getEvents();
        final ModelAndView mav = new ModelAndView("myEvents");
        mav.addObject("page", page);
        mav.addObject("myEvents", events);
        mav.addObject("size", events.size());
        return mav;
    }

    @RequestMapping(value = "/stats", method = { RequestMethod.GET })
    public ModelAndView getStats() {
        final long userId = userManager.getUserId();
        EventStats stats = userService.getEventStats(userId).orElse(null);
        if (stats == null) {
            LOGGER.error("Stats not found");
            throw new StatsNotFoundException();
        }
        final ModelAndView mav = new ModelAndView("eventStats");
        mav.addObject("stats", stats);
        return mav;
    }

    private boolean isEventOwner(Event event) {
        return event.getOrganizer().getId() == userManager.getUserId();
    }

    @ModelAttribute
    public void addAttributes(Model model, final SearchForm searchForm) {
        Locale locale = LocaleContextHolder.getLocale();
        Tag.setLocale(locale);
        Type.setLocale(locale);
        model.addAttribute("username", userManager.getUsername());
        model.addAttribute("isCreator", userManager.isCreator());
        model.addAttribute("isBouncer", userManager.isBouncer());
    }
}
