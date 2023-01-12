package ar.edu.itba.paw.webapp.config;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

//@Provider
//public class CorsResponseFilter implements ContainerResponseFilter {
//    @Override
//    public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
//        response.getHeaders().add("Access-Control-Allow-Origin", "*");
//        response.getHeaders().add("Access-Control-Allow-Headers", "CSRF-Token, X-Requested-By, Authorization, Content-Type");
//        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
//        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
//    }
//}

@Component
public class CorsResponseFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, HEAD");
        res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");

        if (req.getMethod().equalsIgnoreCase("OPTIONS")) {
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().flush();
            res.getWriter().close();
            return;
        }

        chain.doFilter(req, res);
    }
}

//@Provider
//@PreMatching
//public class CorsResponseFilter implements ContainerRequestFilter, ContainerResponseFilter {
//    @Override
//    public void filter(ContainerRequestContext request) {
//        if (isPreflightRequest(request)) {
//            request.abortWith(Response.ok().build());
//        }
//    }
//
//    private static boolean isPreflightRequest(ContainerRequestContext request) {
//        return request.getHeaderString("Origin") != null
//                && request.getMethod().equalsIgnoreCase("OPTIONS");
//    }
//
//    @Override
//    public void filter(ContainerRequestContext request, ContainerResponseContext response) {
//
//        if (request.getHeaderString("Origin") == null) {
//            return;
//        }
//
////        if (isPreflightRequest(request)) {
//            response.getHeaders().add("Access-Control-Allow-Credentials", "true");
//            response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
//            response.getHeaders().add("Access-Control-Allow-Headers", "X-Requested-With, Authorization, " +
//                            "Accept-Version, Content-MD5, CSRF-Token, Content-Type");
////        }
//
//        response.getHeaders().add("Access-Control-Allow-Origin", "*");
//    }
//}
