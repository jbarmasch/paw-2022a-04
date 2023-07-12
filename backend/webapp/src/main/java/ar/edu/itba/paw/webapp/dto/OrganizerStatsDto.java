package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.OrganizerStats;

import javax.ws.rs.core.UriInfo;
import java.util.Locale;

public class OrganizerStatsDto {
    private int eventsCreated;
    private int bookingsGotten;
    private EventDto popularEvent;
    private double attendance;
    private double income;

    public static OrganizerStatsDto fromOrganizerStats(final UriInfo uriInfo,
                                                       final OrganizerStats organizerStats,
                                                       final String locale) {
        final OrganizerStatsDto dto = new OrganizerStatsDto();

        dto.eventsCreated = organizerStats.getEventsCreated();
        dto.bookingsGotten = organizerStats.getBookingsGotten();
        dto.popularEvent = EventDto.fromEvent(uriInfo, organizerStats.getPopularEvent(), locale);
        dto.attendance = organizerStats.getAttendance();
        dto.income = organizerStats.getIncome();

        return dto;
    }

    public int getEventsCreated() {
        return eventsCreated;
    }

    public void setEventsCreated(int eventsCreated) {
        this.eventsCreated = eventsCreated;
    }

    public int getBookingsGotten() {
        return bookingsGotten;
    }

    public void setBookingsGotten(int bookingsGotten) {
        this.bookingsGotten = bookingsGotten;
    }

    public EventDto getPopularEvent() {
        return popularEvent;
    }

    public void setPopularEvent(EventDto popularEvent) {
        this.popularEvent = popularEvent;
    }

    public double getAttendance() {
        return attendance;
    }

    public void setAttendance(double attendance) {
        this.attendance = attendance;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

}
