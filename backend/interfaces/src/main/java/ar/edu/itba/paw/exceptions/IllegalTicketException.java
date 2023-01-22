package ar.edu.itba.paw.exceptions;

public class IllegalTicketException extends RuntimeException {
    private static final long serialVersionUID = -54L;
    private static final String message = "exception.illegalTicket";

    public IllegalTicketException() {
        super(message);
    }
}
