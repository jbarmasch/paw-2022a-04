package ar.edu.itba.paw.exceptions;

public class TicketNotFoundException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -44L;
    private static final String message = "exception.ticketNotFound";
    private static final int statusCode = 404;

    public TicketNotFoundException() {
        super(message, statusCode);
    }
}
