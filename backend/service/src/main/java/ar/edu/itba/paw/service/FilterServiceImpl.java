package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.persistence.FilterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class FilterServiceImpl implements FilterService {
    @Autowired
    private FilterDao filterDao;

    @Override
    public FilterType getFilterType(List<Long> locations, List<Long> types, Double minPrice, Double maxPrice, String searchQuery, List<Long> tags, Boolean showSoldOut, Boolean showNoTickets, Long userId) {
        for (Long location : locations) {
            if (location == null) {
                // TODO: Change
                throw new RuntimeException();
            }
        }
        for (Long type : types) {
            if (type == null) {
                // TODO: Change
                throw new RuntimeException();
            }
        }
        for (Long tag : tags) {
            if (tag == null) {
                // TODO: Change
                throw new RuntimeException();
            }
        }
        return filterDao.getFilterType(locations, types, minPrice, maxPrice, searchQuery, tags, showSoldOut, showNoTickets, userId);
    }
}


