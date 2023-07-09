package ar.edu.itba.paw.exceptions;

public class TicketNotBookedException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -53L;
    private static final String message = "exception.ticketNotBooked";
    private static final int statusCode = 400;

    public TicketNotBookedException() {
        super(message, statusCode);
    }
}
