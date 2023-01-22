package ar.edu.itba.paw.model;

public class UserStats {
    private final int eventsAttended;
    private final int bookingsMade;
    private final Type favType;
    private final Location favLocation;

    public UserStats(int eventsAttended, int bookingsMade, Type favType, Location favLocation) {
        this.eventsAttended = eventsAttended;
        this.bookingsMade = bookingsMade;
        this.favType = favType;
        this.favLocation = favLocation;
    }

    public int getEventsAttended() {
        return eventsAttended;
    }

    public int getBookingsMade() {
        return bookingsMade;
    }

    public Type getFavType() {
        return favType;
    }

    public Location getFavLocation() {
        return favLocation;
    }
}
