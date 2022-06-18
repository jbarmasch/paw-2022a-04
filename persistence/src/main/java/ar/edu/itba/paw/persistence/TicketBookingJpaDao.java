package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.TicketBooking;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class TicketBookingJpaDao implements TicketBookingDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<TicketBooking> getBookingsForTicket(long ticketId) {
        final TypedQuery<TicketBooking> query = em.createQuery("from TicketBooking where ticketId = :ticketId", TicketBooking.class);
        query.setParameter("ticketId", ticketId);
        return query.getResultList();
    }
}
