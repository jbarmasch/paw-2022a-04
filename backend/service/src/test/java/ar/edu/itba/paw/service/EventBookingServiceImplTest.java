package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.AlreadyMaxTicketsException;
import ar.edu.itba.paw.exceptions.SurpassedMaxTicketsException;
import ar.edu.itba.paw.exceptions.TicketNotBookedException;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.EventBookingDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class EventBookingServiceImplTest {
    private static final User USER = new User(null, null, null, null, null);
    private static final Event EVENT = new Event(1, null, null, null, null, null, null, null, null, null, null, null, null);
    private static final Ticket TICKET = new Ticket(1, null, null, 10, EVENT, null, null, 6);
    private static final Ticket DIFFERENT_TICKET = new Ticket(2, null, null, 10, EVENT, null, null, 6);
    private static final EventBooking EVENT_BOOKING = new EventBooking(USER, EVENT, null, "");
    private static final TicketBooking ACCEPTED_TICKET_BOOKING = new TicketBooking(TICKET, 5, EVENT_BOOKING);
    private static final TicketBooking DECLINED_TICKET_BOOKING = new TicketBooking(TICKET, 7, EVENT_BOOKING);
    private static final EventBooking DIFFERENT_EVENT_BOOKING = new EventBooking(USER, EVENT, null, "");
    private static final TicketBooking DIFFERENT_TICKET_BOOKING = new TicketBooking(DIFFERENT_TICKET, 5, DIFFERENT_EVENT_BOOKING);
    @InjectMocks
    private EventBookingServiceImpl eventBookingService;

    //    @Rule
//    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private EventBookingDao mockDao;
    @Mock
    private CodeService mockCodeService;

    @Test
    public void testBookOk() throws AlreadyMaxTicketsException, SurpassedMaxTicketsException {
        EVENT.setTickets(Arrays.asList(TICKET, DIFFERENT_TICKET));
        Mockito.when(mockCodeService.getCode(Mockito.eq(USER.getId() + ":" + EVENT.getId()))).thenReturn("");
        EVENT_BOOKING.setTicketBookings(Collections.singletonList(ACCEPTED_TICKET_BOOKING));
        eventBookingService.book(EVENT_BOOKING, "", null);
    }

    @Test(expected = SurpassedMaxTicketsException.class)
    public void testBookFails() throws AlreadyMaxTicketsException, SurpassedMaxTicketsException {
        EVENT.setTickets(Arrays.asList(TICKET, DIFFERENT_TICKET));
        EVENT_BOOKING.setTicketBookings(Collections.singletonList(DECLINED_TICKET_BOOKING));
        eventBookingService.book(EVENT_BOOKING, "", null);
    }

//    @Test
//    public void testBookFails() throws AlreadyMaxTicketsException, SurpassedMaxTicketsException {
//        EVENT.setTickets(Arrays.asList(TICKET, DIFFERENT_TICKET));
//        EVENT_BOOKING.setTicketBookings(Collections.singletonList(DECLINED_TICKET_BOOKING));
//        thrown.expect(SurpassedMaxTicketsException.class);
//        eventBookingService.book(EVENT_BOOKING, "", null);
//    }

    @Test
    public void testCancelBookOk() {
        Mockito.when(mockDao.getBookingFromUser(Mockito.eq(EVENT_BOOKING.getUser().getId()), Mockito.eq(EVENT_BOOKING.getEvent().getId()))).thenReturn(Optional.of(EVENT_BOOKING));
        EVENT.setTickets(Arrays.asList(TICKET, DIFFERENT_TICKET));
        EVENT_BOOKING.setTicketBookings(Collections.singletonList(ACCEPTED_TICKET_BOOKING));
        try {
            eventBookingService.cancelBooking(EVENT_BOOKING, null);
        } catch (SurpassedMaxTicketsException | TicketNotBookedException e) {
            throw new RuntimeException();
        }
    }

    @Test(expected = RuntimeException.class)
    public void testCancelBookFails() {
        Mockito.when(mockDao.getBookingFromUser(Mockito.eq(EVENT_BOOKING.getUser().getId()), Mockito.eq(EVENT_BOOKING.getEvent().getId()))).thenReturn(Optional.of(EVENT_BOOKING));
        EVENT.setTickets(Arrays.asList(TICKET, DIFFERENT_TICKET));
        EVENT_BOOKING.setTicketBookings(Collections.singletonList(DIFFERENT_TICKET_BOOKING));
        try {
            eventBookingService.cancelBooking(DIFFERENT_EVENT_BOOKING, null);
        } catch (TicketNotBookedException | SurpassedMaxTicketsException e) {
            throw new RuntimeException();
        }
    }
}
