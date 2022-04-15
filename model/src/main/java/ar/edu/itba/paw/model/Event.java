package ar.edu.itba.paw.model;

import java.sql.Timestamp;

public class Event {
    private long id;
    private String name;
    private String description;
    private int location;
    private int maxCapacity;
    private int attendance = 0;
    private String img = null;
    private double price;
    private Timestamp date;
    private String type;

    public Event(long id, String name, String description, int location, int maxCapacity, double price, String type, Timestamp date) {
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

    public String getType() {
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
