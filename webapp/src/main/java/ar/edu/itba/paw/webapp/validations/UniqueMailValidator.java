package ar.edu.itba.paw.webapp.validations;

import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueMailValidator implements ConstraintValidator<UniqueMail, String> {
    @Autowired
    private UserService userService;

    public void initialize(UniqueMail u) {}

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty())
            return true;

        return userService.isMailUnique(value);
    }
}
