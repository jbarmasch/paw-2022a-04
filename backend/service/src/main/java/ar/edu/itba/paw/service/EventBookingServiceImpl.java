package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.AlreadyMaxTicketsException;
import ar.edu.itba.paw.exceptions.IllegalTicketException;
import ar.edu.itba.paw.exceptions.SurpassedMaxTicketsException;
import ar.edu.itba.paw.exceptions.TicketNotBookedException;
import ar.edu.itba.paw.model.EventBooking;
import ar.edu.itba.paw.model.EventBookingList;
import ar.edu.itba.paw.model.TicketBooking;
import ar.edu.itba.paw.persistence.EventBookingDao;
import ar.edu.itba.paw.persistence.EventDao;
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
    private EventDao eventDao;
    @Autowired
    private MailService mailService;
    @Autowired
    private CodeService codeService;

    @Override
    public EventBookingList getAllBookingsFromUser(long userId, int page) {
        return eventBookingDao.getAllBookingsFromUser(userId, page);
    }

    @Override
    public Optional<EventBooking> getBookingFromUser(long userId, long eventId) {
        Optional<EventBooking> eb = eventBookingDao.getBookingFromUser(userId, eventId);
        if (!eb.isPresent() || (eb.get().getEvent().getDate().isBefore(LocalDateTime.now()) || eb.get().getTicketBookingsSize() == 0))
            return Optional.empty();
        return eb;
    }

    @Override
    public Optional<EventBooking> getBooking(String code) {
        Optional<EventBooking> eb = eventBookingDao.getBooking(code);
       if (!eb.isPresent() || (eb.get().getEvent().getDate().isBefore(LocalDateTime.now()) || eb.get().getTicketBookingsSize() == 0))
    //    if (!eb.isPresent() || eb.get().getTicketBookingsSize() == 0)
            return Optional.empty();
        return eb;
    }

    @Transactional
    @Override
    public EventBooking book(EventBooking booking, String baseUrl, Locale locale) throws AlreadyMaxTicketsException, SurpassedMaxTicketsException {
        EventBooking persistedBooking = eventBookingDao.getBookingFromUser(booking.getUser().getId(), booking.getEvent().getId()).orElse(null);

        if (booking.getUser().getId() == booking.getEvent().getOrganizer().getId()) {
            // TODO: cambiar, no podes bookear tu mismo evento
            throw new IllegalTicketException();
        }

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
        // boolean soldOut = false;
        for (TicketBooking ticketBooking : booking.getTicketBookings()) {
            if (ticketBooking.getQty() != null && ticketBooking.getQty() != 0) {
                if (ticketBooking.getTicket().getEvent().getId() != booking.getEvent().getId())
                    throw new IllegalTicketException();
                if (ticketBooking.getTicket().getTicketsLeft() < ticketBooking.getQty() || ticketBooking.getQty() > ticketBooking.getTicket().getMaxPerUser()) {
                    surpassedTicketsError.put(i, ticketBooking.getTicket().getTicketsLeft());
                }
                // if (ticketBooking.getTicket().getTicketsLeft() - ticketBooking.getQty() == 0) {
                //     soldOut = true;
                // }
                if (persistedBooking != null) {
                    TicketBooking aux = ticketMap.get(ticketBooking.getTicket().getId());
                    if (aux != null) {
                        if (ticketBooking.getQty() + aux.getQty() > ticketBooking.getTicket().getMaxPerUser()) {
                            alreadyTicketsError.put(i, ticketBooking.getTicket().getMaxPerUser() - aux.getQty());
                        }
                        // if (ticketBooking.getQty() + aux.getQty() == ticketBooking.getTicket().getTicketsLeft()) {
                        //     soldOut = true;
                        // }
                    }
                }
            }
            i++;
        }

        // if (soldOut) {
        //     eventDao.soldOut(booking.getEvent().getId());
        // }

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
        }

        eventDao.checkSoldOut(booking.getEvent().getId());

        return eventBooking;
    }

    @Transactional
    @Override
    public void cancelBooking(String code, Locale locale) {
        EventBooking booking = eventBookingDao.getBooking(code).orElse(null);
        
        if (booking == null) {
            // TODO: Change
            throw new RuntimeException();
        }

        if (eventBookingDao.cancelBooking(booking)) {
            TransactionUtil.executeAfterTransaction(() -> mailService.sendCancelMail(booking, locale));
        }
    }

    @Transactional
    @Override
    public void confirmBooking(EventBooking eventBooking) {
        eventBookingDao.confirmBooking(eventBooking);
    }
}
