package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Ticket;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;

public class TicketDto {
    private Long id;
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

        dto.id = ticket.getId();
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getBooked() {
        return booked;
    }

    public void setBooked(Integer booked) {
        this.booked = booked;
    }

    public Integer getMaxPerUser() {
        return maxPerUser;
    }

    public void setMaxPerUser(Integer maxPerUser) {
        this.maxPerUser = maxPerUser;
    }

    public LocalDateTime getStarting() {
        return starting;
    }

    public void setStarting(LocalDateTime starting) {
        this.starting = starting;
    }

    public LocalDateTime getUntil() {
        return until;
    }

    public void setUntil(LocalDateTime until) {
        this.until = until;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getEvent() {
        return event;
    }

    public void setEvent(URI event) {
        this.event = event;
    }

    public URI getBookings() {
        return bookings;
    }

    public void setBookings(URI bookings) {
        this.bookings = bookings;
    }
}
