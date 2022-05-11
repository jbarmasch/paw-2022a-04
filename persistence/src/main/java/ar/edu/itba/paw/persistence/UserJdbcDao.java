package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class UserJdbcDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcRatingInsert;
    private final SimpleJdbcInsert jdbcRolesInsert;

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("users").usingGeneratedKeyColumns("userid");
        jdbcRolesInsert = new SimpleJdbcInsert(ds).withTableName("userroles");
        jdbcRatingInsert = new SimpleJdbcInsert(ds).withTableName("ratings");
    }

    @Override
    public Optional<User> getUserById(long id) {
        return jdbcTemplate.query("SELECT aux.userid, aux.username, aux.mail, aux.password, rolesIds, rolesNames, AVG(COALESCE(rating, 0)) AS rating, COUNT(rating) AS votes FROM " +
                        "(SELECT users.*, r.rating, ARRAY_AGG(u.roleid) AS rolesIds, ARRAY_AGG(r2.name) AS rolesNames " +
                        "FROM users LEFT OUTER JOIN ratings r ON r.organizerid = users.userid LEFT OUTER JOIN userroles u on users.userid = u.userid JOIN roles r2 " +
                        "ON u.roleid = r2.roleid WHERE users.userid = ? GROUP BY users.userid, users.username, users.mail, r.rating) as aux " +
                        "GROUP BY aux.userid, aux.username, aux.mail, aux.password, rolesIds, rolesNames",
                new Object[] { id }, JdbcUtils.USER_ROW_MAPPER).stream().findFirst();
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
        return jdbcTemplate.query("SELECT aux.userid, aux.username, aux.mail, aux.password, rolesIds, rolesNames, AVG(COALESCE(rating, 0)) AS rating, COUNT(rating) AS votes FROM " +
                        "(SELECT users.*, r.rating, ARRAY_AGG(u.roleid) AS rolesIds, ARRAY_AGG(r2.name) AS rolesNames " +
                        "FROM users LEFT OUTER JOIN ratings r ON r.organizerid = users.userid LEFT OUTER JOIN userroles u on users.userid = u.userid JOIN roles r2 " +
                        "ON u.roleid = r2.roleid WHERE users.username = ? GROUP BY users.userid, users.username, users.mail, r.rating) as aux " +
                        "GROUP BY aux.userid, aux.username, aux.mail, aux.password, rolesIds, rolesNames",
                new Object[] { username }, JdbcUtils.USER_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<User> findByMail(String mail) {
        return jdbcTemplate.query("SELECT aux.userid, aux.username, aux.mail, aux.password, rolesIds, rolesNames, AVG(COALESCE(rating, 0)) AS rating, COUNT(rating) AS votes FROM " +
                "(SELECT users.*, r.rating, ARRAY_AGG(u.roleid) AS rolesIds, ARRAY_AGG(r2.name) AS rolesNames " +
                "FROM users LEFT OUTER JOIN ratings r ON r.organizerid = users.userid LEFT OUTER JOIN userroles u on users.userid = u.userid JOIN roles r2 " +
                "ON u.roleid = r2.roleid WHERE users.mail = ? GROUP BY users.userid, users.username, users.mail, r.rating) as aux " +
                "GROUP BY aux.userid, aux.username, aux.mail, aux.password, rolesIds, rolesNames", new Object[] { mail }, JdbcUtils.USER_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public List<EventBooking> getAllPreviousBookingsFromUser(long id, int page, Locale locale) {
        return jdbcTemplate.query("SELECT bookings.userid, ARRAY_AGG(bookings.qty) AS qtys, ARRAY_AGG(bookings.ticketid) AS ticketIds, " +
                    "ARRAY_AGG(ti.name) AS ticketNames, ARRAY_AGG(ti.maxtickets) AS ticketQtys, ARRAY_AGG(ti.booked) AS ticketBookeds, " +
                        "ec.*, rating FROM bookings JOIN event_complete" + JdbcUtils.getLocateExt(locale) + " ec ON bookings.eventid = ec.eventid JOIN tickets ti ON ti.eventId = ec.eventid AND " +
                        "bookings.ticketid = ti.ticketid LEFT JOIN ratings r on ec.userid = r.organizerid WHERE bookings.userid = ? AND bookings.qty > 0 AND date <= ?" +
                        "group by bookings.userid, bookings.eventid, ec.eventid, ec.name, ec.description, ec.locationid, ec.attendance, ec.minPrice, " +
                        "ec.ticketsLeft, ec.typeid, ec.date, ec.imageid, ec.userid, ec.state, ec.locName, ec.typeName, ec.username, ec.ticketIds, ec.ticketNames, " +
                        "ec.ticketQtys, ec.ticketBookeds, ec.ticketPrices, ec.tagIds, ec.tagNames, rating ORDER BY date LIMIT 10 OFFSET ?",
                new Object[] { id, Timestamp.valueOf(LocalDateTime.now()), (page - 1) * 10 }, JdbcUtils.EVENT_BOOKING_ROW_MAPPER);
    }

    @Override
    public List<EventBooking> getAllFutureBookingsFromUser(long id, int page, Locale locale) {
        return jdbcTemplate.query("SELECT bookings.userid, ARRAY_AGG(bookings.qty) AS qtys, ARRAY_AGG(bookings.ticketid) AS ticketIds, " +
                        "ARRAY_AGG(ti.name) AS ticketNames, ARRAY_AGG(ti.maxtickets) AS ticketQtys, ARRAY_AGG(ti.booked) AS ticketBookeds, " +
                        "ec.*, rating FROM bookings JOIN event_complete" + JdbcUtils.getLocateExt(locale) + " ec ON bookings.eventid = ec.eventid JOIN tickets ti ON ti.eventId = ec.eventid AND " +
                        "bookings.ticketid = ti.ticketid LEFT JOIN ratings r on ec.userid = r.organizerid WHERE bookings.userid = ? AND bookings.qty > 0 AND date > ?" +
                        "group by bookings.userid, bookings.eventid, ec.eventid, ec.name, ec.description, ec.locationid, ec.attendance, ec.minPrice, " +
                        "ec.ticketsLeft, ec.typeid, ec.date, ec.imageid, ec.userid, ec.state, ec.locName, ec.typeName, ec.username, ec.ticketIds, ec.ticketNames, " +
                        "ec.ticketQtys, ec.ticketBookeds, ec.ticketPrices, ec.tagIds, ec.tagNames, rating ORDER BY date LIMIT 10 OFFSET ?",
                new Object[] { id, Timestamp.valueOf(LocalDateTime.now()), (page - 1) * 10 }, JdbcUtils.EVENT_BOOKING_ROW_MAPPER);
    }

    @Override
    public Optional<EventBooking> getBookingFromUser(long userId, long eventId, Locale locale) {
        return jdbcTemplate.query("SELECT bookings.userid, ARRAY_AGG(bookings.qty) AS qtys, ARRAY_AGG(bookings.ticketid) AS ticketIds, " +
                        "ARRAY_AGG(ti.name) AS ticketNames, ARRAY_AGG(ti.maxtickets) AS ticketQtys, ARRAY_AGG(ti.booked) AS ticketBookeds, " +
                        "ec.*, rating FROM bookings JOIN event_complete" + JdbcUtils.getLocateExt(locale) + " ec ON bookings.eventid = ec.eventid JOIN tickets ti ON ti.eventId = ec.eventid AND " +
                        "bookings.ticketid = ti.ticketid LEFT JOIN ratings r on ec.userid = r.organizerid WHERE bookings.userid = ? AND bookings.qty > 0 AND bookings.eventid = ?" +
                        "group by bookings.userid, bookings.eventid, ec.eventid, ec.name, ec.description, ec.locationid, ec.attendance, ec.minPrice, " +
                        "ec.ticketsLeft, ec.typeid, ec.date, ec.imageid, ec.userid, ec.state, ec.locName, ec.typeName, ec.username, ec.ticketIds, ec.ticketNames, " +
                        "ec.ticketQtys, ec.ticketBookeds, ec.ticketPrices, ec.tagIds, ec.tagNames, rating",
                new Object[] { userId, eventId }, JdbcUtils.EVENT_BOOKING_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public boolean canRate(long organizerId, long userId) {
        Integer qty = jdbcTemplate.query("SELECT COUNT(*) AS qty FROM bookings b JOIN events e ON e.eventid = b.eventid WHERE e.userid = ? AND b.userid = ? AND date <= ?",
                new Object[]{ organizerId, userId, Timestamp.valueOf(LocalDateTime.now()) }, (rs, i) -> rs.getInt("qty")).stream().findFirst().orElse(null);
        return qty != null && qty != 0;
    }

    @Override
    public Optional<EventStats> getEventStats(long id, Locale locale) {
        return jdbcTemplate.query( "SELECT * FROM ((SELECT COUNT(*) AS events, SUM(bookings) AS bookings, SUM(bookings) / SUM(COALESCE(NULLIF(qty, 0), 1)) " +
                        "AS attendance, SUM(income) AS income FROM (SELECT e.eventid, SUM(COALESCE(booked, 0)) AS bookings, SUM(COALESCE(maxTickets, 0)) AS qty, " +
                        "booked * price AS income FROM events e LEFT JOIN tickets t ON t.eventId = e.eventId WHERE e.userId = ? GROUP BY e.eventid, booked, price) AS aux " +
                        ") AS general CROSS JOIN (SELECT * FROM event_complete" + JdbcUtils.getLocateExt(locale) + " ec JOIN (SELECT e.eventid, SUM(COALESCE(booked, 0)) AS sumqty FROM events e LEFT JOIN tickets t ON " +
                        "t.eventId = e.eventId WHERE e.userid = ? GROUP BY e.eventid ORDER BY sumqty DESC LIMIT 1) AS aux ON ec.eventid = aux.eventid) AS event)",
                new Object[]{ id, id }, JdbcUtils.EVENT_STATS_ROW_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<UserStats> getUserStats(long id, Locale locale) {
        return jdbcTemplate.query("SELECT attended, booked, favTypeId, t.name favTypeName, favLocId, l.name favLocName " +
                "FROM (SELECT COUNT(*) AS attended, SUM(qty) AS booked, MODE() WITHIN GROUP (ORDER BY e.typeId) AS favTypeId, MODE() WITHIN GROUP " +
                "(ORDER BY e.locationId) AS favLocId FROM bookings b JOIN events e ON b.eventId = e.eventId " +
                "WHERE b.userId = ?) AS pre JOIN types" + JdbcUtils.getLocateExt(locale) + " t ON pre.favTypeId = t.typeid JOIN locations l ON pre.favLocId = l.locationid",
                new Object[]{ id }, JdbcUtils.USER_STATS_ROW_MAPPER).stream().findFirst();
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
