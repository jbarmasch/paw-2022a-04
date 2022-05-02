package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Booking;
import ar.edu.itba.paw.model.Stats;
import ar.edu.itba.paw.model.User;
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
    public List<Booking> getAllBookingsFromUser(long id, int page) {
        return userDao.getAllBookingsFromUser(id, page);
    }

    @Override
    public Optional<Booking> getBookingFromUser(long userId, long eventId) {
        return userDao.getBookingFromUser(userId, eventId);
    }

    @Override
    public boolean cancelBooking(int qty, long userId, String username, String userMail, long eventId, String eventName, String eventMail) {
        if (userDao.cancelBooking(userId, eventId, qty)) {
            mailService.sendCancelMail(userMail, username, eventMail, eventName, qty);
            return true;
        }
        mailService.sendErrorMail(userMail, eventName);
        return false;
    }

    @Override
    public Optional<Stats> getUserStats(long id) {
        return userDao.getUserStats(id);
    }
}
