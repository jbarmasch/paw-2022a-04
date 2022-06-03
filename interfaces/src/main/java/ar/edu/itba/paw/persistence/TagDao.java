package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Tag;
import java.util.List;
import java.util.Locale;

public interface TagDao {
    List<Tag> getAll();
}
