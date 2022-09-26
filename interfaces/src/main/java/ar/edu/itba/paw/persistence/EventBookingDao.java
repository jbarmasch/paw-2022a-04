package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.EventBooking;

import java.util.List;
import java.util.Optional;

public interface EventBookingDao {
    List<EventBooking> getAllBookingsFromUser(long userId, int page);

    Optional<EventBooking> getBookingFromUser(long userId, long eventId);

    Optional<EventBooking> getBooking(String code);

    EventBooking book(EventBooking booking);

    boolean cancelBooking(EventBooking booking);

    void confirmBooking(EventBooking eventBooking);
}
