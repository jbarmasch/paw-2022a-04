package ar.edu.itba.paw.webapp.validations;

import ar.edu.itba.paw.webapp.form.BookingForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class BookingListValidator implements ConstraintValidator<BookingList, List<BookingForm>> {
    private String messageNotNull;
    private String messagePositive;

    @Override
    public void initialize(BookingList bookingList) {
        messageNotNull = bookingList.messageNotNull();
        messagePositive = bookingList.messagePositive();
    }

    @Override
    public boolean isValid(List<BookingForm> bookings, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        if (bookings == null)
            return true;

        int i = 0, j = bookings.size();
        for (BookingForm booking : bookings) {
            if (booking.getQty() == null || booking.getQty() == 0) {
                i++;
                continue;
            }
            if (booking.getQty() < 0) {
                context.buildConstraintViolationWithTemplate(messagePositive).addConstraintViolation();
                return false;
            }
        }

        if (i == j) {
            context.buildConstraintViolationWithTemplate(messageNotNull).addConstraintViolation();
            return false;
        }

        return true;
    }
}
