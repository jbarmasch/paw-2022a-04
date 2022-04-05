package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Location;

import java.util.List;
import java.util.Optional;

public interface EventDao {
    List<Event> getAll(int page);
    Optional<Event> getEventById(long id);
    Event create(String name, String description, String location, int maxCapacity, double price);
}
