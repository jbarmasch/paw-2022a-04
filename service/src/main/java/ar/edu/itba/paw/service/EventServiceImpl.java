package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.EventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventDao eventDao;
    @Autowired
    private MailService mailService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private UserService userService;

    @Override
    public Optional<Event> getEventById(long id) {
        return eventDao.getEventById(id);
    }

    @Transactional
    @Override
    public Event create(String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, long userId, Integer minAge) {
        return eventDao.create(name, description, locationId, typeId, date, imageArray, tagIds, userId, minAge);
    }

    @Override
    public List<Event> filterBy(Integer[] locations, Integer[] types, Double minPrice, Double maxPrice, String query, Integer[] tags, String username, Order order, Boolean showSoldOut, int page) {
        return eventDao.filterBy(locations, types, minPrice, maxPrice, query, tags, username, order, showSoldOut, page);
    }

    @Transactional
    @Override
    public void updateEvent(long id, String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, Integer minAge) {
        eventDao.updateEvent(id, name, description, locationId, typeId, date, imageArray, tagIds, minAge);
    }

    @Transactional
    @Override
    public void deleteEvent(long id) {
        eventDao.deleteEvent(id);
    }

    @Transactional
    @Override
    public void book(EventBooking booking, String baseUrl, Locale locale) {
        String code = codeService.getCode(booking.getUser().getId() + ":" + booking.getEvent().getId());
        booking.setCode(code);

        EventBooking eventBooking = eventDao.book(booking);
        if (eventBooking != null) {
            TransactionUtil.executeAfterTransaction(() -> mailService.sendBookMail(baseUrl + "/bookings/" + eventBooking.getCode(), eventBooking, locale));
//            mailService.sendBookMail(baseUrl + "/bookings/" + eventBooking.getCode(), eventBooking, locale);
        }
        else
            mailService.sendErrorMail(booking.getUser().getMail());
    }

    @Transactional
    @Override
    public void cancelBooking(EventBooking booking, Locale locale) {
        if (eventDao.cancelBooking(booking))
            mailService.sendCancelMail(booking, locale);
        else
            mailService.sendErrorMail(booking.getUser().getMail());
    }

    @Override
    public List<Event> getFewTicketsEvents() {
        return eventDao.getFewTicketsEvents();
    }

    @Override
    public List<Event> getUpcomingEvents() {
        return eventDao.getUpcomingEvents();
    }

    @Transactional
    @Override
    public void soldOut(long id) {
        eventDao.soldOut(id);
    }

    @Transactional
    @Override
    public void active(long id) {
        eventDao.active(id);
    }

    @Override
    public List<Event> getSimilarEvents(long eventId) {
        return eventDao.getSimilarEvents(eventId);
    }

    @Override
    public List<Event> getPopularEvents(long eventId) {
        return eventDao.getPopularEvents(eventId);
    }

    @Transactional
    @Override
    public void addTicket(long eventId, String ticketName, double price, int qty) {
        eventDao.addTicket(eventId, ticketName, price, qty);
    }

    @Override
    public Optional<Ticket> getTicketById(long ticketId) {
        return eventDao.getTicketById(ticketId);
    }

    @Transactional
    @Override
    public void updateTicket(long id, String ticketName, double price, int qty) {
        eventDao.updateTicket(id, ticketName, price, qty);
    }

    @Transactional
    @Override
    public void deleteTicket(long ticketId) {
        eventDao.deleteTicket(ticketId);
    }
}


