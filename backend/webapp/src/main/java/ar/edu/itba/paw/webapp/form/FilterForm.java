package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validations.Price;

import javax.validation.constraints.DecimalMin;

@Price
public class FilterForm {
    private String locations;
    private String types;
    private String tags;

    @DecimalMin("0.0")
    private Double minPrice;

    @DecimalMin("0.0")
    private Double maxPrice;

    private String searchQuery;
    private String order;
    private String username;
    private boolean showSoldOut;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public boolean getShowSoldOut() {
        return showSoldOut;
    }

    public void setShowSoldOut(boolean showSoldOut) {
        this.showSoldOut = showSoldOut;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
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

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
