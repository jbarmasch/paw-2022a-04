package ar.edu.itba.paw.model;

public class TicketBooking {
    private int qty;
    private Ticket ticket;

    public TicketBooking(int qty, Ticket ticket) {
        this.qty = qty;
        this.ticket = ticket;
    }

    public int getQty() {
        return qty;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
