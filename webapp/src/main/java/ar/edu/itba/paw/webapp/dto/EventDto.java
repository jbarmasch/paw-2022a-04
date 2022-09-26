package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Tag;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EventDto {
    private String name;
    private String description;
    private LocalDateTime date;
    private double minPrice;
    private int attendance;
    private int maxCapacity;
    private Integer minAge;
    private String location;
    private String type;
    private List<String> tags;

    private URI self;
    private URI image;
    private URI tickets;
    private URI organizer;

    public static EventDto fromEvent(final UriInfo uriInfo, final Event event) {
        final EventDto dto = new EventDto();

        dto.name = event.getName();
        dto.description = event.getDescription();
        dto.date = event.getDate();
        dto.minPrice = event.getMinPrice();
        dto.attendance = event.getAttendance();
        dto.maxCapacity = event.getMaxCapacity();
        dto.minAge = event.getMinAge();
        dto.location = event.getLocation().getName();
        dto.type = event.getType().getName();
        dto.tags = event.getTags().stream().map(Tag::getName).collect(Collectors.toList());

        final UriBuilder eventUriBuilder = uriInfo.getAbsolutePathBuilder().
                replacePath("events").path(String.valueOf(event.getId()));
        dto.self = eventUriBuilder.build();

        final UriBuilder organizerUriBuilder = uriInfo.getAbsolutePathBuilder().
                replacePath("users").path(String.valueOf(event.getOrganizer().getId()));
        dto.organizer = organizerUriBuilder.build();

        final UriBuilder imageUriBuilder = uriInfo.getAbsolutePathBuilder().
                replacePath("image").path(String.valueOf(event.getImage().getId()));
        dto.image = imageUriBuilder.build();

        final UriBuilder ticketsUriBuilder = uriInfo.getAbsolutePathBuilder().
                replacePath("tickets");
        dto.tickets = ticketsUriBuilder.queryParam("forEvent", event.getId()).build();

        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getImage() {
        return image;
    }

    public void setImage(URI image) {
        this.image = image;
    }

    public URI getTickets() {
        return tickets;
    }

    public void setTickets(URI tickets) {
        this.tickets = tickets;
    }

    public URI getOrganizer() {
        return organizer;
    }

    public void setOrganizer(URI organizer) {
        this.organizer = organizer;
    }
}
