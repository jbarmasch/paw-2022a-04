package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.persistence.EventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    private final EventDao eventDao;

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
    public Event create(String name, String description, Integer location, int maxCapacity, double price, int type, LocalDateTime date) {
        return eventDao.create(name, description, location, maxCapacity, price, type, date);
    }

    @Override
    public List<Event> filterBy(Integer[] locations, String[] types, Double minPrice, Double maxPrice, int page) {
        return eventDao.filterBy(locations, types, minPrice, maxPrice, page);
    }

    @Override
    public void updateEvent(int id, String name, String description, Integer locationId, int maxCapacity, double price, int type, LocalDateTime date) {
        eventDao.updateEvent(id, name, description, locationId, maxCapacity, price, type, date);
    }

    @Override
    public void deleteEvent(int id) {
        eventDao.deleteEvent(id);
    }
}
