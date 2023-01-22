package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.EventBooking;
import ar.edu.itba.paw.model.EventBookingList;

import java.util.List;
import java.util.Optional;

public interface EventBookingDao {
    EventBookingList getAllBookingsFromUser(long userId, int page);

    Optional<EventBooking> getBookingFromUser(long userId, long eventId);

    Optional<EventBooking> getBooking(String code);

    EventBooking book(EventBooking booking);

    boolean cancelBooking(EventBooking booking);

    void confirmBooking(EventBooking eventBooking);
}
