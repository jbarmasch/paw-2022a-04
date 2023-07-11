package ar.edu.itba.paw.exceptions;

public class InvalidTypeException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -44L;
    private static final String message = "exception.invalidType";
    private static final int statusCode = 400;

    public InvalidTypeException() {
        super(message, statusCode);
    }
}
