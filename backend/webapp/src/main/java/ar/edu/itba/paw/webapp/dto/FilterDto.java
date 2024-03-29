package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.FilterType;
import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.model.Tag;
import ar.edu.itba.paw.model.Type;

import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.Map;

public class FilterDto {
    private Map<LocationDto, Integer> locations;
    private Map<TypeDto, Integer> types;
    private Map<TagDto, Integer> tags;
    private int soldOut;
    private int noTickets;

    public static FilterDto fromFilter(final UriInfo uriInfo, final FilterType filter, final String locale) {
        final FilterDto dto = new FilterDto();

        Map<LocationDto, Integer> locations = new HashMap<>();
        for (Map.Entry<Location, Integer> entry : filter.getLocations().entrySet()) {
            locations.put(LocationDto.fromLocation(uriInfo, entry.getKey()), entry.getValue());
        }
        dto.locations = locations;

        Map<TypeDto, Integer> types = new HashMap<>();
        for (Map.Entry<Type, Integer> entry : filter.getTypes().entrySet()) {
            types.put(TypeDto.fromType(uriInfo, entry.getKey(), locale), entry.getValue());
        }
        dto.types = types;

        Map<TagDto, Integer> tags = new HashMap<>();
        for (Map.Entry<Tag, Integer> entry : filter.getTags().entrySet()) {
            tags.put(TagDto.fromTag(uriInfo, entry.getKey(), locale), entry.getValue());
        }
        dto.tags = tags;
        dto.soldOut = filter.getSoldOut();
        dto.noTickets = filter.getNoTickets();

        return dto;
    }

    public Map<LocationDto, Integer> getLocations() {
        return this.locations;
    }

    public void setLocations(Map<LocationDto, Integer> locations) {
        this.locations = locations;
    }

    public Map<TypeDto, Integer> getTypes() {
        return this.types;
    }

    public void setTypes(Map<TypeDto, Integer> types) {
        this.types = types;
    }

    public Map<TagDto, Integer> getTags() {
        return this.tags;
    }

    public void setTags(Map<TagDto, Integer> tags) {
        this.tags = tags;
    }

    public int getSoldOut() {
        return soldOut;
    }

    public void setSoldOut(int soldOut) {
        this.soldOut = soldOut;
    }

    public int getNoTickets() {
        return noTickets;
    }

    public void setNoTickets(int noTickets) {
        this.noTickets = noTickets;
    }

}
