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
import java.util.ArrayList;
import java.util.List;

//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserJpaDaoTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private EventJpaDao eventDao;
    @Autowired
    private UserJpaDao userDao;

    @Before
    public void setUp() {
        TestUtils.deleteTables(em);
        TestUtils.setUpUser(em);
    }

    @Test
    public void testCreate() {
        final User user = userDao.createUser(TestUtils.USERNAME2, TestUtils.PASSWORD2, TestUtils.MAIL2, TestUtils.LOCALE);

        Assert.assertNotNull(user);
        Assert.assertEquals(TestUtils.USERNAME2, user.getUsername());
        Assert.assertEquals(TestUtils.PASSWORD2, user.getPassword());
        Assert.assertEquals(TestUtils.MAIL2, user.getMail());
        Assert.assertEquals(2, TestUtils.countUsers(em));
    }

    @Test
    public void testCreateBouncer() {
        final User bouncer = userDao.createBouncer(TestUtils.PASSWORD);

        Assert.assertNotNull(bouncer);
        List<String> roleNames = new ArrayList<>();
        for (Role role : bouncer.getRoles()) {
            roleNames.add(role.getRoleName());
        }
        Assert.assertTrue(TestUtils.contains("ROLE_BOUNCER", roleNames));
        Assert.assertEquals("", bouncer.getUsername());
        Assert.assertEquals(TestUtils.PASSWORD, bouncer.getPassword());
        Assert.assertEquals("", bouncer.getMail());
        Assert.assertEquals(2, TestUtils.countUsers(em));
    }

    @Test
    public void testUpdateUser() {
        User user = TestUtils.createUser(em);

        userDao.updateUser(user.getId(), TestUtils.USERNAME, TestUtils.PASSWORD2, TestUtils.MAIL);

        Assert.assertEquals(TestUtils.USERNAME, user.getUsername());
        Assert.assertEquals(TestUtils.PASSWORD2, user.getPassword());
        Assert.assertEquals(TestUtils.MAIL, user.getMail());
    }

    @Test
    public void testMakeCreator() {
        User creator = TestUtils.createUser(em);

        userDao.makeCreator(creator);

        List<String> roleNames = new ArrayList<>();
        for (Role role : creator.getRoles()) {
            roleNames.add(role.getRoleName());
        }
        Assert.assertTrue(TestUtils.contains("ROLE_CREATOR", roleNames));
    }


    @Test
    public void testMakeBouncer() {
        User bouncer = TestUtils.createUser(em);

        userDao.makeBouncer(bouncer);

        List<String> roleNames = new ArrayList<>();
        for (Role role : bouncer.getRoles()) {
            roleNames.add(role.getRoleName());
        }
        Assert.assertTrue(TestUtils.contains("ROLE_BOUNCER", roleNames));
    }

    @Test
    public void testFindByUsername() {
        final User userById = userDao.findByUsername(TestUtils.USER1.getUsername()).orElse(null);

        Assert.assertNotNull(userById);
        Assert.assertEquals(TestUtils.USER1.getId(), userById.getId());
    }

    @Test
    public void testFindByMail() {
        final User testUser = userDao.findByMail(TestUtils.MAIL).orElse(null);

        Assert.assertNotNull(testUser);
        Assert.assertEquals(TestUtils.USER1.getId(), testUser.getId());
    }

    @Test
    public void testFindById() {
        final User testUser = userDao.getUserById(TestUtils.USER1.getId()).orElse(null);

        Assert.assertNotNull(testUser);
        Assert.assertEquals(TestUtils.USER1.getId(), testUser.getId());
    }

    @Test
    public void testCanRateTrue() {
        final Event pastEvent = TestUtils.createEvent(em);
        pastEvent.setDate(TestUtils.RATEABLE_DATE);
        pastEvent.setOrganizer(TestUtils.USER1);
        em.persist(pastEvent);
        Ticket ticket = TestUtils.createTicket(em);
        ticket.setEvent(pastEvent);
        em.persist(ticket);
        final User attendee = TestUtils.createUser(em);
        EventBooking eBooking = TestUtils.createEventBooking(em);
        eBooking.setEvent(pastEvent);
        eBooking.setUser(attendee);
        em.persist(eBooking);
        TicketBooking tBooking = new TicketBooking(ticket, 5, eBooking);
        em.persist(tBooking);

        boolean test = userDao.canRate(TestUtils.USER1.getId(), attendee.getId());

        Assert.assertTrue(test);
    }

    @Test
    public void testCanRateFalseDate() {
        final Event pastEvent = TestUtils.createEvent(em);
        pastEvent.setDate(TestUtils.EVENT_DATE_BEFORE);
        pastEvent.setOrganizer(TestUtils.USER1);
        em.persist(pastEvent);
        Ticket ticket = TestUtils.createTicket(em);
        ticket.setEvent(pastEvent);
        em.persist(ticket);
        final User attendee = TestUtils.createUser(em);
        EventBooking eBooking = TestUtils.createEventBooking(em);
        eBooking.setEvent(pastEvent);
        eBooking.setUser(attendee);
        em.persist(eBooking);
        TicketBooking tBooking = new TicketBooking(ticket, 5, eBooking);
        em.persist(tBooking);

        boolean test = userDao.canRate(TestUtils.USER1.getId(), attendee.getId());

        Assert.assertFalse(test);
    }

    @Test
    public void testCanRateFalseBooking() {
        final Event pastEvent = TestUtils.createEvent(em);
        pastEvent.setDate(TestUtils.RATEABLE_DATE);
        pastEvent.setOrganizer(TestUtils.USER1);
        em.persist(pastEvent);
        Ticket ticket = TestUtils.createTicket(em);
        ticket.setEvent(pastEvent);
        em.persist(ticket);
        final User notAttendee = TestUtils.createUser(em);

        boolean test = userDao.canRate(TestUtils.USER1.getId(), notAttendee.getId());

        Assert.assertFalse(test);
    }

    @Test
    public void testRate() {
        final Event pastEvent = TestUtils.createEvent(em);
        pastEvent.setDate(TestUtils.RATEABLE_DATE);
        pastEvent.setOrganizer(TestUtils.USER1);
        em.persist(pastEvent);
        Ticket ticket = TestUtils.createTicket(em);
        ticket.setEvent(pastEvent);
        em.persist(ticket);
        User attendee = TestUtils.createUser(em);
        EventBooking eBooking = TestUtils.createEventBooking(em);
        eBooking.setEvent(pastEvent);
        eBooking.setUser(attendee);
        em.persist(eBooking);
        TicketBooking tBooking = new TicketBooking(ticket, 5, eBooking);
        em.persist(tBooking);

        userDao.rateUser(attendee.getId(), TestUtils.USER1.getId(), 3);

        Rating rating = em.find(Rating.class, new RatingId(attendee, TestUtils.USER1));
        Assert.assertEquals(3, rating.getRating());
        Assert.assertEquals(TestUtils.USER1.getId(), rating.getOrganizer().getId());
        Assert.assertEquals(attendee.getId(), rating.getUser().getId());
    }

    //@Test
    //public void testGetOrganizerStats() {
    //    final Event pastEvent = TestUtils.createEvent(em);
    //    pastEvent.setDate(TestUtils.RATEABLE_DATE);
    //    pastEvent.setOrganizer(TestUtils.USER1);
    //    em.persist(pastEvent);
    //    Ticket ticket = TestUtils.createTicket(em);
    //    ticket.setEvent(pastEvent);
    //    em.persist(ticket);
    //    User attendee = TestUtils.createUser(em);
    //    EventBooking eBooking = TestUtils.createEventBooking(em);
    //    eBooking.setEvent(pastEvent);
    //    eBooking.setUser(attendee);
    //    eBooking.setConfirmed(true);
    //    em.persist(eBooking);
    //    TicketBooking tBooking = new TicketBooking(ticket, TestUtils.BOOKING_QTY, eBooking);
    //    em.persist(tBooking);
//
    //    OrganizerStats organizerStats = userDao.getOrganizerStats(TestUtils.USER1.getId()).orElse(null);
//
    //    Assert.assertNotNull(organizerStats);
    //    Assert.assertEquals(1, organizerStats.getEventsCreated());
    //    Assert.assertEquals(TestUtils.BOOKING_QTY, organizerStats.getBookingsGotten());
    //    Assert.assertEquals(pastEvent.getId(), organizerStats.getPopularEvent().getId());
    //    Assert.assertEquals(TestUtils.TICKET_PRICE * TestUtils.BOOKING_QTY, organizerStats.getIncome(), 0.0001);
    //    Assert.assertEquals( (double) TestUtils.BOOKING_QTY / TestUtils.TICKET_QTY, organizerStats.getAttendance(), 0.0001);
    //}

}