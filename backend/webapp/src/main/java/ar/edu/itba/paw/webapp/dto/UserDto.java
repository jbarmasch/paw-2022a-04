package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UserDto {
    private String username;

    private URI self;
    private URI events;

    private double rating;
    private int votes;

    public static UserDto fromUser(final UriInfo uriInfo, final User user) {
        final UserDto dto = new UserDto();

        dto.username = user.getUsername();
        dto.votes = user.getVotes();
        dto.rating = user.getRating();

        final UriBuilder userUriBuilder = uriInfo.getAbsolutePathBuilder().
                replacePath("api/users").path(String.valueOf(user.getId()));
        dto.self = userUriBuilder.build();

        final UriBuilder eventsUriBuilder = uriInfo.getAbsolutePathBuilder().
                replacePath("api/events").path(String.valueOf(user.getId()));
        dto.events = eventsUriBuilder.queryParam("organizedBy", String.valueOf(user.getId())).build();

        return dto;
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

    public URI getEvents() {
        return events;
    }

    public void setEvents(URI events) {
        this.events = events;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}