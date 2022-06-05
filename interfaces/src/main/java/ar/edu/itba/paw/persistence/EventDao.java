package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface EventDao {
    Optional<Event> getEventById(long id);
    Event create(String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, long userId, Integer minAge, String bouncerPass);
    List<Event> filterBy(Integer[] locations, Integer[] types, Double minPrice, Double maxPrice, String query, Integer[] tags, String username, Order order, Boolean showSoldOut, int page);
    void updateEvent(long id, String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, Integer minAge);
    void deleteEvent(long id);
    EventBooking book(EventBooking booking);
    boolean cancelBooking(EventBooking booking);
    void soldOut(long id);
    void active(long id);
    List<Event> getFewTicketsEvents();
    List<Event> getUpcomingEvents();
    List<Event> getSimilarEvents(long eventId);
    List<Event> getPopularEvents(long eventId);
    void addTicket(long eventId, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until);
    Optional<Ticket> getTicketById(long ticketId);
    void updateTicket(long id, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until);
    void deleteTicket(long ticketId);
    List<TicketBooking> getTicketBookings(long ticketId);
}
