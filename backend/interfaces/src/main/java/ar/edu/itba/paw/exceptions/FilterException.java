package ar.edu.itba.paw.exceptions;

public class FilterException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -64L;
    private static final String message = "exception.filterException";
    private static final int statusCode = 400;

    public FilterException() {
        super(message, statusCode);
    }
}
