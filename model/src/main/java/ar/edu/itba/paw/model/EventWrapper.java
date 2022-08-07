package ar.edu.itba.paw.model;

import java.util.List;

public class EventWrapper {
    private final List<Event> eventList;
    private final boolean hasNextPage;

    public EventWrapper(List<Event> eventList, boolean hasNextPage) {
        this.eventList = eventList;
        this.hasNextPage = hasNextPage;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }
}
