package ar.edu.itba.paw.exceptions;

public class DateRangeUntilException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -61L;
    private static final String message = "exception.dateRangeUntil";
    private static final int statusCode = 400;

    public DateRangeUntilException() {
        super(message, statusCode);
    }
}
