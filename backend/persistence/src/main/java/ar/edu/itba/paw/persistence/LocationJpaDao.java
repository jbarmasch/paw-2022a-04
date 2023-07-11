package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Location;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class LocationJpaDao implements LocationDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Location> getAll() {
        final TypedQuery<Location> query = em.createQuery("from Location", Location.class);
        return query.getResultList();
    }

    @Override
    public Optional<Location> getLocationById(long id) {
        return Optional.ofNullable(em.find(Location.class, id));
    }

}
