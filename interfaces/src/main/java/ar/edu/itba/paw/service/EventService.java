package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventService {
    List<Event> getAll(int page);
    Optional<Event> getEventById(long id);
    Event create(String name, String description, int locationId, int maxCapacity, double price, int typeId, LocalDateTime date, int imgId, Integer[] tagIds, int userId);
    List<Event> filterBy(String[] locations, String[] types, Double minPrice, Double maxPrice, int page);
    void updateEvent(int id, String name, String description, Integer locationId, int maxCapacity, double price, int typeId, LocalDateTime date, int imgId, Integer[] tagIds);
    void deleteEvent(int id);
    List<Event> getUserEvents(long id);
    boolean book(int qty, long userId, long eventId);
    Integer getAttendanceOfEventId(long eventId);
}
