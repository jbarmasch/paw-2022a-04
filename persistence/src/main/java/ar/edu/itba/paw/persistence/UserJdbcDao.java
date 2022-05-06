package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserJdbcDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcRatingInsert;
    private final SimpleJdbcInsert jdbcRolesInsert;
    private final static RowMapper<User> ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getInt("userid"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("mail"),
            rs.getDouble("rating"),
            getRoles(rs.getArray("rolesids"), rs.getArray("rolesnames"))
    );

    private static List<Role> getRoles(Array rolesIds, Array rolesNames) throws SQLException {
        List<Role> roles = new ArrayList<>();
        Integer[] idsAux = (Integer[]) rolesIds.getArray();
        String[] namesAux = (String[]) rolesNames.getArray();
        if (idsAux[0] != null && namesAux[0] != null) {
            for (int i = 0; i < idsAux.length; i++) {
                roles.add(new Role(idsAux[i], namesAux[i]));
            }
        }
        return roles;
    }

    private final static RowMapper<EventBooking> EVENT_BOOKING_ROW_MAPPER = (rs, rowNum) -> new EventBooking(
            rs.getInt("userId"),
            new Event(
                    rs.getInt("eventId"),
                    rs.getString("name"),
                    rs.getString("description"),
                    new Location(rs.getInt("locationId"), rs.getString("locName")),
                    rs.getInt("ticketsLeft"),
                    rs.getDouble("minPrice"),
                    new Type(rs.getInt("typeId"), rs.getString("typeName")),
                    rs.getTimestamp("date").toLocalDateTime(),
                    rs.getInt("imageId"),
                    new ArrayList<>(),
                    new User(rs.getInt("userId")),
                    rs.getInt("attendance"),
                    State.getState(rs.getInt("state")),
                    null
            ),
            TicketBooking.getTicketBookings(rs.getArray("ticketIds"), rs.getArray("qtys"), rs.getArray("ticketNames"))
    );

    private final static RowMapper<Stats> STATS_ROW_MAPPER = (rs, rowNum) -> new Stats(
            rs.getInt("attended"),
            rs.getInt("events"),
            rs.getInt("booked"),
            rs.getInt("qty"),
            new Type(rs.getInt("favTypeId"), rs.getString("favTypeName")),
            new Location(rs.getInt("favLocId"), rs.getString("favLocName")),
            new Event(
                    rs.getInt("eventId"),
                    rs.getString("name"),
                    rs.getString("description"),
                    new Location(rs.getInt("locationId"), rs.getString("locName")),
                    rs.getInt("ticketsLeft"),
                    rs.getDouble("minPrice"),
                    new Type(rs.getInt("typeId"), rs.getString("typeName")),
                    rs.getTimestamp("date").toLocalDateTime(),
                    rs.getInt("imageId"),
                    new ArrayList<>(),
                    new User(rs.getInt("userId")),
                    rs.getInt("attendance"),
                    State.getState(rs.getInt("state")),
                    Ticket.getTickets(rs.getArray("ticketIds"), rs.getArray("ticketNames"),
                            rs.getArray("ticketPrices"), rs.getArray("ticketTicketsLeft"))
            )
    );

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("users").usingGeneratedKeyColumns("userid");
        jdbcRolesInsert = new SimpleJdbcInsert(ds).withTableName("userroles");
        jdbcRatingInsert = new SimpleJdbcInsert(ds).withTableName("ratings");
    }

    @Override
    public Optional<User> getUserById(long id) {
        return jdbcTemplate.query("SELECT users.*, AVG(COALESCE(r.rating, 0)) AS rating, ARRAY_AGG(u.roleid) AS rolesIds, ARRAY_AGG(r2.name) AS rolesNames " +
                "FROM users LEFT OUTER JOIN ratings r ON r.organizerid = users.userid LEFT OUTER JOIN userroles u on users.userid = u.userid JOIN roles r2 on u.roleid = r2.roleid " +
                "WHERE users.userid = ? " +
                "GROUP BY users.userid, users.username, users.mail", new Object[] { id }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public User create(String username, String password, String mail) {
        final Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("password", password);
        userData.put("mail", mail);

        final Number userId = jdbcInsert.executeAndReturnKey(userData);

        final Map<String, Object> userRole = new HashMap<>();
        userRole.put("userid", userId);
        userRole.put("roleid", RoleEnum.USER.ordinal() + 1);
        jdbcRolesInsert.execute(userRole);

        return new User(userId.intValue(), username, password, mail);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT users.*, AVG(COALESCE(r.rating, 0)) AS rating, ARRAY_AGG(u.roleid) AS rolesIds, ARRAY_AGG(r2.name) AS rolesNames " +
                "FROM users LEFT OUTER JOIN ratings r ON r.organizerid = users.userid LEFT OUTER JOIN userroles u on users.userid = u.userid JOIN roles r2 on u.roleid = r2.roleid " +
                "WHERE users.username = ? " +
                "GROUP BY users.userid, users.username, users.mail", new Object[] { username }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findByMail(String mail) {
        return jdbcTemplate.query("SELECT users.*, AVG(COALESCE(r.rating, 0)) AS rating, ARRAY_AGG(u.roleid) AS rolesIds, ARRAY_AGG(r2.name) AS rolesNames " +
                "FROM users LEFT OUTER JOIN ratings r ON r.organizerid = users.userid LEFT OUTER JOIN userroles u on users.userid = u.userid JOIN roles r2 on u.roleid = r2.roleid " +
                "WHERE users.mail = ? " +
                "GROUP BY users.userid, users.username, users.mail", new Object[] { mail }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<EventBooking> getAllBookingsFromUser(long id, int page) {
        return jdbcTemplate.query("SELECT bookings.userid, ARRAY_AGG(bookings.qty) AS qtys, ARRAY_AGG(bookings.ticketid) AS ticketIds, " +
                        "ARRAY_AGG(ti.name) AS ticketNames, ec.* FROM bookings JOIN event_complete ec ON bookings.eventid = ec.eventid JOIN tickets ti " +
                        "ON ti.eventId = ec.eventid AND bookings.ticketid = ti.ticketid WHERE bookings.userid = ? AND bookings.qty > 0 " +
                        "group by bookings.userid, bookings.eventid, ec.eventid, ec.name, ec.description, ec.locationid, ec.attendance, ec.minPrice, " +
                        "ec.ticketsLeft, ec.typeid, ec.date, ec.imageid, ec.userid, ec.state, ec.locName, ec.image, ec.typeName, ec.username, " +
                        "ec.ticketIds, ec.ticketTicketsLeft, ec.ticketNames, ec.ticketPrices, ec.tagIds, ec.tagNames ORDER BY date LIMIT 10 OFFSET ?",
                new Object[] { id, (page - 1) * 10 }, EVENT_BOOKING_ROW_MAPPER);
    }

    @Override
    public Optional<EventBooking> getBookingFromUser(long userId, long eventId) {
        return jdbcTemplate.query("SELECT bookings.userid, ARRAY_AGG(bookings.qty) AS qtys, ARRAY_AGG(bookings.ticketid) AS ticketIds, " +
                    "ARRAY_AGG(ti.name) AS ticketNames, ec.* FROM bookings JOIN event_complete ec ON bookings.eventid = ec.eventid JOIN tickets ti " +
                        "ON ti.eventId = ec.eventid AND bookings.ticketid = ti.ticketid WHERE bookings.userid = ? AND bookings.qty > 0 AND bookings.eventId = ? " +
                        "group by bookings.userid, bookings.eventid, ec.eventid, ec.name, ec.description, ec.locationid, ec.attendance, ec.minPrice, " +
                        "ec.ticketsLeft, ec.typeid, ec.date, ec.imageid, ec.userid, ec.state, ec.locName, ec.image, ec.typeName, ec.username, " +
                        "ec.ticketIds, ec.ticketTicketsLeft, ec.ticketNames, ec.ticketPrices, ec.tagIds, ec.tagNames ORDER BY date LIMIT 10 OFFSET ?",
                new Object[] { userId, eventId }, EVENT_BOOKING_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<Stats> getUserStats(long id) {
        return jdbcTemplate.query("SELECT * FROM ((SELECT attended, booked, favTypeId, t.name favTypeName, favLocId, l.name favLocName " +
                "FROM (SELECT COUNT(*) AS attended, SUM(qty) AS booked, MODE() WITHIN GROUP (ORDER BY e.typeId) AS favTypeId, MODE() WITHIN GROUP " +
                "(ORDER BY e.locationId) AS favLocId FROM bookings b JOIN events e ON b.eventId = e.eventId " +
                "WHERE b.userId = ?) AS pre JOIN types t ON pre.favTypeId = t.typeid JOIN locations l ON pre.favLocId = l.locationid) AS userStats " +
                "CROSS JOIN (SELECT * FROM ((SELECT COUNT(*) AS events, SUM(qty) AS qty FROM (SELECT e.eventid, SUM(COALESCE(qty, 0)) AS qty " +
                "FROM events e LEFT JOIN bookings b ON b.eventId = e.eventId WHERE e.userId = ? GROUP BY e.eventid) AS aux) AS general CROSS JOIN ( " +
                "SELECT * FROM event_complete ec JOIN (SELECT e.eventid, SUM(COALESCE(qty, 0)) AS sumqty FROM events e LEFT JOIN bookings b ON " +
                "b.eventId = e.eventId WHERE e.userid = ? GROUP BY e.eventid ORDER BY sumqty DESC LIMIT 1) AS aux ON ec.eventid = aux.eventid) AS " +
                "event)) AS creatorStats)", new Object[]{ id, id, id }, STATS_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public void rateUser(long userId, long organizerId, double rating) {
        int rowsUpdated = jdbcTemplate.update("UPDATE ratings SET rating = ? WHERE organizerid = ? AND userid = ?", rating, organizerId, userId);
        if (rowsUpdated <= 0) {
            final Map<String, Object> ratingData = new HashMap<>();
            ratingData.put("organizerId", organizerId);
            ratingData.put("userId", userId);
            ratingData.put("rating", rating);
            jdbcRatingInsert.execute(ratingData);
        }
    }
}
