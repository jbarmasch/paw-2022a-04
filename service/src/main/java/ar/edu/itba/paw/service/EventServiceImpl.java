package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.persistence.EventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    @Override
    public Event create(String name, String description, int locationId, int maxCapacity, double price, int typeId, LocalDateTime date, byte[] imageArray, Integer[] tagIds, int userId) {
        int imageId = 1;
        if (imageArray != null)
            imageId = imageService.addEventImage(imageArray);
        return eventDao.create(name, description, locationId, maxCapacity, price, typeId, date, imageId, tagIds, userId);
    }

    @Override
    public List<Event> filterBy(String[] locations, String[] types, String minPrice, String maxPrice, int page) {
        return eventDao.filterBy(locations, types, minPrice, maxPrice, page);
    }

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

    @Override
    public boolean book(int qty, long userId, String username, String userMail, long eventId, String eventName, String eventMail) {
        if (eventDao.book(qty, userId, eventId)) {
            mailService.sendBookMail(userMail, username, eventMail, eventName, qty);
            return true;
        }
        mailService.sendErrorMail(userMail, eventName);
        return false;
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
}


