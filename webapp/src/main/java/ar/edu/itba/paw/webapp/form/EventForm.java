package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validations.Future;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class EventForm {
    @Size(max = 100)
    @NotEmpty
    private String name;

    @Size(max = 100)
    private String description;

    @NotNull
    private Integer location;

    @Min(0)
    @NotNull
    private Integer maxCapacity;

    @DecimalMin("0.00")
    @NotNull
    private Double price;

    @NotNull
    private Integer type;

    @NotEmpty
    @Future
    private String date;

    @NotEmpty
    private Integer[] tags;

    private MultipartFile image;

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public MultipartFile getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LocalDateTime getTimestamp() {
        return LocalDateTime.parse(date);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getLocation() {
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer[] getTags() {
        return tags;
    }

    public void setTags(Integer[] tags) {
        this.tags = tags;
    }
}
