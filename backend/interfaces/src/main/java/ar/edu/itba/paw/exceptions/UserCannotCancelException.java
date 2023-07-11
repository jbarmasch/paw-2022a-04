package ar.edu.itba.paw.exceptions;

public class UserCannotCancelException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -56L;
    private static final String message = "exception.userCannotCancel";
    private static final int statusCode = 400;

    public UserCannotCancelException() {
        super(message, statusCode);
    }
}
