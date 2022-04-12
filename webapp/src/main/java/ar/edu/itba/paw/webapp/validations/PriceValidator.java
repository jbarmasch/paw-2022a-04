package ar.edu.itba.paw.webapp.validations;

import ar.edu.itba.paw.webapp.form.FilterForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PriceValidator implements ConstraintValidator<Price, FilterForm> {
    @Override
    public void initialize(Price price) {}

    @Override
    public boolean isValid(FilterForm filterForm, ConstraintValidatorContext constraintValidatorContext) {
        if (filterForm.getMaxPrice() == null || filterForm.getMinPrice() == null)
            return true;

        return filterForm.getMaxPrice() - filterForm.getMinPrice() >= 0;
    }
}
