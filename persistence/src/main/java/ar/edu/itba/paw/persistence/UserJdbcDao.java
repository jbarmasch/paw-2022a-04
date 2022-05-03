package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class UserJdbcDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<User> ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getInt("userid"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("mail")
    );

    private final static RowMapper<Booking> BOOKING_ROW_MAPPER = (rs, rowNum) -> new Booking(
            rs.getInt("bookId"),
            new Event(
                    rs.getInt("eventId"),
                    rs.getString("name"),
                    rs.getString("description"),
                    new Location(rs.getInt("locationId"), rs.getString("locName")),
                    rs.getInt("ticketsLeft"),
                    rs.getDouble("price"),
                    new Type(rs.getInt("typeId"), rs.getString("typeName")),
                    rs.getTimestamp("date").toLocalDateTime(),
                    rs.getInt("imageId"),
                    new ArrayList<>(),
                    new User(rs.getInt("userId")),
                    rs.getDouble("rating"),
                    rs.getInt("attendance"),
                    State.getState(rs.getInt("state"))
            ),
            rs.getInt("qty")
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
                    rs.getDouble("price"),
                    new Type(rs.getInt("typeId"), rs.getString("typeName")),
                    rs.getTimestamp("date").toLocalDateTime(),
                    rs.getInt("imageId"),
                    new ArrayList<>(),
                    new User(rs.getInt("userId")),
                    rs.getDouble("rating"),
                    rs.getInt("attendance"),
                    State.getState(rs.getInt("state"))
            )
    );

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("users").usingGeneratedKeyColumns("userid");
    }

    @Override
    public Optional<User> getUserById(long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE userid = ?", new Object[] { id }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public User create(String username, String password, String mail) {
        final Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("password", password);
        userData.put("mail", mail);

        final Number userId = jdbcInsert.executeAndReturnKey(userData);
        return new User(userId.intValue(), username, password, mail);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", new Object[] { username }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findByMail(String mail) {
        return jdbcTemplate.query("SELECT * FROM users WHERE mail = ?", new Object[] { mail }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Booking> getAllBookingsFromUser(long id, int page) {
        return jdbcTemplate.query("SELECT bookings.userid AS bookId, bookings.qty, * FROM bookings JOIN event_complete ON bookings.eventid = " +
                "event_complete.eventid WHERE bookings.userid = ? AND bookings.qty > 0 ORDER BY date LIMIT 10 OFFSET ?",
                new Object[] { id, (page - 1) * 10 }, BOOKING_ROW_MAPPER);
    }

    @Override
    public Optional<Booking> getBookingFromUser(long userId, long eventId) {
        return jdbcTemplate.query("SELECT bookings.userid AS bookId, bookings.qty, * FROM bookings JOIN event_complete ON bookings.eventid = " +
                "event_complete.eventid WHERE bookings.userid = ? AND bookings.eventId = ?",
                new Object[] { userId, eventId }, BOOKING_ROW_MAPPER).stream().findFirst();
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
}
