package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Autowired
    public UserServiceImpl(final UserDao userDao, final PasswordEncoder passwordEncoder, final MailService mailService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Override
    public Optional<User> getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    public User create(String username, String password, String mail) {
        return userDao.create(username, passwordEncoder.encode(password), mail);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }


    @Override
    public Optional<EventBooking> getBookingFromUser(long userId, long eventId, Locale locale) {
        return userDao.getBookingFromUser(userId, eventId, locale);
    }

    @Override
    public Optional<UserStats> getUserStats(long id, Locale locale) {
        return userDao.getUserStats(id, locale);
    }

    @Override
    public Optional<EventStats> getEventStats(long id, Locale locale) {
        return userDao.getEventStats(id, locale);
    }

    @Override
    public boolean isUsernameUnique(String username) {
        return !userDao.findByUsername(username).isPresent();
    }

    @Override
    public boolean isMailUnique(String mail) {
        return !userDao.findByMail(mail).isPresent();
    }

    @Override
    public void rateUser(long userId, long organizerId, double rating) {
        if (!userDao.canRate(organizerId, userId))
            return;
        userDao.rateUser(userId, organizerId, rating);
    }

    @Override
    public List<EventBooking> getAllPreviousBookingsFromUser(long id, int page, Locale locale){
        return userDao.getAllPreviousBookingsFromUser(id, page, locale);
    }

    @Override
    public List<EventBooking> getAllFutureBookingsFromUser(long id, int page, Locale locale){
        return userDao.getAllFutureBookingsFromUser(id, page, locale);
    }
}
