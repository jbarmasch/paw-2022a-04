package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Ticket;
import ar.edu.itba.paw.model.Event;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TicketDao {
    void addTicket(Event event, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until);
    Optional<Ticket> getTicketById(long ticketId);
    void updateTicket(Ticket ticket, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until);
    void deleteTicket(long ticketId);
}