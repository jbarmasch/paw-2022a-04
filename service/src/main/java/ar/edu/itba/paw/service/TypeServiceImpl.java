package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Type;
import ar.edu.itba.paw.persistence.TypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class TypeServiceImpl implements TypeService {
    private final TypeDao typeDao;

    @Autowired
    public TypeServiceImpl(final TypeDao typeDao) {
        this.typeDao = typeDao;
    }

    @Override
    public List<Type> getAll() {
        return typeDao.getAll();
    }
}
