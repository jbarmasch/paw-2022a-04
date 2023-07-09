package ar.edu.itba.paw.exceptions;

public class EventNotFoundException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -45L;
    private static final String message = "exception.eventNotFound";
    private static final int statusCode = 404;

    public EventNotFoundException() {
        super(message, statusCode);
    }
}
