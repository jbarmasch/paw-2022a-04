package ar.edu.itba.paw.model;

import java.time.LocalDateTime;
import java.util.List;

public class Event {
    private long id;
    private String name;
    private String description;
    private Location location;
    private int maxCapacity;
    private int imageId;
    private double price;
    private LocalDateTime date;;
    private Type type;
    private List<Tag> tags;
    private long userId;
    private int attendance;
    private State state;

    public Event(long id, String name, String description, Location location, int maxCapacity, double price, Type type, LocalDateTime date, int imageId, List<Tag> tags, long userId, int attendance, State state) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.price = price;
        this.date = date;
        this.imageId = imageId;
        this.type = type;
        this.tags = tags;
        this.userId = userId;
        this.attendance = attendance;
        this.state = state;
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

    public double getPrice() {
        return price;
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

    public long getUserId() {
        return userId;
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
}
