package ar.edu.itba.paw.model;

public class OrganizerStats {
    private final int eventsCreated;
    private final int bookingsGotten;
    private final Event popularEvent;
    private final double attendance;
    private final double income;

    public OrganizerStats(int eventsCreated, int bookingsGotten, Event popularEvent, double attendance, double income) {
        this.eventsCreated = eventsCreated;
        this.bookingsGotten = bookingsGotten;
        this.popularEvent = popularEvent;
        this.attendance = attendance;
        this.income = income;
    }

    public int getEventsCreated() {
        return eventsCreated;
    }

    public int getBookingsGotten() {
        return bookingsGotten;
    }

    public Event getPopularEvent() {
        return popularEvent;
    }

    public double getAttendance() {
        return attendance;
    }

    public double getIncome() {
        return income;
    }
}