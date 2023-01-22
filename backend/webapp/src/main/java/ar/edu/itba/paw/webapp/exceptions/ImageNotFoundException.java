package ar.edu.itba.paw.webapp.exceptions;

public class ImageNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -46L;
    private static final String message = "exception.imageNotFound";

    public ImageNotFoundException() {
        super(message);
    }
}
