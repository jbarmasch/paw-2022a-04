package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.LocalizedRuntimeException;

public class EventStatsNotFoundException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -48L;
    private static final String message = "exception.eventStatsNotFound";
    private static final int statusCode = 404;

    public EventStatsNotFoundException() {
        super(message, statusCode);
    }
}
