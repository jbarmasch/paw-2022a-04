package ar.edu.itba.paw.exceptions;

public class BookingFailedException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -62L;
    private static final String message = "exception.bookingFailed";
    private static final int statusCode = 400;

    public BookingFailedException() {
        super(message, statusCode);
    }
}
