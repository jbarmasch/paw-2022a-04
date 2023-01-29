package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserJpaDao implements UserDao {
    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("unchecked")
    @Override
    public UserList getAllUsers(int page) {
        Query query = em.createNativeQuery("SELECT u.userid FROM users u LIMIT 10 OFFSET :page");
        query.setParameter("page", 10 * (page - 1));
        final List<Long> ids = (List<Long>) query.getResultList().stream().map(o -> ((Number) o).longValue()).collect(Collectors.toList());
        if (ids.isEmpty())
            return new UserList(new ArrayList<>(), 0);
        final TypedQuery<User> typedQuery = em.createQuery("from User where userid IN :ids", User.class);
        typedQuery.setParameter("ids", ids);
        Query count = em.createNativeQuery("SELECT COUNT(userid) FROM users");
        return new UserList(typedQuery.getResultList(), (int) Math.ceil((double) ((Number) count.getSingleResult()).intValue() / 10));
    }

    @Override
    public User createUser(String username, String password, String mail, Locale locale) {
        final User user = new User(username, password, mail, em.getReference(Role.class, RoleEnum.ROLE_USER.getValue()), locale.getLanguage());
        em.persist(user);
        return user;
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserList filterBy(String searchQuery, Order order, int page) {
        Map<String, Object> objects = new HashMap<>();
        StringBuilder querySelect = new StringBuilder("FROM users u JOIN userroles ur ON u.userid = ur.userid LEFT JOIN events e ON u.userid = e.userid");
        StringBuilder queryCondition = new StringBuilder(" WHERE ur.roleid = 2");
        if (searchQuery != null) {
            queryCondition.append(" AND ((SELECT to_tsvector('Spanish', u.username) @@ websearch_to_tsquery(:searchquery)) = 't' OR u.username ILIKE CONCAT('%', :searchquery, '%'))");
            objects.put("searchquery", searchQuery);
        }
        StringBuilder orderQuery = new StringBuilder();
        if (order != null) {
            orderQuery.append(" ORDER BY ").append(order.getOrder()).append(" ").append(order.getOrderBy());
        } else {
            orderQuery.append(" ORDER BY username ");
        }

        objects.put("page", (page - 1) * 8);
        String query = "SELECT aux.userid FROM (SELECT u.userid " + querySelect.append(queryCondition) + " GROUP BY u.userid" + orderQuery + ") as aux ";
        Query queryNative = em.createNativeQuery(query + " LIMIT 8 OFFSET :page");
        objects.forEach(queryNative::setParameter);
        final List<Long> ids = (List<Long>) queryNative.getResultList().stream().map(o -> ((Number) o).longValue()).collect(Collectors.toList());
        if (ids.isEmpty())
            return new UserList(new ArrayList<>(), 0);
        System.out.println("IDS" + ids);
        final TypedQuery<User> typedQuery = em.createQuery("FROM User WHERE userid IN :ids " + orderQuery, User.class);
        typedQuery.setParameter("ids", ids);

        System.out.println("CACA" + typedQuery.getResultList());

        String theQuery = String.valueOf(querySelect);
//        theQuery = theQuery.replace("GROUP BY u.userid", "");
        Query count = em.createNativeQuery("SELECT COUNT(u.userid) " + theQuery);
        objects.remove("page");
        objects.forEach(count::setParameter);
        return new UserList(typedQuery.getResultList(), (int) Math.ceil((double) ((Number) count.getSingleResult()).intValue() / 8));
    }

    @Override
    public User createBouncer(String password) {
        final User user = new User("", password, "", em.getReference(Role.class, RoleEnum.ROLE_BOUNCER.getValue()));
        em.persist(user);
        return user;
    }

    @Override
    public void updateUser(long userId, String username, String password, String mail) {
        final User user = em.find(User.class, userId);
        user.setUsername(username);
        if (password != null)
            user.setPassword(password);
        user.setMail(mail);
        em.persist(user);
    }

    @Override
    public void makeCreator(User user) {
        user.addRole(em.getReference(Role.class, RoleEnum.ROLE_CREATOR.getValue()));
        em.persist(user);
    }

    @Override
    public void makeBouncer(User user) {
        user.addRole(em.getReference(Role.class, RoleEnum.ROLE_BOUNCER.getValue()));
        em.persist(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.username = :username", User.class);
        query.setParameter("username", username);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<OrganizerStats> getOrganizerStats(long id) {
        Query query = em.createNativeQuery("SELECT events, COALESCE(bookings, 0) AS bookings, COALESCE(bookings / COALESCE(NULLIF(expetedBooked, 0), 1), 0) " +
                "AS attendance, COALESCE(income, 0) AS income, eventid FROM ( " +
                "SELECT (SELECT COUNT(eventid) FROM events e WHERE e.userid = :userid) AS events, SUM(realqty) AS bookings, SUM(booked) AS expetedBooked, " +
                "SUM(price * realqty) AS income FROM (SELECT booked, price, SUM(CASE WHEN confirmed THEN tb.qty ELSE 0 END) AS realqty FROM events e LEFT JOIN tickets t " +
                "ON e.eventId = t.eventId LEFT JOIN ticketbookings tb on t.ticketId = tb.ticketId JOIN eventbookings eb on tb.id = eb.id WHERE e.userid = :userid " +
                "GROUP BY t.ticketid) AS aux) AS general CROSS JOIN (SELECT ec.eventid FROM events ec JOIN (SELECT e.eventid, SUM(COALESCE(booked, 0)) AS sumqty " +
                "FROM events e LEFT JOIN tickets t ON t.eventId = e.eventId WHERE e.userid = :userid AND e.state != 1 GROUP BY e.eventid ORDER BY sumqty DESC LIMIT 1) AS aux " +
                "ON ec.eventid = aux.eventid) AS event");
        query.setParameter("userid", id);
        List<Object[]> resultSet = query.getResultList();
        if (resultSet.isEmpty())
            return Optional.empty();
        Object[] result = resultSet.get(0);
        if (result == null)
            return Optional.empty();
        return Optional.of(new OrganizerStats(((Number) result[0]).intValue(), ((Number) result[1]).intValue(),
                em.getReference(Event.class, ((Number) result[4]).longValue()), ((Number) result[2]).doubleValue(), ((Number) result[3]).doubleValue()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<UserStats> getUserStats(long id) {
        Query query = em.createNativeQuery("SELECT attended, booked, favTypeId, favLocId " +
                "FROM (SELECT COUNT(*) AS attended, SUM(qty) AS booked, MODE() WITHIN GROUP (ORDER BY e.typeId) AS favTypeId, MODE() WITHIN GROUP " +
                "(ORDER BY e.locationId) AS favLocId FROM eventbookings eb JOIN ticketbookings tb ON eb.id = tb.id JOIN events e ON eb.eventId = e.eventId " +
                "WHERE eb.userId = :userId) AS pre JOIN types t ON pre.favTypeId = t.typeid JOIN locations l ON pre.favLocId = l.locationid");
        query.setParameter("userId", id);
        List<Object[]> resultSet = query.getResultList();
        if (resultSet.isEmpty())
            return Optional.empty();
        Object[] result = resultSet.get(0);
        if (result == null)
            return Optional.empty();
        return Optional.of(new UserStats(((Number) result[0]).intValue(), ((Number) result[1]).intValue(),
                em.getReference(Type.class, ((Number) result[2]).longValue()), em.getReference(Location.class, ((Number) result[3]).longValue())));
    }

    @Override
    public Optional<User> findByMail(String mail) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.mail = :mail", User.class);
        query.setParameter("mail", mail);
        return query.getResultList().stream().findFirst();
    }

    @Transactional
    @Override
    public void rateUser(long userId, long organizerId, int rating) {
        User user = em.getReference(User.class, userId);
        User organizer = em.getReference(User.class, organizerId);
        Rating rate = em.find(Rating.class, new RatingId(user, organizer));
        if (rate == null)
            rate = new Rating(user, organizer, rating);
        else
            rate.setRating(rating);
        em.persist(rate);
    }

    @Override
    public boolean canRate(long organizerId, long userId) {
        final TypedQuery<EventBooking> query = em.createQuery("from EventBooking as eb where eb.user.id = :userid and eb.event.date > :date and eb.event.date < NOW() and eb.event.organizer.id = :organizerid", EventBooking.class);
        query.setParameter("userid", userId);
        query.setParameter("organizerid", organizerId);
        query.setParameter("date", LocalDateTime.now().minusMonths(1));
        return !query.getResultList().isEmpty();
    }
}
