package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Location;
import ar.edu.itba.paw.service.LocationService;
import ar.edu.itba.paw.webapp.dto.LocationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("api/locations")
@Component
public class LocationController {
    @Autowired
    private LocationService ls;
    @Context
    private UriInfo uriInfo;

    private static final Logger LOGGER = LoggerFactory.getLogger(LocationController.class);

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON,})
    public Response listLocations() {
        final List<Location> res = ls.getAll();
        final List<LocationDto> locationList = res
                .stream()
                .map(e -> LocationDto.fromLocation(uriInfo, e))
                .collect(Collectors.toList());

        if (locationList.isEmpty()) {
            LOGGER.warn("No locations");
            return Response.noContent().build();
        }

        return Response.ok(new GenericEntity<List<LocationDto>>(locationList) {}).build();
    }
}
