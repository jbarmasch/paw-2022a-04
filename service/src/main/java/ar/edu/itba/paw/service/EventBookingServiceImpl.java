package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.AlreadyMaxTicketsException;
import ar.edu.itba.paw.exceptions.BookingNotFoundException;
import ar.edu.itba.paw.exceptions.SurpassedMaxTicketsException;
import ar.edu.itba.paw.model.EventBooking;
import ar.edu.itba.paw.model.Ticket;
import ar.edu.itba.paw.model.TicketBooking;
import ar.edu.itba.paw.persistence.EventBookingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EventBookingServiceImpl implements EventBookingService {
    @Autowired
    private EventBookingDao eventBookingDao;
    @Autowired
    private MailService mailService;
    @Autowired
    private CodeService codeService;

    private static final int MAX_TICKETS = 6;

    @Override
    public List<EventBooking> getAllBookingsFromUser(long userId, int page) {
        return eventBookingDao.getAllBookingsFromUser(userId, page);
    }

    @Override
    public Optional<EventBooking> getBookingFromUser(long userId, long eventId) {
        Optional<EventBooking> eb =  eventBookingDao.getBookingFromUser(userId, eventId);
        if (eb.isPresent() && (eb.get().getEvent().getDate().isBefore(LocalDateTime.now()) || eb.get().getTicketBookingsSize() == 0))
            throw new BookingNotFoundException();
        return eb;
    }

    @Override
    public Optional<EventBooking> getBooking(String code) {
        Optional<EventBooking> eb = eventBookingDao.getBooking(code);
        if (eb.isPresent() && (eb.get().getEvent().getDate().isBefore(LocalDateTime.now()) || eb.get().getTicketBookingsSize() == 0))
            throw new BookingNotFoundException();
        return eb;
    }

    @Transactional
    @Override
    public void book(EventBooking booking, String baseUrl, Locale locale) {
        EventBooking persistedBooking = eventBookingDao.getBookingFromUser(booking.getUser().getId(), booking.getEvent().getId()).orElse(null);

        Map<Long, TicketBooking> ticketMap = new HashMap<>();
        if (persistedBooking != null) {
            List<TicketBooking> ticketBookings = persistedBooking.getTicketBookings();
            for (TicketBooking ticketBooking : ticketBookings) {
                ticketMap.put(ticketBooking.getTicket().getId(), ticketBooking);
            }
        }

        Map<Integer, Integer> alreadyTicketsError = new HashMap<>();
        Map<Integer, Integer> surpassedTicketsError = new HashMap<>();
        int i = 0;
        for (TicketBooking ticketBooking : booking.getTicketBookings()) {
            if (ticketBooking.getTicket().getTicketsLeft() < Math.min(MAX_TICKETS, ticketBooking.getQty())) {
                surpassedTicketsError.put(i, ticketBooking.getTicket().getTicketsLeft());
            }
            if (persistedBooking != null) {
                TicketBooking aux = ticketMap.get(ticketBooking.getTicket().getId());
                if (aux != null) {
                    if (ticketBooking.getQty() + aux.getQty() > MAX_TICKETS) {
                        alreadyTicketsError.put(i, MAX_TICKETS - aux.getQty());
                    }
                }
            }
            i++;
        }

        if (!alreadyTicketsError.isEmpty()) {
            throw new AlreadyMaxTicketsException(alreadyTicketsError);
        }
        if (!surpassedTicketsError.isEmpty()) {
            throw new SurpassedMaxTicketsException(surpassedTicketsError);
        }

        String code = codeService.getCode(booking.getUser().getId() + ":" + booking.getEvent().getId());
        booking.setCode(code);

        EventBooking eventBooking = eventBookingDao.book(booking);
        if (eventBooking != null) {
            TransactionUtil.executeAfterTransaction(() -> mailService.sendBookMail(baseUrl + "/bookings/" + eventBooking.getCode(), eventBooking, locale));
//            mailService.sendBookMail(baseUrl + "/bookings/" + eventBooking.getCode(), eventBooking, locale);
            return;
        }

        mailService.sendErrorMail(booking.getUser().getMail());
    }

    @Transactional
    @Override
    public void cancelBooking(EventBooking booking, Locale locale) {
        EventBooking persistedBooking = eventBookingDao.getBookingFromUser(booking.getUser().getId(), booking.getEvent().getId()).orElse(null);
        Map<Long, TicketBooking> ticketMap = new HashMap<>();
        if (persistedBooking != null) {
            List<TicketBooking> ticketBookings = persistedBooking.getTicketBookings();
            for (TicketBooking ticketBooking : ticketBookings) {
                ticketMap.put(ticketBooking.getId(), ticketBooking);
            }
        }

        Map<Integer, Integer> ticketsError = new HashMap<>();
        int i = 0;
        for (TicketBooking ticketBooking : booking.getTicketBookings()) {
            if (ticketMap.get(ticketBooking.getId()) == null) {
                throw new BookingNotFoundException();
            }
            if (ticketBooking.getQty() != null && ticketBooking.getQty() > ticketMap.get(ticketBooking.getId()).getQty())
                ticketsError.put(i, ticketMap.get(ticketBooking.getTicket().getId()).getQty());
            i++;
        }

        if (!ticketsError.isEmpty())
            throw new SurpassedMaxTicketsException(ticketsError);

        if (eventBookingDao.cancelBooking(booking))
            mailService.sendCancelMail(booking, locale);
        else
            mailService.sendErrorMail(booking.getUser().getMail());
    }

    @Transactional
    @Override
    public void confirmBooking(EventBooking eventBooking) {
        eventBookingDao.confirmBooking(eventBooking);
    }
}
