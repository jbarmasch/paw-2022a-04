package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.InvalidTagException;
import ar.edu.itba.paw.model.Tag;
import ar.edu.itba.paw.persistence.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagDao tagDao;

    @Override
    public List<Tag> getAll() {
        return tagDao.getAll();
    }

    @Override
    public List<Tag> getTagsById(Long[] tagIds) {
        List<Tag> tagList = null;
        if (tagIds != null) {
            tagList = new ArrayList<>();
            for (Long tagId : tagIds) {
                Tag tag = tagDao.getTagById(tagId).orElseThrow(InvalidTagException::new);
                tagList.add(tag);
            }
        }
        return tagList;
    }
}
