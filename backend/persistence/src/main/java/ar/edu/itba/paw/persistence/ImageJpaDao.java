package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Image;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class ImageJpaDao implements ImageDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Image createImage(byte[] imageArray) {
        if (imageArray == null)
            return em.getReference(Image.class, 1L);

        Image image = new Image(imageArray);
        em.persist(image);
        return image;
    }

    @Override
    public Optional<Image> getImageById(long id) {
        return Optional.ofNullable(em.find(Image.class, id));
    }
}
