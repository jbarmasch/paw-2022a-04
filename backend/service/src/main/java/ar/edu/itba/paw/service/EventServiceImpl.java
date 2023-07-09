package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.FilterException;
import ar.edu.itba.paw.exceptions.ForbiddenAccessException;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.EventDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventDao eventDao;
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private AuthService authService;

    @Override
    public Optional<Event> getEventById(long id) {
        return eventDao.getEventById(id);
    }

    @Transactional
    @Override
    public Event create(String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, long userId, Integer minAge, String baseURL, Locale locale) {
        if (!(authService.isAuthenticated())) {
            throw new ForbiddenAccessException();
        }

        Random random = new SecureRandom();
        IntStream specialChars = random.ints(8, 48, 58);
        Stream<Character> passwordStream = specialChars.mapToObj(data -> (char) data);
        specialChars = random.ints(8, 65, 91);
        passwordStream = Stream.concat(passwordStream, specialChars.mapToObj(data -> (char) data));
        specialChars = random.ints(8, 97, 123);
        passwordStream = Stream.concat(passwordStream, specialChars.mapToObj(data -> (char) data));
        List<Character> charList = passwordStream.collect(Collectors.toList());
        Collections.shuffle(charList);
        String password = charList.stream().collect(StringBuilder::new, StringBuilder::append, StringBuilder::append).substring(0, 8);

        User bouncer = userService.createBouncer(password);
        User organizer = userService.getUserById(userId).orElse(null);
        Event event = eventDao.createEvent(name, description, locationId, typeId, date, imageArray, tagIds, organizer, minAge, bouncer);
        userService.makeCreator(organizer);
        userService.updateUser(bouncer.getId(), String.valueOf(event.getId()), null, String.valueOf(event.getId()));
        mailService.sendBouncerMail(event, password, baseURL + "events/" + event.getId(), locale);

        System.out.println("BOUNCER" + event.getId() + ":" + password);
        return event;
    }

    @Override
    public EventList filterBy(List<Long> locations, List<Long> types, Double minPrice, Double maxPrice, String query, List<Long> tags, String username, Long userId, Order orderBy, Boolean showSoldOut, Boolean showNoTickets, Boolean showPast, Long similarEvent, Long recommendedEvent, Boolean fewTickets, Boolean upcoming, int page) {
        if (similarEvent != null) {
            return new EventList(eventDao.getSimilarEvents(similarEvent), 1);
        }
        if (recommendedEvent != null) {
            return new EventList(eventDao.getPopularEvents(recommendedEvent), 1);
        }
        if (fewTickets != null) {
            return new EventList(eventDao.getFewTicketsEvents(), 1);
        }
        if (upcoming != null) {
            return new EventList(eventDao.getUpcomingEvents(), 1);
        }

        for (Long location : locations) {
            if (location == null) {
                throw new FilterException();
            }
        }
        for (Long type : types) {
            if (type == null) {
                throw new FilterException();
            }
        }
        for (Long tag : tags) {
            if (tag == null) {
                throw new FilterException();
            }
        }
        return eventDao.filterBy(locations, types, minPrice, maxPrice, query, tags, username, userId, orderBy, showSoldOut, showNoTickets, showPast, page);
    }

    @Transactional
    @Override
    public void updateEvent(long id, String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, Integer minAge) {
        if (!(authService.isAuthenticated() && authService.isTheEventOrganizer(id))) {
            throw new ForbiddenAccessException();
        }

        eventDao.updateEvent(id, name, description, locationId, typeId, date, imageArray, tagIds, minAge);
    }

    @Transactional
    @Override
    public void updateEventImage(long id, byte[] imageArray) {
        if (!(authService.isAuthenticated() && authService.isTheEventOrganizer(id))) {
            throw new ForbiddenAccessException();
        }

        eventDao.updateEventImage(id, imageArray);
    }

    @Transactional
    @Override
    public void deleteEvent(long id) {
        if (!(authService.isAuthenticated() && authService.isTheEventOrganizer(id))) {
            throw new ForbiddenAccessException();
        }

        eventDao.deleteEvent(id);
    }

    @Override
    public List<Event> getFewTicketsEvents() {
        return eventDao.getFewTicketsEvents();
    }

    @Override
    public List<Event> getUpcomingEvents() {
        return eventDao.getUpcomingEvents();
    }

    @Transactional
    @Override
    public void soldOut(long id) {
        if (!(authService.isAuthenticated() && authService.isTheEventOrganizer(id))) {
            throw new ForbiddenAccessException();
        }

        eventDao.soldOut(id);
    }

    @Transactional
    @Override
    public void active(long id) {
        if (!(authService.isAuthenticated() && authService.isTheEventOrganizer(id))) {
            throw new ForbiddenAccessException();
        }

        eventDao.active(id);
    }

    @Override
    public List<Event> getSimilarEvents(long eventId) {
        return eventDao.getSimilarEvents(eventId);
    }

    @Override
    public List<Event> getPopularEvents(long eventId) {
        return eventDao.getPopularEvents(eventId);
    }

    @Override
    public List<Event> getUserEvents(long id, int page) {
        return eventDao.getUserEvents(id, page);
    }

    @Override
    public Optional<EventStats> getEventStats(long id) {
        if (!(authService.isAuthenticated() && authService.isTheEventOrganizer(id))) {
            throw new ForbiddenAccessException();
        }

        return eventDao.getEventStats(id);
    }

    @Override
    public void checkSoldOut(long id) {
        eventDao.checkSoldOut(id);
    }
}


