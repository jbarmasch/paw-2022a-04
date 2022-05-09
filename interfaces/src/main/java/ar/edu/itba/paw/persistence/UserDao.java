package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.EventBooking;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.EventStats;
import ar.edu.itba.paw.model.UserStats;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> getUserById(long id);
    User create(String username, String password, String mail);
    Optional<User> findByUsername(String username);
    List<EventBooking> getAllBookingsFromUser(long id, int page);
    Optional<EventBooking> getBookingFromUser(long userId, long eventId);
    Optional<EventStats> getEventStats(long id);
    Optional<UserStats> getUserStats(long id);
    Optional<User> findByMail(String mail);
    void rateUser(long userId, long organizerId, double rating);
    boolean canRate(long organizerId, long userId);
}
