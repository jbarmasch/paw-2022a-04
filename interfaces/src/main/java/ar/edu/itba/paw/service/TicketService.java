package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.DateRangeException;
import ar.edu.itba.paw.exceptions.TicketUnderflowException;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Ticket;
import ar.edu.itba.paw.model.TicketStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketService {
    void addTicket(Event event, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until, Integer maxPerUser) throws DateRangeException;

    Optional<Ticket> getTicketById(long ticketId);

    void updateTicket(Ticket ticket, String ticketName, double price, int qty, LocalDateTime starting, LocalDateTime until, Integer maxPerUser) throws TicketUnderflowException;

    void deleteTicket(long ticketId);

    List<TicketStats> getTicketStats(long id);
}
