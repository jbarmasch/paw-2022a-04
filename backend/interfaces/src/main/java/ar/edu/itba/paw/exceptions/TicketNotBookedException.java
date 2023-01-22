package ar.edu.itba.paw.exceptions;

public class TicketNotBookedException extends RuntimeException {
    private static final long serialVersionUID = -53L;
    private static final String message = "exception.ticketNotBooked";

    public TicketNotBookedException() {
        super(message);
    }
}
