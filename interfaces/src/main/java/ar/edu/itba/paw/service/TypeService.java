package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Type;

import java.util.List;
import java.util.Locale;

public interface TypeService {
    List<Type> getAll(Locale locale);
}
