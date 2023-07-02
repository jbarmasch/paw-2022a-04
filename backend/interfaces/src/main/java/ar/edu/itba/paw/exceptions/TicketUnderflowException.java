package ar.edu.itba.paw.exceptions;

public class TicketUnderflowException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -60L;
    private static final String message = "exception.ticketUnderflow";

    public TicketUnderflowException() {
        super(message);
    }
}
