package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validations.Price;
import javax.validation.constraints.DecimalMin;

@Price
public class FilterForm {
    private String locations;
    private String types;

    @DecimalMin("0.0")
    private Double minPrice;

    @DecimalMin("0.0")
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

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }
}
