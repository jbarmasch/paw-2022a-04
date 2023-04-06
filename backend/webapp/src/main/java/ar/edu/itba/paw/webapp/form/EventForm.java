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
    private Long location;

    @NotNull
    private Long type;

    @NotEmpty
    @Future
    @Pattern(regexp = "[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2}T[0-9]{1,2}:[0-9]{1,2}")
    private String date;

    private Long[] tags;

    private boolean hasMinAge;
    @Min(value = 14)
    @Max(value = 27)
    private Integer minAge;

    private MultipartFile image;

    public boolean isHasMinAge() {
        return hasMinAge;
    }

    public void setHasMinAge(boolean hasMinAge) {
        this.hasMinAge = hasMinAge;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLocation() {
        return location;
    }

    public void setLocation(Long location) {
        this.location = location;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long[] getTags() {
        return tags;
    }

    public void setTags(Long[] tags) {
        this.tags = tags;
    }
}
