package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.BookingFailedException;
import ar.edu.itba.paw.exceptions.CancelBookingFailedException;
import ar.edu.itba.paw.exceptions.IllegalTicketException;
import ar.edu.itba.paw.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class EventBookingJpaDao implements EventBookingDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public EventBookingList getAllBookingsFromUser(long userId, int page) {
        final TypedQuery<EventBooking> query = em.createQuery("from EventBooking as eb where eb.user.id = :userid order by eb.event.date DESC", EventBooking.class);
        query.setParameter("userid", userId);

        Query count = em.createNativeQuery("SELECT COUNT(DISTINCT eb.id) FROM eventbookings eb WHERE eb.userid = :userid");
        count.setParameter("userid", userId);
        return new EventBookingList(query.getResultList(), (int) Math.ceil((double) ((Number) count.getSingleResult()).intValue() / 6));
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
        // EventBooking booking = getBooking(code).orElse(null);

        if (booking == null) {
            // TODO: Change
            throw new CancelBookingFailedException();
        }

        if (booking.getEvent().isFinished())
            return false;

        for (TicketBooking ticketBooking : booking.getTicketBookings()) {
            ticketBooking.setQty(0);
            em.persist(ticketBooking);
        }
        em.persist(booking);
        return true;
    }

    @Override
    public void confirmBooking(EventBooking eventBooking) {
        eventBooking.setConfirmed(true);
        em.persist(eventBooking);
    }
}
