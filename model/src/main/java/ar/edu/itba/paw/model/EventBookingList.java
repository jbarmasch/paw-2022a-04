package ar.edu.itba.paw.model;

import java.util.List;

public class EventBookingList {
    private final List<EventBooking> bookingList;
    private final int pages;

    public EventBookingList(List<EventBooking> bookingList, int pages) {
        this.bookingList = bookingList;
        this.pages = pages;
    }

    public List<EventBooking> getBookingList() {
        return bookingList;
    }

    public int getPages() {
        return pages;
    }
}
