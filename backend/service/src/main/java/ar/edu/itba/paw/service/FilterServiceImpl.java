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
    public FilterType getFilterType(List<Integer> locations, List<Integer> types, Double minPrice, Double maxPrice, String searchQuery, List<Integer> tags, Boolean showSoldOut, Boolean showNoTickets, Integer userId) {
        for (Integer loc : locations) {
            if (loc == null) {
                // TODO: Change
                throw new RuntimeException();
            }
        }
        for (Integer typ : types) {
            if (typ == null) {
                // TODO: Change
                throw new RuntimeException();
            }
        }
        for (Integer tag : tags) {
            if (tag == null) {
                // TODO: Change
                throw new RuntimeException();
            }
        }
        return filterDao.getFilterType(locations, types, minPrice, maxPrice, searchQuery, tags, showSoldOut, showNoTickets, userId);
    }
}


