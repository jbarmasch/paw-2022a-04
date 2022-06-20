package ar.edu.itba.paw.exceptions;

import java.time.LocalDateTime;

public class DateRangeException extends Exception {
    private static final long serialVersionUID = -61L;
    private final LocalDateTime starting;
    private final LocalDateTime until;

    public DateRangeException(LocalDateTime starting, LocalDateTime until) {
        this.starting = starting;
        this.until = until;
    }

    public LocalDateTime getStarting() {
        return starting;
    }

    public LocalDateTime getUntil() {
        return until;
    }
}
