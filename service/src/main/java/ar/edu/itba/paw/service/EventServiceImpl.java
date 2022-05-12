package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.EventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    private final EventDao eventDao;
    private final MailService mailService;
    private final ImageService imageService;

    @Autowired
    public EventServiceImpl(final EventDao eventDao, final MailService mailService, final ImageService imageService) {
        this.eventDao = eventDao;
        this.mailService = mailService;
        this.imageService = imageService;
    }

    @Override
    public Optional<Event> getEventById(long id, Locale locale) {
        return eventDao.getEventById(id, locale);
    }

    @Transactional
    @Override
    public Event create(String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Integer[] tagIds, long userId, Locale locale) {
        int imageId = 1;
        if (imageArray != null)
            imageId = imageService.addEventImage(imageArray);
        return eventDao.create(name, description, locationId, typeId, date, imageId, tagIds, userId, locale);
    }

    @Override
    public List<Event> filterBy(Integer[] locations, Integer[] types, Double minPrice, Double maxPrice, String query, Integer[] tags, String username, Order order, int page, Locale locale) {
        return eventDao.filterBy(locations, types, minPrice, maxPrice, query, tags, username, order, page, locale);
    }

    @Transactional
    @Override
    public void updateEvent(long id, String name, String description, Integer locationId, long typeId, LocalDateTime date, byte[] imageArray, Integer[] tagIds) {
        int imageId = 1;
        if (imageArray != null)
            imageId = imageService.addEventImage(imageArray);
        eventDao.updateEvent(id, name, description, locationId, typeId, date, imageId, tagIds);
    }

    @Override
    public void deleteEvent(long id) {
        eventDao.deleteEvent(id);
    }

    @Override
    public List<Event> getUserEvents(long id, int page, Locale locale) {
        return eventDao.getUserEvents(id, page, locale);
    }

    @Transactional
    @Override
    public void book(List<Booking> bookings, long userId, String username, String userMail, long eventId, String organizerName, String eventName, String eventMail, Locale locale) {
        if (eventDao.isFinished(eventId))
            return;
        eventDao.book(bookings, userId, eventId, locale);
        mailService.sendBookMail(userMail, username, eventMail, organizerName, eventName, bookings, bookings.stream().filter(o -> o.getQty() != null).mapToInt(Booking::getQty).sum(), locale);
    }

    @Transactional
    @Override
    public void cancelBooking(List<Booking> bookings, long userId, String username, String userMail, long eventId, String eventName, String organizerName, String eventMail, Locale locale) {
        if (eventDao.isFinished(eventId))
            return;
        eventDao.cancelBooking(bookings, userId, eventId);
        mailService.sendCancelMail(userMail, username, eventMail, organizerName, eventName, bookings, bookings.stream().filter(o -> o.getQty() != null).mapToInt(Booking::getQty).sum(), locale);
    }

    @Override
    public List<Event> getFewTicketsEvents(Locale locale){
        return eventDao.getFewTicketsEvents(locale);
    }

    @Override
    public List<Event> getUpcomingEvents(Locale locale){
        return eventDao.getUpcomingEvents(locale);
    }

    @Override
    public void soldOut(long id) {
        eventDao.soldOut(id);
    }

    @Override
    public void active(long id) {
        eventDao.active(id);
    }

    @Override
    public List<Event> getSimilarEvents(long eventId, Locale locale) {
        return eventDao.getSimilarEvents(eventId, locale);
    }

    @Override
    public List<Event> getPopularEvents(long eventId, Locale locale) {
        return eventDao.getPopularEvents(eventId, locale);
    }

    @Override
    public void addTicket(long eventId, String ticketName, double price, int qty) {
        eventDao.addTicket(eventId, ticketName, price, qty);
    }

    @Override
    public Optional<Ticket> getTicketById(long ticketId) {
        return eventDao.getTicketById(ticketId);
    }

    @Override
    public void updateTicket(long id, String ticketName, double price, int qty) {
        eventDao.updateTicket(id, ticketName, price, qty);
    }

    @Override
    public void deleteTicket(long ticketId) {
        eventDao.deleteTicket(ticketId);
    }
}


