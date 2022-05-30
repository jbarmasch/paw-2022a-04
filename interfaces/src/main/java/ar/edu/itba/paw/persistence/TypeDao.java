package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Type;
import java.util.List;
import java.util.Locale;

public interface TypeDao {
    List<Type> getAll(Locale locale);
}
