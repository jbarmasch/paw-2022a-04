package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class UserJpaDao implements UserDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public User create(String username, String password, String mail) {
        final User user = new User(username, password, mail, Collections.singletonList(em.getReference(Role.class, RoleEnum.USER.ordinal() + 1)));
        em.persist(user);
        return user;
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

    @Override
    public List<EventBooking> getAllBookingsFromUser(long userId, int page) {
        final TypedQuery<EventBooking> query = em.createQuery("from EventBooking as eb where eb.user.id = :userid order by eb.event.date DESC", EventBooking.class);
        query.setParameter("userid", userId);
        return query.getResultList();
    }

    @Override
    public Optional<EventBooking> getBookingFromUser(long userId, long eventId) {
        final TypedQuery<EventBooking> query = em.createQuery("from EventBooking as eb where eb.user.id = :userid AND eb.event.id = :eventid", EventBooking.class);
        query.setParameter("userid", userId);
        query.setParameter("eventid", eventId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<EventBooking> getBooking(String code) {
        final TypedQuery<EventBooking> query = em.createQuery("from EventBooking as eb where eb.code = :code", EventBooking.class);
        query.setParameter("code", code);
        return query.getResultList().stream().findFirst();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<EventStats> getEventStats(long id) {
        Query query = em.createNativeQuery("SELECT events, bookings, attendance, income, eventid FROM ((SELECT COUNT(*) AS events, SUM(bookings) AS bookings, SUM(bookings) / SUM(COALESCE(NULLIF(qty, 0), 1)) " +
               "AS attendance, SUM(income) AS income FROM (SELECT e.eventid, SUM(COALESCE(booked, 0)) AS bookings, SUM(COALESCE(maxTickets, 0)) AS qty, " +
               "booked * price AS income FROM events e LEFT JOIN tickets t ON t.eventId = e.eventId WHERE e.userId = :userId GROUP BY e.eventid, booked, price) AS aux " +
               ") AS general CROSS JOIN (SELECT ec.eventid FROM events ec JOIN (SELECT e.eventid, SUM(COALESCE(booked, 0)) AS sumqty FROM events e LEFT JOIN tickets t ON " +
               "t.eventId = e.eventId WHERE e.userid = :userId GROUP BY e.eventid ORDER BY sumqty DESC LIMIT 1) AS aux ON ec.eventid = aux.eventid) AS event) AS aux"                );
        query.setParameter("userId", id);
        List<Object[]> resultSet = query.getResultList();
        Object[] result = resultSet.get(0);
        if (result == null)
            return Optional.empty();
        return Optional.of(new EventStats(((Number) result[0]).intValue(), ((Number) result[1]).intValue(),
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
        final TypedQuery<EventBooking> query = em.createQuery("from EventBooking as eb where eb.user.id = :userid and eb.event.date <= :date and eb.event.organizer.id = :organizerid", EventBooking.class);
        query.setParameter("userid", userId);
        query.setParameter("organizerid", organizerId);
        query.setParameter("date", LocalDateTime.now());
        return !query.getResultList().isEmpty();
    }
}