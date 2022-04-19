package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Tag;
import ar.edu.itba.paw.model.Type;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class EventJdbcDao implements EventDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Event> ROW_MAPPER = (rs, rowNum) -> new Event(
        rs.getInt("eventId"),
        rs.getString("name"),
        rs.getString("description"),
        getLocationFromEventId(rs.getInt("eventId")).orElseThrow(RuntimeException::new),
        rs.getInt("maxCapacity"),
        rs.getDouble("price"),
        getTypeFromEventId(rs.getInt("eventId")).orElseThrow(RuntimeException::new),
        rs.getTimestamp("date").toLocalDateTime(),
        getImgFromEventId(rs.getInt("eventId")).orElseThrow(RuntimeException::new),
        getTagsFromEventId(rs.getInt("eventId"))
    );
    private static final RowMapper<Tag> ROW_MAPPER_TAG = (rs, rowNum) -> new Tag(
        rs.getInt("tagId"),
        rs.getString("name")
    );
    private static final RowMapper<Image> ROW_MAPPER_IMAGE = (rs, rowNum) -> new Image(
        rs.getInt("imageId"),
        rs.getBytes("image")
    );
    private static final RowMapper<Type> ROW_MAPPER_TYPE = (rs, rowNum) -> new Type(
        rs.getInt("typeId"),
        rs.getString("name")
    );
    private static final RowMapper<Location> ROW_MAPPER_LOCATION = (rs, rowNum) -> new Location(
        rs.getInt("locationId"),
        rs.getString("name")
    );

    @Autowired
    public EventJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("events").usingGeneratedKeyColumns("eventid");
    }

    @Override
    public Optional<Event> getEventById(long id) {
        List<Event> query = jdbcTemplate.query("SELECT * FROM events WHERE eventid = ?", new Object[]{id}, ROW_MAPPER);
        return query.stream().findFirst();
    }

    @Override
    public Event create(String name, String description, int locationId, int maxCapacity, double price, int typeId, LocalDateTime date, int imgId, Integer[] tagIds) {
        final Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", name);
        eventData.put("description", description);
        eventData.put("locationId", locationId);
        eventData.put("maxCapacity", maxCapacity);
        eventData.put("price", price);
        eventData.put("typeId", typeId);
        eventData.put("date", Timestamp.valueOf(date));
//        eventData.put("imgId", imgId);
//        eventData.put("tagId", tagIds);

        final int eventId = jdbcInsert.executeAndReturnKey(eventData).intValue();
        return new Event(eventId,
                name,
                description,
                getLocationFromEventId(eventId).orElseThrow(RuntimeException::new),
                maxCapacity,
                price,
                getTypeFromEventId(eventId).orElseThrow(RuntimeException::new),
                date,
                getImgFromEventId(eventId).orElseThrow(RuntimeException::new),
                getTagsFromEventId(eventId));
    }

    public List<Event> filterBy(Integer[] locations, String[] types , Double minPrice, Double maxPrice, int page) {
        StringBuilder query = new StringBuilder("SELECT * FROM events");

        if (locations != null || minPrice != null || maxPrice != null || types != null) {
            boolean append = false;
            query.append(" WHERE ");
            if (locations != null && locations.length > 0) {
                append = true;
                Integer lastLocation = locations[locations.length - 1];
                query.append("locationId IN (");
                for (Integer location : locations) {
                    query.append("'").append(location).append("'");
                    if (!Objects.equals(location, lastLocation))
                        query.append(", ");
                }
                query.append(")");
            }
            if (minPrice != null) {
                if (append)
                    query.append(" AND ");
                append = true;
                query.append("price >= ").append(minPrice);
            }
            if (maxPrice != null) {
                if (append)
                    query.append(" AND ");
                append = true;
                query.append("price <= ").append(maxPrice);
            }
            if (types != null && types.length > 0) {
                if (append)
                    query.append(" AND ");
                String lastType = types[types.length - 1];
                query.append("typeId IN (");
                for (String type : types) {
                    query.append("'").append(type).append("'");
                    if (!Objects.equals(type, lastType))
                        query.append(", ");
                }
                query.append(")");
            }
        }

        return jdbcTemplate.query(query + " LIMIT 10 OFFSET ?", new Object[]{(page - 1) * 10}, ROW_MAPPER);
    }

    @Override
    public List<Event> getAll(int page) {
        return jdbcTemplate.query("SELECT * FROM events LIMIT 10 OFFSET ?", new Object[]{(page - 1) * 10}, ROW_MAPPER);
    }

    @Override
    public void updateEvent(int id, String name, String description, Integer locationId, int maxCapacity, double price, int typeId, LocalDateTime date, int imgId, Integer[] tagIds) {
        jdbcTemplate.update("UPDATE events SET name = ?, description = ?, locationid = ?, maxcapacity = ?, price = ?, typeid = ?, date = ?, imgid = ?, tagids = ? WHERE eventid = ?",
                name, description, locationId, maxCapacity, price, typeId, Timestamp.valueOf(date), imgId, tagIds, id);
    }

    public List<Tag> getTagsFromEventId(int id) {
        return jdbcTemplate.query("SELECT tagid, tags.name FROM eventtags NATURAL JOIN tags WHERE eventid = ?", new Object[]{id}, ROW_MAPPER_TAG);
    }

    public Optional<Image> getImgFromEventId(int id) {
        return jdbcTemplate.query("SELECT images.imageid, images.image FROM events JOIN images ON events.imageid = images.imageid WHERE eventid = ?", new Object[]{id}, ROW_MAPPER_IMAGE).stream().findFirst();
    }

    public Optional<Type> getTypeFromEventId(int id) {
        return jdbcTemplate.query("SELECT types.typeid, types.name FROM events JOIN types ON events.typeid = types.typeid WHERE eventid = ?", new Object[]{id}, ROW_MAPPER_TYPE).stream().findFirst();
    }

    public Optional<Location> getLocationFromEventId(int id) {
        return jdbcTemplate.query("SELECT locations.locationid, locations.name FROM events JOIN locations ON events.locationid = locations.locationid WHERE eventid = ?", new Object[]{id}, ROW_MAPPER_LOCATION).stream().findFirst();
    }

    @Override
    public void deleteEvent(int id) {
        jdbcTemplate.update("DELETE FROM events WHERE eventid = ?", id);
    }
}
