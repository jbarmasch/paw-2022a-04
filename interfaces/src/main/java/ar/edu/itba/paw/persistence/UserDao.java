package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface UserDao {
    Optional<User> getUserById(long id);
    User create(String username, String password, String mail);
    Optional<User> findByUsername(String username);
    Optional<EventBooking> getBookingFromUser(long userId, long eventId, Locale locale);
    List<EventBooking> getAllBookingsFromUser(long userId, int page, Locale locale);
    Optional<EventStats> getEventStats(long id, Locale locale);
    Optional<UserStats> getUserStats(long id, Locale locale);
    Optional<User> findByMail(String mail);
    void rateUser(long userId, long organizerId, int rating);
    boolean canRate(long organizerId, long userId);
    Optional<EventBooking> getBooking(String code, Locale locale);
}
