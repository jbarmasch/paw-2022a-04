package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.DateRangeException;
import ar.edu.itba.paw.exceptions.TicketUnderflowException;
import ar.edu.itba.paw.model.Ticket;
import ar.edu.itba.paw.persistence.TicketDao;
import ar.edu.itba.paw.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketDao ticketDao;

    @Transactional
    @Override
    public void addTicket(Event event, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until) {
        if (starting != null && starting.isAfter(event.getDate()))
            throw new DateRangeException(starting, null);
        if (until != null && until.isAfter(event.getDate()))
            throw new DateRangeException(null, until);
        ticketDao.addTicket(event, ticketName, price, qty, starting, until);
    }

    @Override
    public Optional<Ticket> getTicketById(long ticketId) {
        return ticketDao.getTicketById(ticketId);
    }

    @Transactional
    @Override
    public void updateTicket(Ticket ticket, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until) {
        if (qty < ticket.getBooked())
            throw new TicketUnderflowException();
        ticketDao.updateTicket(ticket, ticketName, price, qty, starting, until);
    }

    @Transactional
    @Override
    public void deleteTicket(long ticketId) {
        ticketDao.deleteTicket(ticketId);
    }
}
