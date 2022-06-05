package ar.edu.itba.paw.webapp.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateValidator implements ConstraintValidator<Date, String> {
    @Override
    public void initialize(Date contactNumber) {}

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty())
            return true;

        Pattern pattern = Pattern.compile("^[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2}T[0-9]{1,2}:[0-9]{1,2}$");
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
    }
}