package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Type;

import java.util.List;
import java.util.Optional;

public interface TypeService {
    List<Type> getAll();

    Optional<Type> getTypeById(long id);
}
