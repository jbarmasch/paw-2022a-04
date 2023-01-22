package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validations.BookingList;

import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;
import java.util.List;

public class TicketsForm {
    @NotNull
    @BookingList
    private List<TicketForm> tickets;

//    @NotNull
//    private Long eventId;

//    public Long getEventId() {
//        return eventId;
//    }
//
//    public void setEventId(Long eventId) {
//        this.eventId = eventId;
//    }

    public List<TicketForm> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketForm> tickets) {
        this.tickets = tickets;
    }
}

