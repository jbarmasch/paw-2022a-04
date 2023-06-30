package ar.edu.itba.paw.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ResponseFilter extends GenericFilterBean {
    private final boolean cors;

    @Autowired
    public ResponseFilter(Environment env) {
        this.cors = Boolean.parseBoolean(env.getProperty("cors"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        if (cors) {
            res.setHeader("Access-Control-Allow-Origin", "*");
            res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, PATCH, OPTIONS, DELETE, HEAD");
            res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
            res.setHeader("Access-Control-Expose-Headers", "Access-Token, Refresh-Token, User-ID, Location, Link");
        }

        if (req.getMethod().equalsIgnoreCase("OPTIONS")) {
            res.setStatus(HttpServletResponse.SC_OK);
            res.getWriter().flush();
            res.getWriter().close();
            return;
        }

        chain.doFilter(req, res);
    }
}
