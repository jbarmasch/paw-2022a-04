package ar.edu.itba.paw.exceptions;

public class BookingNotFoundException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -56L;
    private static final String message = "exception.bookingNotFound";
    private static final int statusCode = 404;

    public BookingNotFoundException() {
        super(message, statusCode);
    }
}
