package ar.edu.itba.paw.exceptions;

public class CancelBookingFailedException extends RuntimeException {
    private static final long serialVersionUID = -56L;
    private static final String message = "exception.cancelFailed";

    public CancelBookingFailedException() {
        super(message);
    }
}
