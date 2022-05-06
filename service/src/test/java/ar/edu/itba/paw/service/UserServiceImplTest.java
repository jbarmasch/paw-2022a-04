package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@Component
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String MAIL = "username@mail.com";
    private static final String USERNAME2 = "username2";
    private static final String MAIL2 = "username2@mail.com";

    @InjectMocks
    private UserServiceImpl userService;

//    @Mock
//    private PasswordEncoder passwordEncoder;
    @Mock
    private UserDao mockDao;

    @Test
    public void testCreate() {
//        Mockito.when(mockDao.create(Mockito.eq(USERNAME), Mockito.eq(PASSWORD), Mockito.eq(MAIL))).thenReturn(new User(1, USERNAME, PASSWORD, MAIL));
//
//        Optional<User> maybeUser = Optional.ofNullable(userService.create(USERNAME, PASSWORD, MAIL));
//
//        Assert.assertNotNull(maybeUser);
//        Assert.assertTrue(maybeUser.isPresent());
//        Assert.assertEquals(USERNAME, maybeUser.get().getUsername());
//        Assert.assertEquals(PASSWORD, maybeUser.get().getPassword());
//        Assert.assertEquals(MAIL, maybeUser.get().getMail());
    }

//    @Test
//    public void testCreateEmptyPassword() {
//        Optional<User> maybeUser = Optional.ofNullable(userService.create(USERNAME, "", MAIL));
//        Assert.assertNotNull(maybeUser);
//        Assert.assertFalse(maybeUser.isPresent());
//    }
//
//    @Test
//    public void testCreateUsernameAlreadyExists() {
//        Mockito.when(mockDao.findByUsername(Mockito.eq(USERNAME))).thenReturn(Optional.of(new User(1, USERNAME, PASSWORD, MAIL)));
//        Optional<User> maybeUser = Optional.ofNullable(userService.create(USERNAME, PASSWORD, MAIL));
//        Assert.assertNotNull(maybeUser);
//        Assert.assertTrue(maybeUser.isPresent());
//    }
//
//    @Test
//    public void testCreateMailAlreadyExists() {
//        Mockito.when(mockDao.findByMail(Mockito.eq(MAIL))).thenReturn(Optional.of(new User(2, USERNAME2, PASSWORD, MAIL2)));
//        Optional<User> maybeUser = Optional.ofNullable(userService.create(USERNAME2, PASSWORD, MAIL2));
//        Assert.assertNotNull(maybeUser);
//        Assert.assertTrue(maybeUser.isPresent());
//    }
}
