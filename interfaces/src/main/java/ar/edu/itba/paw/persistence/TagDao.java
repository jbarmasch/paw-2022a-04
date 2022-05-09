package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Tag;
import java.util.List;
import java.util.Optional;

public interface TagDao {
    List<Tag> getAll();
    Optional<Tag> getTagById(long id);
}
