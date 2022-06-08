package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Ticket;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.auth.UserManager;
import ar.edu.itba.paw.exceptions.DateRangeException;
import ar.edu.itba.paw.webapp.exceptions.EventNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.TicketNotFoundException;
import ar.edu.itba.paw.exceptions.TicketUnderflowException;
import ar.edu.itba.paw.webapp.form.TicketForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Controller
public class TicketController {
    @Autowired
    private EventService eventService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserManager userManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketController.class);

    @RequestMapping(value = "/events/{eventId}/add-ticket", method = { RequestMethod.GET })
    public ModelAndView createTicketsForm(@ModelAttribute("ticketForm") TicketForm form, @PathVariable("eventId") @Min(1) final long eventId) {
        final Event event = eventService.getEventById(eventId).orElse(null);
        if (event == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }
        if (!userManager.isEventOwner(event)) {
            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
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
        if (!userManager.isEventOwner(event)) {
            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
            return new ModelAndView("redirect:/events/" + eventId);
        }

        if (errors.hasErrors()) {
            LOGGER.error("TicketForm has errors: {}", errors.getAllErrors().toArray());
            return createTicketsForm(form, eventId);
        }

        try {
            ticketService.addTicket(event, form.getTicketName(), form.getPrice(), form.getQty(),
                    form.getLocalDate(form.getStarting()), form.getLocalDate(form.getUntil()));
        } catch (DateRangeException e) {
            if (e.getStarting() != null) {
                errors.rejectValue("starting", "Min.bookForm.qtyAnother", null, "");
            } else {
                errors.rejectValue("until", "Min.bookForm.qtyAnother", null, "");
            }
            LOGGER.error("TicketForm has errors: {}", errors.getAllErrors().toArray());
            return createTicketsForm(form, eventId);
        }
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
        if (!userManager.isEventOwner(event)) {
            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
            return new ModelAndView("redirect:/events/" + eventId);
        }
        final Ticket ticket = ticketService.getTicketById(ticketId).orElse(null);
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
        if (!userManager.isEventOwner(event)) {
            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
            return new ModelAndView("redirect:/events/" + eventId);
        }
        final Ticket ticket = ticketService.getTicketById(ticketId).orElse(null);
        if (ticket == null) {
            LOGGER.error("Ticket not found");
            throw new TicketNotFoundException();
        }
        if (errors.hasErrors()) {
            LOGGER.error("TicketForm has errors: {}", errors.getAllErrors().toArray());
            return modifyTicketForm(form, eventId, ticketId);
        }
        try {
            ticketService.updateTicket(ticket, form.getTicketName(), form.getPrice(), form.getQty(), form.getLocalDate(form.getStarting()), form.getLocalDate(form.getUntil()));
        } catch (TicketUnderflowException e) {
            errors.rejectValue("qty", "Min.bookForm.qtyAnother", new Object[]{ticket.getBooked()}, "");
            LOGGER.error("TicketForm has errors: {}", errors.getAllErrors().toArray());
            return modifyTicketForm(form, eventId, ticketId);
        }
        return new ModelAndView("redirect:/events/" + eventId);
    }

    @RequestMapping(value = "/events/{eventId}/delete-ticket/{ticketId}", method = { RequestMethod.POST })
    public ModelAndView deleteTicket(@PathVariable("eventId") @Min(1) final long eventId, @PathVariable("ticketId") @Min(1) final long ticketId) {
        final Event event = eventService.getEventById(eventId).orElse(null);
        if (event == null) {
            LOGGER.error("Event not found");
            throw new EventNotFoundException();
        }
        if (!userManager.isEventOwner(event)) {
            LOGGER.debug("Logged user is not the owner of event {}", event.getName());
            return new ModelAndView("redirect:/events/" + eventId);
        }

        ticketService.deleteTicket(ticketId);
        return new ModelAndView("redirect:/events/" + eventId);
    }
}
