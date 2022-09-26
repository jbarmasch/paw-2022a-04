package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.BookingFailedException;
import ar.edu.itba.paw.exceptions.CancelBookingFailedException;
import ar.edu.itba.paw.exceptions.IllegalTicketException;
import ar.edu.itba.paw.model.BookingId;
import ar.edu.itba.paw.model.EventBooking;
import ar.edu.itba.paw.model.Ticket;
import ar.edu.itba.paw.model.TicketBooking;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class EventBookingJpaDao implements EventBookingDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<EventBooking> getAllBookingsFromUser(long userId, int page) {
        final TypedQuery<EventBooking> query = em.createQuery("from EventBooking as eb where eb.user.id = :userid order by eb.event.date DESC", EventBooking.class);
        query.setParameter("userid", userId);
        return query.getResultList();
    }

    @Override
    public Optional<EventBooking> getBookingFromUser(long userId, long eventId) {
        final TypedQuery<EventBooking> query = em.createQuery("from EventBooking as eb where eb.user.id = :userid AND eb.event.id = :eventid", EventBooking.class);
        query.setParameter("userid", userId);
        query.setParameter("eventid", eventId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<EventBooking> getBooking(String code) {
        final TypedQuery<EventBooking> query = em.createQuery("from EventBooking as eb where eb.code = :code", EventBooking.class);
        query.setParameter("code", code);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public EventBooking book(EventBooking booking) {
        if (booking.getEvent().isFinished())
            return null;

        final TypedQuery<EventBooking> query = em.createQuery("from EventBooking as eb where eb.user.id = :userid and eb.event.id = :eventid", EventBooking.class);
        query.setParameter("eventid", booking.getEvent().getId());
        query.setParameter("userid", booking.getUser().getId());
        EventBooking eventBooking = query.getResultList().stream().findFirst().orElse(null);

        if (eventBooking == null) {
            for (TicketBooking ticketBooking : booking.getTicketBookings()) {
                if (ticketBooking.getQty() != null && ticketBooking.getQty() > 0) {
                    Ticket ticket = ticketBooking.getTicket();
                    if (!ticket.book(ticketBooking.getQty())) {
                        throw new BookingFailedException();
                    }
                    em.persist(ticket);
                    em.persist(ticketBooking);
                }
            }
            em.persist(booking);
            return booking;
        } else {
            for (TicketBooking ticketBooking : booking.getTicketBookings()) {
                if (ticketBooking.getTicket().getEvent().getId() != booking.getEvent().getId()) {
                    throw new IllegalTicketException();
                }

                TicketBooking ticketAux = em.find(TicketBooking.class, new BookingId(ticketBooking.getTicket(), eventBooking));
                if (ticketAux != null) {
                    Ticket ticket = ticketAux.getTicket();
                    if (!ticket.book(ticketBooking.getQty())) {
                        throw new BookingFailedException();
                    }
                    em.persist(ticket);
                    if (ticketAux.getQty() != null && ticketAux.getQty() > 0) {
                        ticketAux.setQty(ticketAux.getQty() + ticketBooking.getQty());
                        em.persist(ticketAux);
                    }
                } else {
                    Ticket ticket = ticketBooking.getTicket();
                    if (!ticket.book(ticketBooking.getQty())) {
                        throw new BookingFailedException();
                    }
                    em.persist(ticket);
                    ticketBooking.setEventBooking(eventBooking);
                    eventBooking.addBooking(ticketBooking);
                    em.persist(ticketBooking);
                }
            }
            em.persist(eventBooking);

            return eventBooking;
        }
    }

    @Override
    public boolean cancelBooking(EventBooking booking) {
        if (booking.getEvent().isFinished())
            return false;

        final TypedQuery<EventBooking> query = em.createQuery("from EventBooking as eb where eb.user.id = :userid and eb.event.id = :eventid", EventBooking.class);
        query.setParameter("userid", booking.getUser().getId());
        query.setParameter("eventid", booking.getEvent().getId());
        EventBooking eventBooking = query.getResultList().stream().findFirst().orElse(null);

        if (eventBooking == null) {
            throw new CancelBookingFailedException();
        }

        for (TicketBooking ticketBooking : booking.getTicketBookings()) {
            TicketBooking tb = em.find(TicketBooking.class, new BookingId(ticketBooking.getTicket(), eventBooking));
            if (tb == null) {
                throw new CancelBookingFailedException();
            }
            Ticket ticket = ticketBooking.getTicket();
            if (!ticket.cancelBooking(ticketBooking.getQty())) {
                throw new CancelBookingFailedException();
            }
            em.persist(ticket);
            tb.setQty(tb.getQty() - ticketBooking.getQty());
            em.persist(tb);
        }
        em.persist(eventBooking);
        return true;
    }

    @Override
    public void confirmBooking(EventBooking eventBooking) {
        eventBooking.setConfirmed(true);
        em.persist(eventBooking);
    }
}
