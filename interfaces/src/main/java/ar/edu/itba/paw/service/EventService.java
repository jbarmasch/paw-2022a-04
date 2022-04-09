package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Event;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface EventService {
    List<Event> getAll(int page);
    Optional<Event> getEventById(long id);
    Event create(String name, String description, String location, int maxCapacity, double price, String type, Timestamp date);
    List<Event> filterBy(String[] filters, String[] locations, String[] types, Double minPrice, Double maxPrice, int page);
}
