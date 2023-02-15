package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.EventBooking;
import ar.edu.itba.paw.model.Tag;
import ar.edu.itba.paw.model.TicketBooking;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingDto {
    private long id;
    private List<TicketBookingDto> ticketBookings;
    private Integer rating;
    private String code;
    private boolean confirmed;
    private String image;

    private URI self;
    private URI user;
    private URI event;

    public static BookingDto fromBooking(final UriInfo uriInfo, final EventBooking eventBooking) {
        final BookingDto dto = new BookingDto();

        dto.id = eventBooking.getId();

        List<TicketBookingDto> list = new ArrayList<>();
        for (TicketBooking ticketBooking : eventBooking.getTicketBookings()) {
            list.add(TicketBookingDto.fromBooking(uriInfo, ticketBooking));
        }
        dto.ticketBookings = list;

        dto.rating = eventBooking.getRating();
        dto.code = eventBooking.getCode();
        dto.confirmed = eventBooking.isConfirmed();

        final UriBuilder eventUriBuilder = uriInfo.getBaseUriBuilder().
                path("api/bookings").path(String.valueOf(eventBooking.getCode()));
        dto.self = eventUriBuilder.build();

        final UriBuilder organizerUriBuilder = uriInfo.getBaseUriBuilder().
                path("api/users").path(String.valueOf(eventBooking.getUser().getId()));
        dto.user = organizerUriBuilder.build();

        final UriBuilder imageUriBuilder = uriInfo.getBaseUriBuilder().
                path("api/events").path(String.valueOf(eventBooking.getEvent().getId()));
        dto.event = imageUriBuilder.build();

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<TicketBookingDto> getTicketBookings() {
        return ticketBookings;
    }

    public void setTicketBookings(List<TicketBookingDto> ticketBookings) {
        this.ticketBookings = ticketBookings;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
