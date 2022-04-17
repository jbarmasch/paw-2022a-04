package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> getAll();
    Optional<Tag> getTagById(long id);
}
