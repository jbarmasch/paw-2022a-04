package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.DateRangeException;
import ar.edu.itba.paw.exceptions.EventFinishedException;
import ar.edu.itba.paw.exceptions.TicketUnderflowException;
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
    private MailService mailService;

    @Transactional
    @Override
    public void addTicket(Event event, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until, Integer maxPerUser) throws DateRangeException {
        if (event.isFinished())
            throw new EventFinishedException();
        if (starting != null && starting.isAfter(event.getDate()))
            throw new DateRangeException(starting, null);
        if (until != null && until.isAfter(event.getDate()))
            throw new DateRangeException(null, until);
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
    public void updateTicket(Ticket ticket, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until, Integer maxPerUser) throws TicketUnderflowException {
        if (qty < ticket.getBooked())
            throw new TicketUnderflowException();
        ticketDao.updateTicket(ticket, ticketName, price, qty, starting, until, maxPerUser);
    }

    @Transactional
    @Override
    public void deleteTicket(long ticketId) {
        List<TicketBooking> ticketBookings = ticketBookingService.getBookingsForTicket(ticketId);
        for (TicketBooking ticketBooking : ticketBookings) {
            mailService.sendCancelTicketMail(ticketBooking);
        }
        ticketDao.deleteTicket(ticketId);
    }

    @Override
    public List<TicketStats> getTicketStats(long id) {
        return ticketDao.getTicketStats(id);
    }
}
