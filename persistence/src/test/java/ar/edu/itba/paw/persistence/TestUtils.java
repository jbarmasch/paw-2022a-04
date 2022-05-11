package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Booking;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Ticket;
import ar.edu.itba.paw.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDateTime;
import java.util.*;

public class TestUtils {
    public static void deleteAllTables(JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "roles");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "events");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "locations");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "types");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "tags");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "eventtags");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "images");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "userroles");
        jdbcTemplate.query("TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK", r -> r);
    }

    public static void createRoles(SimpleJdbcInsert simpleJdbcInsert) {
        Map<String, Object> roleData = new HashMap<>();
        roleData.put("roleid", 1);
        roleData.put("name", "ROLE_USER");
        simpleJdbcInsert.execute(roleData);
        roleData.put("roleid", 2);
        roleData.put("name", "ROLE_CREATOR");
        simpleJdbcInsert.execute(roleData);
    }

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

    public static Event addTicketToEvent(long eventId, String ticketName, double ticketPrice, int qty, EventDao eventDao){
        eventDao.addTicket(eventId, ticketName, ticketPrice, qty);
        return eventDao.getEventById(eventId, new Locale("es")).orElse(null);
    }

    public static Event generateEventWithTickets(User user, LocalDateTime date, EventDao eventDao, String eventName, String eventDesc, String ticketName, int qty, double ticketPrice) {
        Event event = eventDao.create(eventName, eventDesc, 0, 0, date, 0, new Integer[] {0}, user.getId(), new Locale("es"));
        return addTicketToEvent(event.getId(), ticketName, ticketPrice, qty, eventDao);
    }

    public static Event generateEvent(User user, LocalDateTime date, EventDao eventDao, String eventName, String eventDesc) {
        return eventDao.create(eventName, eventDesc, 0, 0, date, 0, new Integer[] {0}, user.getId(), new Locale("es"));
    }

    public static void bookForEvent(Event event, Ticket ticket, User user, EventDao eventDao, int qty) {
        Booking booking = new Booking(qty, ticket.getId());
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        eventDao.book(bookings, user.getId(), event.getId(), new Locale("es"));
    }
}
