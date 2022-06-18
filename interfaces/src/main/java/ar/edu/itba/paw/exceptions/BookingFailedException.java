package ar.edu.itba.paw.exceptions;

public class BookingFailedException extends RuntimeException {
    private static final long serialVersionUID = -56L;
    private static final String message = "exception.bookingFailed";

    public BookingFailedException() {
        super(message);
    }
}
