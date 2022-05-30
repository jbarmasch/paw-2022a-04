package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Location;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class LocationJpaDao implements LocationDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Location> getAll() {
        final TypedQuery<Location> query = em.createQuery("from Location", Location.class);
        return query.getResultList();
    }
}
