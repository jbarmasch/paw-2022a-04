package ar.edu.itba.paw.exceptions;

public class InvalidUserException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -44L;
    private static final String message = "exception.invalidUser";
    private static final int statusCode = 400;

    public InvalidUserException() {
        super(message, statusCode);
    }
}
