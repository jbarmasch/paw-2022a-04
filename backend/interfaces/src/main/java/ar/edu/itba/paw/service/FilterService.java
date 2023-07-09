package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.FilterType;

import java.util.List;

public interface FilterService {
    FilterType getFilterType(List<Long> locations, List<Long> types, Double minPrice, Double maxPrice, String searchQuery, List<Long> tags, Boolean showSoldOut, Boolean showNoTickets, Long userId);
}
