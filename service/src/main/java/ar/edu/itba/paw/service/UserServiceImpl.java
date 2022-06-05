package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserDao userDao, final PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Transactional
    @Override
    public User create(String username, String password, String mail) {
        return userDao.create(username, passwordEncoder.encode(password), mail);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
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
    public void rateUser(long userId, long organizerId, int rating) {
        if (!userDao.canRate(organizerId, userId))
            return;
        userDao.rateUser(userId, organizerId, rating);
    }

    @Override
    public List<EventBooking> getAllBookingsFromUser(long userId, int page) {
        return userDao.getAllBookingsFromUser(userId, page);
    }

    @Override
    public Optional<EventBooking> getBookingFromUser(long userId, long eventId) {
        return userDao.getBookingFromUser(userId, eventId);
    }

    @Override
    public Optional<EventBooking> getBooking(String code){
        return userDao.getBooking(code);
    }

    @Transactional
    @Override
    public void confirmBooking(EventBooking eventBooking) {
        userDao.confirmBooking(eventBooking);
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
