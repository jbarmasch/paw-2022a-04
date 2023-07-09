package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.LocalizedRuntimeException;

public class OrganizerNotFoundException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -44L;
    private static final String message = "exception.organizerNotFound";
    private static final int statusCode = 404;

    public OrganizerNotFoundException() {
        super(message, statusCode);
    }
}
