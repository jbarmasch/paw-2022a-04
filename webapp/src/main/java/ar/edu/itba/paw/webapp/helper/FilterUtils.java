package ar.edu.itba.paw.webapp.helper;

import java.util.List;
import java.util.Map;

public class FilterUtils {
    public static String createFilter(Map<String, String> filters) {
        StringBuilder queryBuilder = new StringBuilder();
        for (Map.Entry<String, String> filter : filters.entrySet()) {
            queryBuilder.append("&").append(filter.getKey()).append("=").append(filter.getValue());
        }
        return queryBuilder.toString();
    }

    public static String createFilter(String locations, String types, Double minPrice, Double maxPrice) {
        StringBuilder queryBuilder = new StringBuilder();
        if (locations != null)
            queryBuilder.append("locations=").append(locations);
        if (types != null) {
            if (queryBuilder.length() > 0)
                queryBuilder.append("&");
            queryBuilder.append("types=").append(types);
        }
        if (minPrice != null) {
            if (queryBuilder.length() > 0)
                queryBuilder.append("&");
            queryBuilder.append("&minPrice=").append(minPrice);
        }
        if (maxPrice != null) {
            if (queryBuilder.length() > 0)
                queryBuilder.append("&");
            queryBuilder.append("&maxPrice=").append(maxPrice);
        }
        return queryBuilder.toString();
    }
}
