package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Event;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface EventDao {
    List<Event> getAll(int page);
    Optional<Event> getEventById(long id);
    Event create(String name, String description, Integer location, int maxCapacity, double price, int type, Timestamp date);
    List<Event> filterBy(Integer[] locations, String[] types, Double minPrice, Double maxPrice, int page);
    void updateEvent(int id, String name, String description, Integer locationId, int maxCapacity, double price, int type, Timestamp date);
    void deleteEvent(int id);
}
