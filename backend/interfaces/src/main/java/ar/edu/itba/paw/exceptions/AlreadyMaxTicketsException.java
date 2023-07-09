package ar.edu.itba.paw.exceptions;

import java.util.Map;

public class AlreadyMaxTicketsException extends TicketsException {
    private static final long serialVersionUID = -51L;

    public AlreadyMaxTicketsException(Map<Integer, Integer> errorMap) {
        super(errorMap);
    }
}
