package ar.edu.itba.paw.model;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    static public List<TicketBooking> getTicketBookings(Array ids, Array qtys, Array names) throws SQLException {
        List<TicketBooking> ticketBookings = new ArrayList<>();
        Integer[] idsAux = (Integer[]) ids.getArray();
        Integer[] qtysAux = (Integer[]) qtys.getArray();
        String[] namesAux = (String[]) names.getArray();
        if (idsAux[0] != null && qtysAux[0] != null && namesAux[0] != null) {
            for (int i = 0; i < idsAux.length; i++) {
                ticketBookings.add(new TicketBooking(qtysAux[i], new Ticket(idsAux[i], namesAux[i])));
            }
        }
        return ticketBookings;
    }
}
