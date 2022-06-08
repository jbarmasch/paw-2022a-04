package ar.edu.itba.paw.exceptions;

public class TicketNotBookedException extends RuntimeException {
    private static final long serialVersionUID = -53L;
    private static final String message = "Cancellation of unbooked ticket is impossible";

    public TicketNotBookedException() {
        super(message);
    }
}
