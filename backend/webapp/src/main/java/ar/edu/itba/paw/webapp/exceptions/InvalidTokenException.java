package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.LocalizedRuntimeException;

public class InvalidTokenException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -48L;
    private static final String message = "exception.invalidTokenException";
    private static final int statusCode = 400;

    public InvalidTokenException() {
        super(message, statusCode);
    }
}
