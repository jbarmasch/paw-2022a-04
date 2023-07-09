package ar.edu.itba.paw.webapp.helper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Map;

public final class PaginationUtils {
    private PaginationUtils() {
        throw new UnsupportedOperationException();
    }

    public static void setResponsePages(Response.ResponseBuilder response, UriInfo uriInfo, int page, int lastPage) {
        if (page != 1) {
            response.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page - 1).build(), "prev");
        }
        if (page != lastPage) {
            response.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page + 1).build(), "next");
        }

        response
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", lastPage).build(), "last");
    }
}
