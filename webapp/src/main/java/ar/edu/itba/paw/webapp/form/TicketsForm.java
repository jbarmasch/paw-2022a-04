package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.Booking;
import ar.edu.itba.paw.model.Ticket;

import javax.validation.constraints.NotNull;
import java.util.List;

public class TicketsForm {
    @NotNull
    private List<TicketForm> tickets;
    @NotNull
    private Integer ticketQty;

    public List<TicketForm> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketForm> tickets) {
        this.tickets = tickets;
    }

    public Integer getTicketQty() {
        return ticketQty;
    }

    public void setTicketQty(Integer ticketQty) {
        this.ticketQty = ticketQty;
    }
}
