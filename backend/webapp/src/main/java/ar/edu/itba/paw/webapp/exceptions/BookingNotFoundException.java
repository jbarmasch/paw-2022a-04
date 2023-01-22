package ar.edu.itba.paw.webapp.exceptions;

public class BookingNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -49L;
    private static final String message = "exception.bookingNotFound";

    public BookingNotFoundException() {
        super(message);
    }
}
