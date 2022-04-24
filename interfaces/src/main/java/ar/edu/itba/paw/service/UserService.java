package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Booking;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(long id);
    User create(String username, String password, String mail);
    Optional<User> findByUsername(String username);
    List<Booking> getAllBookingsFromUser(long id);
    Optional<Booking> getBookingFromUser(long userId, long eventId);
    boolean cancelBooking(long userId, long eventId, int qty);
}
