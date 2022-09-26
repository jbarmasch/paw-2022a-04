package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.EventBooking;
import ar.edu.itba.paw.model.TicketBooking;

import java.util.Locale;

public interface MailService {
    void sendBookMail(String URL, EventBooking booking, Locale locale);

    void sendCancelMail(EventBooking booking, Locale locale);

    void sendCancelTicketMail(TicketBooking ticketBooking);

    void sendBouncerMail(Event event, String password, String eventURL, Locale locale);
}
