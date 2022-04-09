package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.Location;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.sql.Timestamp;//coco estuvo aqui mua jajajajjajj xd

public class EventForm {

    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String description;

    private String location;

    @DecimalMin("0.0")
    private int maxCapacity;

    @DecimalMin("0.0")
    private double price;

    @Future
    private Timestamp date;

    public void setDate(String date) {
        this.date = Timestamp.valueOf(parseDate(date));
    }

    private String parseDate(String s) {
        String date = s.substring(0,10);
        String time = s.substring(11, 16);
        return date + " " + time + ":00";
    }

    public Timestamp getDate() {
        return date;
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

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
