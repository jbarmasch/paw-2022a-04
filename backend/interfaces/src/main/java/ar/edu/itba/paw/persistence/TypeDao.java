package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Type;

import java.util.List;
import java.util.Optional;

public interface TypeDao {
    List<Type> getAll();

    Optional<Type> getTypeById(long id);
}
