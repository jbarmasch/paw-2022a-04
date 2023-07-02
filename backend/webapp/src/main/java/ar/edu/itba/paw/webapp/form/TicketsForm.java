package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validations.BookingList;

//import javax.validation.Valid;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;
import java.util.List;

public class TicketsForm {
    @NotNull
    @BookingList
//    @Valid
    // TODO: NO USAR LISTAS, NO SE VALIDAN
    private List<TicketForm> tickets;

    public List<TicketForm> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketForm> tickets) {
        this.tickets = tickets;
    }
}

