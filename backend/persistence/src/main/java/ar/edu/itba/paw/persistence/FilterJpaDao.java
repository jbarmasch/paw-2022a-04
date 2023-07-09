package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.FilterType;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.Tag;
import ar.edu.itba.paw.model.Type;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FilterJpaDao implements FilterDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public FilterType getFilterType(List<Long> locations, List<Long> types, Double minPrice, Double maxPrice, String searchQuery, List<Long> tags, Boolean showSoldOut, Boolean showNoTickets, Long userId) {
        return new FilterType(
                getLocationFilterCount(types, minPrice, maxPrice, searchQuery, tags, showSoldOut, showNoTickets, userId),
                getTypeFilterCount(locations, minPrice, maxPrice, searchQuery, tags, showSoldOut, showNoTickets, userId),
                getTagFilterCount(locations, types, minPrice, maxPrice, searchQuery, showSoldOut, showNoTickets, userId),
                getSoldOutFilterCount(locations, types, minPrice, maxPrice, searchQuery, tags, showNoTickets, userId),
                getNoTicketsFilterCount(locations, types, minPrice, maxPrice, searchQuery, tags, showSoldOut, userId)
        );
    }

    @SuppressWarnings("unchecked")
    private Map<Location, Integer> getLocationFilterCount(List<Long> types, Double minPrice, Double maxPrice, String searchQuery, List<Long> tags, Boolean showSoldOut, Boolean showNoTickets, Long userId) {
        boolean having = false;
        Map<String, Object> objects = new HashMap<>();
        StringBuilder querySelect = new StringBuilder("SELECT l.locationid, l.name, e.eventid FROM locations l JOIN events e ON l.locationid = e.locationid");
        StringBuilder queryCondition = new StringBuilder(" WHERE state != 1 AND date > NOW()");
        if (showSoldOut == null || !showSoldOut) {
            queryCondition.append(" AND state != 2");
        }
        if (userId != null) {
            queryCondition.append(" AND userid = :userid");
            objects.put("userid", userId);
        }
        if (types != null && types.size() > 0) {
            queryCondition.append(" AND typeid IN :typeids");
            objects.put("typeids", types);
        }
        if (searchQuery != null && searchQuery.length() > 0) {
            queryCondition.append(" AND ((SELECT to_tsvector('Spanish', e.name) @@ websearch_to_tsquery(:searchquery)) = 't' OR e.name ILIKE CONCAT('%', :searchquery, '%'))");
            objects.put("searchquery", searchQuery);
        }

        queryCondition.append(" GROUP BY l.locationid, e.eventid ");

        if (showNoTickets == null || !showNoTickets) {
            querySelect.append(" LEFT JOIN (SELECT * FROM tickets t WHERE (t.starting IS NULL OR t.starting <= NOW()) AND (t.until IS NULL OR t.until >= NOW())) AS t ON e.eventid = t.eventid");
            having = true;
            queryCondition.append(" HAVING");
            queryCondition.append(" COUNT(t.ticketid) > 0");
        }


        if ((minPrice != null || maxPrice != null) && showNoTickets != null && showNoTickets) {
            querySelect.append(" LEFT JOIN (SELECT * FROM tickets t WHERE (t.starting IS NULL OR t.starting <= NOW()) AND (t.until IS NULL OR t.until >= NOW())) AS t ON e.eventid = t.eventid");
        }

        if (minPrice != null) {
            if (!having) {
                queryCondition.append(" HAVING");
                having = true;
            } else queryCondition.append(" AND");
            queryCondition.append(" COALESCE(MIN(t.price), 0) >= :minPrice");
            objects.put("minPrice", minPrice);
        }
        if (maxPrice != null) {
            if (!having) {
                queryCondition.append(" HAVING");
                having = true;
            } else queryCondition.append(" AND");
            queryCondition.append(" COALESCE(MIN(t.price), 0) <= :maxPrice");
            objects.put("maxPrice", maxPrice);
        }

        if (tags != null && tags.size() > 0) {
            querySelect.append(" LEFT JOIN eventtags et on e.eventid = et.eventid");
            if (!having)
                queryCondition.append(" HAVING");
            else
                queryCondition.append(" AND");
            queryCondition.append(" ARRAY_AGG(et.tagid) && CAST(ARRAY");
            queryCondition.append(tags);
            queryCondition.append(" AS bigint[])");
        }

        String query = "SELECT aux.locationid, aux.name, COUNT(aux.eventid) FROM(" + querySelect.append(queryCondition) + ") AS aux GROUP BY aux.locationid, aux.name ORDER BY aux.name";
        Query queryNative = em.createNativeQuery(query);
        objects.forEach(queryNative::setParameter);
        List<Object[]> resultSet = queryNative.getResultList();
        if (resultSet.isEmpty())
            return Collections.emptyMap();
        final Map<Location, Integer> map = new HashMap<>();
        for (Object[] res : resultSet) {
            Location location = new Location(((Number) res[0]).longValue(), (String) res[1]);
            map.put(location, ((Number) res[2]).intValue());
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private Map<Type, Integer> getTypeFilterCount(List<Long> locations, Double minPrice, Double maxPrice, String searchQuery, List<Long> tags, Boolean showSoldOut, Boolean showNoTickets, Long userId) {
        boolean having = false, condition = false;
        Map<String, Object> objects = new HashMap<>();

        StringBuilder querySelect = new StringBuilder("SELECT t.typeid, t.name, t.name_en, e.eventid FROM types t JOIN events e ON t.typeid = e.typeid");
        StringBuilder queryCondition = new StringBuilder(" WHERE state != 1 AND date > NOW()");
        if (showSoldOut == null || !showSoldOut) {
            queryCondition.append(" AND state != 2");
        }
        if (userId != null) {
            queryCondition.append(" AND userid = :userid");
            objects.put("userid", userId);
        }
        if (locations != null && locations.size() > 0) {
            queryCondition.append(" AND locationid IN :locationids");
            objects.put("locationids", locations);
        }
        if (searchQuery != null && searchQuery.length() > 0) {
            queryCondition.append(" AND ((SELECT to_tsvector('Spanish', e.name) @@ websearch_to_tsquery(:searchquery)) = 't' OR e.name ILIKE CONCAT('%', :searchquery, '%'))");
            objects.put("searchquery", searchQuery);
        }

        queryCondition.append(" GROUP BY t.typeid, e.eventid ");

        if (showNoTickets == null || !showNoTickets) {
            querySelect.append(" LEFT JOIN (SELECT * FROM tickets ti WHERE (ti.starting IS NULL OR ti.starting <= NOW()) AND (ti.until IS NULL OR ti.until >= NOW())) AS ti ON e.eventid = ti.eventid");
            having = true;
            queryCondition.append(" HAVING");
            queryCondition.append(" COUNT(ti.ticketid) > 0");
        }

        if ((minPrice != null || maxPrice != null) && showNoTickets != null && showNoTickets) {
            querySelect.append(" LEFT JOIN (SELECT * FROM tickets ti WHERE (ti.starting IS NULL OR ti.starting <= NOW()) AND (ti.until IS NULL OR ti.until >= NOW())) AS ti ON e.eventid = ti.eventid");
        }

        if (minPrice != null) {
            if (!having) {
                queryCondition.append(" HAVING");
                having = true;
            } else queryCondition.append(" AND");
            queryCondition.append(" COALESCE(MIN(ti.price), 0) >= :minPrice");
            objects.put("minPrice", minPrice);
        }
        if (maxPrice != null) {
            if (!having) {
                queryCondition.append(" HAVING");
                having = true;
            } else queryCondition.append(" AND");
            queryCondition.append(" COALESCE(MIN(ti.price), 0) <= :maxPrice");
            objects.put("maxPrice", maxPrice);
        }

        if (tags != null && tags.size() > 0) {
            querySelect.append(" LEFT JOIN eventtags et on e.eventid = et.eventid");
            if (!having)
                queryCondition.append(" HAVING");
            else
                queryCondition.append(" AND");
            queryCondition.append(" ARRAY_AGG(et.tagid) && CAST(ARRAY");
            queryCondition.append(tags);
            queryCondition.append(" AS bigint[])");
        }

        String query = "SELECT aux.typeid, aux.name, aux.name_en, COUNT(aux.eventid) FROM (" + querySelect.append(queryCondition) + ") AS aux GROUP BY aux.typeid, aux.name, aux.name_en ORDER BY aux.typeid";
        Query queryNative = em.createNativeQuery(query);
        objects.forEach(queryNative::setParameter);
        List<Object[]> resultSet = queryNative.getResultList();
        if (resultSet.isEmpty())
            return Collections.emptyMap();
        final Map<Type, Integer> map = new HashMap<>();
        for (Object[] res : resultSet) {
            Type type = new Type(((Number) res[0]).longValue(), (String) res[1], (String) res[2]);
            map.put(type, ((Number) res[3]).intValue());
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private Map<Tag, Integer> getTagFilterCount(List<Long> locations, List<Long> types, Double minPrice, Double maxPrice, String searchQuery, Boolean showSoldOut, Boolean showNoTickets, Long userId) {
        boolean having = false;
        Map<String, Object> objects = new HashMap<>();

        StringBuilder querySelect = new StringBuilder("SELECT ta.tagid, ta.name, ta.name_en, e.eventid FROM tags ta JOIN eventtags et ON ta.tagid = et.tagid JOIN events e ON et.eventid = e.eventid");
        StringBuilder queryCondition = new StringBuilder(" WHERE state != 1 AND date > NOW()");
        if (showSoldOut == null || !showSoldOut) {
            queryCondition.append(" AND state != 2");
        }
        if (userId != null) {
            queryCondition.append(" AND userid = :userid");
            objects.put("userid", userId);
        }
        if (locations != null && locations.size() > 0) {
            queryCondition.append(" AND locationid IN :locationids");
            objects.put("locationids", locations);
        }
        if (types != null && types.size() > 0) {
            queryCondition.append(" AND typeid IN :typeids");
            objects.put("typeids", types);
        }
        if (searchQuery != null && searchQuery.length() > 0) {
            queryCondition.append(" AND ((SELECT to_tsvector('Spanish', e.name) @@ websearch_to_tsquery(:searchquery)) = 't' OR e.name ILIKE CONCAT('%', :searchquery, '%'))");
            objects.put("searchquery", searchQuery);
        }

        queryCondition.append(" GROUP BY ta.tagid, e.eventid");

        if (showNoTickets == null || !showNoTickets) {
            querySelect.append(" LEFT JOIN (SELECT * FROM tickets ti WHERE (ti.starting IS NULL OR ti.starting <= NOW()) AND (ti.until IS NULL OR ti.until >= NOW())) AS ti ON e.eventid = ti.eventid");
            having = true;
            queryCondition.append(" HAVING");
            queryCondition.append(" COUNT(ti.ticketid) > 0");
        }

        if ((minPrice != null || maxPrice != null) && showNoTickets != null && showNoTickets) {
            querySelect.append(" LEFT JOIN (SELECT * FROM tickets ti WHERE (ti.starting IS NULL OR ti.starting <= NOW()) AND (ti.until IS NULL OR ti.until >= NOW())) AS ti ON e.eventid = ti.eventid");
        }

        if (minPrice != null) {
            if (!having) {
                queryCondition.append(" HAVING");
                having = true;
            } else queryCondition.append(" AND");
            queryCondition.append(" COALESCE(MIN(ti.price), 0) >= :minPrice");
            objects.put("minPrice", minPrice);
        }
        if (maxPrice != null) {
            if (!having) {
                queryCondition.append(" HAVING");
                having = true;
            } else queryCondition.append(" AND");
            queryCondition.append(" COALESCE(MIN(ti.price), 0) <= :maxPrice");
            objects.put("maxPrice", maxPrice);
        }
        String query = "SELECT aux.tagid, aux.name, aux.name_en, COUNT(aux.eventid) FROM (" + querySelect.append(queryCondition) + ") AS aux GROUP BY aux.tagid, aux.name, aux.name_en ORDER BY aux.tagid";
        Query queryNative = em.createNativeQuery(query);
        objects.forEach(queryNative::setParameter);
        List<Object[]> resultSet = queryNative.getResultList();
        if (resultSet.isEmpty())
            return Collections.emptyMap();
        final Map<Tag, Integer> map = new HashMap<>();
        for (Object[] res : resultSet) {
            Tag tag = new Tag(((Number) res[0]).longValue(), (String) res[1], (String) res[2]);
            map.put(tag, ((Number) res[3]).intValue());
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private int getSoldOutFilterCount(List<Long> locations, List<Long> types, Double minPrice, Double maxPrice, String searchQuery, List<Long> tags, Boolean showNoTickets, Long userId) {
        boolean having = false;
        Map<String, Object> objects = new HashMap<>();

        StringBuilder querySelect = new StringBuilder("SELECT e.eventid FROM events e ");
        StringBuilder queryCondition = new StringBuilder(" WHERE state != 1 AND date > NOW()");
        queryCondition.append(" AND state = 2");
        if (userId != null) {
            queryCondition.append(" AND userid = :userid");
            objects.put("userid", userId);
        }
        if (locations != null && locations.size() > 0) {
            queryCondition.append(" AND locationid IN :locationids");
            objects.put("locationids", locations);
        }
        if (types != null && types.size() > 0) {
            queryCondition.append(" AND typeid IN :typeids");
            objects.put("typeids", types);
        }
        if (searchQuery != null && searchQuery.length() > 0) {
            queryCondition.append(" AND ((SELECT to_tsvector('Spanish', e.name) @@ websearch_to_tsquery(:searchquery)) = 't' OR e.name ILIKE CONCAT('%', :searchquery, '%'))");
            objects.put("searchquery", searchQuery);
        }

        queryCondition.append(" GROUP BY e.eventid");

        if (showNoTickets == null || !showNoTickets) {
            querySelect.append(" LEFT JOIN (SELECT * FROM tickets ti WHERE (ti.starting IS NULL OR ti.starting <= NOW()) AND (ti.until IS NULL OR ti.until >= NOW())) AS ti ON e.eventid = ti.eventid");
            having = true;
            queryCondition.append(" HAVING");
            queryCondition.append(" COUNT(ti.ticketid) > 0");
        }

        if ((minPrice != null || maxPrice != null) && showNoTickets != null && showNoTickets) {
            querySelect.append(" LEFT JOIN (SELECT * FROM tickets ti WHERE (ti.starting IS NULL OR ti.starting <= NOW()) AND (ti.until IS NULL OR ti.until >= NOW())) AS ti ON e.eventid = ti.eventid");
        }

        if (minPrice != null) {
            if (!having) {
                queryCondition.append(" HAVING");
                having = true;
            } else
                queryCondition.append(" AND");
            queryCondition.append(" COALESCE(MIN(ti.price), 0) >= :minPrice");
            objects.put("minPrice", minPrice);
        }
        if (maxPrice != null) {
            if (!having) {
                queryCondition.append(" HAVING");
                having = true;
            } else queryCondition.append(" AND");
            queryCondition.append(" COALESCE(MIN(ti.price), 0) <= :maxPrice");
            objects.put("maxPrice", maxPrice);
        }

        if (tags != null && tags.size() > 0) {
            querySelect.append(" LEFT JOIN eventtags et on e.eventid = et.eventid");
            if (!having)
                queryCondition.append(" HAVING");
            else
                queryCondition.append(" AND");
            queryCondition.append(" ARRAY_AGG(et.tagid) && CAST(ARRAY");
            queryCondition.append(tags);
            queryCondition.append(" AS bigint[])");
        }

        String query = "SELECT COUNT(aux.eventid) FROM (" + querySelect.append(queryCondition) + ") AS aux";
        Query queryNative = em.createNativeQuery(query);
        objects.forEach(queryNative::setParameter);
        List<Object[]> resultSet = queryNative.getResultList();
        if (resultSet.isEmpty())
            return 0;

        return ((Number) queryNative.getSingleResult()).intValue();
    }

    @SuppressWarnings("unchecked")
    private int getNoTicketsFilterCount(List<Long> locations, List<Long> types, Double minPrice, Double maxPrice, String searchQuery, List<Long> tags, Boolean showSoldOut, Long userId) {
        Map<String, Object> objects = new HashMap<>();

        StringBuilder querySelect = new StringBuilder("SELECT e.eventid FROM events e ");
        StringBuilder queryCondition = new StringBuilder(" WHERE state != 1 AND date > NOW()");
        if (showSoldOut == null || !showSoldOut) {
            queryCondition.append(" AND state != 2");
        }
        if (userId != null) {
            queryCondition.append(" AND userid = :userid");
            objects.put("userid", userId);
        }
        if (types != null && types.size() > 0) {
            queryCondition.append(" AND typeid IN :typeids");
            objects.put("typeids", types);
        }
        if (locations != null && locations.size() > 0) {
            queryCondition.append(" AND locationid IN :locationids");
            objects.put("locationids", locations);
        }
        if (searchQuery != null && searchQuery.length() > 0) {
            queryCondition.append(" AND ((SELECT to_tsvector('Spanish', e.name) @@ websearch_to_tsquery(:searchquery)) = 't' OR e.name ILIKE CONCAT('%', :searchquery, '%'))");
            objects.put("searchquery", searchQuery);
        }

        queryCondition.append(" GROUP BY e.eventid");

        querySelect.append(" LEFT JOIN (SELECT * FROM tickets ti WHERE (ti.starting IS NULL OR ti.starting <= NOW()) AND (ti.until IS NULL OR ti.until >= NOW())) AS ti ON e.eventid = ti.eventid");
        queryCondition.append(" HAVING");
        queryCondition.append(" COUNT(ti.ticketid) = 0");

        if (minPrice != null) {
            queryCondition.append(" AND");
            queryCondition.append(" COALESCE(MIN(ti.price), 0) >= :minPrice");
            objects.put("minPrice", minPrice);
        }
        if (maxPrice != null) {
            queryCondition.append(" AND");
            queryCondition.append(" COALESCE(MIN(ti.price), 0) <= :maxPrice");
            objects.put("maxPrice", maxPrice);
        }

        if (tags != null && tags.size() > 0) {
            querySelect.append(" LEFT JOIN eventtags et on e.eventid = et.eventid");
            queryCondition.append(" AND");
            queryCondition.append(" ARRAY_AGG(et.tagid) && CAST(ARRAY");
            queryCondition.append(tags);
            queryCondition.append(" AS bigint[])");
        }

        String query = "SELECT COUNT(aux.eventid) FROM (" + querySelect.append(queryCondition) + ") AS aux";
        Query queryNative = em.createNativeQuery(query);
        objects.forEach(queryNative::setParameter);
        List<Object[]> resultSet = queryNative.getResultList();
        if (resultSet.isEmpty())
            return 0;

        return ((Number) queryNative.getSingleResult()).intValue();
    }
}
