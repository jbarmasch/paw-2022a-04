package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Ticket;
import ar.edu.itba.paw.model.TicketStats;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TicketJpaDao implements TicketDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Ticket addTicket(Event event, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until, Integer maxPerUser) {
        Ticket newTicket = new Ticket(ticketName, price, qty, event, starting, until, maxPerUser);
        if (event.getTickets() == null)
            event.setTickets(new ArrayList<>());
        event.addTicket(newTicket);
        em.persist(event);
        em.persist(newTicket);
        return newTicket;
    }

    @Override
    public Optional<Ticket> getTicketByName(String ticketName) {
        final TypedQuery<Ticket> query = em.createQuery("from Ticket as t where t.ticketName = :ticketname", Ticket.class);
        query.setParameter("ticketname", ticketName);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<Ticket> getTicketById(long ticketId) {
        return Optional.ofNullable(em.find(Ticket.class, ticketId));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Ticket> getTickets(long id) {
        Query idQuery = em.createNativeQuery("SELECT ticketid FROM tickets WHERE eventid = :eventid AND qty > 0");
        idQuery.setParameter("eventid", id);
        final List<Long> ids = (List<Long>) idQuery.getResultList().stream().map(o -> ((Number) o).longValue()).collect(Collectors.toList());
        if (ids.isEmpty())
            return Collections.emptyList();
        final TypedQuery<Ticket> query = em.createQuery("from Ticket where ticketid IN :ids", Ticket.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public void updateTicket(Ticket ticket, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until, Integer maxPerUser) {
        ticket.setTicketName(ticketName);
        ticket.setPrice(price);
        ticket.setQty(qty);
        ticket.setStarting(starting);
        ticket.setUntil(until);
        ticket.setMaxPerUser(maxPerUser);
        em.persist(ticket);
    }

    @Override
    public void deleteTicket(long ticketId) {
        em.remove(em.find(Ticket.class, ticketId));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TicketStats> getTicketStats(long id) {
        Query query = em.createNativeQuery("SELECT ticketName, CAST(realqty AS numeric) / COALESCE(NULLIF(booked, 0), 1) AS attendance, CAST(realqty AS numeric) / " +
                "COALESCE(NULLIF(qty, 0), 1) AS saleRatio, price, realqty, qty, price * realqty AS income, booked FROM (SELECT booked, price, SUM(CASE WHEN confirmed " +
                "THEN tb.qty ELSE 0 END) AS realqty, t.qty AS qty, t.name AS ticketName FROM events e LEFT JOIN tickets t on e.eventid = t.eventid LEFT JOIN ticketbookings tb on " +
                "t.ticketId = tb.ticketId JOIN eventbookings eb on tb.id = eb.id WHERE e.eventid = :eventid GROUP BY t.ticketid) AS aux");
        query.setParameter("eventid", id);
        List<Object[]> resultSet = query.getResultList();
        if (resultSet.isEmpty())
            return Collections.emptyList();
        List<TicketStats> ticketStats = new ArrayList<>();
        for (Object[] result : resultSet) {
            if (result != null) {
                ticketStats.add(new TicketStats((String) result[0], ((Number) result[1]).doubleValue(), ((Number) result[2]).doubleValue(), ((Number) result[3]).doubleValue(),
                        ((Number) result[4]).intValue(), ((Number) result[5]).intValue(), ((Number) result[6]).doubleValue(), ((Number) result[7]).intValue()));
            }
        }
        return ticketStats;
    }
}
