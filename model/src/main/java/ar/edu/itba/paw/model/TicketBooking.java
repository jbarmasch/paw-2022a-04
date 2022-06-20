package ar.edu.itba.paw.model;

import org.hibernate.annotations.Check;

import javax.persistence.*;

@Entity
@Table(name = "ticketbookings")
@IdClass(BookingId.class)
@Check(constraints = "qty >= 0")
public class TicketBooking {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticketid")
    private Ticket ticket;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private EventBooking eventBooking;

    private Integer qty;

    public TicketBooking(Ticket ticket, Integer qty, EventBooking eventBooking) {
        this.ticket = ticket;
        this.qty = qty;
        this.eventBooking = eventBooking;
    }

    public TicketBooking() {}

    public long getId() {
        return eventBooking.getId();
    }

    public EventBooking getEventBooking() {
        return eventBooking;
    }

    public void setEventBooking(EventBooking eventBooking) {
        this.eventBooking = eventBooking;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
