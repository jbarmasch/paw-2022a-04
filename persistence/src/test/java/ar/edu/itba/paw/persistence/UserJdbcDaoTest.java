package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
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
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class UserJdbcDaoTest {
//    private static final String PASSWORD = "password";
//    private static final String MAIL = "username@mail.com";
//    private static final String USERNAME2 = "username2";
//    private static final String MAIL2 = USERNAME2 + "@mail.com";
//    private static final byte[] BYTE_ARRAY = new byte[] {0x00};
//
//    @Autowired
//    private DataSource ds;
//    @Autowired
//    private EventJdbcDao eventDao;
//    @Autowired
//    private UserJdbcDao userDao;
//    @Autowired
//    private ImageJdbcDao imageDao;
//
//    private JdbcTemplate jdbcTemplate;
//    private SimpleJdbcInsert jdbcInsert;
//    private SimpleJdbcInsert jdbcInsertLocation;
//    private SimpleJdbcInsert jdbcInsertType;
//    private SimpleJdbcInsert jdbcInsertTag;
//    private SimpleJdbcInsert jdbcInsertImage;
//    private SimpleJdbcInsert jdbcInsertRole;
//    private SimpleJdbcInsert jdbcInsertBooking;
//
//    private final String GENERIC = "genericName";
//    private final String GENERIC2 = "genericName2";
//    private final String EVENTNAME = "eventName";
//    private final String EVENTDESC = "eventDesc";
//    private final String USERNAME = "user";
//    private final LocalDateTime EVENTDATE = LocalDateTime.of(1025, Month.JULY, 29, 19, 30, 40);
//    private final LocalDateTime FUTURE = LocalDateTime.of(3025, Month.JULY, 29, 19, 30, 40);
//
//    private final static int TICKET_QTY = 100;
//    private final static double TICKET_PRICE_1 = 15.15;
//    private final static double TICKET_PRICE_2 = 15.85;
//    private final static int BOOKING_QTY_1 = 71;
//    private final static int BOOKING_QTY_2 = 37;
//    private final static double DELTA = .001;
//
//    @Before
//    public void setUp() {
//        jdbcTemplate = new JdbcTemplate(ds);
//        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("events").usingGeneratedKeyColumns("eventid");
//        jdbcInsertLocation = new SimpleJdbcInsert(ds).withTableName("locations").usingGeneratedKeyColumns("locationid");
//        jdbcInsertType = new SimpleJdbcInsert(ds).withTableName("types").usingGeneratedKeyColumns("typeid");
//        jdbcInsertTag = new SimpleJdbcInsert(ds).withTableName("tags").usingGeneratedKeyColumns("tagid");
//        jdbcInsertImage = new SimpleJdbcInsert(ds).withTableName("images").usingGeneratedKeyColumns("imageid");
//        jdbcInsertBooking = new SimpleJdbcInsert(ds).withTableName("bookings");
//        jdbcInsertRole = new SimpleJdbcInsert(ds).withTableName("roles");
//
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "roles");
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "events");
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "locations");
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "types");
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "tags");
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "eventtags");
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "images");
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "userroles");
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "bookings");
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "tickets");
//        jdbcTemplate.query("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK", r -> r);
//
//        Map<String, Object> generic = new HashMap<>();
//        generic.put("name", GENERIC);
//        jdbcInsertLocation.execute(generic);
//        jdbcInsertTag.execute(generic);
//        jdbcInsertType.execute(generic);
//        generic.put("name", GENERIC2);
//        jdbcInsertLocation.execute(generic);
//        jdbcInsertType.execute(generic);
//
//        imageDao.addEventImage(BYTE_ARRAY);
//
//        Map<String, Object> userRole = new HashMap<>();
//        userRole.put("roleid", 1);
//        userRole.put("name", "ROLE_USER");
//        jdbcInsertRole.execute(userRole);
//        userRole = new HashMap<>();
//        userRole.put("roleid", 2);
//        userRole.put("name", "ROLE_CREATOR");
//        jdbcInsertRole.execute(userRole);
//    }
//
//    @Test
//    public void testCreate() {
//        final User user = userDao.create(USERNAME, PASSWORD, MAIL);
//        Assert.assertNotNull(user);
//        Assert.assertEquals(USERNAME, user.getUsername());
//        Assert.assertEquals(PASSWORD, user.getPassword());
//        Assert.assertEquals(MAIL, user.getMail());
//        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
//    }
//
//    @Test
//    public void testFindByUsername() {
//        final User user = userDao.create(USERNAME, PASSWORD, MAIL);
//        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
//        final User testUser = userDao.findByUsername(USERNAME).orElse(null);
//        Assert.assertNotNull(testUser);
//        Assert.assertEquals(testUser.getUsername(), user.getUsername());
//        Assert.assertEquals(testUser.getPassword(), user.getPassword());
//        Assert.assertEquals(testUser.getMail(), user.getMail());
//    }
//
//    @Test
//    public void testFindByMail() {
//        final User user = userDao.create(USERNAME, PASSWORD, MAIL);
//        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
//        final User testUser = userDao.findByMail(MAIL).orElse(null);
//        Assert.assertNotNull(testUser);
//        Assert.assertEquals(testUser.getUsername(), user.getUsername());
//        Assert.assertEquals(testUser.getPassword(), user.getPassword());
//        Assert.assertEquals(testUser.getMail(), user.getMail());
//    }
//
//
//    @Test
//    public void testFindById() {
//        final User user = userDao.create(USERNAME, PASSWORD, MAIL);
//        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
//        final User testUser = userDao.getUserById(0).orElse(null);
//        Assert.assertNotNull(testUser);
//        Assert.assertEquals(testUser.getUsername(), user.getUsername());
//        Assert.assertEquals(testUser.getPassword(), user.getPassword());
//        Assert.assertEquals(testUser.getMail(), user.getMail());
//    }
//
//    @Test
//    public void testCanRate() {
//        final User organizer = userDao.create(USERNAME, PASSWORD, MAIL);
//        final User user = userDao.create(USERNAME2, PASSWORD, MAIL2);
//
//        Event event = TestUtils.generateEventWithTickets(organizer, EVENTDATE, eventDao, EVENTNAME, EVENTDESC, GENERIC, 10, 0);
//        TestUtils.bookForEvent(event, event.getTickets().get(0), user, eventDao, 5);
//
//        Assert.assertFalse(userDao.canRate(user.getId(), organizer.getId()));
//        Assert.assertTrue(userDao.canRate(organizer.getId(), user.getId()));
//    }
//
//    @Test
//    public void testGetAllFutureBookings() {
//        final User organizer = userDao.create(USERNAME, PASSWORD, MAIL);
//        final User user = userDao.create(USERNAME2, PASSWORD, MAIL2);
//
//        Event futureEvent = TestUtils.generateEventWithTickets(organizer, FUTURE, eventDao, EVENTNAME, EVENTDESC, GENERIC, 10, 0);
//        TestUtils.bookForEvent(futureEvent, futureEvent.getTickets().get(0), user, eventDao, 5);
//        Event pastEvent = TestUtils.generateEventWithTickets(organizer, EVENTDATE, eventDao, EVENTNAME, EVENTDESC, GENERIC, 10, 0);
//        TestUtils.bookForEvent(pastEvent, pastEvent.getTickets().get(0), user, eventDao, 5);
//
//        final List<EventBooking> futureBookings = userDao.getAllFutureBookingsFromUser(user.getId(), 1, new Locale("es"));
//        List<Event> futureEvents = new ArrayList<>();
//        for (EventBooking booking : futureBookings) {
//            futureEvents.add(booking.getEvent());
//        }
//
//        Assert.assertTrue(TestUtils.contains(futureEvent, futureEvents));
//        Assert.assertFalse(TestUtils.contains(pastEvent, futureEvents));
//    }
//
//    @Test
//    public void testGetAllPreviousBookings() {
//        final User organizer = userDao.create(USERNAME, PASSWORD, MAIL);
//        final User user = userDao.create(USERNAME2, PASSWORD, MAIL2);
//
//        Event futureEvent = TestUtils.generateEventWithTickets(organizer, FUTURE, eventDao, EVENTNAME, EVENTDESC, GENERIC, 10, 0);
//        TestUtils.bookForEvent(futureEvent, futureEvent.getTickets().get(0), user, eventDao, 5);
//        Event pastEvent = TestUtils.generateEventWithTickets(organizer, EVENTDATE, eventDao, EVENTNAME, EVENTDESC, GENERIC, 10, 0);
//        TestUtils.bookForEvent(pastEvent, pastEvent.getTickets().get(0), user, eventDao, 5);
//
//        final List<EventBooking> pastBookings = userDao.getAllPreviousBookingsFromUser(user.getId(), 1, new Locale("es"));
//        List<Event> pastEvents = new ArrayList<>();
//        for (EventBooking booking : pastBookings) {
//            pastEvents.add(booking.getEvent());
//        }
//
//        Assert.assertFalse(TestUtils.contains(futureEvent, pastEvents));
//        Assert.assertTrue(TestUtils.contains(pastEvent, pastEvents));
//    }
//
//    @Test
//    public void testGetBookingFromUser() {
//        final User organizer = userDao.create(USERNAME, PASSWORD, MAIL);
//        final User user = userDao.create(USERNAME2, PASSWORD, MAIL2);
//
//        Event event = TestUtils.generateEventWithTickets(organizer, EVENTDATE, eventDao, EVENTNAME, EVENTDESC, GENERIC, 10, 0);
//        TestUtils.bookForEvent(event, event.getTickets().get(0), user, eventDao, 5);
//
//        final EventBooking booking = userDao.getBookingFromUser(user.getId(), event.getId(), new Locale("es")).orElse(null);
//        Assert.assertNotNull(booking);
//        Assert.assertEquals(booking.getEvent(), event);
//    }
//
//    @Test
//    public void testGetEventStats() {
//        final User organizer = userDao.create(USERNAME, PASSWORD, MAIL);
//        final User user = userDao.create(USERNAME2, PASSWORD, MAIL2);
//        final User user2 = userDao.create(USERNAME + "3", PASSWORD, "3" + MAIL);
//
//        Event futureEvent = TestUtils.generateEventWithTickets(organizer, FUTURE, eventDao, EVENTNAME, EVENTDESC, GENERIC, TICKET_QTY, TICKET_PRICE_1);
//        Event pastEvent = TestUtils.generateEventWithTickets(organizer, EVENTDATE, eventDao, EVENTNAME, EVENTDESC, GENERIC, TICKET_QTY, TICKET_PRICE_2);
//        TestUtils.bookForEvent(pastEvent, pastEvent.getTickets().get(0), user, eventDao, BOOKING_QTY_1);
//        TestUtils.bookForEvent(futureEvent, futureEvent.getTickets().get(0), user2, eventDao, BOOKING_QTY_2);
//
//        final EventStats stats = userDao.getEventStats(organizer.getId(), new Locale("es")).orElse(null);
//
//        Assert.assertNotNull(stats);
//        Assert.assertEquals(2, stats.getEventsCreated());
//        Assert.assertEquals(BOOKING_QTY_1 + BOOKING_QTY_2, stats.getBookingsGotten());
//        Assert.assertEquals(pastEvent, stats.getPopularEvent());
//    }
//
//    @Test
//    public void testRateUser(){
//        User organizer = userDao.create(USERNAME, PASSWORD, MAIL);
//        User user = userDao.create(USERNAME2, PASSWORD, MAIL2);
//        User user2 = userDao.create(USERNAME + "3", PASSWORD, "3" + MAIL);
//
//        Event event1 = TestUtils.generateEventWithTickets(organizer, EVENTDATE, eventDao, EVENTNAME, EVENTDESC, GENERIC, TICKET_QTY, TICKET_PRICE_1);
//        Event event2 = TestUtils.generateEventWithTickets(organizer, FUTURE, eventDao, EVENTNAME, EVENTDESC, GENERIC, TICKET_QTY, TICKET_PRICE_2);
//        TestUtils.bookForEvent(event1, event1.getTickets().get(0), user, eventDao, BOOKING_QTY_1);
//        TestUtils.bookForEvent(event2, event2.getTickets().get(0), user2, eventDao, BOOKING_QTY_2);
//
//        Assert.assertEquals(0, organizer.getRating(), DELTA);
//        userDao.rateUser(user.getId(), organizer.getId(), 3.5);
//        organizer = userDao.getUserById(organizer.getId()).orElse(null);
//        Assert.assertNotNull(organizer);
//        Assert.assertEquals(3.5, organizer.getRating(), DELTA);
//        userDao.rateUser(user2.getId(), organizer.getId(), 1);
//        organizer = userDao.getUserById(organizer.getId()).orElse(null);
//        Assert.assertNotNull(organizer);
//        Assert.assertEquals(2.25, organizer.getRating(), DELTA);
//    }
//
//}