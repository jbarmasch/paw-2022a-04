package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Ticket;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TicketJpaDaoTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private TicketJpaDao ticketJpaDao;

    @Before
    public void setUp() {
        TestUtils.deleteTables(em);
        TestUtils.setUpTicket(em);
    }

    @Test
    public void addTicket() {
        Ticket ticket = ticketJpaDao.addTicket(TestUtils.EVENT, TestUtils.TICKET_NAME, TestUtils.TICKET_PRICE, TestUtils.TICKET_QTY, null, null, TestUtils.TICKET_MAX_PER_USER);
        Assert.assertNotNull(TestUtils.EVENT.getTickets());
        Assert.assertEquals(1, TestUtils.EVENT.getTickets().size());
        Assert.assertEquals(TestUtils.EVENT.getTickets().get(0).getId(), ticket.getId());
    }

    @Test
    public void getTicketById() {
        Ticket ticket = new Ticket(TestUtils.TICKET_NAME, TestUtils.TICKET_PRICE, TestUtils.TICKET_QTY, TestUtils.EVENT, TestUtils.TICKET_MAX_PER_USER);
        em.persist(ticket);
        Ticket ticketGotten = ticketJpaDao.getTicketById(ticket.getId()).orElse(null);
        Assert.assertNotNull(ticketGotten);
        Assert.assertEquals(ticket.getId(), ticketGotten.getId());
    }

    @Test
    public void updateTicket() {
        Ticket ticket = TestUtils.createTicket(em);
        ticket.setTicketName(TestUtils.TICKET_NAME2);
        ticket.setQty(100);
        em.persist(ticket);
        Assert.assertEquals(TestUtils.TICKET_NAME2, ticket.getTicketName());
        Assert.assertEquals(100, (int) ticket.getQty());
    }

    @Test
    public void deleteTicket() {
        Ticket ticket = new Ticket(TestUtils.TICKET_NAME, TestUtils.TICKET_PRICE, TestUtils.TICKET_QTY, TestUtils.EVENT, TestUtils.TICKET_MAX_PER_USER);
        em.persist(ticket);
        ticketJpaDao.deleteTicket(ticket.getId());
        Assert.assertNull(TestUtils.EVENT.getTickets());
    }
}
