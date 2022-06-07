package ar.edu.itba.paw.exceptions;

import java.time.LocalDateTime;

public class DateRangeException extends RuntimeException {
    private static final long serialVersionUID = -53L;
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
