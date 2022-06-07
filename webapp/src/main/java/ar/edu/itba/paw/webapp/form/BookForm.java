package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validations.BookingList;

import javax.validation.constraints.*;
import java.util.List;

public class BookForm {
    @NotNull
    @BookingList
    private List<BookingForm> bookings;

    private int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<BookingForm> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingForm> bookings) {
        this.bookings = bookings;
    }
}
