package ar.edu.itba.paw.model;

public enum State {
    ACTIVE,
    DELETED,
    SOLDOUT;

    public static State getState(int id) {
        State[] states = State.values();
        if (id >= states.length || id < 0)
            return null;
        return states[id];
    }
}
