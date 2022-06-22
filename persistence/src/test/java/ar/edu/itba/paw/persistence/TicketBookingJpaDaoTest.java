package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.EventBooking;
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
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TicketBookingJpaDaoTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private TicketBookingJpaDao ticketBookingJpaDao;

    @Before
    public void setUp() {
        TestUtils.deleteTables(em);
        TestUtils.setUpEventBooking(em);
    }

    @Test
    public void testGetBookingsForTicket() {
        EventBooking eventBooking = new EventBooking(TestUtils.USER2, TestUtils.EVENT, null, TestUtils.EVENT_BOOKING_CODE);
        em.persist(eventBooking);
        TicketBooking ticketBooking = new TicketBooking(TestUtils.TICKET, TestUtils.TICKET.getQty() / 2, eventBooking);
        em.persist(ticketBooking);
        List<TicketBooking> ticketBookingList = ticketBookingJpaDao.getBookingsForTicket(TestUtils.TICKET.getId());
        Assert.assertNotNull(ticketBookingList);
        Assert.assertEquals(1, ticketBookingList.size());
        Assert.assertEquals(TestUtils.TICKET.getId(), ticketBookingList.get(0).getTicket().getId());
    }
}
