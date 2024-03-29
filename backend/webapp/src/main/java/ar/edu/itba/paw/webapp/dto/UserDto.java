package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UserDto {
    private String username;
    private long id;

    private URI self;
    private URI stats;
    private URI ticketBookings;

    public static UserDto fromUser(final UriInfo uriInfo, final User user) {
        final UserDto dto = new UserDto();

        dto.id = user.getId();
        dto.username = user.getUsername();

        final UriBuilder userUriBuilder = uriInfo.getBaseUriBuilder()
                .path("api/users")
                .path(String.valueOf(user.getId()));
        dto.self = userUriBuilder.build();

        final UriBuilder statsUriBuilder = uriInfo
                .getBaseUriBuilder()
                .path("api/users")
                .path(String.valueOf(user.getId()))
                .path("stats");
        dto.stats = statsUriBuilder.build();

        final UriBuilder ticketBookings = uriInfo
                .getBaseUriBuilder()
                .path("api/users")
                .path(String.valueOf(user.getId()))
                .path("ticket-bookings");
        dto.stats = ticketBookings.build();

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getStats() {
        return stats;
    }

    public void setStats(URI stats) {
        this.stats = stats;
    }

    public URI getTicketBookings() {
        return ticketBookings;
    }

    public void setTicketBookings(URI ticketBookings) {
        this.ticketBookings = ticketBookings;
    }
}
