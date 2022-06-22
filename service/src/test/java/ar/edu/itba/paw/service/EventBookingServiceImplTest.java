package ar.edu.itba.paw.service;
import ar.edu.itba.paw.exceptions.AlreadyMaxTicketsException;
import ar.edu.itba.paw.exceptions.SurpassedMaxTicketsException;
import ar.edu.itba.paw.exceptions.TicketNotBookedException;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.EventBookingDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class EventBookingServiceImplTest {
    @InjectMocks
    private EventBookingServiceImpl eventBookingService;

    private static final User USER = new User(null, null, null, null, null);
    private static final Event EVENT = new Event(1, null, null, null, null, null, null, null, null, null, null, null, null);
    private static final Ticket TICKET = new Ticket(1, null, null, 10, EVENT, null, null, 6);
    private static final Ticket DIFFERENT_TICKET = new Ticket(2, null, null, 10, EVENT, null, null, 6);
    private static final EventBooking EVENT_BOOKING = new EventBooking(USER, EVENT, null, "");
    private static final EventBooking DIFFERENT_EVENT_BOOKING = new EventBooking(USER, EVENT, null, "");
    private static final TicketBooking DIFFERENT_TICKET_BOOKING = new TicketBooking(DIFFERENT_TICKET, 5, DIFFERENT_EVENT_BOOKING);
    private static final TicketBooking ACCEPTED_TICKET_BOOKING = new TicketBooking(TICKET, 5, EVENT_BOOKING);
    private static final TicketBooking DECLINED_TICKET_BOOKING = new TicketBooking(TICKET, 7, EVENT_BOOKING);

    @Mock
    private EventBookingDao mockDao;
    @Mock
    private CodeService mockCodeService;

    @Test
    public void testBookOk() {
        EVENT.setTickets(Arrays.asList(TICKET, DIFFERENT_TICKET));
        Mockito.when(mockCodeService.getCode(Mockito.eq(USER.getId() + ":" + EVENT.getId()))).thenReturn("");
        EVENT_BOOKING.setTicketBookings(Collections.singletonList(ACCEPTED_TICKET_BOOKING));
        try {
            eventBookingService.book(EVENT_BOOKING, "", null);
        } catch (AlreadyMaxTicketsException | SurpassedMaxTicketsException e) {
            throw new RuntimeException();
        }
    }

    @Test(expected = RuntimeException.class)
    public void testBookFails() {
        EVENT.setTickets(Arrays.asList(TICKET, DIFFERENT_TICKET));
        EVENT_BOOKING.setTicketBookings(Collections.singletonList(DECLINED_TICKET_BOOKING));
        try {
            eventBookingService.book(EVENT_BOOKING, "", null);
        } catch (AlreadyMaxTicketsException | SurpassedMaxTicketsException e) {
            throw new RuntimeException();
        }
    }

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

