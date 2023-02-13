package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.RoleEnum;
import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UserDto {
    private String username;
    private String mail;
    private String role;

    private URI self;
    private URI events;

    private double rating;
    private int votes;
    private long id;

    public static UserDto fromUser(final UriInfo uriInfo, final User user) {
        final UserDto dto = new UserDto();

        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.mail = user.getMail();
        dto.votes = user.getVotes();
        dto.rating = user.getRating();

        List<Role> role = user.getRoles();
        dto.role = role.size() > 1 ? RoleEnum.ROLE_CREATOR.toString() : role.isEmpty() ? "ROLE_USER" : role.get(0).getRoleName();

        final UriBuilder userUriBuilder = uriInfo.getAbsolutePathBuilder().
                replacePath("api/users").path(String.valueOf(user.getId()));
        dto.self = userUriBuilder.build();

        final UriBuilder eventsUriBuilder = uriInfo.getAbsolutePathBuilder().replacePath("api/events");
        dto.events = eventsUriBuilder.queryParam("userId", String.valueOf(user.getId())).build();

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


    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
