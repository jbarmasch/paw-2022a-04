package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.EventBooking;
import ar.edu.itba.paw.model.EventStats;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserStats;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(long id);
    User create(String username, String password, String mail);
    Optional<User> findByUsername(String username);
    Optional<EventBooking> getBookingFromUser(long userId, long eventId, Locale locale);
    Optional<EventStats> getEventStats(long id, Locale locale);
    Optional<UserStats> getUserStats(long id, Locale locale);
    boolean isUsernameUnique(String username);
    boolean isMailUnique(String mail);
    void rateUser(long userId, long organizerId, double rating);
    List<EventBooking> getAllPreviousBookingsFromUser(long id, int page, Locale locale);
    List<EventBooking> getAllFutureBookingsFromUser(long id, int page, Locale locale);
}
