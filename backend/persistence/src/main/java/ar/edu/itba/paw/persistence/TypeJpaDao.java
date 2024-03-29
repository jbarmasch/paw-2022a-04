package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Type;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class TypeJpaDao implements TypeDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Type> getAll() {
        final TypedQuery<Type> query = em.createQuery("from Type", Type.class);
        return query.getResultList();
    }

    @Override
    public Optional<Type> getTypeById(long id) {
        return Optional.ofNullable(em.find(Type.class, id));
    }
}
