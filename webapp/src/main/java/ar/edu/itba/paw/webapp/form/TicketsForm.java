package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.Booking;
import ar.edu.itba.paw.model.Ticket;

import java.util.List;

public class TicketsForm {
    private List<Ticket> tickets;

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
