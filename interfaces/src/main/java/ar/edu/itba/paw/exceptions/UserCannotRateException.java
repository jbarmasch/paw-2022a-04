package ar.edu.itba.paw.exceptions;

public class UserCannotRateException extends RuntimeException {
    private static final long serialVersionUID = -50L;
    private static final String message = "User cannot rate";

    public UserCannotRateException() {
        super(message);
    }
}
