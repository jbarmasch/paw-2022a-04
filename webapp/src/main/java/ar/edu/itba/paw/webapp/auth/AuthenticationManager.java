package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class AuthenticationManager {
    public void setReferrer(HttpServletRequest request, String referrer) {
        HttpSession session = request.getSession();
        if (session != null && referrer != null && !referrer.contains("login") && !referrer.contains("register")) {
            session.setAttribute("url_prior_login", referrer);
        }
    }

    private String getReferrer(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            return (String) session.getAttribute("url_prior_login");
        }
        return request.getContextPath() + "/";
    }

    public String redirectionAuthenticationSuccess(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            String redirectUrl = getReferrer(request);
            if (!redirectUrl.equals(request.getContextPath() + "/")) {
                return redirectUrl;
            }
            redirectUrl = (String) session.getAttribute("url_prior_login");
            if (redirectUrl != null) {
                session.removeAttribute("url_prior_login");
                if (!(redirectUrl.contains("login") || redirectUrl.contains("register")))
                    return redirectUrl;
            }
        }
        return "/";
    }

    public void requestAuthentication(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        request.getSession();
        token.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
