package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(long id);
    User create(String username, String password, String mail);
    Optional<User> findByUsername(String username);
    Optional<EventStats> getEventStats(long id);
    Optional<UserStats> getUserStats(long id);
    boolean isUsernameUnique(String username);
    boolean isMailUnique(String mail);
    void rateUser(long userId, long organizerId, int rating);
    List<EventBooking> getAllBookingsFromUser(long userId, int page);
    Optional<EventBooking> getBookingFromUser(long userId, long eventId);
    Optional<EventBooking> getBooking(String code);
    void confirmBooking(EventBooking eventBooking);
    String encodePassword(String password);
}
