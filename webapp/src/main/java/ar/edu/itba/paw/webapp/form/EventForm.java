package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.Location;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.sql.Timestamp;

public class EventForm {
    @Size(max = 100)
    @NotEmpty
    private String name;

    @Size(max = 100)
    private String description;

    @NotEmpty
    private String location;

    @Min(0)
    @NotNull
    private Integer maxCapacity;

    @DecimalMin("0.00")
    @NotNull
    private Double price;

    @NotEmpty
    public String type;

    @NotEmpty
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String parseDate(String s) {
        String date = s.substring(0,10);
        String time = s.substring(11, 16);
        return date + " " + time + ":00";
    }

    public Timestamp getTimestamp() {
        return Timestamp.valueOf(parseDate(date));
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

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public Double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
