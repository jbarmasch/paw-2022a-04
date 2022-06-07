package ar.edu.itba.paw.exceptions;

import java.util.Map;

public class SurpassedMaxTicketsException extends RuntimeException implements TicketsException {
    private static final long serialVersionUID = -52L;
    private final Map<Integer, Integer> errorMap;

    public SurpassedMaxTicketsException(Map<Integer, Integer> errorMap) {
        this.errorMap = errorMap;
    }

    @Override
    public Map<Integer, Integer> getErrorMap() {
        return errorMap;
    }
}
