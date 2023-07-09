package ar.edu.itba.paw.exceptions;

public class ForbiddenAccessException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -44L;
    private static final String message = "exception.forbiddenAccess";
    private static final int statusCode = 403;

    public ForbiddenAccessException() {
        super(message, statusCode);
    }
}
