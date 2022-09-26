package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Ticket;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;

public class TicketDto {
    private String ticketName;
    private Double price;
    private Integer qty;
    private Integer booked;
    private Integer maxPerUser;
    private LocalDateTime starting;
    private LocalDateTime until;

    private URI self;
    private URI event;
    private URI bookings;

    public static TicketDto fromTicket(final UriInfo uriInfo, final Ticket ticket) {
        final TicketDto dto = new TicketDto();

        dto.ticketName = ticket.getTicketName();
        dto.price = ticket.getPrice();
        dto.qty = ticket.getQty();
        dto.booked = ticket.getBooked();
        dto.maxPerUser = ticket.getMaxPerUser();
        dto.starting = ticket.getStarting();
        dto.until = ticket.getUntil();

        final UriBuilder ticketUriBuilder = uriInfo.getAbsolutePathBuilder().
                replacePath("tickets").path(String.valueOf(ticket.getId()));
        dto.self = ticketUriBuilder.build();

        final UriBuilder eventUriBuilder = uriInfo.getAbsolutePathBuilder().
                replacePath("events").path(String.valueOf(ticket.getEvent().getId()));
        dto.event = eventUriBuilder.build();

        final UriBuilder bookingsUriBuilder = uriInfo.getAbsolutePathBuilder().
                replacePath("eventBookings");
        dto.bookings = bookingsUriBuilder.build();

        return dto;
    }
}
