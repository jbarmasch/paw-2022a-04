package ar.edu.itba.paw.model;

import java.util.List;

public class EventList {
    private final List<Event> eventList;
    private final int pages;

    public EventList(List<Event> eventList, int pages) {
        this.eventList = eventList;
        this.pages = pages;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public int getPages() {
        return pages;
    }
}
