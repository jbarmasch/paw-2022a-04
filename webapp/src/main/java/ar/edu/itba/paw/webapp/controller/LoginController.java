package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.RoleEnum;
import ar.edu.itba.paw.webapp.dto.TokenDto;
import ar.edu.itba.paw.webapp.auth.AuthenticationTokenDetails;
import ar.edu.itba.paw.webapp.auth.AuthenticationTokenService;
import ar.edu.itba.paw.webapp.dto.UserCredentialsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;
import java.util.stream.Collectors;

@Path("api/token")
@Component
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(UserCredentialsDto credentials) {

        Authentication authenticationRequest =
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());

        Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
        SecurityContextHolder.getContext().setAuthentication(authenticationResult);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        System.out.println(username);

        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());

//        Set<RoleEnum> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
//                .map(grantedAuthority -> {
//                    System.out.println(grantedAuthority.toString());
//                    return RoleEnum.valueOf(grantedAuthority.toString());
//                })
//                .collect(Collectors.toSet());

//        Set<RoleEnum> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
//                .map(x -> RoleEnum.valueOf(x.toString().substring(5))).collect(Collectors.toSet());

        Set<RoleEnum> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(x -> RoleEnum.valueOf(x.toString())).collect(Collectors.toSet());

        String token = authenticationTokenService.issueToken(username, authorities);
        TokenDto authenticationToken = new TokenDto();
        authenticationToken.setToken(token);

        return Response.ok(authenticationToken).build();
    }

    @POST
    @Path("refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public Response refresh() {

        AuthenticationTokenDetails tokenDetails = (AuthenticationTokenDetails)
                SecurityContextHolder.getContext().getAuthentication().getDetails();

        String token = authenticationTokenService.refreshToken(tokenDetails);
        TokenDto authenticationToken = new TokenDto();
        authenticationToken.setToken(token);

        return Response.ok(authenticationToken).build();
    }
}
