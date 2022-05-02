package ar.edu.itba.paw.model;

public class Stats {
    int eventsAttended;
    int eventsCreated;
    int bookingsMade;
    int bookingsGotten;
    Type favType;
    Location favLocation;
    Event popularEvent;

    public Stats(int eventsAttended, int eventsCreated, int bookingsMade, int bookingsGotten, Type favType, Location favLocation, Event popularEvent) {
        this.eventsAttended = eventsAttended;
        this.eventsCreated = eventsCreated;
        this.bookingsMade = bookingsMade;
        this.bookingsGotten = bookingsGotten;
        this.favType = favType;
        this.favLocation = favLocation;
        this.popularEvent = popularEvent;
    }

    public int getEventsAttended() {
        return eventsAttended;
    }

    public int getEventsCreated() {
        return eventsCreated;
    }

    public int getBookingsMade() {
        return bookingsMade;
    }

    public int getBookingsGotten() {
        return bookingsGotten;
    }

    public Type getFavType() {
        return favType;
    }

    public Location getFavLocation() {
        return favLocation;
    }

    public Event getPopularEvent() {
        return popularEvent;
    }
}