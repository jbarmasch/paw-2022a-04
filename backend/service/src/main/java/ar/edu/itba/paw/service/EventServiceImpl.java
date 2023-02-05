package ar.edu.itba.paw.service;

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

    @Override
    public Optional<Event> getEventById(long id) {
        return eventDao.getEventById(id);
    }

    @Transactional
    @Override
    public Event create(String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, long userId, Integer minAge, String baseURL, Locale locale) {
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
        mailService.sendBouncerMail(event, password, baseURL + "/events/" + event.getId(), locale);

        return event;
    }

    @Override
    public EventList filterBy(List<Integer> locations, List<Integer> types, Double minPrice, Double maxPrice, String query, List<Integer> tags, String username, Long userId, Order order, Boolean showSoldOut, int page) {
        for (Integer loc : locations) {
            if (loc == null) {
                // TODO: Change
                throw new RuntimeException();
            }
        }
        for (Integer typ : types) {
            if (typ == null) {
                // TODO: Change
                throw new RuntimeException();
            }
        }
        for (Integer tag : tags) {
            if (tag == null) {
                // TODO: Change
                throw new RuntimeException();
            }
        }
        return eventDao.filterBy(locations, types, minPrice, maxPrice, query, tags, username, userId, order, showSoldOut, page);
    }

    @Transactional
    @Override
    public void updateEvent(long id, String name, String description, long locationId, long typeId, LocalDateTime date, byte[] imageArray, Long[] tagIds, Integer minAge) {
        eventDao.updateEvent(id, name, description, locationId, typeId, date, imageArray, tagIds, minAge);
    }

    @Transactional
    @Override
    public void updateEventImage(long id, byte[] imageArray) {
        eventDao.updateEventImage(id, imageArray);
    }

    @Transactional
    @Override
    public void deleteEvent(long id) {
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
        eventDao.soldOut(id);
    }

    @Transactional
    @Override
    public void active(long id) {
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
        return eventDao.getEventStats(id);
    }
}


