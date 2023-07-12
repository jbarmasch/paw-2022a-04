package ar.edu.itba.paw.exceptions;

public class InvalidEventStateException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -44L;
    private static final String message = "exception.invalidStateEvent";
    private static final int statusCode = 400;

    public InvalidEventStateException() {
        super(message, statusCode);
    }
}
