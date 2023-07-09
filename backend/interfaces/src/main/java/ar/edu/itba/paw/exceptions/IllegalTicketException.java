package ar.edu.itba.paw.exceptions;

public class IllegalTicketException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -54L;
    private static final String message = "exception.illegalTicket";
    private static final int statusCode = 400;

    public IllegalTicketException() {
        super(message, statusCode);
    }
}
