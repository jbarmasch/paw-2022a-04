package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Type;
import ar.edu.itba.paw.persistence.TypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeDao typeDao;

    @Override
    public List<Type> getAll() {
        return typeDao.getAll();
    }

    @Override
    public Optional<Type> getTypeById(long id) {
        return typeDao.getTypeById(id);
    }
}
