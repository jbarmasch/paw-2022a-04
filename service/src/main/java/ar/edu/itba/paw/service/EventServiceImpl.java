package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.persistence.EventDao;
import ar.edu.itba.paw.persistence.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    private final EventDao eventDao;
//    private final TagDao tagDao;

    @Autowired
    public EventServiceImpl(final EventDao eventDao) {
        this.eventDao = eventDao;
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
    public Event create(String name, String description, int locationId, int maxCapacity, double price, int typeId, LocalDateTime date, int imgId, Integer[] tagIds, int userId) {
        Event event = eventDao.create(name, description, locationId, maxCapacity, price, typeId, date, imgId, tagIds, userId);
//        tagDao.
        return event;
    }

    @Override
    public List<Event> filterBy(String[] locations, String[] types, Double minPrice, Double maxPrice, int page) {
        return eventDao.filterBy(locations, types, minPrice, maxPrice, page);
    }

    @Override
    public void updateEvent(int id, String name, String description, Integer locationId, int maxCapacity, double price, int typeId, LocalDateTime date, int imgId, Integer[] tagIds) {
        eventDao.updateEvent(id, name, description, locationId, maxCapacity, price, typeId, date, imgId, tagIds);
    }

    @Override
    public void deleteEvent(int id) {
        eventDao.deleteEvent(id);
    }

    @Override
    public List<Event> getUserEvents(long id) {
        return eventDao.getUserEvents(id);
    }
}
