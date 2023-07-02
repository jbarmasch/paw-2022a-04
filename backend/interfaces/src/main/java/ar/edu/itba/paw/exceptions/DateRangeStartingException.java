package ar.edu.itba.paw.exceptions;

public class DateRangeStartingException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -78L;
    private static final String message = "exception.dateRangeStarting";

    public DateRangeStartingException() {
        super(message);
    }
}
