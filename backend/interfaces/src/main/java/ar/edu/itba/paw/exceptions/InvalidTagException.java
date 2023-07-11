package ar.edu.itba.paw.exceptions;

public class InvalidTagException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -44L;
    private static final String message = "exception.invalidTag";
    private static final int statusCode = 400;

    public InvalidTagException() {
        super(message, statusCode);
    }
}
