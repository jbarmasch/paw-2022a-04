package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.FilterType;
import java.util.List;

public interface FilterDao {
   FilterType getFilterType(List<Integer> locations, List<Integer> types, Double minPrice, Double maxPrice, String searchQuery, List<Integer> tags, Boolean showSoldOut, Boolean showNoTickets);
}