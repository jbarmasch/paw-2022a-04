package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(long id);
    User create(String username, String password, String mail);
    Optional<User> findByUsername(String username);
    Optional<EventStats> getEventStats(long id, Locale locale);
    Optional<UserStats> getUserStats(long id, Locale locale);
    boolean isUsernameUnique(String username);
    boolean isMailUnique(String mail);
    void rateUser(long userId, long organizerId, int rating);
    List<EventBooking> getAllBookingsFromUser(long userId, int page, Locale locale);
    Optional<EventBooking> getBookingFromUser(long userId, long eventId, Locale locale);
    Optional<EventBooking> getBooking(String code, Locale locale);
}
