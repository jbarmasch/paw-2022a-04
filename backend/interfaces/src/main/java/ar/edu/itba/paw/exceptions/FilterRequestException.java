package ar.edu.itba.paw.exceptions;

public class FilterRequestException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -64L;
    private static final String message = "exception.filterRequestException";
    private static final int statusCode = 400;

    public FilterRequestException() {
        super(message, statusCode);
    }
}
