package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.EventDao;
import ar.edu.itba.paw.persistence.UserDao;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventDao eventDao;
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private CodeService codeService;

    @Override
    public Optional<Event> getEventById(long id, Locale locale) {
        return eventDao.getEventById(id, locale);
    }

    @Transactional
    @Override
    public Event create(String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, long userId, Integer minAge, Locale locale) {
        return eventDao.create(name, description, locationId, typeId, date, imageArray, tagIds, userId, minAge, locale);
    }

    @Override
    public List<Event> filterBy(Integer[] locations, Integer[] types, Double minPrice, Double maxPrice, String query, Integer[] tags, String username, Order order, Boolean showSoldOut, int page, Locale locale) {
        return eventDao.filterBy(locations, types, minPrice, maxPrice, query, tags, username, order, showSoldOut, page, locale);
    }

    @Transactional
    @Override
    public void updateEvent(long id, String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, Integer minAge) {
        eventDao.updateEvent(id, name, description, locationId, typeId, date, imageArray, tagIds, minAge);
    }

    @Transactional
    @Override
    public void deleteEvent(long id) {
        eventDao.deleteEvent(id);
    }

    @Transactional
    @Override
    public void book(EventBooking booking, String baseUrl, Locale locale) {
        String code = codeService.getCode(booking.getUser().getId() + ":" + booking.getEvent().getId());
        booking.setCode(code);

        if (eventDao.book(booking)) {
            booking = userService.getBookingFromUser(booking.getUser().getId(), booking.getEvent().getId(), locale).orElse(null);
            if (booking == null)
                return;
            mailService.sendBookMail(baseUrl + "/bookings/" + booking.getCode(), booking, locale);
        } else
            mailService.sendErrorMail(booking.getUser().getMail());
    }

    @Transactional
    @Override
    public void cancelBooking(EventBooking booking, Locale locale) {
        if (eventDao.cancelBooking(booking)) {
            booking = userService.getBooking(booking.getCode(), locale).orElse(null);
            if (booking == null)
                return;
            mailService.sendCancelMail(booking, locale);
        } else
            mailService.sendErrorMail(booking.getUser().getMail());
    }

    @Override
    public List<Event> getFewTicketsEvents(Locale locale) {
        return eventDao.getFewTicketsEvents(locale);
    }

    @Override
    public List<Event> getUpcomingEvents(Locale locale) {
        return eventDao.getUpcomingEvents(locale);
    }

    @Transactional
    @Override
    public void soldOut(long id) {
        eventDao.soldOut(id);
    }

    @Transactional
    @Override
    public void active(long id) {
        eventDao.active(id);
    }

    @Override
    public List<Event> getSimilarEvents(long eventId, Locale locale) {
        return eventDao.getSimilarEvents(eventId, locale);
    }

    @Override
    public List<Event> getPopularEvents(long eventId, Locale locale) {
        return eventDao.getPopularEvents(eventId, locale);
    }

    @Transactional
    @Override
    public void addTicket(long eventId, String ticketName, double price, int qty) {
        eventDao.addTicket(eventId, ticketName, price, qty);
    }

    @Override
    public Optional<Ticket> getTicketById(long ticketId) {
        return eventDao.getTicketById(ticketId);
    }

    @Transactional
    @Override
    public void updateTicket(long id, String ticketName, double price, int qty) {
        eventDao.updateTicket(id, ticketName, price, qty);
    }

    @Transactional
    @Override
    public void deleteTicket(long ticketId) {
        eventDao.deleteTicket(ticketId);
    }
}


