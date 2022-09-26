package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Tag;
import ar.edu.itba.paw.persistence.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagDao tagDao;

    @Override
    public List<Tag> getAll() {
        return tagDao.getAll();
    }
}
