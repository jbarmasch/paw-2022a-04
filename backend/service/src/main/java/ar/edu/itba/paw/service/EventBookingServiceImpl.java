package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.model.EventBooking;
import ar.edu.itba.paw.model.EventBookingList;
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
    private EventService eventService;
    @Autowired
    private MailService mailService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private AuthService authService;

    @Override
    public EventBookingList getAllBookingsFromUser(long userId, int page) {
        if (!(authService.isAuthenticated() && (authService.isTheLoggedUser(userId)))) {
            throw new ForbiddenAccessException();
        }

        return eventBookingDao.getAllBookingsFromUser(userId, page);
    }

    @Override
    public List<TicketBooking> getTicketBookingsFromUser(long userId, long eventId) {
        if (!(authService.isAuthenticated() && (authService.isTheLoggedUser(userId)))) {
            throw new ForbiddenAccessException();
        }

        Optional<EventBooking> eb = eventBookingDao.getBookingFromUser(userId, eventId);
        if (!eb.isPresent() || (eb.get().getEvent().getDate().isBefore(LocalDateTime.now())))
            return Collections.emptyList();

        return eb.get().getTicketBookings();
    }

    @Override
    public Optional<EventBooking> getBooking(String code) {
        if (!(authService.isAuthenticated())) {
            throw new ForbiddenAccessException();
        }

        Optional<EventBooking> eb = eventBookingDao.getBooking(code);

        if (!eb.isPresent()) {
            return Optional.empty();
        }

        if (!(authService.isTheBookingOwner(eb.get().getUser()) ||
                (authService.isBouncer() && authService.isTheEventBouncer(eb.get().getEvent().getBouncer())))) {
            throw new ForbiddenAccessException();
        }

        if (eb.get().getTicketBookingsSize() == 0)
            return Optional.empty();
        return eb;
    }

    @Transactional
    @Override
    public EventBooking book(EventBooking booking, String baseUrl, Locale locale)
            throws AlreadyMaxTicketsException, SurpassedMaxTicketsException {
        if (!(authService.isAuthenticated())) {
            throw new ForbiddenAccessException();
        }

        if (authService.isTheEventOrganizer(booking.getEvent()) || authService.isBouncer()) {
            throw new UserCannotBookException();
        }

        EventBooking persistedBooking = eventBookingDao
                .getBookingFromUser(booking.getUser().getId(), booking.getEvent().getId())
                .orElse(null);

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
            if (ticketBooking.getQty() != null && ticketBooking.getQty() != 0) {
                if (ticketBooking.getTicket().getEvent().getId() != booking.getEvent().getId())
                    throw new IllegalTicketException();
                if (ticketBooking.getTicket().getTicketsLeft() < ticketBooking.getQty() || ticketBooking.getQty() > ticketBooking.getTicket().getMaxPerUser()) {
                    surpassedTicketsError.put(i, ticketBooking.getTicket().getTicketsLeft());
                }
                if (persistedBooking != null) {
                    TicketBooking aux = ticketMap.get(ticketBooking.getTicket().getId());
                    if (aux != null) {
                        if (ticketBooking.getQty() + aux.getQty() > ticketBooking.getTicket().getMaxPerUser()) {
                            alreadyTicketsError.put(i, ticketBooking.getTicket().getMaxPerUser() - aux.getQty());
                        }
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
            TransactionUtil.executeAfterTransaction(() -> mailService.sendBookMail(baseUrl + "/bookings/", eventBooking, locale));
        }

        eventService.checkSoldOut(booking.getEvent().getId());

        return eventBooking;
    }

    @Transactional
    @Override
    public void cancelBooking(String code, Locale locale) {
        if (!authService.isAuthenticated()) {
            throw new ForbiddenAccessException();
        }

        EventBooking booking = eventBookingDao.getBooking(code).orElseThrow(CancelBookingFailedException::new);

        if (!authService.isTheBookingOwner(booking.getUser())) {
            throw new UserCannotCancelException();
        }

        if (eventBookingDao.cancelBooking(booking)) {
            TransactionUtil.executeAfterTransaction(() -> mailService.sendCancelMail(booking, locale));
        }
    }

    @Transactional
    @Override
    public void confirmBooking(EventBooking eventBooking) {
        if (!(authService.isAuthenticated() && authService.isBouncer() &&
                authService.isTheEventBouncer(eventBooking.getEvent().getId()))) {
            throw new ForbiddenAccessException();
        }

        eventBookingDao.confirmBooking(eventBooking);
    }

    @Transactional
    @Override
    public void invalidateBooking(EventBooking eventBooking) {
        if (!(authService.isAuthenticated() && authService.isBouncer() &&
                authService.isTheEventBouncer(eventBooking.getEvent().getId()))) {
            throw new ForbiddenAccessException();
        }

        eventBookingDao.invalidateBooking(eventBooking);
    }
}
