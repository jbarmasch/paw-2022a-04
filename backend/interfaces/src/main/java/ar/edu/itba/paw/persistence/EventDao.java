package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.EventFinishedException;
import ar.edu.itba.paw.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventDao {
    Optional<Event> getEventById(long id);

    Event createEvent(String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, User organizer, Integer minAge, User bouncer);

    Event createEvent(String name, String description, Location location, Type type, LocalDateTime date, byte[] imageArray, List<Tag> tags, User organizer, Integer minAge, User bouncer);

    EventList filterBy(List<Long> locations, List<Long> types, Double minPrice, Double maxPrice, String query, List<Long> tags, String username, Long userId, Order order, Boolean showSoldOut, Boolean showNoTickets, Boolean showPast, int page);

    void updateEvent(long id, String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, Integer minAge);

    void updateEventImage(Event event, byte[] imageArray);

    void updateEvent(Event event, String name, String description, Location location, Type type, LocalDateTime date, byte[] imageArray, List<Tag> tags, Integer minAge);

    void deleteEvent(Event event);

    void soldOut(Event event) throws EventFinishedException;

    void active(Event event) throws EventFinishedException;

    List<Event> getFewTicketsEvents();

    List<Event> getUpcomingEvents();

    List<Event> getSimilarEvents(long eventId);

    List<Event> getPopularEvents(long eventId);

    List<Event> getUserEvents(long id, int page);

    Optional<EventStats> getEventStats(long id);

    void checkSoldOut(Event event);
}
