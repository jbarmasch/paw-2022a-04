package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.AlreadyMaxTicketsException;
import ar.edu.itba.paw.exceptions.SurpassedMaxTicketsException;
import ar.edu.itba.paw.model.EventBooking;
import ar.edu.itba.paw.model.EventBookingList;
import ar.edu.itba.paw.model.TicketBooking;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface EventBookingService {
    //    EventBookingList getAllBookingsFromUser(long userId, Long eventId, int page);
    EventBookingList getAllBookingsFromUser(long userId, int page);

    List<TicketBooking> getTicketBookingsFromUser(long userId, long eventId);

    Optional<EventBooking> getBooking(String code);

    EventBooking book(EventBooking booking, String baseUrl, Locale locale) throws AlreadyMaxTicketsException, SurpassedMaxTicketsException;

    void cancelBooking(String code, Locale locale);

    void confirmBooking(EventBooking eventBooking);

    void invalidateBooking(EventBooking eventBooking);
}
