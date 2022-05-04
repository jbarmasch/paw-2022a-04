package ar.edu.itba.paw.model;

import java.util.List;

public class EventBooking {
    private long userId;
    private Event event;
    private List<TicketBooking> bookings;

    public EventBooking(long userId, Event event, List<TicketBooking> bookings) {
        this.userId = userId;
        this.event = event;
        this.bookings = bookings;
    }

    public long getUserId() {
        return userId;
    }

    public Event getEvent() {
        return event;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<TicketBooking> getBookings() {
        return bookings;
    }

    public void setBookings(List<TicketBooking> bookings) {
        this.bookings = bookings;
    }
}
