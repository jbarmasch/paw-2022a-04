package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Booking;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Ticket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventService {
    List<Event> getAll(int page);
    Optional<Event> getEventById(long id);
    Event create(String name, String description, int locationId, int maxCapacity, double price, int typeId, LocalDateTime date, byte[] imageArray, Integer[] tagIds, int userId);
    List<Event> filterBy(String[] locations, String[] types, String minPrice, String maxPrice, String query, String tags[], String order, String orderBy, int page);
    void updateEvent(int id, String name, String description, Integer locationId, int maxCapacity, double price, int typeId, LocalDateTime date, byte[] imageArray, Integer[] tagIds);
    void deleteEvent(int id);
    List<Event> getUserEvents(long id, int page);
    void book(List<Booking> bookings, long userId, String username, String mail, long eventId, String eventName, String eventMail);
    void cancelBooking(List<Booking> bookings, long userId, String username, String userMail, long eventId, String eventName, String eventMail);
    Integer getAttendanceOfEventId(long eventId);
    void soldOut(int id);
    void active(int id);
    List<Event> getFewTicketsEvents();
    List<Event> getUpcomingEvents();
    List<Event> getSimilarEvents(long eventId);
    List<Event> getPopularEvents(long eventId);
    void addTicket(long eventId, String ticketName, double price, int qty);
    Optional<Ticket> getTicketById(long ticketId);
    void updateTicket(long id, String ticketName, double price, int booked, int qty);
    void deleteTicket(int ticketId);
}
