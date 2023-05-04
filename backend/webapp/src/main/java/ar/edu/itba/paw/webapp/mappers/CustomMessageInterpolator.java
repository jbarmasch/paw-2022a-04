package ar.edu.itba.paw.webapp.mappers;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.validation.MessageInterpolator;
import javax.ws.rs.core.HttpHeaders;
import java.util.Locale;

//public class CustomMessageInterpolator implements MessageInterpolator {
//    private final Provider<HttpHeaders> headers;
//
//    CustomMessageInterpolator(Provider<HttpHeaders> headers) {
//        this.headers = headers;
//    }
//
//    @Override
//    public String interpolate(final String messageTemplate, final Context context) {
//        return interpolate(messageTemplate, context, headers.get().getLanguage());
//    }
//
//    @Override
//    public String interpolate(final String messageTemplate, final Context context, final Locale locale) {
//        System.out.println(locale);
//        if (locale != null) {
//            System.out.println(locale.getLanguage());
//            return "BYE";
//        } else {
//            return "CHAU";
//        }
//    }
//}