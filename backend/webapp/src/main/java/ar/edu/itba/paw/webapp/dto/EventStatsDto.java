package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.EventStats;

import javax.ws.rs.core.UriInfo;

public class EventStatsDto {
    private String eventName;
    private int attended;
    private int booked;
    private double attendance;
    private double saleRatio;
    private double income;
    private double expectedIncome;

    public static EventStatsDto fromEventStats(final UriInfo uriInfo, final EventStats eventStats) {
        final EventStatsDto dto = new EventStatsDto();

        dto.eventName = eventStats.getEventName();
        dto.attended = eventStats.getAttended();
        dto.booked = eventStats.getBooked();
        dto.attendance = eventStats.getAttendance();
        dto.saleRatio = eventStats.getSaleRatio();
        dto.income = eventStats.getIncome();
        dto.expectedIncome = eventStats.getExpectedIncome();

        return dto;
    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getAttended() {
        return attended;
    }

    public void setAttended(int attended) {
        this.attended = attended;
    }

    public int getBooked() {
        return booked;
    }

    public void setBooked(int booked) {
        this.booked = booked;
    }

    public double getAttendance() {
        return attendance;
    }

    public void setAttendance(double attendance) {
        this.attendance = attendance;
    }

    public double getSaleRatio() {
        return saleRatio;
    }

    public void setSaleRatio(double saleRatio) {
        this.saleRatio = saleRatio;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpectedIncome() {
        return expectedIncome;
    }

    public void setExpectedIncome(double expectedIncome) {
        this.expectedIncome = expectedIncome;
    }

}
