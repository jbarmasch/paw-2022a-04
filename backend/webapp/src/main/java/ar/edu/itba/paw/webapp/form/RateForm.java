package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RateForm {
    @Min(value = 1, message = "{Min.rateForm.rating}")
    @Max(value = 5, message = "{Max.rateForm.rating}")
    @NotNull(message = "{NotNull.rateForm.rating}")
    private Integer rating;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
