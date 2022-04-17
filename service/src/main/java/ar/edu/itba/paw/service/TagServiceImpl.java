package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Tag;
import ar.edu.itba.paw.persistence.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(final TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<Tag> getAll() {
        return tagDao.getAll();
    }

    @Override
    public Optional<Tag> getTagById(long id) {
        return tagDao.getTagById(id);
    }
}
