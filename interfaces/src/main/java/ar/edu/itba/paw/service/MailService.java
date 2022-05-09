package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Booking;

import java.util.List;
import java.util.Locale;

public interface MailService {
    void sendBookMail(String userMail, String username, String eventMail, String organizerName, String eventName, List<Booking> bookings, int qty, Locale locale);
    void sendCancelMail(String userMail, String username, String eventMail, String organizerName, String eventName, List<Booking> bookings, int qty, Locale locale);
    void sendErrorMail(String userMail, String eventName);
}
