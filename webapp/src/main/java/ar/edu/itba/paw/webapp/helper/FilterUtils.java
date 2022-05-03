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
}
