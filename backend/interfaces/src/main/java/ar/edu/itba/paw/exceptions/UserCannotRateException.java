package ar.edu.itba.paw.exceptions;

public class UserCannotRateException extends RuntimeException {
    private static final long serialVersionUID = -50L;
    private static final String message = "exception.userNotRate";

    public UserCannotRateException() {
        super(message);
    }
}