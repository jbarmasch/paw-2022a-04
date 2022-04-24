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
                ImageJdbcDao.ROW_MAPPER.mapRow(rs, rowNum),
                new ArrayList<>(),
                rs.getInt("userId")
            ),
            rs.getInt("qty")
    );

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("users").usingGeneratedKeyColumns("userid");
    }

    @Override
    public Optional<User> getUserById(long id) {
        List<User> query = jdbcTemplate.query("SELECT * FROM users WHERE userid = ?", new Object[] { id }, ROW_MAPPER);
        return query.stream().findFirst();
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

    public List<User> getAll(int page) {
        return jdbcTemplate.query("SELECT * FROM users LIMIT 10 OFFSET ?", new Object[] { (page - 1) * 10 }, ROW_MAPPER);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", new Object[] { username }, ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<Booking> getAllBookingsFromUser(long id) {
        return jdbcTemplate.query("SELECT bookings.userid AS bookId, bookings.qty, events.eventid, events.name, events.description, events.locationid, events.ticketsleft, events.price, " +
                "events.typeid, events.date, events.imageid, events.userid, locations.name AS locName, images.image, types.name AS typeName " +
                "FROM bookings JOIN events ON bookings.eventid = events.eventid JOIN locations ON events.locationid = locations.locationid JOIN images ON " +
                "events.imageid = images.imageid JOIN types ON events.typeid = types.typeid WHERE bookings.userid = ?", new Object[] { id }, BOOKING_ROW_MAPPER);
    }

    @Override
    public Optional<Booking> getBookingFromUser(long userId, long eventId) {
        return jdbcTemplate.query("SELECT bookings.userid AS bookId, bookings.qty, events.eventid, events.name, events.description, events.locationid, events.ticketsleft, events.price, " +
                "events.typeid, events.date, events.imageid, events.userid, locations.name AS locName, images.image, types.name AS typeName " +
                "FROM bookings JOIN events ON bookings.eventid = events.eventid JOIN locations ON events.locationid = locations.locationid JOIN images ON " +
                "events.imageid = images.imageid JOIN types ON events.typeid = types.typeid WHERE bookings.userid = ? AND events.eventId = ?", new Object[] { userId, eventId }, BOOKING_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public boolean cancelBooking(long userId, long eventId, int qty) {
        int rowsUpdated;
        try {
            rowsUpdated = jdbcTemplate.update("UPDATE bookings SET qty = qty - ? WHERE eventId = ?", qty, eventId);
        } catch (DataAccessException e) {
            return false;
        }

        if (rowsUpdated <= 0)
            return false;

        rowsUpdated = jdbcTemplate.update("UPDATE events SET ticketsLeft = ticketsLeft + ? WHERE eventId = ?", qty, eventId);
        return rowsUpdated > 0;
    }
}
