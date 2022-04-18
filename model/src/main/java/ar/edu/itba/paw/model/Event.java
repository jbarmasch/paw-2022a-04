package ar.edu.itba.paw.model;

import java.time.LocalDateTime;

public class Event {
    private long id;
    private String name;
    private String description;
    private int location;
    private int maxCapacity;
    private int attendance = 0;
    private int img;
    private double price;
    private LocalDateTime date;
    private int type;

    public Event(long id, String name, String description, int location, int maxCapacity, double price, int type, LocalDateTime date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.attendance = 0;
        this.price = price;
        this.date = date;
        this.type = type;
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
    
    public int getLocation() {
        return location;
    }

    public int getImg() {
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

    public int getType() {
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
}
