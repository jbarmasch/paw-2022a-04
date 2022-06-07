package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface UserDao {
    Optional<User> getUserById(long id);
    User createUser(String username, String password, String mail);
    User createBouncer(String password);
    void updateUser(long userId, String username, String password, String mail);
    Optional<User> findByUsername(String username);
    Optional<EventStats> getEventStats(long id);
    Optional<UserStats> getUserStats(long id);
    Optional<User> findByMail(String mail);
    void rateUser(long userId, long organizerId, int rating);
    void makeCreator(User user);
    void makeBouncer(User user);
    boolean canRate(long organizerId, long userId);
}
