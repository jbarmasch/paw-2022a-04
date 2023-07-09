package ar.edu.itba.paw.exceptions;

public class IllegalBookingException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -54L;
    private static final String message = "exception.illegalBooking";
    private static final int statusCode = 400;

    public IllegalBookingException() {
        super(message, statusCode);
    }
}
