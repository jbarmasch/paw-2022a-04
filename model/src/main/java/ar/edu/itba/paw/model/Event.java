package ar.edu.itba.paw.model;

import java.time.LocalDateTime;
import java.util.List;

public class Event {
    private long id;
    private String name;
    private String description;
    private Location location;
    private int maxCapacity;
    private int attendance;
    private Image img;
    private double price;
    private LocalDateTime date;;
    private Type type;
    private List<Tag> tags;
    private long userId;

    public Event(long id, String name, String description, Location location, int maxCapacity, double price, Type type, LocalDateTime date, Image img, List<Tag> tags, long userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.attendance = 0;
        this.price = price;
        this.date = date;
        this.img = img;
        this.type = type;
        this.tags = tags;
        this.userId = userId;
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

    public Image getImg() {
        return img;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getAttendance() {
        return attendance;
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
}
