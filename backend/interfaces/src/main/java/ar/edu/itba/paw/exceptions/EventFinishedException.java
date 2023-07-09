package ar.edu.itba.paw.exceptions;

public class EventFinishedException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -55L;
    private static final String message = "exception.eventFinished";
    private static final int statusCode = 400;

    public EventFinishedException() {
        super(message, statusCode);
    }
}
