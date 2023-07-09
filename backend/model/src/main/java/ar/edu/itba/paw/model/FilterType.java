package ar.edu.itba.paw.model;

import java.util.Map;

public class FilterType {
    private final Map<Location, Integer> locations;
    private final Map<Type, Integer> types;
    private final Map<Tag, Integer> tags;
    private final int soldOut;
    private final int noTickets;

    public FilterType(Map<Location, Integer> locations, Map<Type, Integer> types, Map<Tag, Integer> tags, int soldOut, int noTickets) {
        this.locations = locations;
        this.types = types;
        this.tags = tags;
        this.soldOut = soldOut;
        this.noTickets = noTickets;
    }

    public Map<Location, Integer> getLocations() {
        return this.locations;
    }

    public Map<Type, Integer> getTypes() {
        return this.types;
    }

    public Map<Tag, Integer> getTags() {
        return this.tags;
    }

    public int getSoldOut() {
        return this.soldOut;
    }

    public int getNoTickets() {
        return this.noTickets;
    }
}