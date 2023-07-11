package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;

import java.util.Locale;
import java.util.Optional;

public interface UserDao {
    UserList getAllUsers(int page);

    Optional<User> getUserById(long id);

    User createUser(String username, String password, String mail, Locale locale);

    User createBouncer(String password);

    void updateUser(long userId, String username, String password, String mail);

    Optional<User> findByUsername(String username);

    Optional<OrganizerStats> getOrganizerStats(long id);

    Optional<UserStats> getUserStats(long id);

    Optional<User> findByMail(String mail);

    void rateUser(long userId, long organizerId, int rating);

    void makeCreator(User user);

    void makeBouncer(User user);

    boolean canRate(long organizerId, long userId);

    UserList filterByOrganizers(String searchQuery, Order order, int page);

    boolean checkEventBouncer(long userId, long eventId);
}
