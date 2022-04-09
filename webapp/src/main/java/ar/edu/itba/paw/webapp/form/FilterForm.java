package ar.edu.itba.paw.webapp.form;

public class FilterForm {
    private String filters;
    private String locations;
    private String types;
    private Double minPrice;
    private Double maxPrice;

    public String getLocations() {
        return locations;
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

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }
}
