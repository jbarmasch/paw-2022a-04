package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Tag;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface TagService {
    List<Tag> getAll(Locale locale);
}
