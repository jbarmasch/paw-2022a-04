package ar.edu.itba.paw.exceptions;

public class EventFinishedException extends RuntimeException {
    private static final long serialVersionUID = -55L;
    private static final String message = "exception.eventFinished";

    public EventFinishedException() {
        super(message);
    }
}
