package ar.edu.itba.paw.model;

public class Event {
    private long id;
    private String name;
    private String description;
    private String location;
    private int maxCapacity;
    private int attendance = 0;

    public Event(long id, String name, String description, String location, int maxCapacity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.maxCapacity = maxCapacity;
        this.attendance = 0;
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
}
