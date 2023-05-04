package ar.edu.itba.paw.webapp.mappers;

import org.glassfish.jersey.server.validation.ValidationConfig;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

//@Provider
//public class ValidationConfigurationContextResolver implements ContextResolver<ValidationConfig> {
//    @Context
//    javax.inject.Provider<HttpHeaders> headers;
//
//    private final ValidationConfig config;
//
//    public ValidationConfigurationContextResolver() {
//        config = new ValidationConfig();
//    }
//
//    @PostConstruct
//    public void post() {
//        config.messageInterpolator(new CustomMessageInterpolator(headers));
//    }
//
//    @Override
//    public ValidationConfig getContext(final Class<?> type) {
//        return ValidationConfig.class.isAssignableFrom(type) ? config : null;
//    }
//}