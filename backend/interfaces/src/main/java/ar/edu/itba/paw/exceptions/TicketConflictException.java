package ar.edu.itba.paw.exceptions;

public class TicketConflictException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -60L;
    private static final String message = "exception.ticketConflict";
    private static final int statusCode = 400;

    public TicketConflictException() {
        super(message, statusCode);
    }
}
