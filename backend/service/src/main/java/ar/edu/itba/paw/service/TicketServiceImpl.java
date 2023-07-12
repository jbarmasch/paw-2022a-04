package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Ticket;
import ar.edu.itba.paw.model.TicketBooking;
import ar.edu.itba.paw.model.TicketStats;
import ar.edu.itba.paw.persistence.TicketDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketDao ticketDao;
    @Autowired
    private TicketBookingService ticketBookingService;
    @Autowired
    private EventService eventService;
    @Autowired
    private MailService mailService;
    @Autowired
    private AuthService authService;

    @Transactional
    @Override
    public void addTicket(Event event, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until, Integer maxPerUser) {
        if (!(authService.isAuthenticated() && authService.isTheEventOrganizer(event))) {
            throw new ForbiddenAccessException();
        }
        if (event.isFinished()) {
            throw new EventFinishedException();
        }
        if (starting != null && starting.isAfter(event.getDate())) {
            throw new DateRangeStartingException();
        }
        if (until != null && until.isAfter(event.getDate())) {
            throw new DateRangeUntilException();
        }

//        if (ticketDao.getTicketByName(ticketName).isPresent()) {
//            throw new TicketConflictException();
//        }

        ticketDao.addTicket(event, ticketName, price, qty, starting, until, maxPerUser);
    }

    @Override
    public Optional<Ticket> getTicketById(long ticketId) {
        return ticketDao.getTicketById(ticketId);
    }

    @Override
    public List<Ticket> getTickets(long eventId) {
        return ticketDao.getTickets(eventId);
    }

    @Transactional
    @Override
    public void updateTicket(Ticket ticket, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until, Integer maxPerUser) {
        if (!(authService.isAuthenticated() && authService.isTheEventOrganizer(ticket.getEvent()))) {
            throw new ForbiddenAccessException();
        }
        if (ticket.getEvent().isFinished()) {
            throw new EventFinishedException();
        }

        if (qty < ticket.getBooked()) {
            throw new TicketUnderflowException();
        }
        if (starting != null && starting.isAfter(ticket.getEvent().getDate())) {
            throw new DateRangeStartingException();
        }
        if (until != null && until.isAfter(ticket.getEvent().getDate())) {
            throw new DateRangeUntilException();
        }

        ticketDao.updateTicket(ticket, ticketName, price, qty, starting, until, maxPerUser);
        eventService.checkSoldOut(ticket.getEvent().getId());
    }

    @Transactional
    @Override
    public void deleteTicket(long ticketId) {
        if (!(authService.isAuthenticated() && authService.isTheEventOrganizerFromTicket(ticketId))) {
            throw new ForbiddenAccessException();
        }

        List<TicketBooking> ticketBookings = ticketBookingService.getBookingsForTicket(ticketId);
        for (TicketBooking ticketBooking : ticketBookings) {
            mailService.sendCancelTicketMail(ticketBooking);
        }
        ticketDao.deleteTicket(ticketId);
    }

    @Override
    public List<TicketStats> getTicketStats(long id) {
        if (!(authService.isAuthenticated() && authService.isTheEventOrganizer(id))) {
            throw new ForbiddenAccessException();
        }

        return ticketDao.getTicketStats(id);
    }
}
