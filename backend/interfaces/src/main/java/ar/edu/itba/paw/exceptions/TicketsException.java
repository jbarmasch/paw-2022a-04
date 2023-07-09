package ar.edu.itba.paw.exceptions;

import java.util.Map;

public class TicketsException extends RuntimeException {
    private final Map<Integer, Integer> errorMap;

    public TicketsException(Map<Integer, Integer> errorMap) {
        this.errorMap = errorMap;
    }

    public Map<Integer, Integer> getErrorMap() {
        return errorMap;
    }
}
