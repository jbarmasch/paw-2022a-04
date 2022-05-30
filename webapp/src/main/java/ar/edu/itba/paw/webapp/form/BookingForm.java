package ar.edu.itba.paw.webapp.form;

import java.util.List;

public class BookingForm {
    private Long ticketId;
    private Integer qty;

    public BookingForm(Long ticketId, Integer qty) {
        this.ticketId = ticketId;
        this.qty = qty;
    }

    public BookingForm() {}

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
