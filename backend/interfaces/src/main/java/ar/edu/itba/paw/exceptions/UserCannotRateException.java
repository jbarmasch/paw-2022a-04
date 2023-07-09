package ar.edu.itba.paw.exceptions;

public class UserCannotRateException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -50L;
    private static final String message = "exception.userNotRate";
    private static final int statusCode = 400;

    public UserCannotRateException() {
        super(message, statusCode);
    }
}
