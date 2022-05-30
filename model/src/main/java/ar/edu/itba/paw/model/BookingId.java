package ar.edu.itba.paw.model;

import java.io.Serializable;
import java.util.Objects;

public class BookingId implements Serializable {
    private Ticket ticket;
    private EventBooking eventBooking;

    BookingId() {}

    public BookingId(Ticket ticket, EventBooking eventBooking) {
        this.ticket = ticket;
        this.eventBooking = eventBooking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingId)) return false;
        BookingId bookingId = (BookingId) o;
        return eventBooking == bookingId.eventBooking && Objects.equals(ticket, bookingId.ticket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticket, eventBooking);
    }
}
