package ar.edu.itba.paw.webapp.form;
import javax.validation.constraints.*;

public class RateForm {
    @DecimalMin("0")
    @DecimalMax("5.0")
    @NotNull
    private Integer rating;
    private Integer eventId;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
}
