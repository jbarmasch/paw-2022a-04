package ar.edu.itba.paw.model;

import java.time.LocalDateTime;
import java.util.List;

public class Event {
    private final long id;
    private final String name;
    private final String description;
    private final Location location;
    private final int maxCapacity;
    private final int imageId;
    private final double minPrice;
    private final LocalDateTime date;
    private final Type type;
    private final List<Tag> tags;
    private final List<Ticket> tickets;
    private final User user;
    private final double rating;
    private final int attendance;
    private final State state;

    public Event(long id, String name, String description, Location location, int maxCapacity, double minPrice, Type type,
                 LocalDateTime date, int imageId, List<Tag> tags, User user, double rating, int attendance, State state,
                 List<Ticket> tickets) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.minPrice = minPrice;
        this.date = date;
        this.imageId = imageId;
        this.type = type;
        this.tags = tags;
        this.user = user;
        this.rating = rating;
        this.attendance = attendance;
        this.state = state;
        this.tickets = tickets;
    }

    public State getState() {
        return state;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
    public Location getLocation() {
        return location;
    }

    public int getImageId() {
        return imageId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public Type getType() {
        return type;
    }

    public String getDateFormatted() {
        String dateStr = date.toString();
        String year = dateStr.substring(0, 4);
        String month = dateStr.substring(5, 7);
        String day = dateStr.substring(8, 10);
        return day + "/" + month + "/" + year;
    }

    public String getTimeFormatted() {
        return date.toString().substring(11, 16);
    }

    public List<Tag> getTags() {
        return tags;
    }

    public int getAttendance() {
        return attendance;
    }

    public boolean getSoldOut() {
        return state == State.SOLDOUT;
    }
    
    public boolean getDeleted() {
        return state == State.DELETED;
    }

    public boolean getActive() {
        return state == State.ACTIVE;
    }

    public User getUser() {
        return user;
    }

    public double getRating() {
        return rating;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }
}
