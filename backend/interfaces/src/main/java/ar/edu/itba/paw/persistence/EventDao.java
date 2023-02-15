package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.EventFinishedException;
import ar.edu.itba.paw.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventDao {
    Optional<Event> getEventById(long id);

    Event createEvent(String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, User organizer, Integer minAge, User bouncer);

    EventList filterBy(List<Integer> locations, List<Integer> types, Double minPrice, Double maxPrice, String query, List<Integer> tags, String username, Long userId, Order order, Boolean showSoldOut, Boolean showNoTickets, Boolean showPast, int page);

    void updateEvent(long id, String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, Integer minAge);

    void updateEventImage(long id, byte[] imageArray);

    void deleteEvent(long id);

    void soldOut(long id) throws EventFinishedException;

    void active(long id) throws EventFinishedException;

    List<Event> getFewTicketsEvents();

    List<Event> getUpcomingEvents();

    List<Event> getSimilarEvents(long eventId);

    List<Event> getPopularEvents(long eventId);

    List<Event> getUserEvents(long id, int page);

    Optional<EventStats> getEventStats(long id);

    void checkSoldOut(long id);
}
