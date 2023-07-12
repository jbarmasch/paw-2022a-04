package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.State;
import ar.edu.itba.paw.model.Tag;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventDto {
    private long id;
    private String name;
    private String description;
    private LocalDateTime date;
    private double minPrice;
    private int attendance;
    private int maxCapacity;
    private Integer minAge;
    private LocationDto location;
    private TypeDto type;
    private List<TagDto> tags;
    private Boolean soldOut;

    private URI self;
    private URI image;
    private URI tickets;
    private URI organizer;

    private URI stats;
    private URI ticketStats;

    public static EventDto fromEvent(final UriInfo uriInfo, final Event event, final String locale) {
        final EventDto dto = new EventDto();

        dto.id = event.getId();
        dto.name = event.getName();
        dto.description = event.getDescription();
        dto.date = event.getDate();
        dto.minPrice = event.getMinPrice();
        dto.attendance = event.getAttendance();
        dto.maxCapacity = event.getMaxCapacity();
        dto.minAge = event.getMinAge();
        dto.location = LocationDto.fromLocation(uriInfo, event.getLocation());
        dto.type = TypeDto.fromType(uriInfo, event.getType(), locale);
        List<TagDto> list = new ArrayList<>();
        for (Tag tag : event.getTags()) {
            list.add(TagDto.fromTag(uriInfo, tag, locale));
        }
        dto.tags = list;
        dto.soldOut = event.getState() == State.SOLDOUT;

        final UriBuilder eventUriBuilder = uriInfo.getBaseUriBuilder().
                path("api/events").path(String.valueOf(event.getId()));
        dto.self = eventUriBuilder.build();

        final UriBuilder organizerUriBuilder = uriInfo.getBaseUriBuilder().
                path("api/organizers").path(String.valueOf(event.getOrganizer().getId()));
        dto.organizer = organizerUriBuilder.build();

        final UriBuilder imageUriBuilder = uriInfo.getBaseUriBuilder().
                path("api/image").path(String.valueOf(event.getImage().getId()));
        dto.image = imageUriBuilder.build();

        final UriBuilder ticketsUriBuilder = uriInfo.getBaseUriBuilder().
                path("api/events").path(String.valueOf(event.getId())).path("tickets");
        dto.tickets = ticketsUriBuilder.build();

        final UriBuilder statsUriBuilder = uriInfo
                .getBaseUriBuilder()
                .path("api/events")
                .path(String.valueOf(event.getId()))
                .path("stats");
        dto.stats = statsUriBuilder.build();

        final UriBuilder ticketStatsUriBuilder = uriInfo
                .getBaseUriBuilder()
                .path("api/events")
                .path(String.valueOf(event.getId()))
                .path("ticket-stats");
        dto.ticketStats = ticketStatsUriBuilder.build();

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public TypeDto getType() {
        return type;
    }

    public void setType(TypeDto type) {
        this.type = type;
    }

    public List<TagDto> getTags() {
        return tags;
    }

    public void setTags(List<TagDto> tags) {
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

    public Boolean getSoldOut() {
        return soldOut;
    }

    public void setSoldOut(Boolean soldOut) {
        this.soldOut = soldOut;
    }

    public URI getStats() {
        return stats;
    }

    public void setStats(URI stats) {
        this.stats = stats;
    }

    public URI getTicketStats() {
        return ticketStats;
    }

    public void setTicketStats(URI ticketStats) {
        this.ticketStats = ticketStats;
    }
}
