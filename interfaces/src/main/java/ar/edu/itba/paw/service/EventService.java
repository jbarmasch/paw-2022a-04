package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.EventFinishedException;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.EventList;
import ar.edu.itba.paw.model.EventStats;
import ar.edu.itba.paw.model.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface EventService {
    Optional<Event> getEventById(long id);

    Event create(String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, long userId, Integer minAge, String baseURL, Locale locale);

    EventList filterBy(List<Integer> locations, List<Integer> types, Double minPrice, Double maxPrice, String query, List<Integer> tags, String username, Order orderBy, Boolean showSoldOut, int page);

    void updateEvent(long id, String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, Integer minAge);

    void deleteEvent(long id);

    void soldOut(long id) throws EventFinishedException;

    void active(long id) throws EventFinishedException;

    List<Event> getFewTicketsEvents();

    List<Event> getUpcomingEvents();

    List<Event> getSimilarEvents(long eventId);

    List<Event> getPopularEvents(long eventId);

    List<Event> getUserEvents(long id, int page);

    Optional<EventStats> getEventStats(long id);
}
