package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Booking;
import ar.edu.itba.paw.model.Ticket;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.State;
import ar.edu.itba.paw.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.awt.print.Book;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserJdbcDaoTest {
    private static final String PASSWORD = "password";
    private static final String MAIL = "username@mail.com";
    private static final String USERNAME2 = "username2";
    private static final String MAIL2 = USERNAME2 + "@mail.com";
    private static final byte[] BYTE_ARRAY = new byte[] {0x00};

    @Autowired
    private DataSource ds;
    @Autowired
    private EventJdbcDao eventDao;
    @Autowired
    private UserJdbcDao userDao;
    @Autowired
    private ImageJdbcDao imageDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private SimpleJdbcInsert jdbcInsertLocation;
    private SimpleJdbcInsert jdbcInsertType;
    private SimpleJdbcInsert jdbcInsertTag;
    private SimpleJdbcInsert jdbcInsertImage;
    private SimpleJdbcInsert jdbcInsertRole;
    private SimpleJdbcInsert jdbcInsertBooking;

    private final String GENERIC = "genericName";
    private final String EVENTNAME = "eventName";
    private final String EVENTDESC = "eventDesc";
    private final String USERNAME = "user";
    private final LocalDateTime EVENTDATE = LocalDateTime.of(1025, Month.JULY, 29, 19, 30, 40);

    @Before
    public void setUp() {
        System.out.println("hola");
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("events").usingGeneratedKeyColumns("eventid");
        jdbcInsertLocation = new SimpleJdbcInsert(ds).withTableName("locations").usingGeneratedKeyColumns("locationid");
        jdbcInsertType = new SimpleJdbcInsert(ds).withTableName("types").usingGeneratedKeyColumns("typeid");
        jdbcInsertTag = new SimpleJdbcInsert(ds).withTableName("tags").usingGeneratedKeyColumns("tagid");
        jdbcInsertImage = new SimpleJdbcInsert(ds).withTableName("images").usingGeneratedKeyColumns("imageid");
        jdbcInsertBooking = new SimpleJdbcInsert(ds).withTableName("bookings");
        jdbcInsertRole = new SimpleJdbcInsert(ds).withTableName("roles");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "roles");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "events");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "locations");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "types");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "tags");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "eventtags");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "images");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "userroles");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "bookings");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "tickets");
        jdbcTemplate.query("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK", r -> r);

        Map<String, Object> generic = new HashMap<>();
        generic.put("name", GENERIC);
        jdbcInsertLocation.executeAndReturnKey(generic);
        jdbcInsertType.execute(generic);
        jdbcInsertTag.execute(generic);

        imageDao.addEventImage(BYTE_ARRAY);

        Map<String, Object> userRole = new HashMap<>();
        userRole.put("roleid", 1);
        userRole.put("name", "ROLE_USER");
        jdbcInsertRole.execute(userRole);
        userRole = new HashMap<>();
        userRole.put("roleid", 2);
        userRole.put("name", "ROLE_CREATOR");
        jdbcInsertRole.execute(userRole);
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


    @Test
    public void testFindById() {
        final User user = userDao.create(USERNAME, PASSWORD, MAIL);
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
        final User testUser = userDao.getUserById(0).orElse(null);
        Assert.assertNotNull(testUser);
        Assert.assertEquals(testUser.getUsername(), user.getUsername());
        Assert.assertEquals(testUser.getPassword(), user.getPassword());
        Assert.assertEquals(testUser.getMail(), user.getMail());
    }

    @Test
    public void testCanRate(){
        final User organizer = userDao.create(USERNAME, PASSWORD, MAIL);
        final User user = userDao.create(USERNAME2, PASSWORD, MAIL2);

        Event event = eventDao.create(EVENTNAME, EVENTDESC, 0, 0, EVENTDATE, 0, new Integer[] {0}, 0, new Locale("es"));

        // final Event event = eventDao.create(EVENTNAME, EVENTDESC, 0, 0, EVENTDATE, 0, new Integer[] {0}, organizer.getId(), new Locale("es"));
//        final Event event = eventDao.create(EVENTNAME, EVENTDESC, 0, 0, EVENTDATE, 0, new Integer[] {0}, 0, new Locale("es"));
        eventDao.addTicket(event.getId(), GENERIC, 0.0, 10);
        event = eventDao.getEventById(event.getId(), new Locale("es")).orElse(null);
        List<Ticket> tickets = event.getTickets();
        Ticket ticket = tickets.get(0);
        Booking booking = new Booking(5, ticket.getId());
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        eventDao.book(bookings, user.getId(), event.getId(), new Locale("es"));

        Assert.assertFalse(userDao.canRate(user.getId(), organizer.getId()));
        Assert.assertTrue(userDao.canRate(organizer.getId(), user.getId()));
    }
}