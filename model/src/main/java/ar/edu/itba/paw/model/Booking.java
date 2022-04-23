package ar.edu.itba.paw.model;

public class Booking {
    long userId;
    Event event;
    int qty;

    public Booking(long userId, Event event, int qty) {
        this.userId = userId;
        this.event = event;
        this.qty = qty;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
