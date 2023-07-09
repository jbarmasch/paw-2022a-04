package ar.edu.itba.paw.exceptions;

public class LocalizedRuntimeException extends RuntimeException {
    private final int statusCode;

    public LocalizedRuntimeException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
