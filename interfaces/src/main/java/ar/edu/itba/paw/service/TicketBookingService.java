package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.TicketBooking;

import java.util.List;

public interface TicketBookingService {
    List<TicketBooking> getBookingsForTicket(long ticketId);
}
