package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.RoleEnum;
import ar.edu.itba.paw.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String MAIL = "username@mail.com";

    @Autowired
    private DataSource ds;

    @Autowired
    private UserJdbcDao userDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("roles");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "userroles");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "roles");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "ratings");

        Map<String, Object> userRole = new HashMap<>();
        userRole.put("roleid", 1);
        userRole.put("name", "ROLE_USER");
        jdbcInsert.execute(userRole);
        userRole = new HashMap<>();
        userRole.put("roleid", 2);
        userRole.put("name", "ROLE_CREATOR");
        jdbcInsert.execute(userRole);
    }

    @Test
    public void testCreate() {
        final User user = userDao.create(USERNAME, PASSWORD, MAIL);
        Assert.assertNotNull(user);
        Assert.assertEquals(USERNAME, user.getUsername());
        Assert.assertEquals(PASSWORD, user.getPassword());
        Assert.assertEquals(MAIL, user.getMail());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    @Test
    public void testFindByUsername() {
        final User user = userDao.create(USERNAME, PASSWORD, MAIL);
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
        final User testUser = userDao.findByUsername(USERNAME).orElse(null);
        Assert.assertNotNull(testUser);
        Assert.assertEquals(testUser.getUsername(), user.getUsername());
        Assert.assertEquals(testUser.getPassword(), user.getPassword());
        Assert.assertEquals(testUser.getMail(), user.getMail());
    }

    @Test
    public void testFindByMail() {
        final User user = userDao.create(USERNAME, PASSWORD, MAIL);
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
        final User testUser = userDao.findByMail(MAIL).orElse(null);
        Assert.assertNotNull(testUser);
        Assert.assertEquals(testUser.getUsername(), user.getUsername());
        Assert.assertEquals(testUser.getPassword(), user.getPassword());
        Assert.assertEquals(testUser.getMail(), user.getMail());
    }
}