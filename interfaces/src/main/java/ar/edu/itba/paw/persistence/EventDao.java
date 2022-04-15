package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Event;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface EventDao {
    List<Event> getAll(int page);
    Optional<Event> getEventById(long id);
    Event create(String name, String description, String location, int maxCapacity, double price, String type, Timestamp date);
    List<Event> filterBy(String[] locations, String[] types, Double minPrice, Double maxPrice, int page);
}
