package ar.edu.itba.paw.webapp.mappers;

import org.glassfish.jersey.server.validation.ValidationConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Validation;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
@Component
public class ValidationConfigurationContextResolver implements ContextResolver<ValidationConfig> {
    @Context
    private HttpServletRequest headers;
    private final ValidationConfig config;

    public ValidationConfigurationContextResolver() {
        config = new ValidationConfig();
    }

    @PostConstruct
    public void post() {
        config.messageInterpolator(new CustomMessageInterpolator(headers,
                Validation.byDefaultProvider().configure().getDefaultMessageInterpolator()));
    }

    @Override
    public ValidationConfig getContext(final Class<?> type) {
        return ValidationConfig.class.isAssignableFrom(type) ? config : null;
    }
}
