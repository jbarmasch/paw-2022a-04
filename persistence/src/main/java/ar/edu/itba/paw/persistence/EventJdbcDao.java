package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
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
    private final SimpleJdbcInsert jdbcTagInsert;
    private final RowMapper<Event> ROW_MAPPER = (rs, i) -> {
        Location location = new Location(rs.getInt("locationId"), rs.getString("locName"));
        Type type = new Type(rs.getInt("typeId"), rs.getString("typeName"));
        Image image = ImageJdbcDao.ROW_MAPPER.mapRow(rs, i);
//        Image image = new Image(rs.getInt("imageId"), rs.getBytes("image"));

        return new Event(
                rs.getInt("eventId"),
                rs.getString("name"),
                rs.getString("description"),
                location,
                rs.getInt("maxCapacity"),
                rs.getDouble("price"),
                type,
                rs.getTimestamp("date").toLocalDateTime(),
                image,
                getTagsFromEventId(rs.getInt("eventId"))
        );
    };

    @Autowired
    public EventJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("events").usingGeneratedKeyColumns("eventid");
        jdbcTagInsert = new SimpleJdbcInsert(ds).withTableName("eventtags");
    }

    @Override
    public Optional<Event> getEventById(long id) {
        List<Event> query = jdbcTemplate.query(
                "SELECT events.eventid, events.name, events.description, events.locationid, events.maxcapacity, events.price, " +
                        "events.typeid, events.date, events.imageid, locations.name AS locName, images.image, types.name AS typeName " +
                        "FROM events JOIN locations ON events.locationid = locations.locationid JOIN images ON events.imageid = images.imageid " +
                        "JOIN types ON events.typeid = types.typeid WHERE eventid = ?",
                new Object[]{id}, ROW_MAPPER);
        return query.stream().findFirst();
    }

    @Override
    public Event create(String name, String description, int locationId, int maxCapacity, double price, int typeId, LocalDateTime date, int imageId, Integer[] tagIds) {
        final Map<String, Object> eventData = new HashMap<>();
        eventData.put("name", name);
        eventData.put("description", description);
        eventData.put("locationId", locationId);
        eventData.put("maxCapacity", maxCapacity);
        eventData.put("price", price);
        eventData.put("typeId", typeId);
        eventData.put("date", Timestamp.valueOf(date));
        eventData.put("imageId", imageId);

        final int eventId = jdbcInsert.executeAndReturnKey(eventData).intValue();

        for (Integer tagId : tagIds) {
            addTagToEvent(eventId, tagId);
        }

        return getEventById(eventId).orElseThrow(RuntimeException::new);

//        return new Event(eventId,
//                name,
//                description,
//                locationDao.getLocationFromEventId(eventId).orElse(null),
//                maxCapacity,
//                price,
//                typeDao.getTypeFromEventId(eventId).orElse(null),
//                date,
//                imageDao.getImgFromEventId(eventId).orElse(null),
//                tagDao.getTagsFromEventId(eventId));
    }

    public List<Event> filterBy(String[] locations, String[] types , Double minPrice, Double maxPrice, int page) {
        StringBuilder query = new StringBuilder(
                "SELECT events.eventid, events.name, events.description, events.locationid, events.maxcapacity, events.price, " +
                "events.typeid, events.date, events.imageid, locations.name AS locName, images.image, types.name AS typeName " +
                "FROM events JOIN locations ON events.locationid = locations.locationid JOIN images ON events.imageid = images.imageid " +
                "JOIN types ON events.typeid = types.typeid"
        );

        if (locations != null || minPrice != null || maxPrice != null || types != null) {
            boolean append = false;
            query.append(" WHERE ");
            if (locations != null && locations.length > 0) {
                append = true;
                String lastLocation = locations[locations.length - 1];
                query.append("events.locationid IN (");
                for (String location : locations) {
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
                query.append("events.typeid IN (");
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
        jdbcTemplate.update("UPDATE events SET name = ?, description = ?, locationid = ?, maxcapacity = ?, price = ?, typeid = ?, date = ?, imageid = ? WHERE eventid = ?",
                name, description, locationId, maxCapacity, price, typeId, Timestamp.valueOf(date), imgId, id);

        cleanTagsFromEvent(id);
        for (Integer tagId : tagIds) {
            addTagToEvent(id, tagId);
        }
    }

    @Override
    public void deleteEvent(int id) {
        jdbcTemplate.update("DELETE FROM events WHERE eventid = ?", id);
    }


//    @Override
    public void addTagToEvent(int eventId, int tagId) {
        final Map<String, Object> eventData = new HashMap<>();
        eventData.put("eventId", eventId);
        eventData.put("tagId", tagId);

        jdbcTagInsert.execute(eventData);
    }

//    @Override
    public void cleanTagsFromEvent(int eventId) {
        jdbcTemplate.update("DELETE FROM eventtags WHERE eventid = ?", eventId);
    }

//    @Override
    public List<Tag> getTagsFromEventId(int eventId) {
        return jdbcTemplate.query("SELECT tagid, tags.name FROM eventtags NATURAL JOIN tags WHERE eventid = ?", new Object[]{eventId}, TagJdbcDao.ROW_MAPPER);
    }
}
