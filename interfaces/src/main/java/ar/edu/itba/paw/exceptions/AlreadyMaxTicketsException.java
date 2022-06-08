package ar.edu.itba.paw.exceptions;

import java.util.Map;

public class AlreadyMaxTicketsException extends Exception implements TicketsException {
    private static final long serialVersionUID = -51L;
    private final Map<Integer, Integer> errorMap;

    public AlreadyMaxTicketsException(Map<Integer, Integer> errorMap) {
        this.errorMap = errorMap;
    }

    @Override
    public Map<Integer, Integer> getErrorMap() {
        return errorMap;
    }
}
