package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Booking;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.State;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Ticket;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class EventJdbcDaoTest {
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
    private SimpleJdbcInsert jdbcInsertLocation;
    private SimpleJdbcInsert jdbcInsertType;
    private SimpleJdbcInsert jdbcInsertTag;
    private SimpleJdbcInsert jdbcInsertRole;

    private final static String GENERIC = "genericName";
    private final static String EVENT_NAME = "eventName";
    private final static String EVENT_NAME_2 = "eventName2";
    private final static String EVENT_DESC = "eventDesc";
    private final static String USERNAME = "user";
    private final static String USERNAME2 = "user2";
    private final static LocalDateTime EVENT_DATE = LocalDateTime.of(3025, Month.JULY, 29, 19, 30, 40);
    private final static LocalDateTime EVENT_DATE_BEFORE = LocalDateTime.of(2005, Month.JULY, 29, 19, 30, 40);
    private final static LocalDateTime EVENT_DATE_LATER = LocalDateTime.of(3050, Month.JULY, 29, 19, 30, 40);
    private final static String TICKET_NAME = "ticketName";
    private final static String TICKET_NAME2 = "ticketName2";
    private final static Locale LOCALE = new Locale("es");

    private User user;
    private User user2;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertLocation = new SimpleJdbcInsert(ds).withTableName("locations").usingGeneratedKeyColumns("locationid");
        jdbcInsertType = new SimpleJdbcInsert(ds).withTableName("types").usingGeneratedKeyColumns("typeid");
        jdbcInsertTag = new SimpleJdbcInsert(ds).withTableName("tags").usingGeneratedKeyColumns("tagid");
        jdbcInsertRole = new SimpleJdbcInsert(ds).withTableName("roles");
        TestUtils.deleteAllTables(jdbcTemplate);

        Map<String, Object> generic = new HashMap<>();
        generic.put("name", GENERIC);
        jdbcInsertLocation.execute(generic);
        jdbcInsertType.execute(generic);
        jdbcInsertTag.execute(generic);

        imageDao.addEventImage(BYTE_ARRAY);

        TestUtils.createRoles(jdbcInsertRole);

        user = userDao.create(USERNAME, "useruser", "user@user.com");
        user2 = userDao.create(USERNAME2, "useruser", "user2@user.com");
    }

    @Test
    public void testCreateEvent() {
        final Event event = eventDao.create(EVENT_NAME, EVENT_DESC, 0, 0, EVENT_DATE, 0, new Integer[] {0}, 0, LOCALE);
        Assert.assertNotNull(event);
        Assert.assertEquals(EVENT_NAME, event.getName());
        Assert.assertEquals(EVENT_DESC, event.getDescription());
        Assert.assertEquals(EVENT_DATE, event.getDate());
        Assert.assertEquals(GENERIC, event.getLocation().getName());
        Assert.assertEquals(GENERIC, event.getType().getName());
        Assert.assertEquals(GENERIC, event.getTags().get(0).getName());
        Assert.assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "events"));
    }

    @Test
    public void testFilterBy() {
        final Event event = eventDao.create(EVENT_NAME, EVENT_DESC, 0, 0, EVENT_DATE, 0, new Integer[] {0}, 0, LOCALE);
        List<Event> events = eventDao.filterBy(new Integer[] {0}, new Integer[] {0}, null, null, null, null, USERNAME, null, 1, LOCALE);
        Assert.assertNotNull(events);
        Assert.assertNotNull(events.get(0));
        Assert.assertEquals(event.getName(), events.get(0).getName());
    }

    @Test
    public void testFilterByNoEvents() {
        eventDao.create(EVENT_NAME, EVENT_DESC, 0, 0, EVENT_DATE, 0, new Integer[] {0}, 0, LOCALE);
        List<Event> events = eventDao.filterBy(new Integer[] {0}, new Integer[] {0}, null, null, null, null, USERNAME2, null, 1, LOCALE);
        Assert.assertNotNull(events);
        Assert.assertEquals(0, events.size());
    }

    @Test
    public void testGetEventById() {
        final Event event = eventDao.create(EVENT_NAME, EVENT_DESC, 0, 0, EVENT_DATE, 0, new Integer[] {0}, 0, LOCALE);
        final Event eventById = eventDao.getEventById(event.getId(), LOCALE).orElse(null);

        Assert.assertNotNull(event);
        Assert.assertNotNull(eventById);
        Assert.assertEquals(event.getId(), eventById.getId());
    }

    @Test
    public void testGetFewTickets() {
        Event event = TestUtils.generateEventWithTickets(user, EVENT_DATE, eventDao, EVENT_NAME, EVENT_DESC, TICKET_NAME, 10, 0);

        List<Event> events = eventDao.getFewTicketsEvents(LOCALE);
        Assert.assertNotNull(events);
        Assert.assertEquals(0, events.size());

        TestUtils.bookForEvent(event, event.getTickets().get(0), user, eventDao, 9);
        events = eventDao.getFewTicketsEvents(LOCALE);
        Assert.assertNotNull(events);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(event.getId(), events.get(0).getId());
    }

    @Test
    public void testGetUpcoming() {
        Event event = TestUtils.generateEventWithTickets(user, EVENT_DATE, eventDao, EVENT_NAME, EVENT_DESC, TICKET_NAME, 10, 0);
        Event event_later = TestUtils.generateEventWithTickets(user, EVENT_DATE_LATER, eventDao, EVENT_NAME, EVENT_DESC, TICKET_NAME, 10, 0);
        Event event_before = TestUtils.generateEventWithTickets(user, EVENT_DATE_BEFORE, eventDao, EVENT_NAME, EVENT_DESC, TICKET_NAME, 10, 0);

        List<Event> events = eventDao.getUpcomingEvents(LOCALE);
        Assert.assertNotNull(events);
        Assert.assertEquals(2, events.size());
        Assert.assertEquals(event.getId(), events.get(0).getId());
        Assert.assertEquals(event_later.getId(), events.get(1).getId());
    }

    @Test
    public void testEventUpdate() {
        Event event = TestUtils.generateEvent(user, EVENT_DATE, eventDao, EVENT_NAME, EVENT_DESC);
        Assert.assertNotNull(event);
        Assert.assertEquals(EVENT_NAME, event.getName());
        Assert.assertEquals(EVENT_DATE, event.getDate());

        eventDao.updateEvent(event.getId(), EVENT_NAME_2, EVENT_DESC, 0, 0, EVENT_DATE_LATER, 0, new Integer[] {0});
        event = eventDao.getEventById(event.getId(), LOCALE).orElse(null);
        Assert.assertNotNull(event);
        Assert.assertNotEquals(EVENT_NAME, event.getName());
        Assert.assertNotEquals(EVENT_DATE, event.getDate());

        Assert.assertEquals(EVENT_NAME_2, event.getName());
        Assert.assertEquals(EVENT_DATE_LATER, event.getDate());
    }

    @Test
    public void testDeleteEvent() {
        Event event = TestUtils.generateEvent(user, EVENT_DATE, eventDao, EVENT_NAME, EVENT_DESC);
        Assert.assertNotNull(event);
        event = eventDao.getEventById(event.getId(), LOCALE).orElse(null);
        Assert.assertNotNull(event);
        Assert.assertEquals(State.ACTIVE, event.getState());
        eventDao.deleteEvent(event.getId());
        event = eventDao.getEventById(event.getId(), LOCALE).orElse(null);
        Assert.assertNotNull(event);
        Assert.assertEquals(State.DELETED, event.getState());
    }

    @Test
    public void testSoldOutEvent() {
        Event event = TestUtils.generateEvent(user, EVENT_DATE, eventDao, EVENT_NAME, EVENT_DESC);
        Assert.assertNotNull(event);
        event = eventDao.getEventById(event.getId(), LOCALE).orElse(null);
        Assert.assertNotNull(event);
        Assert.assertEquals(State.ACTIVE, event.getState());
        eventDao.soldOut(event.getId());
        event = eventDao.getEventById(event.getId(), LOCALE).orElse(null);
        Assert.assertNotNull(event);
        Assert.assertEquals(State.SOLDOUT, event.getState());
    }

    @Test
    public void testActiveEvent() {
        Event event = TestUtils.generateEvent(user, EVENT_DATE, eventDao, EVENT_NAME, EVENT_DESC);
        Assert.assertNotNull(event);
        Assert.assertEquals(State.ACTIVE, event.getState());
        eventDao.soldOut(event.getId());
        eventDao.active(event.getId());
        Assert.assertEquals(State.ACTIVE, event.getState());
    }

    @Test
    public void testGetUserEvents() {
        Event event = TestUtils.generateEvent(user, EVENT_DATE, eventDao, EVENT_NAME, EVENT_DESC);
        Assert.assertNotNull(event);
        Assert.assertEquals(user.getUsername(), event.getUser().getUsername());
        Assert.assertEquals(user.getId(), event.getUser().getId());

        List<Event> events = eventDao.getUserEvents(user.getId(), 1, LOCALE);
        Assert.assertNotNull(events);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(user.getUsername(), events.get(0).getUser().getUsername());
        Assert.assertEquals(user.getId(), events.get(0).getUser().getId());
    }

    @Test
    public void testGetPopular() {
        Event event = TestUtils.generateEventWithTickets(user, EVENT_DATE, eventDao, EVENT_NAME, EVENT_DESC, TICKET_NAME, 10, 0);
        Event event2 = TestUtils.generateEventWithTickets(user, EVENT_DATE, eventDao, EVENT_NAME, EVENT_DESC, TICKET_NAME2, 10, 0);

        List<Event> events = eventDao.getPopularEvents(event.getId(), LOCALE);
        Assert.assertNotNull(events);
        Assert.assertEquals(0, events.size());

        TestUtils.bookForEvent(event, event.getTickets().get(0), user2, eventDao, 1);
        TestUtils.bookForEvent(event2, event2.getTickets().get(0), user2, eventDao, 1);
        events = eventDao.getPopularEvents(event.getId(), LOCALE);
        Assert.assertNotNull(events);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(event2.getId(), events.get(0).getId());
    }

    @Test
    public void testBooking() {
        Event event = TestUtils.generateEventWithTickets(user, EVENT_DATE, eventDao, EVENT_NAME, EVENT_DESC, TICKET_NAME, 10, 0);

        Assert.assertNotNull(event);
        Assert.assertNotNull(event.getTickets());
        Assert.assertNotNull(event.getTickets().get(0));
        Assert.assertEquals((Integer) 10, event.getTickets().get(0).getTicketsLeft());

        Booking booking = new Booking(5, event.getTickets().get(0).getId());
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        eventDao.book(bookings, user2.getId(), event.getId(), LOCALE);

        event = eventDao.getEventById(event.getId(), LOCALE).orElse(null);

        Assert.assertNotNull(event);
        Assert.assertNotNull(event.getTickets());
        Assert.assertNotNull(event.getTickets().get(0));
        Assert.assertEquals((Integer) 5, event.getTickets().get(0).getTicketsLeft());

        booking = new Booking(5, event.getTickets().get(0).getId());
        bookings = new ArrayList<>();
        bookings.add(booking);
        eventDao.book(bookings, user2.getId(), event.getId(), LOCALE);

        event = eventDao.getEventById(event.getId(), LOCALE).orElse(null);
        Assert.assertNotNull(event);
        Assert.assertEquals(State.SOLDOUT, event.getState());
    }

    @Test
    public void testCancelBooking() {
        Event event = TestUtils.generateEventWithTickets(user, EVENT_DATE, eventDao, EVENT_NAME, EVENT_DESC, TICKET_NAME, 10, 0);

        Assert.assertNotNull(event);
        Assert.assertNotNull(event.getTickets());
        Assert.assertNotNull(event.getTickets().get(0));
        Assert.assertEquals((Integer) 10, event.getTickets().get(0).getTicketsLeft());

        TestUtils.bookForEvent(event, event.getTickets().get(0), user2, eventDao, 5);

        List<Booking> bookings = new ArrayList<>();
        Booking b = new Booking(5, event.getTickets().get(0).getId());
        bookings.add(b);
        eventDao.cancelBooking(bookings, user2.getId(), event.getId());

        event = eventDao.getEventById(event.getId(), LOCALE).orElse(null);

        Assert.assertNotNull(event);
        Assert.assertNotNull(event.getTickets());
        Assert.assertNotNull(event.getTickets().get(0));
        Assert.assertEquals((Integer) 10, event.getTickets().get(0).getTicketsLeft());

        bookings = new ArrayList<>();
        b = new Booking(0, event.getTickets().get(0).getId());
        bookings.add(b);
        eventDao.cancelBooking(bookings, user2.getId(), event.getId());

        event = eventDao.getEventById(event.getId(), LOCALE).orElse(null);

        Assert.assertNotNull(event);
        Assert.assertNotNull(event.getTickets());
        Assert.assertNotNull(event.getTickets().get(0));
        Assert.assertEquals((Integer) 10, event.getTickets().get(0).getTicketsLeft());
    }

    @Test
    public void testGetTicketById() {
        Event event = TestUtils.generateEventWithTickets(user, EVENT_DATE, eventDao, EVENT_NAME, EVENT_DESC, TICKET_NAME, 10, 0);
        Assert.assertNotNull(event);
        Assert.assertNotNull(event.getTickets());
        Assert.assertNotNull(event.getTickets().get(0));

        Ticket ticket = eventDao.getTicketById(event.getTickets().get(0).getId()).orElse(null);

        Assert.assertNotNull(ticket);
        Assert.assertEquals(event.getTickets().get(0).getId(), ticket.getId());
        Assert.assertEquals(TICKET_NAME, ticket.getTicketName());
    }

    @Test
    public void testDeleteTicket() {
        Event event = TestUtils.generateEventWithTickets(user, EVENT_DATE, eventDao, EVENT_NAME, EVENT_DESC, TICKET_NAME, 10, 0);
        Assert.assertNotNull(event);
        Assert.assertNotNull(event.getTickets());
        Assert.assertNotNull(event.getTickets().get(0));

        Ticket ticket = eventDao.getTicketById(event.getTickets().get(0).getId()).orElse(null);
        Assert.assertNotNull(ticket);

        eventDao.deleteTicket(ticket.getId());

        ticket = eventDao.getTicketById(event.getTickets().get(0).getId()).orElse(null);
        Assert.assertNull(ticket);
    }

    @Test
    public void testUpdateTicket() {
        Event event = TestUtils.generateEventWithTickets(user, EVENT_DATE, eventDao, EVENT_NAME, EVENT_DESC, TICKET_NAME, 10, 0);
        Assert.assertNotNull(event);
        Assert.assertNotNull(event.getTickets());
        Assert.assertNotNull(event.getTickets().get(0));

        eventDao.updateTicket(event.getTickets().get(0).getId(), TICKET_NAME2, 10, 20);

        Ticket ticket = eventDao.getTicketById(event.getTickets().get(0).getId()).orElse(null);

        Assert.assertNotNull(ticket);
        Assert.assertEquals(event.getTickets().get(0).getId(), ticket.getId());
        Assert.assertNotEquals(TICKET_NAME, ticket.getTicketName());
        Assert.assertEquals(TICKET_NAME2, ticket.getTicketName());
        Assert.assertEquals(20, (int) ticket.getQty());
        Assert.assertEquals(10.0, ticket.getPrice(), 0.001);
    }
}