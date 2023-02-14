package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.EventBooking;
import ar.edu.itba.paw.model.EventBookingList;
import ar.edu.itba.paw.model.TicketBooking;
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
import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class EventBookingJpaDaoTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private EventBookingJpaDao eventBookingDao;

    @Before
    public void setUp() {
        TestUtils.setUpEventBooking(em);
    }

//    @Test
//    public void testGetAllBookingsFromUser() {
//        EventBooking eventBooking = new EventBooking(TestUtils.USER2, TestUtils.EVENT, null, TestUtils.EVENT_BOOKING_CODE);
//        em.persist(eventBooking);
//        EventBookingList eventBookings = eventBookingDao.getAllBookingsFromUser(TestUtils.USER2.getId(), 1);
//        Assert.assertNotNull(eventBookings.getBookingList());
//        Assert.assertFalse(eventBookings.getBookingList().isEmpty());
//        Assert.assertEquals(1, eventBookings.getBookingList().size());
//        Assert.assertEquals(eventBooking.getId(), eventBookings.getBookingList().get(0).getId());
//    }

    @Test
    public void testGetBookingFromUser() {
        EventBooking eventBooking = new EventBooking(TestUtils.USER2, TestUtils.EVENT, null, TestUtils.EVENT_BOOKING_CODE);
        em.persist(eventBooking);
        TicketBooking ticketBooking = new TicketBooking(TestUtils.TICKET, TestUtils.TICKET.getQty() / 2, eventBooking);
        em.persist(ticketBooking);
        TestUtils.TICKET.setBooked(TestUtils.TICKET.getQty() / 2);
        em.persist(TestUtils.TICKET);
        em.persist(eventBooking);

        EventBooking eventBookingGotten = eventBookingDao.getBookingFromUser(TestUtils.USER2.getId(), eventBooking.getEvent().getId()).orElse(null);
        Assert.assertNotNull(eventBookingGotten);
        Assert.assertEquals(eventBookingGotten.getId(), eventBookingGotten.getId());
    }

    @Test
    public void testGetBooking() {
        EventBooking eventBooking = new EventBooking(TestUtils.USER2, TestUtils.EVENT, null, TestUtils.EVENT_BOOKING_CODE);
        em.persist(eventBooking);
        EventBooking eventBookingGotten = eventBookingDao.getBooking(eventBooking.getCode()).orElse(null);
        Assert.assertNotNull(eventBookingGotten);
        Assert.assertEquals(eventBookingGotten.getId(), eventBookingGotten.getId());
    }

    @Test
    public void testBook() {
        TicketBooking ticketBooking = new TicketBooking(TestUtils.TICKET, TestUtils.TICKET.getQty() / 2, null);
        EventBooking eventBooking = new EventBooking(TestUtils.USER2, TestUtils.EVENT, Collections.singletonList(ticketBooking), TestUtils.EVENT_BOOKING_CODE);
        int preBook = TestUtils.TICKET.getBooked();
        eventBookingDao.book(eventBooking);
        EventBooking eventBookingGotten = em.find(EventBooking.class, eventBooking.getId());
        Assert.assertNotNull(eventBookingGotten);
        Assert.assertEquals(eventBooking.getId(), eventBookingGotten.getId());
        Assert.assertNotEquals(preBook, (int) TestUtils.TICKET.getBooked());
        Assert.assertEquals(TestUtils.TICKET.getQty() / 2, (int) TestUtils.TICKET.getBooked());
    }

    @Test
    public void testCancelBooking() {
        EventBooking eventBooking = new EventBooking(TestUtils.USER2, TestUtils.EVENT, null, TestUtils.EVENT_BOOKING_CODE);
        em.persist(eventBooking);
        TicketBooking ticketBooking = new TicketBooking(TestUtils.TICKET, TestUtils.TICKET.getQty() / 2, eventBooking);
        em.persist(ticketBooking);
        TestUtils.TICKET.setBooked(TestUtils.TICKET.getQty() / 2);
        em.persist(TestUtils.TICKET);

        TicketBooking ticketBookingCancel = new TicketBooking(TestUtils.TICKET, TestUtils.TICKET.getQty() / 2, null);
        eventBooking.setTicketBookings(Collections.singletonList(ticketBookingCancel));
        boolean cancelBooking = eventBookingDao.cancelBooking(eventBooking);

        Assert.assertNotNull(eventBooking);
        Assert.assertTrue(cancelBooking);
        Assert.assertNotEquals(TestUtils.TICKET.getQty() / 2, (int) TestUtils.TICKET.getBooked());
        Assert.assertEquals(0, (int) TestUtils.TICKET.getBooked());
    }

    @Test
    public void testConfirmBooking() {
        EventBooking eventBooking = new EventBooking(TestUtils.USER2, TestUtils.EVENT, null, TestUtils.EVENT_BOOKING_CODE);
        em.persist(eventBooking);
        eventBookingDao.confirmBooking(eventBooking);
        Assert.assertTrue(eventBooking.isConfirmed());
    }
}
