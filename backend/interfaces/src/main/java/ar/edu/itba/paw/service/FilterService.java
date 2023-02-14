package ar.edu.itba.paw.service;

import java.util.List;

import ar.edu.itba.paw.model.FilterType;

public interface FilterService {
   FilterType getFilterType(List<Integer> locations, List<Integer> types, Double minPrice, Double maxPrice, String searchQuery, List<Integer> tags, Boolean showSoldOut, Boolean showNoTickets, Integer userId);
}
