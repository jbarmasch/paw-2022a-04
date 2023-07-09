package ar.edu.itba.paw.service;

import ar.edu.itba.paw.exceptions.FilterRequestException;
import ar.edu.itba.paw.model.FilterType;
import ar.edu.itba.paw.persistence.FilterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilterServiceImpl implements FilterService {
    @Autowired
    private FilterDao filterDao;

    @Override
    public FilterType getFilterType(List<Long> locations, List<Long> types, Double minPrice, Double maxPrice, String searchQuery, List<Long> tags, Boolean showSoldOut, Boolean showNoTickets, Long userId) {
        for (Long location : locations) {
            if (location == null) {
                throw new FilterRequestException();
            }
        }
        for (Long type : types) {
            if (type == null) {
                throw new FilterRequestException();
            }
        }
        for (Long tag : tags) {
            if (tag == null) {
                throw new FilterRequestException();
            }
        }
        return filterDao.getFilterType(locations, types, minPrice, maxPrice, searchQuery, tags, showSoldOut, showNoTickets, userId);
    }
}


