package ar.edu.itba.paw.webapp.exceptions;

public class EventNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -45L;
    private static final String message = "exception.eventNotFound";

    public EventNotFoundException() {
        super(message);
    }
}
