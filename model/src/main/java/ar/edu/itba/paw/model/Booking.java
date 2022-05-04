package ar.edu.itba.paw.model;

public class Booking {
    private int qty;
    private long ticketId;

    public Booking() {}

    public Booking(int qty, long ticketId) {
        this.qty = qty;
        this.ticketId = ticketId;
    }

    public int getQty() {
        return qty;
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }
}
