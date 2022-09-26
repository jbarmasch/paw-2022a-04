package ar.edu.itba.paw.webapp.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class FutureValidator implements ConstraintValidator<Future, String> {
    @Override
    public void initialize(Future contactNumber) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty())
            return true;

        try {
            return LocalDateTime.parse(s).isAfter(LocalDateTime.now());
        } catch (DateTimeParseException e) {
            return true;
        }
    }
}