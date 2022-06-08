package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface EventService {
    Optional<Event> getEventById(long id);
    Event create(String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, long userId, Integer minAge, String baseURL, Locale locale);
    List<Event> filterBy(Integer[] locations, Integer[] types, Double minPrice, Double maxPrice, String query, Integer[] tags, String username, Order orderBy, Boolean showSoldOut, int page);
    void updateEvent(long id, String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, Integer minAge);
    void deleteEvent(long id);
    void soldOut(long id);
    void active(long id);
    List<Event> getFewTicketsEvents();
    List<Event> getUpcomingEvents();
    List<Event> getSimilarEvents(long eventId);
    List<Event> getPopularEvents(long eventId);
    List<Event> getUserEvents(long id, int page);
}
