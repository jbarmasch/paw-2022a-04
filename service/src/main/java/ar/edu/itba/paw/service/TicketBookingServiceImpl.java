package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.TicketBooking;
import ar.edu.itba.paw.persistence.TicketBookingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketBookingServiceImpl implements TicketBookingService {
    @Autowired
    TicketBookingDao ticketBookingDao;

    public List<TicketBooking> getBookingsForTicket(long ticketId) {
        return ticketBookingDao.getBookingsForTicket(ticketId);
    }
}
