package ar.edu.itba.paw.exceptions;

public class InvalidLocationException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -44L;
    private static final String message = "exception.invalidLocation";
    private static final int statusCode = 400;

    public InvalidLocationException() {
        super(message, statusCode);
    }
}
