package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Ticket;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class TicketJpaDao implements TicketDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void addTicket(Event event, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until) {
        Ticket newTicket = new Ticket(ticketName, price, qty, event, starting, until);
        event.addTicket(newTicket);
        em.persist(event);
        em.persist(newTicket);
    }

    @Override
    public Optional<Ticket> getTicketById(long ticketId) {
        return Optional.ofNullable(em.find(Ticket.class, ticketId));
    }

    @Override
    public void updateTicket(Ticket ticket, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until) {
        ticket.setTicketName(ticketName);
        ticket.setPrice(price);
        ticket.setQty(qty);
        ticket.setStarting(starting);
        ticket.setUntil(until);
        em.persist(ticket);
    }

    @Override
    public void deleteTicket(long ticketId) {
        em.remove(em.find(Ticket.class, ticketId));
    }
}
