package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.TicketBooking;

import java.util.List;

public interface TicketBookingDao {
    List<TicketBooking> getBookingsForTicket(long ticketId);
}
