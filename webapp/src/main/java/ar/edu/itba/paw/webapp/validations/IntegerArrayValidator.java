package ar.edu.itba.paw.webapp.validations;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IntegerArrayValidator implements ConstraintValidator<IntegerArray, String[]> {
    @Override
    public void initialize(IntegerArray contactNumber) {}

    @Override
    public boolean isValid(String[] s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null)
            return true;

        for (String c : s) {
            try {
                Integer.parseInt(c);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }
}
