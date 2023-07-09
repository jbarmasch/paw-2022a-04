package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.LocalizedRuntimeException;

public class ImageNotFoundException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -46L;
    private static final String message = "exception.imageNotFound";
    private static final int statusCode = 404;

    public ImageNotFoundException() {
        super(message, statusCode);
    }
}
