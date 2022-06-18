package ar.edu.itba.paw.webapp.exceptions;

public class StatsNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -48L;
    private static final String message = "exception.statsNotFound";

    public StatsNotFoundException() {
        super(message);
    }
}
