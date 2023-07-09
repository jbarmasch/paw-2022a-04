package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Ticket;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface AuthService {
    boolean isAuthenticated();

    User getUser();

    boolean isBouncer();

    boolean isTheLoggedUser(long userId);

    boolean isTheLoggedUser(String username);

    boolean isTheEventBouncer(long eventId);

    boolean isTheEventBouncer(User bouncer);

    boolean isTheEventOrganizer(long eventId);

    boolean isTheEventOrganizer(Event event);

    boolean isTheBookingOwner(User user);

    boolean isTheEventOrganizerFromTicket(long ticketId);
}
