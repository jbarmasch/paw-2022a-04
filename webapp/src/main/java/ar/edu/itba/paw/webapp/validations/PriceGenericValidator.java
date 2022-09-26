package ar.edu.itba.paw.webapp.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PriceGenericValidator implements ConstraintValidator<Price, Object[]> {
    @Override
    public void initialize(Price price) {
    }

    @Override
    public boolean isValid(Object[] objects, ConstraintValidatorContext constraintValidatorContext) {
        if (objects[0] == null || objects[1] == null)
            return true;

        return (Integer) objects[1] - (Integer) objects[0] >= 0;
    }
}
