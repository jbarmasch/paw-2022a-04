package ar.edu.itba.paw.model;

public class Booking {
    private long userId;
    private Event event;
    private int qty;
    private long ticketId;
    private String ticketName;

    public Booking(long userId, Event event, int qty, long ticketId, String ticketName) {
        this.userId = userId;
        this.event = event;
        this.qty = qty;
        this.ticketId = ticketId;
        this.ticketName = ticketName;
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

    public long getTicketId() {
        return ticketId;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }
}
