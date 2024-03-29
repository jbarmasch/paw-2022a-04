package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.TicketBooking;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class TicketBookingDto {
    private TicketDto ticket;
    private Integer qty;

    private URI self;
    private URI user;
    private URI event;

    public static TicketBookingDto fromBooking(final UriInfo uriInfo, final TicketBooking ticketBooking) {
        final TicketBookingDto dto = new TicketBookingDto();

        dto.ticket = TicketDto.fromTicket(uriInfo, ticketBooking.getTicket());
        dto.qty = ticketBooking.getQty();

        final UriBuilder eventUriBuilder = uriInfo.getBaseUriBuilder().
                path("api/tickets").path(String.valueOf(ticketBooking.getId()));
        dto.self = eventUriBuilder.build();

        final UriBuilder organizerUriBuilder = uriInfo.getBaseUriBuilder().
                path("api/users").path(String.valueOf(ticketBooking.getEventBooking().getUser().getId()));
        dto.user = organizerUriBuilder.build();

        final UriBuilder imageUriBuilder = uriInfo.getBaseUriBuilder().
                path("api/events").path(String.valueOf(ticketBooking.getEventBooking().getEvent().getId()));
        dto.event = imageUriBuilder.build();

        return dto;
    }

    public TicketDto getTicket() {
        return ticket;
    }

    public void setTicket(TicketDto ticket) {
        this.ticket = ticket;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }

    public URI getEvent() {
        return event;
    }

    public void setEvent(URI event) {
        this.event = event;
    }
}
