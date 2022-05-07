package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.EventBooking;
import ar.edu.itba.paw.model.EventStats;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserStats;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<EventBooking> getAllBookingsFromUser(long id, int page) {
        return userDao.getAllBookingsFromUser(id, page);
    }

    @Override
    public Optional<EventBooking> getBookingFromUser(long userId, long eventId) {
        return userDao.getBookingFromUser(userId, eventId);
    }

    @Override
    public Optional<UserStats> getUserStats(long id) {
        return userDao.getUserStats(id);
    }

    @Override
    public Optional<EventStats> getEventStats(long id) {
        return userDao.getEventStats(id);
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
        userDao.rateUser(userId, organizerId, rating);
    }
}
