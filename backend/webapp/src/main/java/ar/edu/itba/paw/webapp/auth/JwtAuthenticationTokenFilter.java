package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.RoleEnum;
import java.util.Base64;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final AuthenticationTokenService authenticationTokenService;
    private final UserService us;

    public JwtAuthenticationTokenFilter(AuthenticationManager authenticationManager, AuthenticationTokenService authenticationTokenService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.authenticationTokenService = authenticationTokenService;
        this.us = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null) {
            if (authorizationHeader.startsWith("Bearer")) {
                String authenticationToken = authorizationHeader.substring(7);

                AuthenticationTokenDetails authenticationTokenDetails;
                try {
                     authenticationTokenDetails = authenticationTokenService.parseToken(authenticationToken);

                    if (authenticationTokenDetails.isRefresh()) {
                        String accessToken = authenticationTokenService.issueRefreshToken(authenticationTokenDetails.getUsername(), authenticationTokenDetails.getAuthorities());
                        Authentication authenticationRequest = new JwtAuthenticationToken(accessToken);
                        Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        context.setAuthentication(authenticationResult);
                        SecurityContextHolder.setContext(context);
                        response.setHeader("Access-Token", accessToken);
                    } else {
                        Authentication authenticationRequest = new JwtAuthenticationToken(authenticationToken);
                        Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        context.setAuthentication(authenticationResult);
                        SecurityContextHolder.setContext(context);
                    }
                } catch (ExpiredJwtException e) {
                    System.out.println("Expired!");
                }
            } else if (authorizationHeader.startsWith("Basic")) {
                String[] credentials = new String(Base64.getDecoder().decode(authorizationHeader.substring(6))).split(":");
                Authentication authenticationRequest = new UsernamePasswordAuthenticationToken(credentials[0], credentials[1]);

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
                context.setAuthentication(authenticationResult);
                SecurityContextHolder.setContext(context);

                Set<RoleEnum> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                        .map(x -> RoleEnum.valueOf(x.toString())).collect(Collectors.toSet());

                String accessToken = authenticationTokenService.issueAccessToken(credentials[0], authorities);
                String refreshToken = authenticationTokenService.issueRefreshToken(credentials[0], authorities);
                response.setHeader("Access-Token", accessToken);
                response.setHeader("Refresh-Token", refreshToken);
                us.findByUsername(credentials[0]).ifPresent(user -> response.setHeader("User-ID", String.valueOf(user.getId())));
            }
        }

        filterChain.doFilter(request, response);
    }
}