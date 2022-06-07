package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.EventBooking;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface EventBookingService {
    List<EventBooking> getAllBookingsFromUser(long userId, int page);
    Optional<EventBooking> getBookingFromUser(long userId, long eventId);
    Optional<EventBooking> getBooking(String code);
    void book(EventBooking booking, String baseUrl,Locale locale);
    void cancelBooking(EventBooking booking, Locale locale);
    void confirmBooking(EventBooking eventBooking);
}
