package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Tag;

import java.util.List;

public interface TagDao {
    List<Tag> getAll();
}
