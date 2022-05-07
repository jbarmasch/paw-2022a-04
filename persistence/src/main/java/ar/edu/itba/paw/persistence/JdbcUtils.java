package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUtils {
    public static final RowMapper<Event> EVENT_ROW_MAPPER = (rs, i) -> {
        Location location = new Location(rs.getInt("locationId"), rs.getString("locName"));
        Type type = new Type(rs.getInt("typeId"), rs.getString("typeName"));

        return new Event(
                rs.getInt("eventId"),
                rs.getString("name"),
                rs.getString("description"),
                location,
                rs.getInt("ticketsLeft"),
                rs.getDouble("minPrice"),
                type,
                rs.getTimestamp("date").toLocalDateTime(),
                rs.getInt("imageId"),
                getTags(rs.getArray("tagIds"), rs.getArray("tagNames")),
                new User(rs.getInt("userId"), rs.getString("username")),
                rs.getInt("attendance"),
                State.getState(rs.getInt("state")),
                getTickets(rs.getArray("ticketIds"), rs.getArray("ticketNames"), rs.getArray("ticketPrices"), rs.getArray("ticketTicketsLeft"))
        );
    };

    public final static RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getInt("userid"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("mail"),
            rs.getDouble("rating"),
            getRoles(rs.getArray("rolesids"), rs.getArray("rolesnames"))
    );

    public final static RowMapper<EventBooking> EVENT_BOOKING_ROW_MAPPER = (rs, rowNum) -> new EventBooking(
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
            getTicketBookings(rs.getArray("ticketIds"), rs.getArray("qtys"), rs.getArray("ticketNames"))
    );

    public final static RowMapper<EventStats> EVENT_STATS_ROW_MAPPER = (rs, rowNum) -> new EventStats(
            rs.getInt("events"),
            rs.getInt("qty"),
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
                    getTickets(rs.getArray("ticketIds"), rs.getArray("ticketNames"), rs.getArray("ticketPrices"), rs.getArray("ticketTicketsLeft"))
            )
    );

    public final static RowMapper<UserStats> USER_STATS_ROW_MAPPER = (rs, rowNum) -> new UserStats(
            rs.getInt("attended"),
            rs.getInt("booked"),
            new Type(rs.getInt("favTypeId"), rs.getString("favTypeName")),
            new Location(rs.getInt("favLocId"), rs.getString("favLocName"))
    );

    public static List<Role> getRoles(Array rolesIds, Array rolesNames) throws SQLException {
        List<Role> roles = new ArrayList<>();
        Object[] idsAux = (Object[]) rolesIds.getArray();
        Object[] namesAux = (Object[]) rolesNames.getArray();
        if (idsAux[0] != null && namesAux[0] != null) {
            for (int i = 0; i < idsAux.length; i++) {
                roles.add(new Role(Integer.parseInt(idsAux[i].toString()), namesAux[i].toString()));
            }
        }
        return roles;
    }

    public static List<Ticket> getTickets(Array ids, Array names, Array prices, Array lefts) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        Object[] idsAux = (Object[]) ids.getArray();
        Object[] namesAux = (Object[]) names.getArray();
        Object[] pricesAux = (Object[]) prices.getArray();
        Object[] leftsAux = (Object[]) lefts.getArray();
        if (idsAux[0] != null && namesAux[0] != null && pricesAux[0] != null && leftsAux[0] != null) {
            for (int i = 0; i < idsAux.length; i++) {
                tickets.add(new Ticket(Integer.parseInt(idsAux[i].toString()), namesAux[i].toString(),
                        Double.parseDouble(pricesAux[i].toString()), Integer.parseInt(leftsAux[i].toString())));
            }
        }
        return tickets;
    }

    public static List<Tag> getTags(Array ids, Array names) throws SQLException {
        List<Tag> tags = new ArrayList<>();
        Object[] idsAux = (Object[]) ids.getArray();
        Object[] namesAux = (Object[]) names.getArray();
        if (idsAux[0] != null && namesAux[0] != null) {
            for (int i = 0; i < idsAux.length; i++) {
                tags.add(new Tag(Integer.parseInt(idsAux[i].toString()), namesAux[i].toString()));
            }
        }
        return tags;
    }

    public static List<TicketBooking> getTicketBookings(Array ids, Array qtys, Array names) throws SQLException {
        List<TicketBooking> ticketBookings = new ArrayList<>();
        Object[] idsAux = (Object[]) ids.getArray();
        Object[] qtysAux = (Object[]) qtys.getArray();
        Object[] namesAux = (Object[]) names.getArray();
        if (idsAux[0] != null && qtysAux[0] != null && namesAux[0] != null) {
            for (int i = 0; i < idsAux.length; i++) {
                ticketBookings.add(new TicketBooking(Integer.parseInt(qtysAux[i].toString()),
                        new Ticket(Integer.parseInt(idsAux[i].toString()), namesAux[i].toString())));
            }
        }
        return ticketBookings;
    }
}
