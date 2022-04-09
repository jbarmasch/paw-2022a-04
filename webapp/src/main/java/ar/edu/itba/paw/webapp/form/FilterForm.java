package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Arrays;

public class FilterForm {
    private String[] filters;
    private String[] locations;
    private String[] types;
    private Double minPrice;
    private Double maxPrice;

    public FilterForm(String[] filters, String[] locations, String[] types, Double minPrice, Double maxPrice) {
        this.filters = filters;
        this.locations = locations;
        this.types = types;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String getLocations() {
        return getURLFromArray(locations);
    }

    public void setLocations(String[] locations) {
        this.locations = locations;
    }

    public String getTypes() {
        return getURLFromArray(types);
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String getFilters() {
        return getURLFromArray(filters);
    }

    private String getURLFromArray(String[] strings) {
        return Arrays.asList(strings).toString().substring(1, strings.length - 2);
    }

    public void setFilters(String[] filters) {
        this.filters = filters;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
