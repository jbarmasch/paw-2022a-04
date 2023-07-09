package ar.edu.itba.paw.exceptions;

public class CancelBookingFailedException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -56L;
    private static final String message = "exception.cancelFailed";
    private static final int statusCode = 400;

    public CancelBookingFailedException() {
        super(message, statusCode);
    }
}
