package ar.edu.itba.paw.model;

public class Booking {
    private Integer qty;
    private long ticketId;

    public Booking() {}

    public Booking(Integer qty, long ticketId) {
        this.qty = qty;
        this.ticketId = ticketId;
    }

    public Integer getQty() {
        return qty;
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }
}
