package ar.edu.itba.paw.webapp.exceptions;

public class TicketNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -47L;
    private static final String message = "exception.ticketNotFound";

    public TicketNotFoundException() {
        super(message);
    }
}
