package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.EventNotFoundException;
import ar.edu.itba.paw.exceptions.TicketNotFoundException;
import ar.edu.itba.paw.exceptions.UserNotFoundException;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Ticket;
import ar.edu.itba.paw.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private TicketService ticketService;

    @Override
    public boolean isAuthenticated() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && !(auth instanceof AnonymousAuthenticationToken);
    }

    @Override
    public User getUser() {
        final String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        return userService.findByUsername(username).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public boolean isBouncer() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null
                && !(auth instanceof AnonymousAuthenticationToken)
                && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_BOUNCER"));
    }

    @Override
    public boolean isTheLoggedUser(long userId) {
        return getUser().getId() == userId;
    }

    @Override
    public boolean isTheLoggedUser(String username) {
        final String savedUsername = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        return Objects.equals(savedUsername, username);
    }

    @Override
    public boolean isTheEventBouncer(long eventId) {
        return userService.checkEventBouncer(getUser().getId(), eventId);
    }

    @Override
    public boolean isTheEventBouncer(User bouncer) {
        return getUser().getId() == bouncer.getId();
    }

    @Override
    public boolean isTheEventOrganizer(long eventId) {
        final Event event = eventService.getEventById(eventId).orElseThrow(EventNotFoundException::new);
        return getUser().getId() == event.getOrganizer().getId();
    }

    @Override
    public boolean isTheEventOrganizer(Event event) {
        return getUser().getId() == event.getOrganizer().getId();
    }


    @Override
    public boolean isTheBookingOwner(User user) {
        return getUser().getId() == user.getId();
    }

    @Override
    public boolean isTheEventOrganizerFromTicket(long ticketId) {
        Ticket ticket = ticketService.getTicketById(ticketId).orElseThrow(TicketNotFoundException::new);
        return getUser().getId() == ticket.getEvent().getOrganizer().getId();
    }
}
