package ar.edu.itba.paw.webapp.exceptions;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -44L;
    private static final String message = "exception.userNotFound";

    public UserNotFoundException() {
        super(message);
    }
}
