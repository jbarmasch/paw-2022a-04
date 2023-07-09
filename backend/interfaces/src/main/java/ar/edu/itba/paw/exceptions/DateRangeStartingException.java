package ar.edu.itba.paw.exceptions;

public class DateRangeStartingException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -78L;
    private static final String message = "exception.dateRangeStarting";
    private static final int statusCode = 400;

    public DateRangeStartingException() {
        super(message, statusCode);
    }
}
