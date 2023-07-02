package ar.edu.itba.paw.webapp.mappers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.MessageInterpolator;
import java.util.Locale;

public class CustomMessageInterpolator implements MessageInterpolator {
    private final HttpServletRequest headers;
    private final MessageInterpolator messageSource;

    public CustomMessageInterpolator(HttpServletRequest headers, MessageInterpolator messageSource) {
        this.headers = headers;
        this.messageSource = messageSource;
    }

    @Override
    public String interpolate(final String messageTemplate, final Context context) {
        return interpolate(messageTemplate, context, headers.getLocale());
    }

    @Override
    public String interpolate(final String messageTemplate, final Context context, final Locale locale) {
        if (locale != null) {
            return messageSource.interpolate(messageTemplate, context, locale);
        } else {
            return messageSource.interpolate(messageTemplate, context, Locale.ENGLISH);
        }
    }
}
