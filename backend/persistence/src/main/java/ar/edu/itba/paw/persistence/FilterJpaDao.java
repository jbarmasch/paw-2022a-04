package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.exceptions.EventFinishedException;
import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilterJpaDao implements FilterDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public FilterType getFilterType(List<Integer> locations, List<Integer> types, Double minPrice, Double maxPrice, String searchQuery, List<Integer> tags, Boolean showSoldOut) {
        return new FilterType(
            getLocationFilterCount(types, minPrice, maxPrice, searchQuery, tags, showSoldOut),
            getTypeFilterCount(locations, minPrice, maxPrice, searchQuery, tags, showSoldOut),
            getTagFilterCount(locations, types, minPrice, maxPrice, searchQuery, showSoldOut)
        );
    }

    @SuppressWarnings("unchecked")
    private Map<Location, Integer> getLocationFilterCount(List<Integer> types, Double minPrice, Double maxPrice, String searchQuery, List<Integer> tags, Boolean showSoldOut) {
        boolean having = false, condition = false;
        Map<String, Object> objects = new HashMap<>();
        objects.put("date", Timestamp.valueOf(LocalDateTime.now()));
        StringBuilder querySelect = new StringBuilder("FROM locations l JOIN events e ON l.locationid = e.locationid");
        StringBuilder queryCondition = new StringBuilder(" WHERE state != 1 AND date > :date");
        if (showSoldOut == null || !showSoldOut) {
            queryCondition.append(" AND state != 2");
        }
        if (types != null && types.size() > 0) {
            queryCondition.append(" AND typeid IN :typeids");
            objects.put("typeids", types);
        }
        if (searchQuery != null && searchQuery.length() > 0) {
            queryCondition.append(" AND ((SELECT to_tsvector('Spanish', e.name) @@ websearch_to_tsquery(:searchquery)) = 't' OR e.name ILIKE CONCAT('%', :searchquery, '%'))");
            objects.put("searchquery", searchQuery);
        }

        queryCondition.append(" GROUP BY l.locationid ");

        if (minPrice != null || maxPrice != null) {
            querySelect.append(" LEFT JOIN tickets t on e.eventid = t.eventid");
            
            if (minPrice != null) {
                condition = true;
                queryCondition.append(" HAVING");
                having = true;
                queryCondition.append(" COALESCE(MIN(t.price), 0) >= :minPrice");
                objects.put("minPrice", minPrice);
            }
            if (maxPrice != null) {
                if (!condition)
                    querySelect.append(" LEFT JOIN tickets t on e.eventid = t.eventid");
                if (!having) {
                    queryCondition.append(" HAVING");
                    having = true;
                } else queryCondition.append(" AND");
                queryCondition.append(" COALESCE(MIN(t.price), 0) <= :maxPrice");
                objects.put("maxPrice", maxPrice);
            }
        }
        if (tags != null && tags.size() > 0) {
            querySelect.append(" LEFT JOIN eventtags et on e.eventid = et.eventid");
            if (!having)
                queryCondition.append(" HAVING");
            else
                queryCondition.append(" AND");
            queryCondition.append(" ARRAY_AGG(et.tagid) @> ARRAY");
            queryCondition.append(tags);
        }

        String query = "SELECT l.locationid, l.name, COUNT(e.eventid) " + querySelect.append(queryCondition) + " ORDER BY l.name";
        Query queryNative = em.createNativeQuery(query);
        objects.forEach(queryNative::setParameter);
        List<Object[]> resultSet = queryNative.getResultList();
        if (resultSet.isEmpty())
            return new HashMap<>();
        final Map<Location, Integer> map = new HashMap<>();
        for (Object[] res : resultSet) {
            Location location = new Location(((Number) res[0]).longValue(), (String) res[1]);
            map.put(location, ((Number) res[2]).intValue());
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private Map<Type, Integer> getTypeFilterCount(List<Integer> locations, Double minPrice, Double maxPrice, String searchQuery, List<Integer> tags, Boolean showSoldOut) {
        boolean having = false, condition = false;
        Map<String, Object> objects = new HashMap<>();
        objects.put("date", Timestamp.valueOf(LocalDateTime.now()));
        StringBuilder querySelect = new StringBuilder("FROM types t JOIN events e ON t.typeid = e.typeid");
        StringBuilder queryCondition = new StringBuilder(" WHERE state != 1 AND date > :date");
        if (showSoldOut == null || !showSoldOut) {
            queryCondition.append(" AND state != 2");
        }
        if (locations != null && locations.size() > 0) {
            queryCondition.append(" AND locationid IN :locationids");
            objects.put("locationids", locations);
        }
        if (searchQuery != null && searchQuery.length() > 0) {
            queryCondition.append(" AND ((SELECT to_tsvector('Spanish', e.name) @@ websearch_to_tsquery(:searchquery)) = 't' OR e.name ILIKE CONCAT('%', :searchquery, '%'))");
            objects.put("searchquery", searchQuery);
        }

        queryCondition.append(" GROUP BY t.typeid ");

        if (minPrice != null || maxPrice != null) {
            querySelect.append(" LEFT JOIN tickets ti on e.eventid = ti.eventid");
            
            if (minPrice != null) {
                condition = true;
                queryCondition.append(" HAVING");
                having = true;
                queryCondition.append(" COALESCE(MIN(t.price), 0) >= :minPrice");
                objects.put("minPrice", minPrice);
            }
            if (maxPrice != null) {
                if (!condition)
                    querySelect.append(" LEFT JOIN tickets ti on e.eventid = ti.eventid");
                if (!having) {
                    queryCondition.append(" HAVING");
                    having = true;
                } else queryCondition.append(" AND");
                queryCondition.append(" COALESCE(MIN(t.price), 0) <= :maxPrice");
                objects.put("maxPrice", maxPrice);
            }
        }
        if (tags != null && tags.size() > 0) {
            querySelect.append(" LEFT JOIN eventtags et on e.eventid = et.eventid");
            if (!having)
                queryCondition.append(" HAVING");
            else
                queryCondition.append(" AND");
            queryCondition.append(" ARRAY_AGG(et.tagid) @> ARRAY");
            queryCondition.append(tags);
        }

        String query = "SELECT t.typeid, t.name, t.name_en, COUNT(e.eventid) " + querySelect.append(queryCondition) + "ORDER BY t.typeid";
        Query queryNative = em.createNativeQuery(query);
        objects.forEach(queryNative::setParameter);
        List<Object[]> resultSet = queryNative.getResultList();
        if (resultSet.isEmpty())
            return new HashMap<>();
        final Map<Type, Integer> map = new HashMap<>();
        for (Object[] res : resultSet) {
            Type type = new Type(((Number) res[0]).longValue(), (String) res[1], (String) res[2]);
            map.put(type, ((Number) res[3]).intValue());
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private Map<Tag, Integer> getTagFilterCount(List<Integer> locations, List<Integer> types, Double minPrice, Double maxPrice, String searchQuery, Boolean showSoldOut) {
        boolean having = false, condition = false;
        Map<String, Object> objects = new HashMap<>();
        objects.put("date", Timestamp.valueOf(LocalDateTime.now()));
        StringBuilder querySelect = new StringBuilder("FROM tags ta JOIN eventtags et ON ta.tagid = et.tagid JOIN events e ON et.eventid = e.eventid");
        StringBuilder queryCondition = new StringBuilder(" WHERE state != 1 AND date > :date");
        if (showSoldOut == null || !showSoldOut) {
            queryCondition.append(" AND state != 2");
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

        queryCondition.append(" GROUP BY ta.tagid ");

        if (minPrice != null || maxPrice != null) {
            querySelect.append(" LEFT JOIN tickets ti on e.eventid = ti.eventid");
            
            if (minPrice != null) {
                condition = true;
                queryCondition.append(" HAVING");
                having = true;
                queryCondition.append(" COALESCE(MIN(t.price), 0) >= :minPrice");
                objects.put("minPrice", minPrice);
            }
            if (maxPrice != null) {
                if (!condition)
                    querySelect.append(" LEFT JOIN tickets ti on e.eventid = ti.eventid");
                if (!having) {
                    queryCondition.append(" HAVING");
                    having = true;
                } else queryCondition.append(" AND");
                queryCondition.append(" COALESCE(MIN(t.price), 0) <= :maxPrice");
                objects.put("maxPrice", maxPrice);
            }
        }

        String query = "SELECT ta.tagid, ta.name, ta.name_en, COUNT(e.eventid) " + querySelect.append(queryCondition) + "ORDER BY ta.tagid";
        Query queryNative = em.createNativeQuery(query);
        objects.forEach(queryNative::setParameter);
        List<Object[]> resultSet = queryNative.getResultList();
        if (resultSet.isEmpty())
            return new HashMap<>();
        final Map<Tag, Integer> map = new HashMap<>();
        for (Object[] res : resultSet) {
            Tag tag = new Tag(((Number) res[0]).longValue(), (String) res[1], (String) res[2]);
            map.put(tag, ((Number) res[3]).intValue());
        }
        return map;
    }
}
