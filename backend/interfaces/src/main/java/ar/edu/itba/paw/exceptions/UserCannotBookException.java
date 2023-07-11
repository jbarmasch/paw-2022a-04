package ar.edu.itba.paw.exceptions;

public class UserCannotBookException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -56L;
    private static final String message = "exception.userCannotBook";
    private static final int statusCode = 400;

    public UserCannotBookException() {
        super(message, statusCode);
    }
}
