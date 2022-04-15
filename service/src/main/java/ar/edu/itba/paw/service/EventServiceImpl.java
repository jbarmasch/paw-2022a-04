package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.persistence.EventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
    public Event create(String name, String description, String location, int maxCapacity, double price, String type, Timestamp date) {
        return eventDao.create(name, description, location, maxCapacity, price, type, date);
    }

    @Override
    public List<Event> filterBy(String[] locations, String[] types, Double minPrice, Double maxPrice, int page) {
        return eventDao.filterBy(locations, types, minPrice, maxPrice, page);
    }
}
