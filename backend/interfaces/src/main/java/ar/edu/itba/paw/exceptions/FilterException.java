package ar.edu.itba.paw.exceptions;

public class FilterException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -64L;
    private static final String message = "exception.filterException";

    public FilterException() {
        super(message);
    }
}
