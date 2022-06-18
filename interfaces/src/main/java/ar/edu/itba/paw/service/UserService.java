package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.UserCannotRateException;
import ar.edu.itba.paw.model.*;

import java.util.Locale;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(long id);
    User create(String username, String password, String mail, Locale locale);
    User createBouncer(String password);
    void updateUser(long userId, String username, String password, String mail);
    Optional<User> findByUsername(String username);
    Optional<OrganizerStats> getOrganizerStats(long id);
    Optional<UserStats> getUserStats(long id);
    boolean isUsernameUnique(String username);
    boolean isMailUnique(String mail);
    void rateUser(long userId, long organizerId, int rating) throws UserCannotRateException;
    void makeCreator(User user);
}
