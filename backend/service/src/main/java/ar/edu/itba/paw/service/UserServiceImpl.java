package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.UserCannotRateException;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    public UserList getAllUsers(int page) {
        return userDao.getAllUsers(page);
    }

    @Transactional
    @Override
    public User create(String username, String password, String mail, Locale locale) {
        return userDao.createUser(username, passwordEncoder.encode(password), mail, locale);
    }

    @Transactional
    @Override
    public User createBouncer(String password) {
        return userDao.createBouncer(passwordEncoder.encode(password));
    }

    @Override
    public void updateUser(long userId, String username, String password, String mail) {
        userDao.updateUser(userId, username, password == null ? null : passwordEncoder.encode(password), mail);
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
    public Optional<OrganizerStats> getOrganizerStats(long id) {
        return userDao.getOrganizerStats(id);
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
            throw new UserCannotRateException();
        userDao.rateUser(userId, organizerId, rating);
    }

    @Override
    public void makeCreator(User user) {
        userDao.makeCreator(user);
    }

    @Override
    public UserList filterBy(String searchQuery, Order order, int page) {
        return userDao.filterBy(searchQuery, order, page);
    }
}
