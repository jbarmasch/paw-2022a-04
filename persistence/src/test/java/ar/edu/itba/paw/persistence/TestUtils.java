package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

public final class TestUtils {
    private TestUtils() {
        throw new UnsupportedOperationException();
    }

    public static final byte[] BYTE_ARRAY = new byte[] {0x00};
    public final static String GENERIC = "genericName";
    public final static String GENERIC2 = "generic2";
    public final static String GENERIC3 = "generic3";
    public final static String EVENT_NAME = "eventName";
    public final static String EVENT_NAME_2 = "eventName2";
    public final static String EVENT_DESC = "eventDesc";
    public final static String USERNAME = "user";
    public final static String ORGANIZER_USERNAME = "org";
    public final static String BOUNCER_USERNAME = "bouncer";
    public final static String PASSWORD = "pass";
    public final static String MAIL = "user@pass.user";
    public final static String USERNAME2 = "USER2";
    public final static String PASSWORD2 = "pass2";
    public final static String MAIL2 = "USER2@pass2.USER2";
    public final static LocalDateTime EVENT_DATE = LocalDateTime.of(3025, Month.JULY, 29, 19, 30, 40);
    public final static LocalDateTime EVENT_DATE_BEFORE = LocalDateTime.of(2005, Month.JULY, 29, 19, 30, 40);
    public final static LocalDateTime RATEABLE_DATE = LocalDateTime.now().minusDays(7);
    public final static LocalDateTime EVENT_DATE_LATER = LocalDateTime.of(3050, Month.JULY, 29, 19, 30, 40);
    public final static String TICKET_NAME = "ticketName";
    public final static String TICKET_NAME2 = "ticketName2";
    public final static Double TICKET_PRICE = 10.0;
    public final static Integer TICKET_QTY = 10;
    public final static Integer TICKET_MAX_PER_USER = 10;
    public final static String EVENT_BOOKING_CODE = "code";
    public final static String EVENT_BOOKING_CODE2 = "code2";
    public final static Locale LOCALE = new Locale("es");
    public final static String TYPE_NAME = "tipo";
    public final static String TYPE_NAME_EN = "type";
    public final static String ROLE_NAME_USER = "ROLE_USER";
    public final static String ROLE_NAME_CREATOR = "ROLE_CREATOR";
    public final static String ROLE_NAME_BOUNCER = "ROLE_BOUNCER";
    public static int BOOKING_QTY = 5;

    public static User USER1;
    public static User USER2;
    public static User ORGANIZER;
    public static User BOUNCER;
    public static Ticket TICKET;
    public static Event EVENT;
    public static Event PAST_EVENT;
    public static Event FUTURE_EVENT;
    public static Tag GENERIC_TAG;
    public static Location GENERIC_LOCATION;
    public static Type GENERIC_TYPE;
    public static Image IMAGE;
    public static Ticket GENERIC_TICKET;

    public static void deleteTables(EntityManager em) {
        em.createNativeQuery("DELETE FROM ticketbookings").executeUpdate();
        em.createNativeQuery("DELETE FROM tickets").executeUpdate();
        em.createNativeQuery("DELETE FROM eventbookings").executeUpdate();
        em.createNativeQuery("DELETE FROM eventtags").executeUpdate();
        em.createNativeQuery("DELETE FROM events").executeUpdate();
        em.createNativeQuery("DELETE FROM tags").executeUpdate();
        em.createNativeQuery("DELETE FROM types").executeUpdate();
        em.createNativeQuery("DELETE FROM locations").executeUpdate();
        em.createNativeQuery("DELETE FROM ratings").executeUpdate();
        em.createNativeQuery("DELETE FROM userroles").executeUpdate();
        em.createNativeQuery("DELETE FROM users").executeUpdate();
        em.createNativeQuery("DELETE FROM images").executeUpdate();
        em.createNativeQuery("DELETE FROM roles").executeUpdate();
        em.createNativeQuery("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK").executeUpdate();
    }

