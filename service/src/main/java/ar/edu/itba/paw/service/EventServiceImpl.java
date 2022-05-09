package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.EventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
    public List<Event> getAll(int page) {
        return eventDao.getAll(page);
    }

    @Override
    public Optional<Event> getEventById(long id) {
        return eventDao.getEventById(id);
    }

    @Transactional
    @Override
    public Event create(String name, String description, int locationId, int maxCapacity, double price, int typeId, LocalDateTime date, byte[] imageArray, Integer[] tagIds, int userId) {
        int imageId = 1;
        if (imageArray != null)
            imageId = imageService.addEventImage(imageArray);
        return eventDao.create(name, description, locationId, maxCapacity, price, typeId, date, imageId, tagIds, userId);
    }

    @Override
    public List<Event> filterBy(String[] locations, String[] types, String minPrice, String maxPrice, String query, String[] tags, String username, String order, String orderBy, int page) {
        return eventDao.filterBy(locations, types, minPrice, maxPrice, query, tags, username, order, orderBy, page);
    }

    @Transactional
    @Override
    public void updateEvent(int id, String name, String description, Integer locationId, int maxCapacity, double price, int typeId, LocalDateTime date, byte[] imageArray, Integer[] tagIds) {
        int imageId = 1;
        if (imageArray != null)
            imageId = imageService.addEventImage(imageArray);
        eventDao.updateEvent(id, name, description, locationId, maxCapacity, price, typeId, date, imageId, tagIds);
    }

    @Override
    public void deleteEvent(int id) {
        eventDao.deleteEvent(id);
    }

    @Override
    public List<Event> getUserEvents(long id, int page) {
        return eventDao.getUserEvents(id, page);
    }

    @Transactional
    @Override
    public void book(List<Booking> bookings, long userId, String username, String userMail, long eventId, String eventName, String eventMail) {
        eventDao.book(bookings, userId, eventId);
        mailService.sendBookMail(userMail, username, eventMail, eventName, bookings.stream().filter(o -> o.getQty() != null).mapToInt(Booking::getQty).sum());
    }

    @Transactional
    @Override
    public void cancelBooking(List<Booking> bookings, long userId, String username, String userMail, long eventId, String eventName, String eventMail) {
        eventDao.cancelBooking(bookings, userId, eventId);
        mailService.sendCancelMail(userMail, username, eventMail, eventName, bookings.stream().filter(o -> o.getQty() != null).mapToInt(Booking::getQty).sum());
//        mailService.sendErrorMail(userMail, eventName);
    }

    @Override
    public List<Event> getFewTicketsEvents(){
        return eventDao.getFewTicketsEvents();
    }

    @Override
    public List<Event> getUpcomingEvents(){
        return eventDao.getUpcomingEvents();
    }

    @Override
    public Integer getAttendanceOfEventId(long eventId) {
        return eventDao.getAttendanceOfEventId(eventId);
    }
    @Override
    public void soldOut(int id) {
        eventDao.soldOut(id);
    }

    @Override
    public void active(int id) {
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

    @Override
    public void addTicket(long eventId, String ticketName, double price, int qty) {
        eventDao.addTicket(eventId, ticketName, price, qty);
    }

    @Override
    public Optional<Ticket> getTicketById(long ticketId) {
        return eventDao.getTicketById(ticketId);
    }

    @Override
    public void updateTicket(long id, String ticketName, double price, int booked, int qty) {
        eventDao.updateTicket(id, ticketName, price, booked, qty);
    }

    @Override
    public void deleteTicket(int ticketId) {
        eventDao.deleteTicket(ticketId);
    }
}


