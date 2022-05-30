package ar.edu.itba.paw.webapp.validations;

import ar.edu.itba.paw.model.EventBooking;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookingListValidator implements ConstraintValidator<BookingList, EventBooking> {
    private String messageNotNull;
    private String messagePositive;

    @Override
    public void initialize(BookingList bookingList) {
        messageNotNull = bookingList.messageNotNull();
        messagePositive = bookingList.messagePositive();
    }

    @Override
    public boolean isValid(EventBooking booking, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
//        if (bookings == null)
//            return true;
//
//        int i = 0, j = bookings.size();
//        for (Booking booking : bookings) {
//            if (booking.getQty() == null) {
//                i++;
//                continue;
//            }
//            if (booking.getQty() < 0) {
//                context.buildConstraintViolationWithTemplate(messagePositive).addConstraintViolation();
//                return false;
//            }
//        }

//        if (i == j) {
//            context.buildConstraintViolationWithTemplate(messageNotNull).addConstraintViolation();
//            return false;
//        }

        return true;
    }
}
