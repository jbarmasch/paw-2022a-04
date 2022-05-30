package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface EventService {
    Optional<Event> getEventById(long id, Locale locale);
    Event create(String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, long userId, Integer minAge, Locale locale);
    List<Event> filterBy(Integer[] locations, Integer[] types, Double minPrice, Double maxPrice, String query, Integer[] tags, String username, Order orderBy, Boolean showSoldOut, int page, Locale locale);
    void updateEvent(long id, String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, Integer minAge);
    void deleteEvent(long id);
    void book(EventBooking booking, String baseURL, Locale locale);
    void cancelBooking(EventBooking booking, Locale locale);
    void soldOut(long id);
    void active(long id);
    List<Event> getFewTicketsEvents(Locale locale);
    List<Event> getUpcomingEvents(Locale locale);
    List<Event> getSimilarEvents(long eventId, Locale locale);
    List<Event> getPopularEvents(long eventId, Locale locale);
    void addTicket(long eventId, String ticketName, double price, int qty);
    Optional<Ticket> getTicketById(long ticketId);
    void updateTicket(long id, String ticketName, double price, int qty);
    void deleteTicket(long ticketId);
}
