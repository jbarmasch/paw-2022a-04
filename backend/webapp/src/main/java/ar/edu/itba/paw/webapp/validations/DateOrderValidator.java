package ar.edu.itba.paw.webapp.validations;

import ar.edu.itba.paw.webapp.form.TicketForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class DateOrderValidator implements ConstraintValidator<DateOrder, TicketForm> {
    @Override
    public void initialize(DateOrder contactNumber) {
    }

    @Override
    public boolean isValid(TicketForm form, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (form == null)
            return true;

        LocalDateTime starting = form.getLocalDate(form.getStarting());
        LocalDateTime until = form.getLocalDate(form.getUntil());

        if (starting != null && until != null && (starting.isAfter(until) || starting.isEqual(until))) {
            context.buildConstraintViolationWithTemplate("{Event.eventForm.order}").addConstraintViolation();
            return false;
        }

        return true;
    }
}
