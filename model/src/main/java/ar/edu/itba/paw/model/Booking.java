package ar.edu.itba.paw.model;

public class Booking {
    private long userId;
    private Event event;
    private int qty;

    public Booking(long userId, Event event, int qty) {
        this.userId = userId;
        this.event = event;
        this.qty = qty;
    }

    public long getUserId() {
        return userId;
    }

    public Event getEvent() {
        return event;
    }

    public int getQty() {
        return qty;
    }
}
