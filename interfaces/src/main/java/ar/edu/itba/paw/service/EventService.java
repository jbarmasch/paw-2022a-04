package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Booking;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Order;
import ar.edu.itba.paw.model.Ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface EventService {
    Optional<Event> getEventById(long id, Locale locale);
    Event create(String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Integer[] tagIds, long userId, Locale locale);
    List<Event> filterBy(Integer[] locations, Integer[] types, Double minPrice, Double maxPrice, String query, Integer[] tags, String username, Order orderBy, int page, Locale locale);
    void updateEvent(long id, String name, String description, Integer locationId, long typeId, LocalDateTime date, byte[] imageArray, Integer[] tagIds);
    void deleteEvent(long id);
    List<Event> getUserEvents(long id, int page, Locale locale);
    void book(List<Booking> bookings, long userId, String username, String mail, long eventId, String organizerName, String eventName, String eventMail, Locale locale);
    void cancelBooking(List<Booking> bookings, long userId, String username, String userMail, long eventId, String organizerName, String eventName, String eventMail, Locale locale);
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
