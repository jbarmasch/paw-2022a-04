package ar.edu.itba.paw.exceptions;

public class FilterException extends RuntimeException {
    private static final long serialVersionUID = -64L;
    private static final String message = "exception.filterException";

    public FilterException() {
        super(message);
    }
}