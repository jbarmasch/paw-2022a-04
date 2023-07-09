package ar.edu.itba.paw.exceptions;

public class UserNotFoundException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -44L;
    private static final String message = "exception.userNotFound";
    private static final int statusCode = 404;

    public UserNotFoundException() {
        super(message, statusCode);
    }
}
