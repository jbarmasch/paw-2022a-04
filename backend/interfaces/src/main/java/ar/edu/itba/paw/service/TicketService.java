package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Ticket;
import ar.edu.itba.paw.model.TicketStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    void addTicket(Event event, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until, Integer maxPerUser);

    Optional<Ticket> getTicketById(long ticketId);

    List<Ticket> getTickets(long eventId);

    void updateTicket(Ticket ticket, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until, Integer maxPerUser);

    void deleteTicket(long ticketId);

    List<TicketStats> getTicketStats(long id);
}
