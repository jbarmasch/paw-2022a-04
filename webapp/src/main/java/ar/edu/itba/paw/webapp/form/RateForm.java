package ar.edu.itba.paw.webapp.form;
import javax.validation.constraints.*;

public class RateForm {
    @DecimalMin("0")
    @DecimalMax("5.0")
    @NotNull
    private Double rating;

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
