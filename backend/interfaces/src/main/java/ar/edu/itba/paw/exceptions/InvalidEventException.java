package ar.edu.itba.paw.exceptions;

public class InvalidEventException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -44L;
    private static final String message = "exception.invalidEvent";
    private static final int statusCode = 400;

    public InvalidEventException() {
        super(message, statusCode);
    }
}
