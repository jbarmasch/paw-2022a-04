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

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserDao mockDao;

    @Test
    public void testCreate() {
        Mockito.when(mockDao.create(Mockito.eq(USERNAME), Mockito.eq(PASSWORD))).thenReturn(new User(1, USERNAME, PASSWORD));

        Optional<User> maybeUser = Optional.ofNullable(userService.create(USERNAME, PASSWORD));

        Assert.assertNotNull(maybeUser);
        Assert.assertTrue(maybeUser.isPresent());
        Assert.assertEquals(USERNAME, maybeUser.get().getUsername());
        Assert.assertEquals(PASSWORD, maybeUser.get().getPassword());
    }

    @Test
    public void testCreateEmptyPassword() {
        Optional<User> maybeUser = Optional.ofNullable(userService.create(USERNAME, ""));
        Assert.assertNotNull(maybeUser);
        Assert.assertFalse(maybeUser.isPresent());
    }

//    @Test
//    public void testCreateAlreadyExists() {
//        Mockito.when(mockDao.findByUsername(Mockito.eq(USERNAME))).thenReturn(Optional.of(new User(1, USERNAME, PASSWORD)));
//        Optional<User> maybeUser = Optional.ofNullable(userService.create(USERNAME, PASSWORD));
//        Assert.assertNotNull(maybeUser);
//        Assert.assertFalse(maybeUser.isPresent());
//    }
}
