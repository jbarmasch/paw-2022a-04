package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Type;

import java.util.List;

public interface TypeDao {
    List<Type> getAll();
}
