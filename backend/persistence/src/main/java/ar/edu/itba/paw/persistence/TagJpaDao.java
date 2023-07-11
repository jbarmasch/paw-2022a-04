package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TagJpaDao implements TagDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Tag> getAll() {
        final TypedQuery<Tag> query = em.createQuery("from Tag", Tag.class);
        return query.getResultList();
    }

    @Override
    public Optional<Tag> getTagById(Long tagId) {
        return Optional.ofNullable(em.find(Tag.class, tagId));
    }
}
