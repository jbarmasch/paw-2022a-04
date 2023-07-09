package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.exceptions.LocalizedRuntimeException;

public class ImageProcessingErrorException extends LocalizedRuntimeException {
    private static final long serialVersionUID = -53L;
    private static final String message = "exception.imageError";
    private static final int statusCode = 400;

    public ImageProcessingErrorException() {
        super(message, statusCode);
    }
}
