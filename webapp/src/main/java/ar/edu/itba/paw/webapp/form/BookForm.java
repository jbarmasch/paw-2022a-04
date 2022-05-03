package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.model.Booking;

import javax.validation.constraints.*;
import java.util.List;

public class BookForm {
    @NotNull
    private List<Booking> bookings;

    private int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
