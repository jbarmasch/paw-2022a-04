package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class EventJpaDaoTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private EventJpaDao eventDao;

    @Before
    public void setUp() {
        TestUtils.deleteTables(em);
        TestUtils.setUpEvent(em);
    }

    @Test
    public void testCreateEvent() {
        final Event event = eventDao.createEvent(TestUtils.EVENT_NAME, TestUtils.EVENT_DESC, TestUtils.GENERIC_LOCATION.getId(), TestUtils.GENERIC_TYPE.getId(), TestUtils.EVENT_DATE, TestUtils.BYTE_ARRAY, new Long[]{TestUtils.GENERIC_TAG.getId()}, TestUtils.USER1, 18, TestUtils.USER2);
        Assert.assertNotNull(event);
        Assert.assertEquals(TestUtils.EVENT_NAME, event.getName());
        Assert.assertEquals(TestUtils.EVENT_DESC, event.getDescription());
        Assert.assertEquals(TestUtils.EVENT_DATE, event.getDate());
        Assert.assertEquals(TestUtils.GENERIC, event.getLocation().getName());
        Assert.assertEquals(TestUtils.GENERIC, event.getType().getName());
        Assert.assertEquals(TestUtils.GENERIC, event.getTags().get(0).getName());
        Assert.assertEquals(1, TestUtils.countEvents(em));
    }

    @Test
    public void testGetEventById() {
        final Event event = TestUtils.createEvent(em);
        final Event eventById = eventDao.getEventById(event.getId()).orElse(null);
        Assert.assertNotNull(eventById);
        Assert.assertEquals(event.getId(), eventById.getId());
    }

    @Test
    public void testGetFewTickets() {
        Event event = TestUtils.createEvent(em);
        List<Event> events = eventDao.getFewTicketsEvents();
        Assert.assertNotNull(events);
        Assert.assertEquals(0, events.size());
        Ticket ticket = TestUtils.createTicket(em);
        ticket.setEvent(event);
        ticket.setQty(10);
        ticket.setBooked(9);
        em.persist(ticket);

        events = eventDao.getFewTicketsEvents();
        Assert.assertNotNull(events);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(event.getId(), events.get(0).getId());
    }

    @Test
    public void testEventUpdate() {
        final Event event = TestUtils.createEvent(em);
        eventDao.updateEvent(event.getId(), TestUtils.EVENT_NAME_2, TestUtils.EVENT_DESC, TestUtils.GENERIC_LOCATION.getId(), TestUtils.GENERIC_TYPE.getId(), TestUtils.EVENT_DATE_LATER, TestUtils.BYTE_ARRAY, new Long[]{TestUtils.GENERIC_TAG.getId()}, 18);
        Assert.assertNotNull(event);
        Assert.assertNotEquals(TestUtils.EVENT_NAME, event.getName());
        Assert.assertNotEquals(TestUtils.EVENT_DATE, event.getDate());

        Assert.assertEquals(TestUtils.EVENT_NAME_2, event.getName());
        Assert.assertEquals(TestUtils.EVENT_DATE_LATER, event.getDate());
    }

    @Test
    public void testDeleteEvent() {
        Event event = TestUtils.createEvent(em);
        eventDao.deleteEvent(event);
        Assert.assertNotNull(event);
        Assert.assertEquals(State.DELETED, event.getState());
    }

    @Test
    public void testSoldOutEvent() {
        Event event = TestUtils.createEvent(em);
        eventDao.soldOut(event);
        Assert.assertNotNull(event);
        Assert.assertEquals(State.SOLDOUT, event.getState());
    }

    @Test
    public void testActiveEvent() {
        Event event = TestUtils.createEvent(em);
        event.setState(State.DELETED);
        em.persist(event);
        eventDao.active(event);
        Assert.assertEquals(State.ACTIVE, event.getState());
    }

    @Test
    public void testEventStats() {
        TicketBooking ticketBooking = TestUtils.createTicketBooking(em);
        EventBooking eventBooking = ticketBooking.getEventBooking();
        Ticket ticket = ticketBooking.getTicket();
        Event event = eventBooking.getEvent();
        ticket.setBooked(ticket.getQty() / 2);
        em.persist(ticket);
        ticketBooking.setQty(ticket.getQty() / 2);
        em.persist(ticketBooking);

        EventStats eventStats = eventDao.getEventStats(event.getId()).orElse(null);
        Assert.assertNotNull(eventStats);
        Assert.assertEquals(ticket.getQty() / 2, eventStats.getBooked());
        Assert.assertEquals(0, eventStats.getAttended());

        eventBooking.setConfirmed(true);
        em.persist(eventBooking);

        eventStats = eventDao.getEventStats(event.getId()).orElse(null);
        Assert.assertNotNull(eventStats);
        Assert.assertEquals(ticket.getQty() / 2, eventStats.getAttended());
    }

    @Test
    public void testGetUserEvents() {
        Event event = TestUtils.createEvent(em);
        List<Event> events = eventDao.getUserEvents(TestUtils.USER1.getId(), 1);
        Assert.assertNotNull(events);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(event.getName(), events.get(0).getName());
        Assert.assertEquals(TestUtils.USER1.getUsername(), events.get(0).getOrganizer().getUsername());
        Assert.assertEquals(TestUtils.USER1.getId(), events.get(0).getOrganizer().getId());
    }

    @Test
    public void testGetPopular() {
        Ticket ticket1 = TestUtils.createTicket(em);
        Ticket ticket2 = TestUtils.createTicket(em);
        Event event1 = ticket1.getEvent();
        Event event2 = ticket2.getEvent();
        ticket1.setBooked(ticket1.getQty() / 2);
        em.persist(ticket1);
        EventBooking eventBooking1 = new EventBooking(TestUtils.USER2, event1, null, TestUtils.EVENT_BOOKING_CODE);
        em.persist(eventBooking1);
        TicketBooking ticketBooking1 = new TicketBooking(ticket1, ticket1.getQty() / 2, eventBooking1);
        em.persist(ticketBooking1);

        List<Event> events = eventDao.getPopularEvents(event1.getId());
        Assert.assertNotNull(events);
        Assert.assertEquals(0, events.size());

        ticket2.setBooked(ticket2.getQty() / 2);
        em.persist(ticket2);
        EventBooking eventBooking2 = new EventBooking(TestUtils.USER2, event2, null, TestUtils.EVENT_BOOKING_CODE2);
        em.persist(eventBooking2);
        TicketBooking ticketBooking2 = new TicketBooking(ticket2, ticket2.getQty() / 2, eventBooking2);
        em.persist(ticketBooking2);

        events = eventDao.getPopularEvents(event1.getId());
        Assert.assertNotNull(events);
        Assert.assertEquals(1, events.size());
        Assert.assertEquals(event2.getId(), events.get(0).getId());
    }
}