    public static void setUpEvent(EntityManager em) {
        GENERIC_TAG = new Tag(GENERIC, GENERIC);
        GENERIC_LOCATION = new Location(GENERIC);
        GENERIC_TYPE = new Type(GENERIC, GENERIC);
        IMAGE = new Image(BYTE_ARRAY);
        Role role = new Role(GENERIC);
        em.persist(role);
        USER1 = new User(USERNAME, PASSWORD, MAIL, role);
        USER2 = new User(USERNAME2, PASSWORD2, MAIL2, role);
        em.persist(GENERIC_TAG);
        em.persist(GENERIC_LOCATION);
        em.persist(GENERIC_TYPE);
        em.persist(IMAGE);
        em.persist(USER1);
        em.persist(USER2);
    }

    public static void setUpUser(EntityManager em) {
        GENERIC_TAG = new Tag(GENERIC, GENERIC);
        GENERIC_LOCATION = new Location(GENERIC);
        GENERIC_TYPE = new Type(GENERIC, GENERIC);
        IMAGE = new Image(BYTE_ARRAY);
        Role user = new Role(ROLE_NAME_USER);
        Role creator = new Role(ROLE_NAME_CREATOR);
        Role bouncer = new Role(ROLE_NAME_BOUNCER);
        em.persist(user);
        em.persist(creator);
        em.persist(bouncer);
        USER1 = new User(USERNAME, PASSWORD, MAIL, user);


        em.persist(GENERIC_TAG);
        em.persist(GENERIC_LOCATION);
        em.persist(GENERIC_TYPE);
        em.persist(IMAGE);
        em.persist(USER1);
    }

    public static void setUpEventBooking(EntityManager em) {
        GENERIC_TAG = new Tag(GENERIC, GENERIC);
        GENERIC_LOCATION = new Location(GENERIC);
        GENERIC_TYPE = new Type(GENERIC, GENERIC);
        IMAGE = new Image(BYTE_ARRAY);
        Role role = new Role(GENERIC);
        em.persist(role);
        USER1 = new User(USERNAME, PASSWORD, MAIL, role);
        USER2 = new User(USERNAME2, PASSWORD2, MAIL2, role);
        em.persist(GENERIC_TAG);
        em.persist(GENERIC_LOCATION);
        em.persist(GENERIC_TYPE);
        em.persist(IMAGE);
        em.persist(USER1);
        em.persist(USER2);
        EVENT = new Event(EVENT_NAME, EVENT_DESC, GENERIC_LOCATION, GENERIC_TYPE, EVENT_DATE, Collections.singletonList(GENERIC_TAG), USER1, State.ACTIVE, null, IMAGE, 18, null);
        em.persist(EVENT);
        TICKET = new Ticket(TICKET_NAME, TICKET_PRICE, TICKET_QTY, EVENT, TICKET_MAX_PER_USER);
    }

    public static void setUpTicket(EntityManager em) {
        GENERIC_TAG = new Tag(GENERIC, GENERIC);
        GENERIC_LOCATION = new Location(GENERIC);
        GENERIC_TYPE = new Type(GENERIC, GENERIC);
        IMAGE = new Image(BYTE_ARRAY);
        Role role = new Role(GENERIC);
        em.persist(role);
        USER1 = new User(USERNAME, PASSWORD, MAIL, role);
        USER2 = new User(USERNAME2, PASSWORD2, MAIL2, role);
        em.persist(GENERIC_TAG);
        em.persist(GENERIC_LOCATION);
        em.persist(GENERIC_TYPE);
        em.persist(IMAGE);
        em.persist(USER1);
        em.persist(USER2);
        EVENT = new Event(EVENT_NAME, EVENT_DESC, GENERIC_LOCATION, GENERIC_TYPE, EVENT_DATE, Collections.singletonList(GENERIC_TAG), USER1, State.ACTIVE, null, IMAGE, 18, null);
        em.persist(EVENT);
    }

    public static void setUpImage(EntityManager em) {
        IMAGE = new Image(BYTE_ARRAY);
        em.persist(IMAGE);
    }

    public static TicketBooking createTicketBooking(EntityManager em) {
        Ticket ticket = createTicket(em);
        Event event = ticket.getEvent();
        EventBooking eventBooking = new EventBooking(USER1, event, null, EVENT_BOOKING_CODE);
        em.persist(eventBooking);
        TicketBooking ticketBooking = new TicketBooking(ticket, TICKET_QTY, eventBooking);
        em.persist(ticketBooking);
        return ticketBooking;
    }

