package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.LocalizedRuntimeException;

public class BookingNotFoundException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -49L;
    private static final String message = "exception.bookingNotFound";
    private static final int statusCode = 404;

    public BookingNotFoundException() {
        super(message, statusCode);
    }
}
