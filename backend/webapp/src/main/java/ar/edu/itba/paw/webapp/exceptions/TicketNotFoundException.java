package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.LocalizedRuntimeException;

public class TicketNotFoundException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -47L;
    private static final String message = "exception.ticketNotFound";
    private static final int statusCode = 404;

    public TicketNotFoundException() {
        super(message, statusCode);
    }
}
