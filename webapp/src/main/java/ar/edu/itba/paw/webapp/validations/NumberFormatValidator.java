package ar.edu.itba.paw.webapp.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NumberFormatValidator implements ConstraintValidator<NumberFormat, Object> {
    private NumberFormat contactNumber;

    @Override
    public void initialize(NumberFormat contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null)
            return true;

        String regex = contactNumber.decimal() ? "[0-9]*([.,][0-9]*)?" : "[0-9]*";
        String data = String.valueOf(value);
        return data.matches(regex);
    }
}
