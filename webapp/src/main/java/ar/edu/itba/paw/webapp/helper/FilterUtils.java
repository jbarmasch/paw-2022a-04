package ar.edu.itba.paw.webapp.helper;

import java.util.Map;

public class FilterUtils {
    public static String createFilter(Map<String, Object> filters) {
        StringBuilder queryBuilder = new StringBuilder();
        boolean isFirst = true;
        for (Map.Entry<String, Object> filter : filters.entrySet()) {
            if (filter.getValue() == null || filter.getValue().toString().isEmpty())
                continue;

            if (isFirst) {
                queryBuilder.append(filter.getKey()).append("=").append(filter.getValue());
                isFirst = false;
            }
            else
                queryBuilder.append("&").append(filter.getKey()).append("=").append(filter.getValue());
        }
        return queryBuilder.toString();
    }

    public static String createFilter(String locations, String types, Double minPrice, Double maxPrice) {
        StringBuilder queryBuilder = new StringBuilder();
        boolean append = false;
        if (locations != null) {
            append = true;
            queryBuilder.append("locations=").append(locations);
        }
        if (types != null) {
            if (append)
                queryBuilder.append("&");
            append = true;
            queryBuilder.append("types=").append(types);
        }
        if (minPrice != null) {
            if (append)
                queryBuilder.append("&");
            append = true;
            queryBuilder.append("&minPrice=").append(minPrice);
        }
        if (maxPrice != null) {
            if (append)
                queryBuilder.append("&");
            queryBuilder.append("&maxPrice=").append(maxPrice);
        }
        return queryBuilder.toString();
    }
}
