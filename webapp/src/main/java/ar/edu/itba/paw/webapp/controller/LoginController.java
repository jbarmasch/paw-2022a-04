package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("api/login")
@Component
public class LoginController {
    @Autowired
    private UserService us;
    @Autowired
    private AuthenticationManager authenticationManager;
//    @Autowired
//    private JwtUtil jwtUtil;

    @Context
    private UriInfo uriInfo;

    @GET
    public Response login(@QueryParam("username") final String username,
                          @QueryParam("password") final String password) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username, password
            ));

            User bianca = (User) authenticate.getPrincipal();
            final ar.edu.itba.paw.model.User user = us.findByUsername(bianca.getUsername()).orElse(null);

            if (user == null) {
                throw new RuntimeException("Cami te amo");
            }

            return Response.ok().build();
//                    .header(
//                            HttpHeaders.AUTHORIZATION,
//                            jwtUtil.generateToken(user)
//                    )
//                    .build();
        } catch (BadCredentialsException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
