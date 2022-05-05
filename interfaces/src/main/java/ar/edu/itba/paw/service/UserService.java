package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Booking;
import ar.edu.itba.paw.model.EventBooking;
import ar.edu.itba.paw.model.Stats;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(long id);
    User create(String username, String password, String mail);
    Optional<User> findByUsername(String username);
    List<EventBooking> getAllBookingsFromUser(long id, int page);
    Optional<EventBooking> getBookingFromUser(long userId, long eventId);
    Optional<Stats> getUserStats(long id);
    boolean isUsernameUnique(String username);
    boolean isMailUnique(String mail);
    void rateUser(long userId, long organizerId, double rating);
}
