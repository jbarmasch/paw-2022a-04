package ar.edu.itba.paw.exceptions;

public class LocalizedRuntimeException extends RuntimeException {
    public LocalizedRuntimeException(String message) {
        super(message);
    }
}
