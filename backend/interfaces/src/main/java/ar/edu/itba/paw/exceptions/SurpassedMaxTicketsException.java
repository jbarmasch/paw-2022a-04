package ar.edu.itba.paw.exceptions;

import java.util.Map;

public class SurpassedMaxTicketsException extends TicketsException {
    private static final long serialVersionUID = -52L;

    public SurpassedMaxTicketsException(Map<Integer, Integer> errorMap) {
        super(errorMap);
    }
}
