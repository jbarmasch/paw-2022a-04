package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Booking;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Stats;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> getUserById(long id);
    User create(String username, String password, String mail);
    Optional<User> findByUsername(String username);
    List<Booking> getAllBookingsFromUser(long id, int page);
    Optional<Booking> getBookingFromUser(long userId, long eventId);
    boolean cancelBooking(long userId, long eventId, int qty);
    Optional<Stats> getUserStats(long id);
}
