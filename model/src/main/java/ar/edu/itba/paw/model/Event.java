package ar.edu.itba.paw.model;

import java.sql.Timestamp;

public class Event {
    private long id;
    private String name;
    private String description;
    private String location;
    private int maxCapacity;
    private int attendance = 0;
    private String img = null;
    private double price;
    private Timestamp date;

    public Event(long id, String name, String description, String location, int maxCapacity, double price, Timestamp date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.attendance = 0;
        this.price = price;
        this.date = date;
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
    
    public String getLocation() {
        return location;
    }

    public String getImg() {
        return img;
    }

    public Timestamp getDate() {
        return date;
    }

    public boolean book() {
        if (attendance < maxCapacity) {
            attendance++;
            return true;
        }
        return false;
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
}
