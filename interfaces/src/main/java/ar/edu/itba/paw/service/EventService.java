package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Location;

import java.util.List;
import java.util.Optional;

public interface EventService {
    List<Event> getAll(int page);
    Optional<Event> getEventById(long id);
    Event create(String name, String description, String location, int maxCapacity, double price);
    List<Event> filterBy(String[] filters, String[] locations, Double minPrice, Double maxPrice, int page);
}
