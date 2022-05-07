package ar.edu.itba.paw.model;

public class EventStats {
    int eventsCreated;
    int bookingsGotten;
    Event popularEvent;

    public EventStats(int eventsCreated, int bookingsGotten, Event popularEvent) {
        this.eventsCreated = eventsCreated;
        this.bookingsGotten = bookingsGotten;
        this.popularEvent = popularEvent;
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
}