    public static EventBooking createEventBooking(EntityManager em) {
        EventBooking eventBooking = new EventBooking(USER1, createEvent(em), null, EVENT_BOOKING_CODE);
        em.persist(eventBooking);
        return eventBooking;
    }

    public static Event createEvent(EntityManager em) {
        Event event = new Event(EVENT_NAME, EVENT_DESC, GENERIC_LOCATION, GENERIC_TYPE, EVENT_DATE, Collections.singletonList(GENERIC_TAG), USER1, State.ACTIVE, null, IMAGE, 18, null);
        em.persist(event);
        return event;
    }

    public static User createUser(EntityManager em) {
        Role role = new Role(GENERIC);
        em.persist(role);
        User user = new User(USERNAME2, PASSWORD2, MAIL2, role, "en");
        em.persist(user);
        return user;
    }

    public static long countEvents(EntityManager em) {
        Query query = em.createNativeQuery("select count(*) from events");
        return ((Number) query.getSingleResult()).longValue();
    }

    public static long countUsers(EntityManager em) {
        Query query = em.createNativeQuery("select count(*) from users");
        return ((Number) query.getSingleResult()).longValue();
    }

    public static Ticket createTicket(EntityManager em) {
        Ticket ticket = new Ticket(TICKET_NAME, TICKET_PRICE, TICKET_QTY, createEvent(em), TICKET_MAX_PER_USER);
        em.persist(ticket);
        return ticket;
    }

    public static Tag createTag(EntityManager em) {
        Tag tag = new Tag(TYPE_NAME, TYPE_NAME_EN);
        em.persist(tag);
        return tag;
    }

    public static Type createType(EntityManager em) {
        Type type = new Type(TYPE_NAME, TYPE_NAME_EN);
        em.persist(type);
        return type;
    }

    public static Image createImage(EntityManager em) {
        Image image = new Image(BYTE_ARRAY);
        em.persist(image);
        return image;
    }

    public static Location createLocation(EntityManager em) {
        Location location = new Location(GENERIC);
        em.persist(location);
        return location;
    }

//    public static void createRoles(SimpleJdbcInsert simpleJdbcInsert) {
//        Map<String, Object> roleData = new HashMap<>();
//        roleData.put("roleid", 1);
//        roleData.put("name", "ROLE_USER");
//        simpleJdbcInsert.execute(roleData);
//        roleData.put("roleid", 2);
//        roleData.put("name", "ROLE_CREATOR");
//        simpleJdbcInsert.execute(roleData);
//    }

    public static <T> boolean contains(T elem, List<T> list) {
        boolean ans = false;
        for (T element: list) {
            if (elem.equals(element)) {
                ans = true;
                break;
            }
        }
        return ans;
    }

//    public static Event addTicketToEvent(long eventId, String ticketName, double ticketPrice, int qty, EventDao eventDao){
//        eventDao.addTicket(eventId, ticketName, ticketPrice, qty);
//        return eventDao.getEventById(eventId, new Locale("es")).orElse(null);
//    }
//
//    public static Event generateEventWithTickets(User user, LocalDateTime date, EventDao eventDao, String eventName, String eventDesc, String ticketName, int qty, double ticketPrice) {
//        Event event = eventDao.create(eventName, eventDesc, 0, 0, date, 0, new Integer[] {0}, user.getId(), new Locale("es"));
//        return addTicketToEvent(event.getId(), ticketName, ticketPrice, qty, eventDao);
//    }
//
//    public static Event generateEvent(User user, LocalDateTime date, EventDao eventDao, String eventName, String eventDesc) {
//        return eventDao.create(eventName, eventDesc, 0, 0, date, 0, new Integer[] {0}, user.getId(), new Locale("es"));
//    }
//
//    public static void bookForEvent(Event event, Ticket ticket, User user, EventDao eventDao, int qty) {
//        Booking booking = new Booking(qty, ticket.getId());
//        List<Booking> bookings = new ArrayList<>();
//        bookings.add(booking);
//        eventDao.book(bookings, user.getId(), event.getId(), new Locale("es"));
//    }
}
