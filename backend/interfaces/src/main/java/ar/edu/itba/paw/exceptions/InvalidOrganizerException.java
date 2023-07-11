package ar.edu.itba.paw.exceptions;

public class InvalidOrganizerException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -44L;
    private static final String message = "exception.invalidOrganizer";
    private static final int statusCode = 400;

    public InvalidOrganizerException() {
        super(message, statusCode);
    }
}
