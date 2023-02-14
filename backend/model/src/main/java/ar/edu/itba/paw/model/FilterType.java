package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.Type;
import ar.edu.itba.paw.model.Tag;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterType {
    private Map<Location, Integer> locations;
    private Map<Type, Integer> types;
    private Map<Tag, Integer> tags;
    private int soldOut;
    private int noTickets;

    public FilterType(Map<Location,Integer> locations, Map<Type,Integer> types, Map<Tag,Integer> tags, int soldOut, int noTickets) {
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