package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Event;
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
    private TagDao tagDao;
    private ImageDao imageDao;
    private TypeDao typeDao;
    private LocationDao locationDao;
    private final EventTagDao eventTagDao;
    private final RowMapper<Event> ROW_MAPPER = (rs, rowNum) -> new Event(
            rs.getInt("eventId"),
            rs.getString("name"),
            rs.getString("description"),
            locationDao.getLocationFromEventId(rs.getInt("eventId")).orElse(null),
            rs.getInt("maxCapacity"),
            rs.getDouble("price"),
            typeDao.getTypeFromEventId(rs.getInt("eventId")).orElse(null),
            rs.getTimestamp("date").toLocalDateTime(),
            imageDao.getImgFromEventId(rs.getInt("eventId")).orElse(null),
            tagDao.getTagsFromEventId(rs.getInt("eventId"))
    );

    @Autowired
    public EventJdbcDao(final DataSource ds, final TagDao tagDao, final ImageDao imageDao, final TypeDao typeDao, final LocationDao locationDao, final EventTagDao eventTagDao) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(ds).withTableName("events").usingGeneratedKeyColumns("eventid");
        this.tagDao = tagDao;
        this.imageDao = imageDao;
        this.typeDao = typeDao;
        this.locationDao = locationDao;
        this.eventTagDao = eventTagDao;
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

        final int eventId = jdbcInsert.executeAndReturnKey(eventData).intValue();

        for (Integer tagId : tagIds) {
            eventTagDao.addTagToEvent(eventId, tagId);
        }

        return new Event(eventId,
                name,
                description,
                locationDao.getLocationFromEventId(eventId).orElse(null),
                maxCapacity,
                price,
                typeDao.getTypeFromEventId(eventId).orElse(null),
                date,
                imageDao.getImgFromEventId(eventId).orElse(null),
                tagDao.getTagsFromEventId(eventId));
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
        jdbcTemplate.update("UPDATE events SET name = ?, description = ?, locationid = ?, maxcapacity = ?, price = ?, typeid = ?, date = ?, imageid = ? WHERE eventid = ?",
                name, description, locationId, maxCapacity, price, typeId, Timestamp.valueOf(date), imgId, id);
        eventTagDao.cleanTagsFromEvent(id);

        for (Integer tagId : tagIds) {
            eventTagDao.addTagToEvent(id, tagId);
        }
    }

    @Override
    public void deleteEvent(int id) {
        jdbcTemplate.update("DELETE FROM events WHERE eventid = ?", id);
    }
}
