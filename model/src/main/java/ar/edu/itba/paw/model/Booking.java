package ar.edu.itba.paw.model;

public class Booking {
    private Integer qty;
    private long ticketId;
    private String ticketName;
    private double price;

    public Booking() {}

    public Booking(Integer qty, long ticketId) {
        this.qty = qty;
        this.ticketId = ticketId;
    }

    public Booking(Integer qty, long ticketId, String ticketName) {
        this.qty = qty;
        this.ticketId = ticketId;
        this.ticketName = ticketName;
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

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
