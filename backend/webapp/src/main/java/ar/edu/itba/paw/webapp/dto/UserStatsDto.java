package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.UserStats;
import ar.edu.itba.paw.model.Tag;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class UserStatsDto {
    private int eventsAttended;
    private int bookingsMade;
    private TypeDto favType;
    private LocationDto favLocation;

    public static UserStatsDto fromUserStats(final UriInfo uriInfo, final UserStats userStats) {
        final UserStatsDto dto = new UserStatsDto();

        dto.eventsAttended = userStats.getEventsAttended();
        dto.bookingsMade = userStats.getBookingsMade();
        dto.favType = TypeDto.fromType(uriInfo, userStats.getFavType());
        dto.favLocation = LocationDto.fromLocation(uriInfo, userStats.getFavLocation());

        return dto;
    }

    public int getEventsAttended() {
        return eventsAttended;
    }

    public void setEventsAttended(int eventsAttended) {
        this.eventsAttended = eventsAttended;
    }

    public int getBookingsMade() {
        return bookingsMade;
    }

    public void setBookingsMade(int bookingsMade) {
        this.bookingsMade = bookingsMade;
    }

    public TypeDto getFavType() {
        return favType;
    }

    public void setFavType(TypeDto favType) {
        this.favType = favType;
    }

    public LocationDto getFavLocation() {
        return favLocation;
    }

    public void setFavLocation(LocationDto favLocation) {
        this.favLocation = favLocation;
    }
}
